package com.japan.blog.servicio;

import java.util.List;

import com.japan.blog.modelo.Articulo;

public interface ServicioArticulo {
    public Articulo guardarArticulo(Articulo articulo);
    public List<Articulo> obtenerTodosArticulos();
    public List<Articulo> obtenerArticulosPorMarca(Integer idmarca);
    public List<Articulo> obtenerUltimosArticulosConImagen(int limite);
    public Articulo obtenerArticuloPorId(Integer id);
    public void eliminarArticulo(Integer id);
    public Articulo actualizarArticulo(Articulo articulo);
    public List<Articulo> obtenerArticulosPorAutor(Integer idusuario); // para el autor
}
