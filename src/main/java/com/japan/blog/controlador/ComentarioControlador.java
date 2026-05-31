package com.japan.blog.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.japan.blog.modelo.Articulo;
import com.japan.blog.modelo.Comentario;
import com.japan.blog.modelo.Usuario;
import com.japan.blog.servicio.ServicioArticulo;
import com.japan.blog.servicio.ServicioComentario;
import com.japan.blog.servicio.ServicioUsuario;

@Controller
@RequestMapping("/comentarios")
public class ComentarioControlador {

    @Autowired
    private ServicioComentario sc;

    @Autowired
    private ServicioArticulo sa;

    @Autowired
    private ServicioUsuario su;

    //  cualquier usuario logueado puede escribir un comentario
    @PostMapping("/guardar/{idArticulo}")
    public String guardarComentario(@PathVariable Integer idArticulo, @RequestParam String texto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario u = su.obtenerUsuarioPorUsername(auth.getName());
        Articulo a = sa.obtenerArticuloPorId(idArticulo);

        if (u != null && a != null) {
            Comentario c = new Comentario();
            c.setTexto(texto);
            c.setUsuario(u);
            c.setArticulo(a);
            sc.guardarComentario(c);
            System.out.println("comentario guardado de: " + u.getUsername());
        }
        return "redirect:/articulos/view/" + idArticulo;
    }

    //  borrar comentario - solo llega el admin, la seguridad lo controla
    @GetMapping("/eliminar/{idComentario}")
    public String eliminarComentario(@PathVariable Integer idComentario) {
        Comentario c = sc.obtenerComentarioPorId(idComentario);
        if (c != null) {
            Integer idArt = c.getArticulo().getIdarticulo();
            sc.eliminarComentario(idComentario);
            return "redirect:/articulos/view/" + idArt;
        }
        return "redirect:/";
    }

    //  el admin puede editar cualquier comentario
    @PostMapping("/editar/{idComentario}")
    public String editarComentario(@PathVariable Integer idComentario, @RequestParam String texto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Comentario c = sc.obtenerComentarioPorId(idComentario);

        if (c != null) {
            // comprobamos que sea admin
            boolean esAdmin = false;
            for (var authority : auth.getAuthorities()) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    esAdmin = true;
                    break;
                }
            }
            if (esAdmin) {
                c.setTexto(texto);
                sc.guardarComentario(c);
            }
            return "redirect:/articulos/view/" + c.getArticulo().getIdarticulo();
        }
        return "redirect:/";
    }
}
