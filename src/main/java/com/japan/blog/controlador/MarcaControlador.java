package com.japan.blog.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.japan.blog.modelo.Marca;
import com.japan.blog.servicio.ServicioMarca;

@Controller
@RequestMapping("/marcas")
public class MarcaControlador {

    @Autowired
    private ServicioMarca sm;

    @GetMapping("/listar")
    public String listarMarcas(Model m) {
        m.addAttribute("marcas", sm.obtenerTodasMarcas());
        return "marcas/listar"; //  listado marcas
    }

    @GetMapping("/crear")
    public String crearMarcaFormulario(Model m) {
        m.addAttribute("marca", new Marca());
        return "marcas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMarca(Marca marca) {
        sm.guardarMarca(marca);
        return "redirect:/marcas/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarMarcaFormulario(@PathVariable Integer id, Model m) {
        Marca marca = sm.obtenerMarcaPorId(id);
        if (marca != null) {
            m.addAttribute("marca", marca);
            return "marcas/formulario";
        }
        return "redirect:/marcas/listar";
    }

    @PostMapping("/actualizar")
    public String actualizarMarca(Marca marca) {
        sm.actualizarMarca(marca);
        return "redirect:/marcas/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Integer id) {
        sm.eliminarMarca(id);
        return "redirect:/marcas/listar";
    }
}
