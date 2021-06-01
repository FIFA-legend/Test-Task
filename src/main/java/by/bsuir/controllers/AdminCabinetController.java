package by.bsuir.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCabinetController {

    @GetMapping("/admin")
    public String adminCabinet() {
        return "admin";
    }

}
