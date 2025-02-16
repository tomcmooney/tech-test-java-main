package com.systemloco.techtest.JavaTechTest.api.controllers;

import java.util.Collection;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.systemloco.techtest.JavaTechTest.api.services.DevicesService;

import lombok.RequiredArgsConstructor;

import static com.systemloco.techtest.JavaTechTest.api.services.DevicesService.Response;

@RestController
@RequiredArgsConstructor
public class DevicesController {
    @NotNull
    private final DevicesService service;

    @GetMapping("/api/devices")
    @ResponseBody
    Collection<Response> findAllActiveDevices() {
        return service.findAllActiveDevices();
    }

    @GetMapping("/api/devices/{id}")
    @ResponseBody
    Response findActiveDeviceById(@PathVariable("id") @NotNull final String deviceId) {
        var device = service.findActiveDeviceById(deviceId);
        return Optional
                .ofNullable(device)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
