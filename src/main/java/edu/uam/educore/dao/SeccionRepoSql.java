package edu.uam.educore.dao;

import edu.uam.educore.db.Conexion;
import edu.uam.educore.db.ConfiguracionBD;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import edu.uam.educore.model.personas.EstudianteBecado;
import edu.uam.educore.model.personas.EstudianteRegular;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeccionRepoSql extends Repositorio<Seccion> {

  private final ConfiguracionBD config;

  public SeccionRepoSql(ConfiguracionBD config) {
    this.config = config;
  }

  private Connection abrir() throws Exception {
    return Conexion.getConnection(config.url(), config.usuario(), config.contrasena());
  }

  @Override
  public void guardar(Seccion s) throws Exception {
    String sql =
        "INSERT INTO seccion (codigo, nombre, docente_id, aula_id) VALUES (?, ?, ?, ?)";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, s.getCodigo());
      ps.setString(2, s.getNombre());
      ps.setInt(3, s.getDocente().getId());
      ps.setInt(4, s.getAula().getId());
      ps.executeUpdate();
      try (ResultSet claves = ps.getGeneratedKeys()) {
        if (claves.next()) {
          s.setId(claves.getInt(1));
        }
      }
    }
  }

  @Override
  public void actualizar(Seccion s) throws Exception {
    String sqlSeccion =
        "UPDATE seccion SET codigo=?, nombre=?, docente_id=?, aula_id=? WHERE id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sqlSeccion)) {
      ps.setString(1, s.getCodigo());
      ps.setString(2, s.getNombre());
      ps.setInt(3, s.getDocente().getId());
      ps.setInt(4, s.getAula().getId());
      ps.setInt(5, s.getId());
      ps.executeUpdate();
    }
    // Sincroniza matriculas
    String sqlBorrar = "DELETE FROM matricula WHERE seccion_id=?";
    String sqlMatricula =
        "INSERT INTO matricula (seccion_id, estudiante_id) VALUES (?, ?)";
    try (Connection con = abrir()) {
      try (PreparedStatement psDel = con.prepareStatement(sqlBorrar)) {
        psDel.setInt(1, s.getId());
        psDel.executeUpdate();
      }
      try (PreparedStatement psIns = con.prepareStatement(sqlMatricula)) {
        for (Estudiante e : s.listarEstudiantes()) {
          psIns.setInt(1, s.getId());
          psIns.setInt(2, e.getId());
          psIns.executeUpdate();
        }
      }
    }
  }

  @Override
  public void eliminar(int id) throws Exception {
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement("DELETE FROM seccion WHERE id=?")) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }

  @Override
  public Optional<Seccion> buscarPorId(int id) throws Exception {
    String sql = "SELECT * FROM seccion WHERE id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Seccion s = mapearSeccion(rs);
          cargarEstudiantes(s);
          return Optional.of(s);
        }
        return Optional.empty();
      }
    }
  }

  @Override
  public List<Seccion> buscarTodos() throws Exception {
    List<Seccion> lista = new ArrayList<>();
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM seccion");
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Seccion s = mapearSeccion(rs);
        cargarEstudiantes(s);
        lista.add(s);
      }
    }
    return lista;
  }

  private Seccion mapearSeccion(ResultSet rs) throws Exception {
    int id = rs.getInt("id");
    String codigo = rs.getString("codigo");
    String nombre = rs.getString("nombre");
    int docenteId = rs.getInt("docente_id");
    int aulaId = rs.getInt("aula_id");

    Empleado docente = buscarEmpleado(docenteId);
    Aula aula = buscarAula(aulaId);

    Seccion s = new Seccion(id, codigo, nombre, docente, aula);
    s.setId(id);
    return s;
  }

  private void cargarEstudiantes(Seccion s) throws Exception {
    String sql =
        "SELECT e.* FROM estudiante e"
            + " JOIN matricula m ON e.id = m.estudiante_id"
            + " WHERE m.seccion_id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, s.getId());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          s.agregarEstudiante(mapearEstudiante(rs));
        }
      }
    }
  }

  private Empleado buscarEmpleado(int id) throws Exception {
    String sql = "SELECT * FROM empleado WHERE id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new Empleado(
              rs.getInt("id"),
              rs.getString("nombre"),
              rs.getString("apellidos"),
              rs.getString("email"),
              rs.getDouble("salario"),
              rs.getDate("fecha_ingreso").toLocalDate(),
              TipoEmpleado.valueOf(rs.getString("tipo")));
        }
      }
    }
    throw new IllegalArgumentException("No existe empleado con ID " + id);
  }

  private Aula buscarAula(int aulaId) throws Exception {
    String sql = "SELECT a.*, e.id as eid, e.codigo as ecodigo, e.nombre as enombre"
        + " FROM aula a JOIN edificio e ON a.edificio_id = e.id WHERE a.id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, aulaId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Edificio edificio = new Edificio(
              rs.getInt("eid"),
              rs.getString("ecodigo"),
              rs.getString("enombre"));
          return new Aula(
              rs.getInt("id"),
              rs.getString("numero"),
              rs.getInt("capacidad"),
              edu.uam.educore.enums.TipoAula.valueOf(rs.getString("tipo")),
              edificio);
        }
      }
    }
    throw new IllegalArgumentException("No existe aula con ID " + aulaId);
  }

  private Estudiante mapearEstudiante(ResultSet rs) throws Exception {
    int id = rs.getInt("id");
    String nombre = rs.getString("nombre");
    String apellidos = rs.getString("apellidos");
    String email = rs.getString("email");
    String carnet = rs.getString("carnet");
    if ("BECADO".equals(rs.getString("tipo"))) {
      return new EstudianteBecado(
          id, nombre, apellidos, email, carnet, rs.getDouble("porcentaje_beca"));
    }
    return new EstudianteRegular(id, nombre, apellidos, email, carnet);
  }
}