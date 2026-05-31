package com.japan.blog.servicioImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.japan.blog.modelo.Comentario;
import com.japan.blog.repositorio.ComentarioRepositorio;
import com.japan.blog.servicio.ServicioComentario;

@Service
public class ServicioComentarioImpl implements ServicioComentario {

    @Autowired
    private ComentarioRepositorio cr;

    @Override
    public Comentario guardarComentario(Comentario c) {
        return cr.save(c);
    }

    @Override
    public List<Comentario> obtenerTodosComentarios() {
        return cr.findAll();
    }

    @Override
    public Comentario obtenerComentarioPorId(Integer id) {
        java.util.Optional<Comentario> opt = cr.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    @Override
    public void eliminarComentario(Integer id) {
        cr.deleteById(id);
    }

    @Override
    public List<Comentario> obtenerComentariosPorArticulo(Integer idarticulo) {
        //  saca los comentarios del articulo
        return cr.findByArticuloIdarticulo(idarticulo);
    }
}
