package com.japan.blog.controlador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.japan.blog.modelo.Articulo;
import com.japan.blog.modelo.Rol;
import com.japan.blog.modelo.Usuario;
import com.japan.blog.servicio.ServicioArticulo;
import com.japan.blog.servicio.ServicioMarca;
import com.japan.blog.servicio.ServicioUsuario;

@Controller
@RequestMapping("/articulos")
public class ArticuloControlador {

    @Autowired
    private ServicioArticulo sa;

    @Autowired
    private ServicioMarca sm;

    @Autowired
    private ServicioUsuario su;

    @Value("${japan.ruta.imagenes}")
    private String rutaImagenes;
    // /Users/seve/prog/japan/imagenes-subidas/

    @GetMapping("/listar")
    public String listarArticulos(Model m) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario u = su.obtenerUsuarioPorUsername(auth.getName());

        //  admin ve todos, autor solo los suyos
        if (tieneRol(auth, "ROLE_ADMIN")) {
            m.addAttribute("articulos", sa.obtenerTodosArticulos());
        } else {
            if (u != null) {
                m.addAttribute("articulos", sa.obtenerArticulosPorAutor(u.getIdusuario()));
            }
        }
        return "articulos/listar";
    }

    @GetMapping("/crear")
    public String crearArticuloFormulario(Model m) {
        Articulo a = new Articulo();
        a.setFecha(LocalDate.now()); // fecha de hoy por defecto
        m.addAttribute("articulo", a);
        m.addAttribute("marcas", sm.obtenerTodasMarcas());
        return "articulos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarArticulo(Articulo a, @RequestParam("archivoImagen") MultipartFile archivoImagen, @RequestParam("idmarca") Integer idmarca) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario u = su.obtenerUsuarioPorUsername(auth.getName());

        if (a.getFecha() == null) {
            a.setFecha(LocalDate.now());
        }
        a.setMarca(sm.obtenerMarcaPorId(idmarca));
        a.setAutor(u); // guardamos  quien lo escribio
        guardarImagen(a, archivoImagen);
        sa.guardarArticulo(a);
        System.out.println("articulo guardado por: " + auth.getName());
        return "redirect:/articulos/listar";
    }

    @GetMapping("/view/{id}")
    public String verArticulo(@PathVariable Integer id, Model m) {
        Articulo a = sa.obtenerArticuloPorId(id);
        if (a != null) {
            m.addAttribute("articulo", a);
            //  nombre que se muestra en el blog
            m.addAttribute("nombreAutorArticulo", resolverNombre(a.getAutor()));
            return "articulos/detalle";
        }
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String editarArticuloFormulario(@PathVariable Integer id, Model m) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Articulo a = sa.obtenerArticuloPorId(id);

        if (a != null) {
            // verificamos que sea suyo o que sea admin
            if (!puedeModificarArticulo(auth, a)) {
                return "redirect:/articulos/listar";
            }
            if (a.getFecha() == null) {
                a.setFecha(LocalDate.now());
            }
            m.addAttribute("articulo", a);
            m.addAttribute("marcas", sm.obtenerTodasMarcas());
            return "articulos/formulario";
        }
        return "redirect:/articulos/listar";
    }

    @PostMapping("/actualizar")
    public String actualizarArticulo(Articulo a, @RequestParam("archivoImagen") MultipartFile archivoImagen, @RequestParam("idmarca") Integer idmarca) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Articulo existe = sa.obtenerArticuloPorId(a.getIdarticulo());

        if (existe != null) {
            // por si alguien accede directo por url sin permiso
            if (!puedeModificarArticulo(auth, existe)) {
                return "redirect:/articulos/listar";
            }
            existe.setTitulo(a.getTitulo());
            existe.setTexto(a.getTexto());
            existe.setFecha(a.getFecha() != null ? a.getFecha() : LocalDate.now());
            existe.setMarca(sm.obtenerMarcaPorId(idmarca));
            guardarImagen(existe, archivoImagen);
            sa.actualizarArticulo(existe);
        }
        return "redirect:/articulos/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarArticulo(@PathVariable Integer id) {
        sa.eliminarArticulo(id); // solo llega el admin
        return "redirect:/articulos/listar";
    }

    // comprueba si el articulo lo puede tocar este usuario
    private boolean puedeModificarArticulo(Authentication auth, Articulo a) {
        if (tieneRol(auth, "ROLE_ADMIN")) return true;
        if (a.getAutor() == null) return false;
        return a.getAutor().getUsername().equals(auth.getName());
    }

    private boolean tieneRol(Authentication auth, String rol) {
        for (var authority : auth.getAuthorities()) {
            if (rol.equals(authority.getAuthority())) return true;
        }
        return false;
    }

    //  si es admin mostramos JapanOldschool, si no su nombre de usuario
    private String resolverNombre(Usuario u) {
        if (u == null) return "JapanOldschool";
        if (u.getRoles() == null || u.getRoles().isEmpty()) return u.getUsername();
        for (Rol r : u.getRoles()) {
            if (r != null && "ROLE_ADMIN".equals(r.getRol())) {
                return "JapanOldschool";
            }
        }
        return u.getUsername();
    }

    // guarda la imagen en la carpeta del servidor
    private void guardarImagen(Articulo a, MultipartFile archivoImagen) {
        if (archivoImagen != null && !archivoImagen.isEmpty()) {
            String nombreImg = UUID.randomUUID().toString() + "_" + archivoImagen.getOriginalFilename();
            a.setImagen(nombreImg);
            try {
                Path rootPath = Paths.get(rutaImagenes).toAbsolutePath();
                if (!Files.exists(rootPath)) {
                    Files.createDirectories(rootPath);
                }
                Files.copy(archivoImagen.getInputStream(), rootPath.resolve(nombreImg));
                System.out.println("imagen guardada: " + nombreImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (a.getImagen() == null || a.getImagen().isBlank()) {
            a.setImagen("no-image.png");
        }
    }
}
