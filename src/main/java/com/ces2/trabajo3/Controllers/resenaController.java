package com.ces2.trabajo3.Controllers;

import com.ces2.trabajo3.Models.Resena;
import com.ces2.trabajo3.Repositories.resena;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resenas")
public class resenaController {

    private final resena repo;

    public resenaController(resena repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listarGeneral(Model model) {
        model.addAttribute("resenas", repo.findAll(100, 0));
        return "resenas/listar";
    }

    @GetMapping("/listar/{productoid}")
    public String listarPorProducto(@PathVariable Integer productoid, Model model) {
        model.addAttribute("resenas", repo.findByProducto(productoid));
        model.addAttribute("productoid", productoid);
        return "resenas/listar";
    }

    @GetMapping("/crear")
    public String crearFormGeneral(Model model) {
        model.addAttribute("resena", new Resena());
        return "resenas/crear";
    }

    @GetMapping("/crear/{productoid}")
    public String crearForm(@PathVariable Integer productoid, Model model) {

        Resena r = new Resena();
        r.setProductoId(productoid);

        model.addAttribute("resena", r);
        model.addAttribute("productoid", productoid);

        return "resenas/crear";
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute Resena r,
                        BindingResult br) {

        if (br.hasErrors()) {
            return "resenas/crear";
        }

        repo.create(r);

        if (r.getProductoId() == null) {
            return "redirect:/resenas";
        }

        return "redirect:/resenas/listar/" + r.getProductoId();
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {
        model.addAttribute("resena", repo.findById(id).orElseThrow());
        return "resenas/editar";
    }

    @PostMapping("/editar")
    public String editar(@Valid @ModelAttribute Resena r,
                         BindingResult br) {

        if (br.hasErrors()) {
            return "resenas/editar";
        }

        repo.update(r);

        if (r.getProductoId() == null) {
            return "redirect:/resenas";
        }

        return "redirect:/resenas/listar/" + r.getProductoId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        Integer productoId = repo.findById(id)
                .orElseThrow()
                .getProductoId();

        repo.delete(id);

        if (productoId == null) {
            return "redirect:/resenas";
        }

        return "redirect:/resenas/listar/" + productoId;
    }
    @GetMapping("/buscar")
    public String buscarForm() {
        return "resenas/buscar";
    }
    @PostMapping("/buscar")
    public String buscar(@RequestParam Integer id, Model model) {

        Resena r = repo.findById(id).orElse(null);

        model.addAttribute("resultado", r);

        return "resenas/buscar-resultado";
    }

}
