package edu.uam.educore.view;

import edu.uam.educore.controller.SeccionController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.List;
import java.util.Scanner;

public class SeccionView extends VistaBase {

  private final SeccionController controller;

  public SeccionView(
      Scanner scanner,
      Repositorio<Seccion> seccionRepo,
      Repositorio<Empleado> empleadoRepo,
      Repositorio<Estudiante> estudianteRepo,
      Repositorio<Edificio> edificioRepo) {
    super(scanner);
    this.controller =
        new SeccionController(seccionRepo, empleadoRepo, estudianteRepo, edificioRepo);
  }

  // ── Ciclo de la vista ─────────────────────────────────────────────────────

  public void iniciar() {
    boolean activo = true;
    while (activo) {
      int opcion = mostrarMenu();
      if (opcion == 1) {
        registrar();
      } else if (opcion == 2) {
        listar();
      } else if (opcion == 3) {
        verDetalle();
      } else if (opcion == 4) {
        agregarEstudiante();
      } else if (opcion == 5) {
        removerEstudiante();
      } else if (opcion == 6) {
        eliminar();
      } else if (opcion == 0) {
        activo = false;
      } else {
        mostrarError("Opcion invalida.");
      }
    }
  }

  // ── Acciones ──────────────────────────────────────────────────────────────

  private void registrar() {
    String codigo = leerTexto("Codigo de la seccion");
    String nombre = leerTexto("Nombre del curso");
    int aulaId = leerEntero("ID del aula");
    int docenteId = leerEntero("ID del docente (empleado)");

    try {
      Seccion registrada = controller.registrar(codigo, nombre, aulaId, docenteId);
      mostrarMensaje("Registrada — ID: " + registrada.getId());
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void listar() {
    try {
      List<Seccion> lista = controller.listar();
      if (lista.isEmpty()) {
        mostrarMensaje("No hay secciones registradas.");
        return;
      }
      System.out.println("\n--- SECCIONES REGISTRADAS (" + lista.size() + ") ---");
      for (Seccion s : lista) {
        System.out.println(
            "  ID "
                + s.getId()
                + " | "
                + s.getCodigo()
                + " - "
                + s.getNombre()
                + " | Aula: "
                + s.getAula().getNumero()
                + " | Docente: "
                + s.getDocente().getNombre()
                + " "
                + s.getDocente().getApellidos()
                + " | Inscritos: "
                + s.listarEstudiantes().size());
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void verDetalle() {
    int id = leerEntero("ID de la seccion");
    try {
      Seccion s = controller.buscarPorId(id);
      if (s == null) {
        mostrarError("No existe sección con ID " + id + ".");
        return;
      }
      System.out.println("\n  " + s.getCodigo() + " - " + s.getNombre());
      System.out.println("  Aula: " + s.getAula().getNumero());
      System.out.println(
          "  Docente: " + s.getDocente().getNombre() + " " + s.getDocente().getApellidos());
      List<Estudiante> estudiantes = s.listarEstudiantes();
      if (estudiantes.isEmpty()) {
        System.out.println("  Sin estudiantes inscritos.");
      } else {
        System.out.println("  Estudiantes inscritos:");
        for (Estudiante e : estudiantes) {
          System.out.println(
              "    ID " + e.getId() + " | " + e.getNombre() + " " + e.getApellidos());
        }
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void agregarEstudiante() {
    int seccionId = leerEntero("ID de la seccion");
    int estudianteId = leerEntero("ID del estudiante");
    try {
      controller.agregarEstudiante(seccionId, estudianteId);
      mostrarMensaje("Estudiante agregado a la seccion.");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void removerEstudiante() {
    int seccionId = leerEntero("ID de la seccion");
    int estudianteId = leerEntero("ID del estudiante a remover");
    try {
      controller.removerEstudiante(seccionId, estudianteId);
      mostrarMensaje("Estudiante removido de la seccion.");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminar() {
    int id = leerEntero("ID de la seccion a eliminar");
    try {
      Seccion existente = controller.buscarPorId(id);
      if (existente == null) {
        mostrarError("No existe sección con ID " + id + ".");
        return;
      }
      System.out.println("\n  " + existente.getCodigo() + " - " + existente.getNombre());
      String confirmacion = leerTexto("¿Confirma la eliminacion? (s/n)");
      if (!confirmacion.equalsIgnoreCase("s")) {
        mostrarMensaje("Operacion cancelada.");
        return;
      }
      controller.eliminar(id);
      mostrarMensaje("Seccion con ID " + id + " eliminada.");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  // ── Menús ─────────────────────────────────────────────────────────────────

  private int mostrarMenu() {
    System.out.println("\n--- GESTION DE SECCIONES ---");
    System.out.println("1. Registrar seccion");
    System.out.println("2. Listar secciones");
    System.out.println("3. Ver detalle (estudiantes inscritos)");
    System.out.println("4. Agregar estudiante a seccion");
    System.out.println("5. Remover estudiante de seccion");
    System.out.println("6. Eliminar seccion");
    System.out.println("0. Volver al menu academico");
    System.out.print("Opcion: ");
    return leerEntero();
  }
}
