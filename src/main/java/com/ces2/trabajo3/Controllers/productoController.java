package com.ces2.trabajo3.Controllers;

import com.ces2.trabajo3.Models.Producto;
import com.ces2.trabajo3.Repositories.producto;
import com.ces2.trabajo3.Repositories.productocategoria;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class productoController {

    private final producto repo;
    private final productocategoria repoPivot;

    public productoController(producto repo, productocategoria repoPivot) {
        this.repo = repo;
        this.repoPivot = repoPivot;
    }

    @GetMapping
    public String listarPaginado(
            Model model,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 5;
        int offset = page * pageSize;

        List<Producto> productos = repo.findAll(pageSize, offset);
        long total = repo.count();

        int totalPages = (int) Math.ceil((double) total / pageSize);

        model.addAttribute("productos", productos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "productos/listar";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/crear";
    }

    @PostMapping("/crear")
    public String crear(
            @Valid @ModelAttribute Producto p,
            BindingResult br,
            RedirectAttributes ra
    ) {

        if (br.hasErrors()) {
            return "productos/crear";
        }

        if (repo.existsByNombre(p.getNombre())) {
            br.rejectValue("nombre", "error.nombre",
                    "Ya existe un producto con ese nombre");
            return "productos/crear";
        }

        repo.save(p);
        ra.addFlashAttribute("msg", "Producto creado exitosamente");
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {

        Producto p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        model.addAttribute("producto", p);
        return "productos/editar";
    }

    @PostMapping("/editar")
    public String editar(
            @Valid @ModelAttribute Producto p,
            BindingResult br,
            RedirectAttributes ra
    ) {

        if (br.hasErrors()) {
            return "productos/editar";
        }

        repo.save(p);
        ra.addFlashAttribute("msg", "Producto actualizado");

        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {

        repo.deleteById(id);
        ra.addFlashAttribute("msg", "Producto eliminado");

        return "redirect:/productos";
    }

    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model) {

        Producto p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        p.setCategorias(repoPivot.findCategoriasByProducto(id));

        model.addAttribute("producto", p);

        return "productos/detalles";
    }

    @GetMapping("/buscar")
    public String buscarForm() {
        return "productos/buscar";
    }

    @PostMapping("/buscar")
    public String buscarPorId(@RequestParam Integer id, Model model) {

        var resultado = repo.findById(id);

        if (resultado.isEmpty()) {
            model.addAttribute("error", "No existe un producto con ID " + id);
            return "productos/buscar";
        }

        model.addAttribute("producto", resultado.get());
        return "productos/buscar-resultado";
    }
}
