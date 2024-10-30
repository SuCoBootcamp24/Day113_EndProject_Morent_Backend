package de.morent.backend.services;

import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.entities.Image;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ImagesService imagesService;

    @InjectMocks
    private VehicleService vehicleService;

    Vehicle vehicle;
    MultipartFile img;

    VehicleRequestDTO vehicleRequestDTO_IMG;
    VehicleRequestDTO vehicleRequestDTO_NoIMG;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("TestBrand");
        vehicle.setModel("TestModel");
        vehicle.setCarType(CarType.SPORT);
        vehicle.setSeats(5);
        vehicle.setFuelType(FuelType.DIESEL);
        vehicle.setEngineCapacity(80);
        vehicle.setConsumption(15.1f);

        img = mock(MultipartFile.class);

        vehicleRequestDTO_IMG = new VehicleRequestDTO(
                CarType.SPORT,
                "TestBrand",
                "TestModel",
                5,
                80,
                true,
                FuelType.DIESEL,
                2.1f,
                img
        );

        vehicleRequestDTO_NoIMG = new VehicleRequestDTO(
                CarType.SPORT,
                "TestBrand",
                "TestModel",
                5,
                80,
                true,
                FuelType.DIESEL,
                2.1f,
                null
        );


    }

    @Test
    public void testFindVehicleById_Found() {
       long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = vehicleService.findVehicleById(vehicleId);

        assertTrue(result.isPresent());
        assertEquals(vehicleId, result.get().getId());
    }

    @Test
    public void testFindVehicleById_NotFound() {
        long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleService.findVehicleById(vehicleId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testCreateVehicle_Success_NoImage() {
        when(vehicleRepository.findByBrandAndModelAndIsAutomatic(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());

        // Act
        boolean result = vehicleService.createVehicle(vehicleRequestDTO_NoIMG);

        // Assert
        assertTrue(result);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(imagesService, never()).setImageToVehicle(any(Vehicle.class), any());
    }

    @Test
    public void testCreateVehicle_Success_WithImage() {
        when(vehicleRepository.findByBrandAndModelAndIsAutomatic(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());

        Image image = new Image();
        when(imagesService.setImageToVehicle(any(Vehicle.class), eq(img))).thenReturn(image);

        // Act
        boolean result = vehicleService.createVehicle(vehicleRequestDTO_IMG);

        // Assert
        assertTrue(result);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(imagesService, times(1)).setImageToVehicle(any(Vehicle.class), eq(img));
    }

    @Test
    public void testCreateVehicle_AlreadyExists() {
        when(vehicleRepository.findByBrandAndModelAndIsAutomatic(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(vehicle));

        // Act & Assert
        assertThrows(EntityExistsException.class, () -> vehicleService.createVehicle(vehicleRequestDTO_NoIMG));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    public void testSetNewImageToVehicle_Success() {
        // Arrange
        long vehicleId = 1L;
        Image image = new Image();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Act
        vehicleService.setNewImageToVehicle(vehicleId, image);

        // Assert
        verify(vehicleRepository, times(1)).save(vehicle);
        assertEquals(image, vehicle.getImage());
    }

    @Test
    public void testSetNewImageToVehicle_VehicleNotFound() {
        // Arrange
        long vehicleId = 1L;
        Image img = new Image();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> vehicleService.setNewImageToVehicle(vehicleId, img));
        assertEquals("VehicleId is failed after Images upload", exception.getMessage());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
}
