package edu.uam.educore.view;

import edu.uam.educore.controller.EdificioController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoAula;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import java.util.List;
import java.util.Scanner;

/**
 * @author tania
 */
public class EdificioView extends VistaBase {

  private final EdificioController controller;

  public EdificioView(Scanner scanner, Repositorio<Edificio> repo) {
    super(scanner);
    this.controller = new EdificioController(repo);
  }

  // ── Ciclo de la vista ─────────────────────────────────────────────────────

  public void iniciar() {
    boolean activo = true;
    while (activo) {
      int opcion = mostrarMenu();
      if (opcion == 1) {
        registrarEdificio();
      } else if (opcion == 2) {
        listarEdificios();
      } else if (opcion == 3) {
        buscarEdificio();
      } else if (opcion == 4) {
        eliminarEdificio();
      } else if (opcion == 5) {
        agregarAula();
      } else if (opcion == 6) {
        listarAulas();
      } else if (opcion == 7) {
        eliminarAula();
      } else if (opcion == 0) {
        activo = false;
      } else {
        mostrarError("Opcion invalida.");
      }
    }
  }

  // Edificios

  private void registrarEdificio() {
    String codigo = leerTexto("Codigo del edificio");
    String nombre = leerTexto("Nombre del edificio");
    try {
      Edificio registrado = controller.registrarEdificio(codigo, nombre);
      mostrarMensaje("Registrado — ID: " + registrado.getId());
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void listarEdificios() {
    try {
      List<Edificio> lista = controller.listarEdificios();
      if (lista.isEmpty()) {
        mostrarMensaje("No hay edificios registrados.");
        return;
      }
      System.out.println("\n_____ EDIFICIOS REGISTRADOS (" + lista.size() + ") _____");
      for (Edificio e : lista) {
        System.out.println(
            "  ID "
                + e.getId()
                + " | "
                + e.getCodigo()
                + " — "
                + e.getNombre()
                + " ("
                + e.listarAulas().size()
                + " aulas)");
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void buscarEdificio() {
    int id = leerEntero("ID del edificio");
    try {
      Edificio e = controller.buscarEdificioPorId(id);
      if (e == null) {
        mostrarError("No existe edificio con ID " + id + ".");
        return;
      }
      System.out.println("\n  " + e.getCodigo() + " — " + e.getNombre());
      List<Aula> aulas = e.listarAulas();
      if (aulas.isEmpty()) {
        System.out.println("  Sin aulas registradas.");
      } else {
        System.out.println("  Aulas:");
        for (Aula a : aulas) {
          System.out.println(
              "    ID "
                  + a.getId()
                  + " | "
                  + a.getNumero()
                  + " | "
                  + a.getTipo()
                  + " | Cap: "
                  + a.getCapacidad());
        }
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminarEdificio() {
    int id = leerEntero("ID del edificio a eliminar");
    try {
      Edificio existente = controller.buscarEdificioPorId(id);
      if (existente == null) {
        mostrarError("No existe edificio con ID " + id + ".");
        return;
      }
      System.out.println("\n  " + existente.getCodigo() + " — " + existente.getNombre());
      String confirmacion = leerTexto("¿Confirma la eliminacion? (s/n)");
      if (!confirmacion.equalsIgnoreCase("s")) {
        mostrarMensaje("Operacion cancelada.");
        return;
      }
      controller.eliminarEdificio(id);
      mostrarMensaje("Edificio con ID " + id + " eliminado.");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  // ── Acciones: Aulas ───────────────────────────────────────────────────────

  private void agregarAula() {
    int edificioId = leerEntero("ID del edificio");
    String numero = leerTexto("Numero de aula");
    int capacidad = leerEntero("Capacidad");
    TipoAula tipo = mostrarTipoAula();
    try {
      Aula aula = controller.agregarAula(edificioId, numero, capacidad, tipo);
      mostrarMensaje("Aula registrada — ID: " + aula.getId());
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void listarAulas() {
    int edificioId = leerEntero("ID del edificio");
    try {
      List<Aula> aulas = controller.listarAulas(edificioId);
      if (aulas.isEmpty()) {
        mostrarMensaje("Este edificio no tiene aulas registradas.");
        return;
      }
      System.out.println("\n_____ AULAS DEL EDIFICIO (" + aulas.size() + ") _____");
      for (Aula a : aulas) {
        System.out.println(
            "  ID "
                + a.getId()
                + " | "
                + a.getNumero()
                + " | "
                + a.getTipo()
                + " | Cap: "
                + a.getCapacidad());
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminarAula() {
    int edificioId = leerEntero("ID del edificio");
    int aulaId = leerEntero("ID del aula a eliminar");
    try {
      String confirmacion = leerTexto("¿Confirma la eliminacion? (s/n)");
      if (!confirmacion.equalsIgnoreCase("s")) {
        mostrarMensaje("Operacion cancelada.");
        return;
      }
      controller.eliminarAula(edificioId, aulaId);
      mostrarMensaje("Aula con ID " + aulaId + " eliminada.");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  // ── Menús ─────────────────────────────────────────────────────────────────

  private int mostrarMenu() {
    System.out.println("\n_____ GESTION DE EDIFICIOS Y AULAS _____");
    System.out.println("1. Registrar edificio");
    System.out.println("2. Listar edificios");
    System.out.println("3. Buscar edificio por ID (ver sus aulas)");
    System.out.println("4. Eliminar edificio");
    System.out.println("5. Agregar aula a un edificio");
    System.out.println("6. Listar aulas de un edificio");
    System.out.println("7. Eliminar aula");
    System.out.println("0. Volver al menu principal");
    System.out.print("Opcion: ");
    return leerEntero();
  }

  private TipoAula mostrarTipoAula() {
    System.out.println("\nTipo de aula:");
    TipoAula[] tipos = TipoAula.values();
    for (int i = 0; i < tipos.length; i++) {
      System.out.println((i + 1) + ". " + tipos[i]);
    }
    System.out.print("Tipo: ");
    int seleccion = leerEntero();
    if (seleccion < 1 || seleccion > tipos.length) {
      return null;
    }
    return tipos[seleccion - 1];
  }
}
