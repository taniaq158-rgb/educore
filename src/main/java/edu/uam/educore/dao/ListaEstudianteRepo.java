package edu.uam.educore.dao;

import edu.uam.educore.model.personas.Estudiante;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListaEstudianteRepo extends Repositorio<Estudiante> {

  private final List<Estudiante> lista = new ArrayList<>();

  @Override
  public void guardar(Estudiante e) {
    lista.add(e);
  }

  @Override
  public void actualizar(Estudiante actualizado) {
    for (int i = 0; i < lista.size(); i++) {
      if (lista.get(i).getId() == actualizado.getId()) {
        lista.set(i, actualizado);
        return;
      }
    }
  }

  @Override
  public void eliminar(int id) {
    for (int i = 0; i < lista.size(); i++) {
      if (lista.get(i).getId() == id) {
        lista.remove(i);
        return;
      }
    }
  }

  @Override
  public Optional<Estudiante> buscarPorId(int id) {
    for (Estudiante e : lista) {
      if (e.getId() == id) {
        return Optional.of(e);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Estudiante> buscarTodos() {
    return new ArrayList<>(lista);
  }
}
