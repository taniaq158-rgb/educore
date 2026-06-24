/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.view;
import edu.uam.educore.controller.EmpleadoController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.personas.Empleado;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author tania
 */


public class EmpleadoView extends VistaBase {

  private final EmpleadoController controller;

  public EmpleadoView(Scanner scanner, Repositorio<Empleado> repo) {
    super(scanner);
    this.controller = new EmpleadoController(repo);
  }

  //Ciclo de la vista 

  public void iniciar() {
    boolean activo = true;
    while (activo) {
      int opcion = mostrarMenu();
      if (opcion == 1) {
        registrar();
      } else if (opcion == 2) {
        listar();
      } else if (opcion == 3) {
        buscar();
      } else if (opcion == 4) {
        actualizar();
      } else if (opcion == 5) {
        eliminar();
      } else if (opcion == 0) {
        activo = false;
      } else {
        mostrarError("Opcion invalida.");
      }
    }
  }

  //Acciones 

  private void registrar() {
    String nombre = leerTexto("Nombre");
    String apellidos = leerTexto("Apellidos");
    String email = leerTexto("Email");
    double salario = leerDecimal("Salario mensual");
    LocalDate fechaIngreso = leerFecha("Fecha de ingreso (AAAA-MM-DD)");
    TipoEmpleado tipo = mostrarTipoEmpleado();

    try {
      Empleado registrado =
          controller.registrar(nombre, apellidos, email, salario, fechaIngreso, tipo);
      mostrarMensaje("Registrado — ID: " + registrado.getId() + "\n  " + registrado.getInfo());
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void listar() {
    try {
      List<Empleado> lista = controller.listar();
      if (lista.isEmpty()) {
        mostrarMensaje("No hay empleados registrados.");
        return;
      }
      System.out.println("\n________ EMPLEADOS REGISTRADOS (" + lista.size() + ") ________");
      for (Empleado e : lista) {
        System.out.println("  " + e.getInfo());
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void buscar() {
    int id = leerEntero("ID del empleado");
    try {
      Empleado e = controller.buscarPorId(id);
      if (e == null) {
        mostrarError("No existe empleado con ID " + id + ".");
      } else {
        System.out.println("\n  " + e.getInfo());
      }
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void actualizar() {
    int id = leerEntero("ID del empleado a actualizar");
    try {
      Empleado existente = controller.buscarPorId(id);
      if (existente == null) {
        mostrarError("No existe empleado con ID " + id + ".");
        return;
      }
      System.out.println("\nDatos actuales:");
      System.out.println("  " + existente.getInfo());
      System.out.println("\nIngrese los nuevos datos:");
      String nombre = leerTexto("Nombre");
      String apellidos = leerTexto("Apellidos");
      String email = leerTexto("Email");
      double salario = leerDecimal("Salario mensual");
      LocalDate fechaIngreso = leerFecha("Fecha de ingreso (AAAA-MM-DD)");
      TipoEmpleado tipo = mostrarTipoEmpleado();
      Empleado actualizado =
          controller.actualizar(id, nombre, apellidos, email, salario, fechaIngreso, tipo);
      mostrarMensaje("Actualizado — " + actualizado.getInfo());
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminar() {
    int id = leerEntero("ID del empleado a eliminar");
    try {
      Empleado existente = controller.buscarPorId(id);
      if (existente == null) {
        mostrarError("No existe empleado con ID " + id + ".");
        return;
      }
      System.out.println("\n  " + existente.getInfo());
      String confirmacion = leerTexto("¿Confirma la eliminacion? (s/n)");
      if (!confirmacion.equalsIgnoreCase("s")) {
        mostrarMensaje("Operacion cancelada.");
        return;
      }
      controller.eliminar(id);
      mostrarMensaje("Empleado con ID " + id + " eliminado.");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  // ── Menús

  private int mostrarMenu() {
    System.out.println("\n________GESTION DE EMPLEADOS________");
    System.out.println("1. Registrar empleado");
    System.out.println("2. Listar todos");
    System.out.println("3. Buscar por ID");
    System.out.println("4. Actualizar empleado");
    System.out.println("5. Eliminar empleado");
    System.out.println("0. Volver al menú principal");
    System.out.print("Opcion: ");
    return leerEntero();
  }

  private TipoEmpleado mostrarTipoEmpleado() {
    System.out.println("\nTipo de empleado:");
    TipoEmpleado[] tipos = TipoEmpleado.values();
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