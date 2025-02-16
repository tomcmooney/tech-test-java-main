package com.systemloco.techtest.JavaTechTest.api.controllers;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DevicesController {
    @NotNull
    private final DevicesUseCase allDevices;

    @GetMapping
    @ResponseBody
    Collection<DevicesUseCase.Response> allDevices() {
        return allDevices.invoke();
    }
}
