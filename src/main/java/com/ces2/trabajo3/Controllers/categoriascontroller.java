package com.ces2.trabajo3.Controllers;

import com.ces2.trabajo3.Models.Categoria;
import com.ces2.trabajo3.Models.TipoCategoria;
import com.ces2.trabajo3.Repositories.categoria;
import com.ces2.trabajo3.Repositories.productocategoria;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class categoriascontroller {

    private final categoria repo;
    private final productocategoria repoPivot;

    public categoriascontroller(categoria repo, productocategoria repoPivot) {
        this.repo = repo;
        this.repoPivot = repoPivot;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", repo.findAll(0, 100));
        return "categorias/listar";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("tipos", TipoCategoria.values());
        return "categorias/crear";
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute Categoria categoria,
                        BindingResult br,
                        RedirectAttributes ra) {

        if (br.hasErrors()) {
            return "categorias/crear";
        }

        Categoria nueva = repo.create(categoria);

        if (categoria.getProductoid() != null) {
            repoPivot.asignarCategoria(categoria.getProductoid(), nueva.getId());
            return "redirect:/productos/detalles/" + categoria.getProductoid();
        }

        ra.addFlashAttribute("msg", "Categoría creada correctamente");
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", repo.findById(id).orElseThrow());
        model.addAttribute("tipos", TipoCategoria.values());
        return "categorias/editar";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                         @Valid @ModelAttribute Categoria categoria,
                         BindingResult br,
                         RedirectAttributes ra) {

        if (br.hasErrors()) {
            return "categorias/editar";
        }

        categoria.setId(id);
        repo.update(categoria);

        ra.addFlashAttribute("msg", "Categoría actualizada correctamente");
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        repo.delete(id);
        ra.addFlashAttribute("msg", "Categoría eliminada");
        return "redirect:/categorias";
    }

    @GetMapping("/asignar/{productoid}")
    public String asignarCategoriaForm(@PathVariable Integer productoid, Model model) {

        model.addAttribute("categorias", repo.findAll(0, 100));
        model.addAttribute("productoid", productoid);

        return "categorias/asignar";
    }

    @PostMapping("/asignar")
    public String asignarCategoria(@RequestParam Integer productoid,
                                   @RequestParam Integer categoriaid,
                                   RedirectAttributes ra) {

        repoPivot.asignarCategoria(productoid, categoriaid);

        ra.addFlashAttribute("msg", "Categoría asignada correctamente");
        return "redirect:/productos/detalles/" + productoid;
    }
}
