package com.ces2.trabajo3.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class menu_controllers {
    @GetMapping("/")
    public String inicio() {
        return "menu";
    }
}
