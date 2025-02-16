package com.systemloco.techtest.JavaTechTest.api.controllers;

import java.util.Collection;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
public class DevicesController {

    @GetMapping
    @ResponseBody
    Collection<?> allDevices() {
        return Collections.emptyList();
    }

}
