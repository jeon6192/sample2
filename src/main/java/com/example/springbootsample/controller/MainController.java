package com.example.springbootsample.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
public class MainController {

    @GetMapping("/")
    public ModelAndView mina() {
        return new ModelAndView("index");
    }
}
