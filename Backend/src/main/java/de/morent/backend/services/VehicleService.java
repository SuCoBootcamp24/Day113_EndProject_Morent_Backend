package de.morent.backend.services;

import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.mappers.VehicleMapper;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;

    private ImagesService imagesService;

    public VehicleService(VehicleRepository vehicleRepository, ImagesService imagesService) {
        this.vehicleRepository = vehicleRepository;
        this.imagesService = imagesService;
    }

    public VehicleDTO findVehicleById(long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new EntityNotFoundException("Vehicle with id: " + vehicleId + " not found"));
        return VehicleMapper.mapToDto(vehicle);
    }

    public boolean createVehicle(VehicleRequestDTO dto) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findByBrandAndModelAndIsAutomatic(dto.brand(), dto.model(), dto.isAutomatic());
        if (existingVehicle.isPresent()) throw new EntityExistsException("Vehicle already exists");

        Vehicle newVehicle = new Vehicle();
        newVehicle.setCarType(dto.carType());
        newVehicle.setBrand(dto.brand());
        newVehicle.setModel(dto.model());
        newVehicle.setSeats(dto.seats());
        newVehicle.setFuelType(dto.fuelType());
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
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new RuntimeException("VehicleId is failed after Images upload"));
        vehicle.setImage(imagesService.setImageToVehicle(vehicle, file));
        vehicleRepository.save(vehicle);
    }

    public boolean createMoreVehicles(VehicleRequestDTO[] dtos) {
        for (VehicleRequestDTO dto : dtos) {
            createVehicle(dto);
        }
        return true;
    }

/*    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
               .map(VehicleMapper::mapToDto)
               .collect(Collectors.toList());
    }*/

    public List<VehicleDTO> getAllVehicles(int pageNo, int recordCount) {
        Pageable pageable = PageRequest.of(pageNo, recordCount);
        return vehicleRepository.findAll(pageable).stream()
                .map(VehicleMapper::mapToDto)
                .collect(Collectors.toList());
    }


    public VehicleDTO updateVehicle(long id, VehicleRequestDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("VehicleId is failed after Images upload"));
        vehicle.setCarType(dto.carType());
        vehicle.setBrand(dto.brand());
        vehicle.setModel(dto.model());
        vehicle.setSeats(dto.seats());
        vehicle.setFuelType(dto.fuelType());
        vehicle.setEngineCapacity(dto.engineCapacity());
        vehicle.setAutomatic(dto.isAutomatic());
        vehicle.setConsumption(dto.consumption());
        if (dto.img()!=null) {
            vehicle.setImage(imagesService.setImageToVehicle(vehicle, dto.img()));
        }
        vehicleRepository.save(vehicle);
        return VehicleMapper.mapToDto(vehicle);
    }

    public void deleteVehicle(long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("VehicleId is failed after Images upload"));
        vehicleRepository.delete(vehicle);
    }

}
