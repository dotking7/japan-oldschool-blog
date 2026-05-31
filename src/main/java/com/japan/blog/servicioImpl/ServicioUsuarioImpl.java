package com.japan.blog.servicioImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.japan.blog.modelo.Usuario;
import com.japan.blog.repositorio.UsuarioRepositorio;
import com.japan.blog.servicio.ServicioUsuario;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario guardarUsuario(Usuario u) {
        //  ciframos la pass
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return ur.save(u);
    }

    @Override
    public Usuario actualizarUsuario(Usuario u) {
        java.util.Optional<Usuario> opt = ur.findById(u.getIdusuario());
        if (!opt.isPresent()) {
            return null;
        }
        Usuario existe = opt.get();
        existe.setUsername(u.getUsername());
        existe.setRoles(u.getRoles());
        // solo cambiamos pass si viene algo
        if (StringUtils.hasText(u.getPassword())) {
            existe.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        return ur.save(existe);
    }

    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        return ur.findByUsername(username);
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return ur.findAll();
    }

    @Override
    public Usuario obtenerUsuarioPorId(Integer id) {
        java.util.Optional<Usuario> opt = ur.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    @Override
    public void eliminarUsuario(Integer id) {
        ur.deleteById(id);
    }
}
