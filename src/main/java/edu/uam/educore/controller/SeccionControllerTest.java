
package edu.uam.educore.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.uam.educore.dao.ListaEdificioRepo;
import edu.uam.educore.dao.ListaEmpleadoRepo;
import edu.uam.educore.dao.ListaEstudianteRepo;
import edu.uam.educore.dao.ListaSeccionRepo;
import edu.uam.educore.enums.TipoAula;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.EstudianteRegular;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class SeccionControllerTest {

  @Test
  void registrar_rechaza_empleado_que_no_es_docente() throws Exception {
    ListaSeccionRepo seccionRepo = new ListaSeccionRepo();
    ListaEmpleadoRepo empleadoRepo = new ListaEmpleadoRepo();
    ListaEstudianteRepo estudianteRepo = new ListaEstudianteRepo();
    ListaEdificioRepo edificioRepo = new ListaEdificioRepo();

    Empleado guarda =
        new Empleado(1, "Carlos", "Mora", "c@uam.edu", 500000.0, LocalDate.of(2023, 1, 1), TipoEmpleado.GUARDA);
    empleadoRepo.guardar(guarda);

    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula = new Aula(1, "101", 30, TipoAula.REGULAR, edificio);
    edificio.agregarAula(aula);
    edificioRepo.guardar(edificio);

    SeccionController controller =
        new SeccionController(seccionRepo, empleadoRepo, estudianteRepo, edificioRepo);

    Exception ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> controller.registrar("PROG3-01", "Programacion III", 1, 1));
    assertTrue(ex.getMessage().contains("DOCENTE"));
  }

  @Test
  void agregarEstudiante_rechaza_id_inexistente() throws Exception {
    ListaSeccionRepo seccionRepo = new ListaSeccionRepo();
    ListaEmpleadoRepo empleadoRepo = new ListaEmpleadoRepo();
    ListaEstudianteRepo estudianteRepo = new ListaEstudianteRepo();
    ListaEdificioRepo edificioRepo = new ListaEdificioRepo();

    Empleado docente =
        new Empleado(1, "Tania", "Rojas", "t@uam.edu", 800000.0, LocalDate.of(2022, 1, 1), TipoEmpleado.DOCENTE);
    empleadoRepo.guardar(docente);

    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula = new Aula(1, "101", 30, TipoAula.REGULAR, edificio);
    edificio.agregarAula(aula);
    edificioRepo.guardar(edificio);

    SeccionController controller =
        new SeccionController(seccionRepo, empleadoRepo, estudianteRepo, edificioRepo);
    controller.registrar("PROG3-01", "Programación III", 1, 1);

    Exception ex =
        assertThrows(
            IllegalArgumentException.class, () -> controller.agregarEstudiante(1, 999));
    assertTrue(ex.getMessage().contains("999"));
  }

  @Test
  void registrar_crea_la_seccion_con_docente_y_aula_validos() throws Exception {
    ListaSeccionRepo seccionRepo = new ListaSeccionRepo();
    ListaEmpleadoRepo empleadoRepo = new ListaEmpleadoRepo();
    ListaEstudianteRepo estudianteRepo = new ListaEstudianteRepo();
    ListaEdificioRepo edificioRepo = new ListaEdificioRepo();

    Empleado docente =
        new Empleado(1, "Tania", "Rojas", "t@uam.edu", 800000.0, LocalDate.of(2022, 1, 1), TipoEmpleado.DOCENTE);
    empleadoRepo.guardar(docente);

    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula = new Aula(1, "101", 30, TipoAula.REGULAR, edificio);
    edificio.agregarAula(aula);
    edificioRepo.guardar(edificio);

    SeccionController controller =
        new SeccionController(seccionRepo, empleadoRepo, estudianteRepo, edificioRepo);

    var seccion = controller.registrar("PROG3-01", "Programacion III", 1, 1);

    assertEquals("PROG3-01", seccion.getCodigo());
    assertEquals(docente, seccion.getDocente());
    assertEquals(aula, seccion.getAula());
  }
}