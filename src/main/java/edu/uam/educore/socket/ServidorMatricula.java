package edu.uam.educore.socket;

import edu.uam.educore.db.ConfiguracionBD;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Servidor de Matrícula. Recibe por socket la orden MATRICULAR &lt;archivo&gt;, lee ese CSV del
 * directorio de entrada (una matrícula por renglón: carnet,codigoSeccion) y matricula todo el lote
 * en UNA transacción: si un renglón falla, revierte el lote completo.
 */
public class ServidorMatricula {

  private final ConfiguracionBD config;
  private final Path entradaDir;

  public ServidorMatricula(ConfiguracionBD config, String entradaDir) {
    this.config = config;
    this.entradaDir = Path.of(entradaDir);
  }

  public static void main(String[] args) throws Exception {
    ConfiguracionBD config = ConfiguracionBD.desdeArchivo(".env");
    String entrada = System.getenv("ENTRADA_DIR");
    int puerto = Integer.parseInt(System.getenv("MATRICULA_PORT"));
    new ServidorMatricula(config, entrada).escuchar(puerto);
  }

  public void escuchar(int puerto) throws IOException {
    try (ServerSocket servidor = new ServerSocket(puerto)) {
      System.out.println("Matricula escuchando en " + puerto);
      while (true) {
        try (Socket cliente = servidor.accept();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out =
                new PrintWriter(cliente.getOutputStream(), true, StandardCharsets.UTF_8)) {
          atender(in, out);
        } catch (IOException e) {
          System.err.println("Error atendiendo cliente: " + e.getMessage());
        }
      }
    }
  }

  private void atender(BufferedReader in, PrintWriter out) throws IOException {
    String linea = in.readLine();
    if (linea == null || !linea.startsWith("MATRICULAR ")) {
      out.println("400 comando invalido");
      return;
    }
    String archivo = linea.substring("MATRICULAR ".length()).trim();
    try {
      int k = procesarLote(archivo);
      out.println("201 " + k);
    } catch (Exception e) {
      out.println("400 " + e.getMessage());
    }
  }

  /**
   * TODO(estudiante · T4/T5): matricular el lote en UNA transacción.
   *
   * <p>Pasos:
   *
   * <ol>
   *   <li>Leer el CSV de entrada (entradaDir.resolve(archivo)) con un BufferedReader; cada renglón
   *       es "carnet,codigoSeccion".
   *   <li>Abrir conexión y con.setAutoCommit(false).
   *   <li>Por cada renglón: buscar el estudiante por carnet (si no existe, es un error), buscar la
   *       sección por código y su cupo (aula.capacidad), validar cupo y duplicado, e insertar en
   *       matricula.
   *   <li>Si todo pasa: con.commit() y devolver la cantidad. Si algo falla: con.rollback() y
   *       relanzar.
   * </ol>
   *
   * <p>Cómo distinguir los cuatro casos de error (carnet inexistente, sección inexistente, cupo
   * lleno, matrícula duplicada) queda a su criterio de diseño — no hay una jerarquía de
   * excepciones provista. Ver "Puntos extra" en el enunciado si quieren diseñar la suya.
   *
   * <p>Referencia del patrón JDBC: EstudianteRepoSql.
   */
  
   private int procesarLote(String archivo) throws Exception {
  java.util.List<String[]> lineas = new java.util.ArrayList<>();
  try (java.io.BufferedReader br =
      java.nio.file.Files.newBufferedReader(entradaDir.resolve(archivo))) {
    String linea;
    while ((linea = br.readLine()) != null) {
      linea = linea.trim();
      if (linea.isEmpty() || linea.startsWith("carnet")) continue;
      lineas.add(linea.split(","));
    }
  }

  edu.uam.educore.db.ConfiguracionBD cfg =
      edu.uam.educore.db.ConfiguracionBD.desdeArchivo(".env");
  try (java.sql.Connection con =
      edu.uam.educore.db.Conexion.getConnection(cfg.url(), cfg.usuario(), cfg.contrasena())) {
    con.setAutoCommit(false);
    try {
      int count = 0;
      for (String[] partes : lineas) {
        String carnet = partes[0].trim();
        String codigoSeccion = partes[1].trim();

        // 1. Buscar estudiante por carnet
        int estudianteId = -1;
        try (java.sql.PreparedStatement ps =
            con.prepareStatement("SELECT id FROM estudiante WHERE carnet=?")) {
          ps.setString(1, carnet);
          try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) throw new Exception("Carnet no existe: " + carnet);
            estudianteId = rs.getInt("id");
          }
        }

        // 2. Buscar sección por código y obtener capacidad
        int seccionId = -1;
        int capacidad = 0;
        try (java.sql.PreparedStatement ps =
            con.prepareStatement(
                "SELECT s.id, a.capacidad FROM seccion s"
                    + " JOIN aula a ON s.aula_id = a.id WHERE s.codigo=?")) {
          ps.setString(1, codigoSeccion);
          try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) throw new Exception("Sección no existe: " + codigoSeccion);
            seccionId = rs.getInt("id");
            capacidad = rs.getInt("capacidad");
          }
        }

        // 3. Verificar cupo
        int inscritos = 0;
        try (java.sql.PreparedStatement ps =
            con.prepareStatement(
                "SELECT COUNT(*) FROM matricula WHERE seccion_id=?")) {
          ps.setInt(1, seccionId);
          try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) inscritos = rs.getInt(1);
          }
        }
        if (inscritos >= capacidad) throw new Exception("Cupo lleno en sección: " + codigoSeccion);

        // 4. Verificar duplicado
        try (java.sql.PreparedStatement ps =
            con.prepareStatement(
                "SELECT COUNT(*) FROM matricula WHERE seccion_id=? AND estudiante_id=?")) {
          ps.setInt(1, seccionId);
          ps.setInt(2, estudianteId);
          try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0)
              throw new Exception("Matrícula duplicada: " + carnet + " en " + codigoSeccion);
          }
        }

        // 5. Insertar
        try (java.sql.PreparedStatement ps =
            con.prepareStatement(
                "INSERT INTO matricula (seccion_id, estudiante_id) VALUES (?, ?)")) {
          ps.setInt(1, seccionId);
          ps.setInt(2, estudianteId);
          ps.executeUpdate();
        }
        count++;
      }
      con.commit();
      return count;
    } catch (Exception e) {
      con.rollback();
      throw e;
    }
  }
}
}
