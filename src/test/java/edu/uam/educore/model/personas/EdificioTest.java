

package edu.uam.educore.model.infraestructura;

import static org.junit.jupiter.api.Assertions.*;

import edu.uam.educore.enums.TipoAula;
import org.junit.jupiter.api.Test;

class EdificioTest {

  @Test
  void agregarAula_la_asocia_al_edificio_correcto() {
    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula = new Aula(1, "101", 30, TipoAula.REGULAR, edificio);

    edificio.agregarAula(aula);

    assertEquals(1, edificio.listarAulas().size());
    assertEquals(edificio, aula.getEdificio());
  }

  @Test
  void buscarAulaPorId_encuentra_el_aula_correcta() {
    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula1 = new Aula(1, "101", 30, TipoAula.REGULAR, edificio);
    Aula aula2 = new Aula(2, "102", 25, TipoAula.LABORATORIO, edificio);
    edificio.agregarAula(aula1);
    edificio.agregarAula(aula2);

    var resultado = edificio.buscarAulaPorId(2);

    assertTrue(resultado.isPresent());
    assertEquals("102", resultado.get().getNumero());
  }

  @Test
  void eliminarAula_quita_el_aula_de_la_lista_del_edificio() {
    Edificio edificio = new Edificio(1, "A", "Edificio Central");
    Aula aula = new Aula(1, "101", 30, TipoAula.REGULAR, edificio);
    edificio.agregarAula(aula);

    edificio.eliminarAula(1);

    assertTrue(edificio.listarAulas().isEmpty());
  }
}