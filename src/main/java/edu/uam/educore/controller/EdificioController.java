

package edu.uam.educore.controller;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoAula;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author tania
 */


public class EdificioController {

  private final Repositorio<Edificio> repo;
  private int proximoIdEdificio = 1;
  private int proximoIdAula = 1; // único a nivel de todo el sistema — una sola instancia de este controller

  public EdificioController(Repositorio<Edificio> repo) {
    this.repo = repo;
  }

  // ── Edificios ─────────────────────────────────────────────────────────────

  public Edificio registrarEdificio(String codigo, String nombre) throws Exception {
    validarEdificio(codigo, nombre);
    Edificio e = new Edificio(proximoIdEdificio, codigo, nombre);
    repo.guardar(e);
    proximoIdEdificio++;
    return e;
  }

  public List<Edificio> listarEdificios() throws Exception {
    return repo.buscarTodos();
  }

  public Edificio buscarEdificioPorId(int id) throws Exception {
    Optional<Edificio> resultado = repo.buscarPorId(id);
    if (resultado.isPresent()) {
      return resultado.get();
    }
    return null;
  }

  public void eliminarEdificio(int id) throws Exception {
    Edificio e = buscarEdificioPorId(id);
    if (e == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + id + ".");
    }
    if (!e.listarAulas().isEmpty()) {
      throw new IllegalArgumentException(
          "No se puede eliminar: el edificio tiene aulas registradas. Elimine las aulas primero.");
    }
    repo.eliminar(id);
  }

  // ── Aulas ─────────────────────────────────────────────────────────────────

  public Aula agregarAula(int edificioId, String numero, int capacidad, TipoAula tipo)
      throws Exception {
    Edificio edificio = buscarEdificioPorId(edificioId);
    if (edificio == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + edificioId + ".");
    }
    validarAula(numero, capacidad, tipo);
    Aula aula = new Aula(proximoIdAula, numero, capacidad, tipo, edificio);
    edificio.agregarAula(aula);
    proximoIdAula++;
    repo.actualizar(edificio);
    return aula;
  }

  public List<Aula> listarAulas(int edificioId) throws Exception {
    Edificio edificio = buscarEdificioPorId(edificioId);
    if (edificio == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + edificioId + ".");
    }
    return edificio.listarAulas();
  }

  public void eliminarAula(int edificioId, int aulaId) throws Exception {
    Edificio edificio = buscarEdificioPorId(edificioId);
    if (edificio == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + edificioId + ".");
    }
    Optional<Aula> aula = edificio.buscarAulaPorId(aulaId);
    if (aula.isEmpty()) {
      throw new IllegalArgumentException("No existe aula con ID " + aulaId + " en este edificio.");
    }
    edificio.eliminarAula(aulaId);
    repo.actualizar(edificio);
  }

  // ── Helpers internos ──────────────────────────────────────────────────────

  private void validarEdificio(String codigo, String nombre) {
    if (codigo == null || codigo.isEmpty() || nombre == null || nombre.isEmpty()) {
      throw new IllegalArgumentException("Código y nombre son obligatorios.");
    }
  }

  private void validarAula(String numero, int capacidad, TipoAula tipo) {
    if (numero == null || numero.isEmpty()) {
      throw new IllegalArgumentException("El número de aula es obligatorio.");
    }
    if (capacidad <= 0) {
      throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
    }
    if (tipo == null) {
      throw new IllegalArgumentException("El tipo de aula es obligatorio.");
    }
  }
}