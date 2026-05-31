package com.japan.blog.servicioImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.japan.blog.modelo.Marca;
import com.japan.blog.repositorio.MarcaRepositorio;
import com.japan.blog.servicio.ServicioMarca;

@Service
public class ServicioMarcaImpl implements ServicioMarca {

    @Autowired
    private MarcaRepositorio mr;

    @Override
    public Marca guardarMarca(Marca m) {
        return mr.save(m);
    }

    @Override
    public List<Marca> obtenerTodasMarcas() {
        return mr.findAll();
    }

    @Override
    public Marca obtenerMarcaPorId(Integer id) {
        java.util.Optional<Marca> opt = mr.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    @Override
    public void eliminarMarca(Integer id) {
        mr.deleteById(id);
    }

    @Override
    public Marca actualizarMarca(Marca m) {
        return mr.save(m);
    }
}
