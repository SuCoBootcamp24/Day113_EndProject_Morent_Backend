package de.morent.backend.services;

import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleExemplarDto;
import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.entities.*;
import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import de.morent.backend.enums.VehicleStatus;
import de.morent.backend.mappers.VehicleExemplarMapper;
import de.morent.backend.mappers.VehicleMapper;
import de.morent.backend.repositories.VehicleExemplarRepository;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleExemplarRepository vehicleExemplarRepository;
    private ImagesService imagesService;
    private StoreService storeService;
    private BookingService bookingService;


    public VehicleService(VehicleRepository vehicleRepository, ImagesService imagesService, VehicleExemplarRepository vehicleExemplarRepository, StoreService storeService, BookingService bookingService) {
        this.vehicleRepository = vehicleRepository;
        this.imagesService = imagesService;
        this.vehicleExemplarRepository = vehicleExemplarRepository;
        this.storeService = storeService;
        this.bookingService = bookingService;
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

    // Get All no Pages
/*    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
               .map(VehicleMapper::mapToDto)
               .collect(Collectors.toList());
    }*/

    // Get All with Pagination

    public List<VehicleDTO> getAllVehicles(int pageNo, int recordCount) {
        Pageable pageable = PageRequest.of(pageNo, recordCount);
        return vehicleRepository.findAll(pageable).stream()
                .map(VehicleMapper::mapToDto)
                .collect(Collectors.toList());
    }


    public VehicleDTO updateVehicle(long vehicleId, VehicleRequestDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new EntityNotFoundException("Vehicle with id: " + vehicleId + " not found"));
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

    public List<VehicleExemplarDto> createVehicleExemplar(long vehicleId, long storeId, int quantity, BigDecimal price) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new EntityNotFoundException("Vehicle with id: " + vehicleId + " not found"));
        Store store = storeService.findById(storeId);
        List<VehicleExemplar> exemplars = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            VehicleExemplar vehicleExemplar = new VehicleExemplar();
            vehicleExemplar.setStore(store);
            vehicleExemplar.setVehicle(vehicle);
            vehicleExemplar.setPricePerDay(price);
            vehicleExemplar.setMileage(0);
            vehicleExemplar.setVehicleStatus(VehicleStatus.EXCELLENT);
            vehicleExemplar.setDamageProfile(new DamageProfile());
            exemplars.add(vehicleExemplar);
            vehicleExemplarRepository.save(vehicleExemplar);
        }
        return exemplars.stream().map(VehicleExemplarMapper::mamToDto).toList();
    }


    public List<VehicleExemplarDto> getAllVehicleExemplarInStoreAvailable(long storeId, LocalDate startDate, LocalDate endDate, CarType carType, FuelType fuelType, BigDecimal price, int capacity) {

        return vehicleExemplarRepository.findAll().stream().filter(ex ->
                (
                        ex.getStore().getId() == storeId) &&
                        (ex.getVehicle().getCarType() == carType) &&
                        (ex.getVehicle().getFuelType() == fuelType) &&
                        (ex.getPricePerDay().compareTo(price) <= 0) &&
                        (ex.getVehicle().getEngineCapacity() >= capacity) &&
                        isAvailable(ex, startDate, endDate)
                )
                .map(VehicleExemplarMapper::mamToDto).toList();
    }

    private Boolean isAvailable(VehicleExemplar ex, LocalDate startDate, LocalDate endDate) {
        List<Booking> bookingHistory = bookingService.getAllExemplarBooking(ex.getId());
        return bookingHistory.stream().noneMatch(booking ->
                booking.getPickUpDate().isBefore(endDate) &&
                booking.getPlannedDropOffDate().isAfter(startDate));
    }

}
