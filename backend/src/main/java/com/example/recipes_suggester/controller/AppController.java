package com.example.recipes_suggester.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class AppController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getBasicMessage(@RequestParam(name = "name", defaultValue = "Guest") String name) {
        return "hello " + name;
    }
}