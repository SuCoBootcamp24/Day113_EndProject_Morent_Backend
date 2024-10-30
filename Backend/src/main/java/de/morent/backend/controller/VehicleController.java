package de.morent.backend.controller;

import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {

    private VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    //---------vehicle model---------

    //POST / Create vehicle
    @PostMapping
    public ResponseEntity<Void> createVehicle(@RequestBody VehicleRequestDTO dto) {
        if (vehicleService.createVehicle(dto)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }

    //GET / Get all vehicles



    //GET /{id} Get a specific vehicle



    //PUT / Update a specific vehicle



    //DELETE /{id} Delete a specific vehicle



}
