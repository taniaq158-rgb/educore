/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.model.infraestructura;
import edu.uam.educore.enums.TipoAula;
/**
 *
 * @author tania
 */

public class Aula {

  private final int id;
  private String numero;
  private int capacidad;
  private TipoAula tipo;
  private final Edificio edificio;

  public Aula(int id, String numero, int capacidad, TipoAula tipo, Edificio edificio) {
    this.id = id;
    this.numero = numero;
    this.capacidad = capacidad;
    this.tipo = tipo;
    this.edificio = edificio;
  }

  public int getId() {
    return id;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public int getCapacidad() {
    return capacidad;
  }

  public void setCapacidad(int capacidad) {
    this.capacidad = capacidad;
  }

  public TipoAula getTipo() {
    return tipo;
  }

  public void setTipo(TipoAula tipo) {
    this.tipo = tipo;
  }

  public Edificio getEdificio() {
    return edificio;
  }
}