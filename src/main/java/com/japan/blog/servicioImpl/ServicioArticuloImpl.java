package com.japan.blog.servicioImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.japan.blog.modelo.Articulo;
import com.japan.blog.repositorio.ArticuloRepositorio;
import com.japan.blog.servicio.ServicioArticulo;

@Service
public class ServicioArticuloImpl implements ServicioArticulo {

    @Autowired
    private ArticuloRepositorio ar;

    @Override
    public Articulo guardarArticulo(Articulo articulo) {
        return ar.save(articulo);
    }

    @Override
    public List<Articulo> obtenerTodosArticulos() {
        return ar.findAllByOrderByFechaDesc();
    }

    @Override
    public List<Articulo> obtenerArticulosPorMarca(Integer idmarca) {
        return ar.findByMarcaIdmarcaOrderByFechaDesc(idmarca);
    }

    @Override
    public List<Articulo> obtenerUltimosArticulosConImagen(int limite) {
        // saco los ultimos con imagen y me quedo con los que me piden
        List<Articulo> todos = ar.findTop4ByImagenIsNotNullAndImagenNotOrderByFechaDesc("no-image.png");
        List<Articulo> resultado = new ArrayList<>();
        int i = 0;
        for (Articulo a : todos) {
            if (i >= limite) break;
            resultado.add(a);
            i++;
        }
        return resultado;
    }

    @Override
    public Articulo obtenerArticuloPorId(Integer id) {
        java.util.Optional<Articulo> opt = ar.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null; // si no existe devuelvo null
    }

    @Override
    public void eliminarArticulo(Integer id) {
        ar.deleteById(id);
    }

    @Override
    public Articulo actualizarArticulo(Articulo articulo) {
        return ar.save(articulo);
    }

    @Override
    public List<Articulo> obtenerArticulosPorAutor(Integer idusuario) {
        //  solo los articulos de este usuario
        return ar.findByAutorIdusuarioOrderByFechaDesc(idusuario);
    }
}
