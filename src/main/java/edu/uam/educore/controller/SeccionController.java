package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.List;
import java.util.Optional;

public class SeccionController {

  private final Repositorio<Seccion> seccionRepo;
  private final Repositorio<Empleado> empleadoRepo;
  private final Repositorio<Estudiante> estudianteRepo;
  private final Repositorio<Edificio> edificioRepo;
  private int proximoId = 1;

  public SeccionController(
      Repositorio<Seccion> seccionRepo,
      Repositorio<Empleado> empleadoRepo,
      Repositorio<Estudiante> estudianteRepo,
      Repositorio<Edificio> edificioRepo) {
    this.seccionRepo = seccionRepo;
    this.empleadoRepo = empleadoRepo;
    this.estudianteRepo = estudianteRepo;
    this.edificioRepo = edificioRepo;
  }

  public Seccion registrar(String codigo, String nombre, int aulaId, int docenteId)
      throws Exception {
    if (codigo == null || codigo.isEmpty() || nombre == null || nombre.isEmpty()) {
      throw new IllegalArgumentException("Codigo y nombre son obligatorios.");
    }

    Optional<Empleado> resultadoEmpleado = empleadoRepo.buscarPorId(docenteId);
    if (resultadoEmpleado.isEmpty()) {
      throw new IllegalArgumentException("No existe empleado con ID " + docenteId + ".");
    }
    Empleado docente = resultadoEmpleado.get();
    if (docente.getTipoEnum() != TipoEmpleado.DOCENTE) {
      throw new IllegalArgumentException(
          "El empleado con ID " + docenteId + " no es de tipo DOCENTE.");
    }

    Aula aula = buscarAula(aulaId);
    if (aula == null) {
      throw new IllegalArgumentException("No existe aula con ID " + aulaId + ".");
    }

    Seccion seccion = new Seccion(proximoId, codigo, nombre, docente, aula);
    seccionRepo.guardar(seccion);
    proximoId++;
    return seccion;
  }

  public void agregarEstudiante(int seccionId, int estudianteId) throws Exception {
    Seccion seccion = buscarPorId(seccionId);
    if (seccion == null) {
      throw new IllegalArgumentException("No existe seccion con ID " + seccionId + ".");
    }
    Optional<Estudiante> estudiante = estudianteRepo.buscarPorId(estudianteId);
    if (estudiante.isEmpty()) {
      throw new IllegalArgumentException("No existe estudiante con ID " + estudianteId + ".");
    }
    seccion.agregarEstudiante(estudiante.get());
    seccionRepo.actualizar(seccion);
  }

  public void removerEstudiante(int seccionId, int estudianteId) throws Exception {
    Seccion seccion = buscarPorId(seccionId);
    if (seccion == null) {
      throw new IllegalArgumentException("No existe seccion con ID " + seccionId + ".");
    }
    seccion.removerEstudiante(estudianteId);
    seccionRepo.actualizar(seccion);
  }

  public List<Seccion> listar() throws Exception {
    return seccionRepo.buscarTodos();
  }

  public Seccion buscarPorId(int id) throws Exception {
    Optional<Seccion> resultado = seccionRepo.buscarPorId(id);
    if (resultado.isPresent()) {
      return resultado.get();
    }
    return null;
  }

  public void eliminar(int id) throws Exception {
    Seccion seccion = buscarPorId(id);
    if (seccion == null) {
      throw new IllegalArgumentException("No existe seccion con ID " + id + ".");
    }
    if (!seccion.listarEstudiantes().isEmpty()) {
      throw new IllegalArgumentException(
          "No se puede eliminar: la seccion tiene estudiantes inscritos. Remuevalos primero.");
    }
    seccionRepo.eliminar(id);
  }

  // ── Helpers internos ──────────────────────────────────────────────────────

  private Aula buscarAula(int aulaId) throws Exception {
    for (Edificio edificio : edificioRepo.buscarTodos()) {
      Optional<Aula> aula = edificio.buscarAulaPorId(aulaId);
      if (aula.isPresent()) {
        return aula.get();
      }
    }
    return null;
  }
}
