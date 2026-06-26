/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.model.personas;

import edu.uam.educore.enums.TipoEmpleado;
import java.time.LocalDate;

public class Empleado extends Persona {

  private double salario;
  private LocalDate fechaIngreso;
  private TipoEmpleado tipo;

  public Empleado(
      int id,
      String nombre,
      String apellidos,
      String email,
      double salario,
      LocalDate fechaIngreso,
      TipoEmpleado tipo) {
    super(id, nombre, apellidos, email);
    this.salario = salario;
    this.fechaIngreso = fechaIngreso;
    this.tipo = tipo;
  }

  public double getSalario() {
    return salario;
  }

  public void setSalario(double salario) {
    this.salario = salario;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public void setTipo(TipoEmpleado tipo) {
    this.tipo = tipo;
  }

  @Override
  public String getTipo() {
    return tipo.toString();
  }

  public TipoEmpleado getTipoEnum() {
    return tipo;
  }

  @Override
  public String getInfo() {
    return String.format(
        "[%s] %s %s | Salario: ₡%.2f", getTipo(), getNombre(), getApellidos(), salario);
  }
}
