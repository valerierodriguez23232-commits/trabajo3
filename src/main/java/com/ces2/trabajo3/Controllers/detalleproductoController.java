package com.ces2.trabajo3.Controllers;

import com.ces2.trabajo3.Models.Detalleproducto;
import com.ces2.trabajo3.Repositories.detalleproducto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/detalle")
public class detalleproductoController {

    private final detalleproducto detalleRepo;

    public detalleproductoController(detalleproducto detalleRepo) {
        this.detalleRepo = detalleRepo;
    }

    @GetMapping("/crear/{productoid}")
    public String crearForm(@PathVariable Integer productoid, Model model) {

        Detalleproducto detalle =
                detalleRepo.findByProductId(productoid).orElse(new Detalleproducto());

        detalle.setProductoid(productoid);

        model.addAttribute("detalle", detalle);
        model.addAttribute("productoid", productoid);

        return "detalle/crear";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("detalle") Detalleproducto detalle,
            BindingResult br,
            Model model,
            RedirectAttributes ra
    ) {

        if (br.hasErrors()) {
            model.addAttribute("productoid", detalle.getProductoid());
            return "detalle/crear";
        }

        detalleRepo.saveOrUpdate(detalle);

        ra.addFlashAttribute("msg", "Detalle guardado correctamente");

        return "redirect:/productos/detalles/" + detalle.getProductoid();
    }

    @GetMapping("/eliminar/{productoid}")
    public String eliminar(@PathVariable Integer productoid, RedirectAttributes ra) {

        detalleRepo.deleteByProductId(productoid);

        ra.addFlashAttribute("msg", "Detalle eliminado");

        return "redirect:/productos/detalles/" + productoid;
    }
}
