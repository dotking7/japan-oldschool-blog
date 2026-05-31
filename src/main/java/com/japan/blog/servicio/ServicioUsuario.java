package com.japan.blog.servicio;

import java.util.List;

import com.japan.blog.modelo.Usuario;

public interface ServicioUsuario {
    public Usuario guardarUsuario(Usuario usuario);
    public Usuario actualizarUsuario(Usuario usuario);
    public Usuario obtenerUsuarioPorUsername(String username);
    public List<Usuario> obtenerTodosUsuarios();
    public Usuario obtenerUsuarioPorId(Integer id);
    public void eliminarUsuario(Integer id);
}
