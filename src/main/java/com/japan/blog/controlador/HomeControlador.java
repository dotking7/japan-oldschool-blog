package com.japan.blog.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.japan.blog.modelo.Marca;
import com.japan.blog.servicio.ServicioArticulo;
import com.japan.blog.servicio.ServicioMarca;

@Controller
public class HomeControlador {

    @Autowired
    private ServicioArticulo sa;

    @Autowired
    private ServicioMarca sm;

    @GetMapping("/")
    public String inicio(@RequestParam(value = "marca", required = false) Integer idmarca, Model m) {
        Marca marcaSel = null;
        if (idmarca != null) {
            marcaSel = sm.obtenerMarcaPorId(idmarca);
            m.addAttribute("articulos", sa.obtenerArticulosPorMarca(idmarca)); // filtrados por marca
        } else {
            m.addAttribute("articulos", sa.obtenerTodosArticulos()); //  todos
        }
        m.addAttribute("marcas", sm.obtenerTodasMarcas());
        m.addAttribute("marcaSeleccionada", marcaSel);
        m.addAttribute("carrusel", sa.obtenerUltimosArticulosConImagen(4));
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // pagina de login
    }
}
