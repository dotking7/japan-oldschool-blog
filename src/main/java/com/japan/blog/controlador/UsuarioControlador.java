package com.japan.blog.controlador;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.japan.blog.modelo.Rol;
import com.japan.blog.modelo.Usuario;
import com.japan.blog.repositorio.RolRepositorio;
import com.japan.blog.servicio.ServicioUsuario;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private ServicioUsuario su;

    @Autowired
    private RolRepositorio rr;

    //  formulario de registro publico
    @GetMapping("/signup")
    public String registroFormulario(Model m) {
        m.addAttribute("usuario", new Usuario());
        return "usuarios/signup";
    }

    @PostMapping("/save")
    public String guardarUsuario(Usuario u, Model m) {
        // comprobamos que el nombre no este ya usado
        if (su.obtenerUsuarioPorUsername(u.getUsername()) != null) {
            m.addAttribute("error", "Ese nombre ya esta pillado. Elige otro.");
            m.addAttribute("usuario", u);
            return "usuarios/signup";
        }
        Rol rolUser = rr.findByRol("ROLE_USER");
        if (rolUser != null) {
            u.getRoles().add(rolUser); // por defecto ROLE_USER
        }
        su.guardarUsuario(u);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/listar")
    public String listarUsuarios(Model m) {
        m.addAttribute("usuarios", su.obtenerTodosUsuarios());
        return "usuarios/listar";
    }

    @GetMapping("/crear")
    public String crearUsuarioFormulario(Model m) {
        m.addAttribute("usuario", new Usuario());
        m.addAttribute("roles", rr.findAll());
        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuarioAdmin(Usuario u, @RequestParam(value = "rolSeleccionado", required = false) Integer rolSeleccionado, Model m) {
        if (su.obtenerUsuarioPorUsername(u.getUsername()) != null) {
            m.addAttribute("error", "Ese nombre de usuario ya existe.");
            m.addAttribute("usuario", u);
            m.addAttribute("roles", rr.findAll());
            return "usuarios/formulario";
        }
        u.setRoles(obtenerUnRol(rolSeleccionado));
        su.guardarUsuario(u);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuarioFormulario(@PathVariable Integer id, Model m) {
        Usuario u = su.obtenerUsuarioPorId(id);
        if (u != null) {
            u.setPassword(""); // vaciamos para que no aparezca en el form
            m.addAttribute("usuario", u);
            m.addAttribute("roles", rr.findAll());
            return "usuarios/formulario";
        }
        return "redirect:/usuarios/listar";
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(Usuario u, @RequestParam(value = "rolSeleccionado", required = false) Integer rolSeleccionado) {
        u.setRoles(obtenerUnRol(rolSeleccionado));
        su.actualizarUsuario(u);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        su.eliminarUsuario(id);
        return "redirect:/usuarios/listar";
    }

    // devuelve un set con un solo rol
    private Set<Rol> obtenerUnRol(Integer idRol) {
        Set<Rol> roles = new HashSet<>();
        if (idRol != null) {
            java.util.Optional<Rol> opt = rr.findById(idRol);
            if (opt.isPresent()) {
                roles.add(opt.get());
            }
        }
        //  si no viene nada le ponemos ROLE_USER
        if (roles.isEmpty()) {
            Rol rolUser = rr.findByRol("ROLE_USER");
            if (rolUser != null) {
                roles.add(rolUser);
            }
        }
        return roles;
    }
}
