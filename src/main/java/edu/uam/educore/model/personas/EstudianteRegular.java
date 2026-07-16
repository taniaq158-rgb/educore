package edu.uam.educore.model.personas;

public class EstudianteRegular extends Estudiante {

  public EstudianteRegular(int id, String nombre, String apellidos, String email, String carnet) {
    super(id, nombre, apellidos, email, carnet);
  }

  @Override
  public double calcularMatricula() {
    return TARIFA_BASE;
  }

  @Override
  public boolean puedeMatricular() {
    return true;
  }

  @Override
  public String getTipo() {
    return "Estudiante Regular";
  }
}
