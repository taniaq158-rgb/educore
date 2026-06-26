package edu.uam.educore.view;

import java.util.Scanner;

public class AcademicoView extends VistaBase {

  private final EdificioView edificioView;
  private final SeccionView seccionView;

  public AcademicoView(Scanner scanner, EdificioView edificioView, SeccionView seccionView) {
    super(scanner);
    this.edificioView = edificioView;
    this.seccionView = seccionView;
  }

  public void iniciar() {
    boolean activo = true;
    while (activo) {
      int opcion = mostrarMenu();
      if (opcion == 1) {
        edificioView.iniciar();
      } else if (opcion == 2) {
        seccionView.iniciar();
      } else if (opcion == 0) {
        activo = false;
      } else {
        mostrarError("Opcion invalida.");
      }
    }
  }

  private int mostrarMenu() {
    System.out.println("\n--- GESTION ACADEMICA ---");
    System.out.println("1. Edificios y Aulas");
    System.out.println("2. Secciones");
    System.out.println("0. Volver al menu principal");
    System.out.print("Opcion: ");
    return leerEntero();
  }
}
