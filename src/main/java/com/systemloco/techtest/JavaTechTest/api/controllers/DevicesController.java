package com.systemloco.techtest.JavaTechTest.api.controllers;

import com.systemloco.techtest.JavaTechTest.api.services.DevicesService;

import static com.systemloco.techtest.JavaTechTest.api.services.DevicesService.Response;

import java.util.*;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DevicesController {
    @NotNull
    private final DevicesService service;

    @GetMapping
    @ResponseBody
    Collection<Response> findAllActiveDevices() {
        return service.findAllActiveDevices();
    }

    @GetMapping("/{id}")
    @ResponseBody
    Response findActiveDeviceById(@PathVariable("id") @NotNull final String deviceId) {
        var activeDevice = service.findActiveDeviceById(deviceId);

        return Optional
                .ofNullable(activeDevice)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
