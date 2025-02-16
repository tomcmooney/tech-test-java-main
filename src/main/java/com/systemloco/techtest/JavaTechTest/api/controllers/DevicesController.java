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

import com.systemloco.techtest.JavaTechTest.api.usecases.DeviceByIdUseCase;
import com.systemloco.techtest.JavaTechTest.api.usecases.DevicesUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DevicesController {
    @NotNull
    private final DevicesUseCase allDevices;

    @NotNull
    private final DeviceByIdUseCase byId;

    @GetMapping("/api/devices")
    @ResponseBody
    Collection<DevicesUseCase.Response> allDevices() {
        return allDevices.invoke();
    }

    @GetMapping("/api/devices/{id}")
    @ResponseBody
    DeviceByIdUseCase.Response byId(
            @PathVariable("id") @NotNull final String deviceId) {
        final var device = byId.invoke(
                deviceId);
        return Optional
                .ofNullable(device)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
