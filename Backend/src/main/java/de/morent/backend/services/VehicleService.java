package de.morent.backend.services;

import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;

    public boolean createVehicle(VehicleRequestDTO dto) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findByBrandAndModelAndIsAutomatic(dto.brand(), dto.model(), dto.isAutomatic());
        if (existingVehicle.isPresent()) throw new EntityExistsException("Vehicle already exists");

        Vehicle newVehicle = new Vehicle();
        newVehicle.setCarType(dto.carType());
        newVehicle.setBrand(dto.brand());
        newVehicle.setModel(dto.model());
        newVehicle.setSeats(dto.seats());
        newVehicle.setEngineCapacity(dto.engineCapacity());
        newVehicle.setAutomatic(dto.isAutomatic());
        newVehicle.setConsumption(dto.consumption());

        if (!dto.img().isEmpty()) {
            String imgName = newVehicle.getBrand() + " " + dto.model();
            if (newVehicle.isAutomatic()) imgName += " Automatik";
            else imgName += " Schalter";
            // Upload image and set URL to newVehicle
        }
        vehicleRepository.save(newVehicle);
        return true;
    }
}