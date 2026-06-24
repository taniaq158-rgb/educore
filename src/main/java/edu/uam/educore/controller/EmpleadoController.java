/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.controller;

/**
 *
 * @author tania
 */

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.util.Validador;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmpleadoController {

  private final Repositorio<Empleado> repo;
  private int proximoId = 1;

  public EmpleadoController(Repositorio<Empleado> repo) {
    this.repo = repo;
  }

  public Empleado registrar(
      String nombre,
      String apellidos,
      String email,
      double salario,
      LocalDate fechaIngreso,
      TipoEmpleado tipo)
      throws Exception {
    validarBase(nombre, apellidos, email, salario, fechaIngreso, tipo);
    Empleado e = new Empleado(proximoId, nombre, apellidos, email, salario, fechaIngreso, tipo);
    repo.guardar(e);
    proximoId++;
    return e;
  }

  public List<Empleado> listar() throws Exception {
    return repo.buscarTodos();
  }

  public Empleado buscarPorId(int id) throws Exception {
    Optional<Empleado> resultado = repo.buscarPorId(id);
    if (resultado.isPresent()) {
      return resultado.get();
    }
    return null;
  }

  public Empleado actualizar(
      int id,
      String nombre,
      String apellidos,
      String email,
      double salario,
      LocalDate fechaIngreso,
      TipoEmpleado tipo)
      throws Exception {
    Empleado e = buscarPorId(id);
    if (e == null) {
      throw new IllegalArgumentException("No existe empleado con ID " + id + ".");
    }
    validarBase(nombre, apellidos, email, salario, fechaIngreso, tipo);
    e.setNombre(nombre);
    e.setApellidos(apellidos);
    e.setEmail(email);
    e.setSalario(salario);
    e.setFechaIngreso(fechaIngreso);
    e.setTipo(tipo);
    repo.actualizar(e);
    return e;
  }

  public void eliminar(int id) throws Exception {
    Empleado e = buscarPorId(id);
    if (e == null) {
      throw new IllegalArgumentException("No existe empleado con ID " + id + ".");
    }
    repo.eliminar(id);
  }

  // ── Helpers internos

  private void validarBase(
      String nombre,
      String apellidos,
      String email,
      double salario,
      LocalDate fechaIngreso,
      TipoEmpleado tipo) {
    if (nombre.isEmpty() || apellidos.isEmpty()) {
      throw new IllegalArgumentException("Nombre y apellidos son obligatorios.");
    }
    if (!Validador.validarEmail(email)) {
      throw new IllegalArgumentException("Email invalido (debe contener '@' y '.').");
    }
    if (salario < 0) {
      throw new IllegalArgumentException("El salario no puede ser negativo.");
    }
    if (!Validador.validarFechaIngreso(fechaIngreso)) {
      throw new IllegalArgumentException("Fecha de ingreso invalida (no puede ser futura).");
    }
    if (tipo == null) {
      throw new IllegalArgumentException("El tipo de empleado es obligatorio.");
    }
  }
}