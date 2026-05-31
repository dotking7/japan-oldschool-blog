package com.japan.blog.servicio;

import java.util.List;

import com.japan.blog.modelo.Comentario;

public interface ServicioComentario {
    public Comentario guardarComentario(Comentario comentario);
    public List<Comentario> obtenerTodosComentarios();
    public Comentario obtenerComentarioPorId(Integer id);
    public void eliminarComentario(Integer id);
    public List<Comentario> obtenerComentariosPorArticulo(Integer idarticulo);
}
