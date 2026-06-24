
package edu.uam.educore.view;
import java.util.Scanner;
/**
 *
 * @author tania
 */

public class AcademicoView extends VistaBase {

  private final EdificioView edificioView;
  // private final SeccionView seccionView; // TODO: agregar cuando esté listo el módulo Sección

  public AcademicoView(Scanner scanner, EdificioView edificioView) {
    super(scanner);
    this.edificioView = edificioView;
  }

  public void iniciar() {
    boolean activo = true;
    while (activo) {
      int opcion = mostrarMenu();
      if (opcion == 1) {
        edificioView.iniciar();
      } else if (opcion == 2) {
        mostrarMensaje("Gestion de secciones — pendiente (mismo patron que Estudiantes)");
        // seccionView.iniciar();
      } else if (opcion == 0) {
        activo = false;
      } else {
        mostrarError("Opcion invalida.");
      }
    }
  }

  private int mostrarMenu() {
    System.out.println("\n_____ GESTION ACADEMICA _____");
    System.out.println("1. Edificios y Aulas");
    System.out.println("2. Secciones");
    System.out.println("0. Volver al menu principal");
    System.out.print("Opcion: ");
    return leerEntero();
  }
}