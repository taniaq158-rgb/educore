/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.model.academico;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.ArrayList;
import java.util.List;

public class Seccion {

  private final int id;
  private String codigo;
  private String nombre;
  private Empleado docente;
  private Aula aula;
  private final List<Estudiante> estudiantes = new ArrayList<>();

  public Seccion(int id, String codigo, String nombre, Empleado docente, Aula aula) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.docente = docente;
    this.aula = aula;
  }

  public int getId() {
    return id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Empleado getDocente() {
    return docente;
  }

  public void setDocente(Empleado docente) {
    this.docente = docente;
  }

  public Aula getAula() {
    return aula;
  }

  public void setAula(Aula aula) {
    this.aula = aula;
  }

  public void agregarEstudiante(Estudiante estudiante) {
    estudiantes.add(estudiante);
  }

  public void removerEstudiante(int estudianteId) {
    estudiantes.removeIf(e -> e.getId() == estudianteId);
  }

  public List<Estudiante> listarEstudiantes() {
    return new ArrayList<>(estudiantes);
  }
}