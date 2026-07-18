package edu.uam.educore.dao;

import edu.uam.educore.db.Conexion;
import edu.uam.educore.db.ConfiguracionBD;
import edu.uam.educore.enums.TipoAula;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EdificioRepoSql extends Repositorio<Edificio> {

  private final ConfiguracionBD config;

  public EdificioRepoSql(ConfiguracionBD config) {
    this.config = config;
  }

  private Connection abrir() throws Exception {
    return Conexion.getConnection(config.url(), config.usuario(), config.contrasena());
  }

  @Override
  public void guardar(Edificio e) throws Exception {
    String sql = "INSERT INTO edificio (codigo, nombre) VALUES (?, ?)";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, e.getCodigo());
      ps.setString(2, e.getNombre());
      ps.executeUpdate();
      try (ResultSet claves = ps.getGeneratedKeys()) {
        if (claves.next()) {
          e.setId(claves.getInt(1));
        }
      }
    }
  }

  @Override
  public void actualizar(Edificio e) throws Exception {
    String sqlEdificio = "UPDATE edificio SET codigo=?, nombre=? WHERE id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sqlEdificio)) {
      ps.setString(1, e.getCodigo());
      ps.setString(2, e.getNombre());
      ps.setInt(3, e.getId());
      ps.executeUpdate();
    }
    String sqlBorrar = "DELETE FROM aula WHERE edificio_id=?";
    String sqlAula =
        "INSERT INTO aula (id, numero, capacidad, tipo, edificio_id) VALUES (?, ?, ?, ?, ?)";
    try (Connection con = abrir()) {
      try (PreparedStatement psDel = con.prepareStatement(sqlBorrar)) {
        psDel.setInt(1, e.getId());
        psDel.executeUpdate();
      }
      try (PreparedStatement psAula = con.prepareStatement(sqlAula)) {
        for (Aula a : e.listarAulas()) {
          psAula.setInt(1, a.getId());
          psAula.setString(2, a.getNumero());
          psAula.setInt(3, a.getCapacidad());
          psAula.setString(4, a.getTipo().name());
          psAula.setInt(5, e.getId());
          psAula.executeUpdate();
        }
      }
    }
  }

  @Override
  public void eliminar(int id) throws Exception {
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement("DELETE FROM edificio WHERE id=?")) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }

  @Override
  public Optional<Edificio> buscarPorId(int id) throws Exception {
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM edificio WHERE id=?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Edificio e = mapearEdificio(rs);
          cargarAulas(e);
          return Optional.of(e);
        }
        return Optional.empty();
      }
    }
  }

  @Override
  public List<Edificio> buscarTodos() throws Exception {
    List<Edificio> lista = new ArrayList<>();
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM edificio");
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Edificio e = mapearEdificio(rs);
        cargarAulas(e);
        lista.add(e);
      }
    }
    return lista;
  }

  private Edificio mapearEdificio(ResultSet rs) throws Exception {
    return new Edificio(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"));
  }

  private void cargarAulas(Edificio edificio) throws Exception {
    String sql = "SELECT * FROM aula WHERE edificio_id=?";
    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, edificio.getId());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Aula aula =
              new Aula(
                  rs.getInt("id"),
                  rs.getString("numero"),
                  rs.getInt("capacidad"),
                  TipoAula.valueOf(rs.getString("tipo")),
                  edificio);
          edificio.agregarAula(aula);
        }
      }
    }
  }
}