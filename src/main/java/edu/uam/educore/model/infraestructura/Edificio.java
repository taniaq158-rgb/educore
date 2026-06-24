/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.model.infraestructura;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author tania
 */

public class Edificio {

  private final int id;
  private String codigo;
  private String nombre;
  private final List<Aula> aulas = new ArrayList<>();

  public Edificio(int id, String codigo, String nombre) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
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

  public void agregarAula(Aula aula) {
    aulas.add(aula);
  }

  public void eliminarAula(int aulaId) {
    aulas.removeIf(a -> a.getId() == aulaId);
  }

  public List<Aula> listarAulas() {
    return new ArrayList<>(aulas);
  }

  public Optional<Aula> buscarAulaPorId(int aulaId) {
    for (Aula a : aulas) {
      if (a.getId() == aulaId) {
        return Optional.of(a);
      }
    }
    return Optional.empty();
  }

  public Optional<Aula> buscarAulaPorNumero(String numero) {
    for (Aula a : aulas) {
      if (a.getNumero().equalsIgnoreCase(numero)) {
        return Optional.of(a);
      }
    }
    return Optional.empty();
  }
}