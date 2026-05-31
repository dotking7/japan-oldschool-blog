package com.japan.blog.servicio;

import java.util.List;

import com.japan.blog.modelo.Marca;

public interface ServicioMarca {
    public Marca guardarMarca(Marca marca);
    public List<Marca> obtenerTodasMarcas();
    public Marca obtenerMarcaPorId(Integer id);
    public void eliminarMarca(Integer id);
    public Marca actualizarMarca(Marca marca);
}
