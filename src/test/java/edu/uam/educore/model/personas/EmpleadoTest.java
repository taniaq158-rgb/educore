

package edu.uam.educore.model.personas;
import static org.junit.jupiter.api.Assertions.*;
import edu.uam.educore.enums.TipoEmpleado;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
/**
 *
 * @author tania
 */


class EmpleadoTest {

  @Test
  void constructor_asigna_atributos_correctamente() {
    LocalDate ingreso = LocalDate.of(2024, 3, 1);
    Empleado e =
        new Empleado(1, "Tania", "Rojas", "t@uam.edu", 850000.0, ingreso, TipoEmpleado.DOCENTE);

    assertEquals(1, e.getId());
    assertEquals("Tania", e.getNombre());
    assertEquals("Rojas", e.getApellidos());
    assertEquals("t@uam.edu", e.getEmail());
    assertEquals(850000.0, e.getSalario(), 0.01);
    assertEquals(ingreso, e.getFechaIngreso());
  }

  @Test
  void getTipo_retorna_texto_correcto() {
    Empleado e =
        new Empleado(
            1, "Carlos", "Mora", "c@uam.edu", 600000.0, LocalDate.of(2023, 1, 15), TipoEmpleado.GUARDA);
    assertEquals("GUARDA", e.getTipo());
  }

  @Test
  void getInfo_incluye_nombre_apellidos_y_salario() {
    Empleado e =
        new Empleado(
            1,
            "Carlos",
            "Mora",
            "c@uam.edu",
            600000.0,
            LocalDate.of(2023, 1, 15),
            TipoEmpleado.MANTENIMIENTO);
    String info = e.getInfo();

    assertTrue(info.contains("Carlos"));
    assertTrue(info.contains("Mora"));
    assertTrue(info.contains("600000"));
    assertTrue(info.contains("MANTENIMIENTO"));
  }

  @Test
  void setTipo_actualiza_el_tipo_y_getInfo_lo_refleja() {
    Empleado e =
        new Empleado(
            1, "Ana", "Vega", "a@uam.edu", 500000.0, LocalDate.of(2022, 6, 10), TipoEmpleado.ADMINISTRATIVO);

    e.setTipo(TipoEmpleado.DOCENTE);

    assertEquals("DOCENTE", e.getTipo());
    assertTrue(e.getInfo().contains("DOCENTE"));
  }
}