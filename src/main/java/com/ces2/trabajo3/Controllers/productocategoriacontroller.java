package com.ces2.trabajo3.Controllers;

import com.ces2.trabajo3.Repositories.categoria;
import com.ces2.trabajo3.Repositories.productocategoria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/producto-categorias")
public class productocategoriacontroller {

    private final categoria categoriaRepo;
    private final productocategoria relacionRepo;

    public productocategoriacontroller(categoria categoriaRepo, productocategoria relacionRepo) {
        this.categoriaRepo = categoriaRepo;
        this.relacionRepo = relacionRepo;
    }

    @GetMapping("/asignar/{productoid}")
    public String asignarForm(@PathVariable Integer productoid, Model model) {
        model.addAttribute("productoid", productoid);
        model.addAttribute("categorias", categoriaRepo.findAll(0, 100));
        return "categorias/asignar";
    }

    @PostMapping("/asignar")
    public String asignar(@RequestParam Integer productoid, @RequestParam Integer categoriaid) {
        relacionRepo.asignarCategoria(productoid, categoriaid);
        return "redirect:/productos/detalles/" + productoid;
    }

    @GetMapping("/eliminar/{productoid}/{categoriaid}")
    public String eliminar(@PathVariable Integer productoid, @PathVariable Integer categoriaid) {
        relacionRepo.eliminarCategoria(productoid, categoriaid);
        return "redirect:/productos/detalles/" + productoid;
    }

    @GetMapping("/editar/{productoid}/{categoriaid}")
    public String editarForm(@PathVariable Integer productoid,
                             @PathVariable Integer categoriaid,
                             Model model) {

        model.addAttribute("productoid", productoid);
        model.addAttribute("categoriaActual", categoriaid);

        model.addAttribute("categorias", categoriaRepo.findAll(0, 100));

        return "categorias/editar-asignacion";
    }

    @PostMapping("/editar")
    public String editarRelacion(@RequestParam Integer productoid,
                                 @RequestParam Integer categoriaOriginal,
                                 @RequestParam Integer categoriaNueva) {

        relacionRepo.eliminarCategoria(productoid, categoriaOriginal);
        relacionRepo.asignarCategoria(productoid, categoriaNueva);

        return "redirect:/productos/detalles/" + productoid;
    }

    @GetMapping("/buscar")
    public String buscarForm() {
        return "categorias/buscar-categorias";
    }

    @PostMapping("/buscar")
    public String buscarResultado(@RequestParam Integer id, Model model) {
        model.addAttribute("productoid", id);
        model.addAttribute("categorias", relacionRepo.findCategoriasByProducto(id));
        return "categorias/buscar-categorias-resultado";
    }
}
