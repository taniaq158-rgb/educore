package edu.uam.educore.model.personas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EstudianteBecadoTest {

  @Test
  void calcularMatricula_con_beca_50_porciento() {
    EstudianteBecado e = new EstudianteBecado(1, "Ana", "López", "a@uam.edu", "EST-002", 0.50);
    assertEquals(75000.0, e.calcularMatricula(), 0.01);
  }

  @Test
  void calcularMatricula_con_beca_100_porciento_es_cero() {
    EstudianteBecado e = new EstudianteBecado(2, "Carlos", "Ruiz", "c@uam.edu", "EST-003", 1.0);
    assertEquals(0.0, e.calcularMatricula(), 0.01);
  }

  @Test
  void calcularMatricula_sin_beca_igual_a_regular() {
    EstudianteBecado e = new EstudianteBecado(3, "Luis", "Mora", "l@uam.edu", "EST-004", 0.0);
    assertEquals(150000.0, e.calcularMatricula(), 0.01);
  }

  @Test
  void puedeMatricular_retorna_true_por_defecto() {
    EstudianteBecado e = new EstudianteBecado(4, "Sofía", "Vega", "s@uam.edu", "EST-005", 0.5);
    assertTrue(e.puedeMatricular());
  }
}
