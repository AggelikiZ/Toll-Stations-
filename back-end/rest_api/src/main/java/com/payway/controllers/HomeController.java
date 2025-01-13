package com.payway.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {

    @RequestMapping("/api")
    public String index() {
        return "redirect:swagger-ui.html";
    }


}

