package de.morent.backend.services;

import de.morent.backend.dtos.search.AutoCountDto;
import de.morent.backend.dtos.search.AutoCountRequestDto;
import de.morent.backend.dtos.search.EnumDto;
import de.morent.backend.dtos.search.FilteringDto;
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
import de.morent.backend.specifications.VehicleSpecification;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleExemplarRepository vehicleExemplarRepository;
    private ImagesService imagesService;
    private StoreService storeService;
    private BookingService bookingService;


    public VehicleService(VehicleRepository vehicleRepository, ImagesService imagesService, VehicleExemplarRepository vehicleExemplarRepository, StoreService storeService,@Lazy BookingService bookingService) {
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
        if (dtos == null) return false;
        for (VehicleRequestDTO dto : dtos) {
            createVehicle(dto);
        }
        return true;
    }

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
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("vehicle not found"));
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
        return exemplars.stream().map(VehicleExemplarMapper::mapToDto).toList();
    }

    public List<VehicleExemplarDto> getFilteredCars(FilteringDto dto, int pageNo, int recordCount) {
        Pageable pageable = PageRequest.of(pageNo, recordCount);
        long storeId = dto.storeId();
        List<CarType> carType = dto.carType();
        List<FuelType> fuelType = dto.fuelType();
        BigDecimal price = dto.pricePerDay();
        LocalDate startDate = dto.startDate();
        LocalDate endDate = dto.endDate();
        List<Integer> seats = dto.seats();

        Specification<VehicleExemplar> spec = Specification.where(VehicleSpecification.inStore(storeId));
        if (carType != null && !carType.isEmpty())
            spec = spec.and(VehicleSpecification.isCarType(carType));

        if (fuelType != null && !fuelType.isEmpty())
            spec = spec.and(VehicleSpecification.isFuelType(fuelType));

        if (price != null)
            spec = spec.and(VehicleSpecification.hasMaxPrice(price));

        if (seats != null && !seats.isEmpty())
            spec = spec.and(VehicleSpecification.seatsCount(seats));


        return vehicleExemplarRepository.findAll(spec, pageable).stream()
                .filter(vehicle -> bookingService.autoIsAvailable(vehicle.getId(), startDate, endDate))
                .collect(Collectors.groupingBy(VehicleExemplar::getVehicle))
                .values().stream()
                .map(List::getFirst)
                .map(VehicleExemplarMapper::mapToDto).toList();
    }

    public VehicleExemplarDto findVehicleExemplarById(long id) {
        VehicleExemplar exemplar = vehicleExemplarRepository.findById(id).orElseThrow(() ->new EntityExistsException("VehicleExemplar not found"));
        return VehicleExemplarMapper.mapToDto(exemplar);
    }

    public VehicleExemplar findEntityVehicleExemplarById(long id) {
        return vehicleExemplarRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("VehicleExemplar not found"));

    }

    public AutoCountDto countVehiclesPerType(AutoCountRequestDto dto) {
        long storeId = dto.storeId();

        // Retrieve a list of available vehicle exemplars for the specified store ID and filter them by availability within the given date range.
        List<VehicleExemplar> availableExemplars = vehicleExemplarRepository.findByStoreId(storeId).stream().filter(vehicle -> bookingService.autoIsAvailable(vehicle.getId(), dto.startDate(), dto.endDate())).toList();

        // Count the number of vehicle exemplars by fuel type and car type, storing the results in separate maps.
        Map<FuelType, Long> fuelTypeCounts = availableExemplars.stream().collect(Collectors.groupingBy(vehicle -> vehicle.getVehicle().getFuelType(), Collectors.counting()));

        Map<CarType, Long> carTypeCounts = availableExemplars.stream()
                .collect(Collectors.groupingBy(vehicle ->
                                vehicle.getVehicle().getCarType(),
                        Collectors.counting()));

        // Create a list of EnumDto objects representing car types and their respective counts.
        List<EnumDto> carTypes = carTypeCounts.entrySet().stream()
                .map(entry -> new EnumDto(
                        entry.getKey().getTypeName(),
                        entry.getKey().name(),
                        entry.getValue()
                ))
                .toList();

        List<EnumDto> fuelTypes = fuelTypeCounts.entrySet().stream()
                .map(entry -> new EnumDto(
                        entry.getKey().getTypeName(),
                        entry.getKey().name(),
                        entry.getValue()
                ))
                .toList();

        return new AutoCountDto(
            carTypes,
            fuelTypes
        );
    }
}