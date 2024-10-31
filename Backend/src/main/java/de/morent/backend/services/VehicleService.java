package de.morent.backend.services;

import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.entities.Image;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;

    private ImagesService imagesService;

    public VehicleService(VehicleRepository vehicleRepository, ImagesService imagesService) {
        this.vehicleRepository = vehicleRepository;
        this.imagesService = imagesService;
    }

    public Optional<Vehicle> findVehicleById(long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

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

        if (dto.img() !=null) {
            newVehicle.setImage(imagesService.setImageToVehicle(newVehicle, dto.img()));
        }
        vehicleRepository.save(newVehicle);
        return true;
    }


    public void setNewImageToVehicle(long vehicleId, MultipartFile file) {
        Vehicle vehicle = findVehicleById(vehicleId).orElseThrow(() -> new RuntimeException("VehicleId is failed after Images upload"));
        vehicle.setImage(imagesService.setImageToVehicle(vehicle, file));
        vehicleRepository.save(vehicle);
    }
}
