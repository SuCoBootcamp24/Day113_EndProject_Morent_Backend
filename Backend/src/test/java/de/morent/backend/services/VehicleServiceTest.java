package de.morent.backend.services;

import de.morent.backend.dtos.search.AutoCountDto;
import de.morent.backend.dtos.search.AutoCountRequestDto;
import de.morent.backend.dtos.search.FilteringDto;
import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleExemplarDto;
import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.entities.*;
import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import de.morent.backend.enums.VehicleStatus;
import de.morent.backend.repositories.VehicleExemplarRepository;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ImagesService imagesService;
    @Mock
    private StoreService storeService;
    @Mock
    private VehicleExemplarRepository vehicleExemplarRepository;
    @Mock
    private BookingService bookingService;


    @Spy
    @InjectMocks
    private VehicleService vehicleService;

    Vehicle vehicle1;
    Vehicle vehicle2;
    MultipartFile img;
    Image image;
    VehicleRequestDTO vehicleRequestDTO_IMG;
    VehicleRequestDTO vehicleRequestDTO_NoIMG;
    VehicleDTO vehicleDTO;
    Store store;
    BigDecimal price;
    FilteringDto filteringDto_noFilter;
    VehicleExemplar exemplar1;
    VehicleExemplar exemplar2;
    VehicleExemplarDto exemplarDto;
    private AutoCountRequestDto autoCountRequestDto;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        store = new Store();
        store.setId(1L);
        price = BigDecimal.valueOf(100.0);

        vehicle1 = new Vehicle();
        vehicle1.setId(1L);
        vehicle1.setBrand("TestBrand");
        vehicle1.setModel("TestModel");
        vehicle1.setCarType(CarType.SPORT);
        vehicle1.setSeats(5);
        vehicle1.setFuelType(FuelType.GASOLINE);
        vehicle1.setEngineCapacity(60);
        vehicle1.setConsumption(15.1f);

        vehicle2 = new Vehicle();
        vehicle2.setId(2L);
        vehicle2.setBrand("TestBrand");
        vehicle2.setModel("TestModel");
        vehicle2.setCarType(CarType.SUV);
        vehicle2.setSeats(5);
        vehicle2.setFuelType(FuelType.DIESEL);
        vehicle2.setEngineCapacity(90);
        vehicle2.setConsumption(15.1f);

        img = mock(MultipartFile.class);

        image = new Image();
        image.setThumbnailUrl("http://example.com/thumbnail.jpg");

        exemplar1 = new VehicleExemplar();
        exemplar1.setId(1L);
        exemplar1.setVehicle(vehicle1);
        exemplar1.setPricePerDay(price);
        exemplar1.setMileage(1000);
        exemplar1.setVehicleStatus(VehicleStatus.EXCELLENT);
        exemplar1.setDamageProfile(new DamageProfile());

        exemplar2 = new VehicleExemplar();
        exemplar1.setId(2L);
        exemplar2.setVehicle(vehicle2);
        exemplar2.setPricePerDay(price.add(BigDecimal.TWO));
        exemplar2.setMileage(2000);
        exemplar2.setVehicleStatus(VehicleStatus.GOOD);
        exemplar2.setDamageProfile(new DamageProfile());

        exemplarDto = new VehicleExemplarDto(
                exemplar1.getId(),
                vehicleDTO,
                price,
                1000,
                VehicleStatus.EXCELLENT.getStatusName(),
                LocalDate.of(2000,1,1),
                0,
                1991
                );

        filteringDto_noFilter = new FilteringDto(
                store.getId(),
                LocalDate.of(2024,8,1),
                LocalDate.of(2024,9,1),
                null,
                null,
                null,
                null
                );

        vehicleDTO = new VehicleDTO(
                vehicle1.getId(),
                vehicle1.getCarType(),
                vehicle1.getBrand(),
                vehicle1.getModel(),
                vehicle1.getSeats(),
                vehicle1.getEngineCapacity(),
                vehicle1.isAutomatic(),
                vehicle1.getFuelType(),
                vehicle1.getImage().getThumbnailUrl(),
                vehicle1.getConsumption()
        );

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

        autoCountRequestDto = new AutoCountRequestDto(
                1L,
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 10));
    }


    //-------

    @Test
    public void testFindVehicleById_Found() {
        long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));

        VehicleDTO result = vehicleService.findVehicleById(vehicleId);

        assertEquals(vehicleId, result.id());
        assertEquals(vehicle1.getSeats(), result.seats());
        assertEquals(vehicle1.getFuelType(), result.fuelType());
    }

    @Test
    public void testFindVehicleById_NotFound() {
        long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.findVehicleById(vehicleId));
    }


    //-------

    @Test
    public void testCreateVehicle_Success_NoImage() {
        when(vehicleRepository.findByBrandAndModelAndIsAutomatic(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());

        boolean result = vehicleService.createVehicle(vehicleRequestDTO_NoIMG);

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

        boolean result = vehicleService.createVehicle(vehicleRequestDTO_IMG);

        assertTrue(result);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(imagesService, times(1)).setImageToVehicle(any(Vehicle.class), eq(img));
    }

    @Test
    public void testCreateVehicle_AlreadyExists() {
        when(vehicleRepository.findByBrandAndModelAndIsAutomatic(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(vehicle1));

        assertThrows(EntityExistsException.class, () -> vehicleService.createVehicle(vehicleRequestDTO_NoIMG));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }


    //-------

    @Test
    public void testSetNewImageToVehicle_Success() {
        long vehicleId = 1L;
        Image image = new Image();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(imagesService.setImageToVehicle(vehicle1, img)).thenReturn(image);

        vehicleService.setNewImageToVehicle(vehicleId, img);

        verify(vehicleRepository, times(1)).save(vehicle1);
        assertEquals(image, vehicle1.getImage());
    }

    @Test
    public void testSetNewImageToVehicle_VehicleNotFound() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> vehicleService.setNewImageToVehicle(vehicleId, img));
        assertEquals("VehicleId is failed after Images upload", exception.getMessage());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }


    //-------

    @Test
    public void testCreateMoreVehicles_WithValidDTOs_ReturnsTrue() {
        VehicleRequestDTO[] dtos = {
                vehicleRequestDTO_NoIMG,
                vehicleRequestDTO_IMG
        };

        boolean result = vehicleService.createMoreVehicles(dtos);

        assertTrue(result, "The method should return true for valid DTOs");
        verify(vehicleService, times(2)).createVehicle(any(VehicleRequestDTO.class));
    }

    @Test
    public void testCreateMoreVehicles_WithEmptyArray_ReturnsTrue() {
        VehicleRequestDTO[] dtos = {};

        boolean result = vehicleService.createMoreVehicles(dtos);

        assertTrue(result, "The method should return true for an empty array");
        verify(vehicleService, never()).createVehicle(any(VehicleRequestDTO.class));
    }

    @Test
    public void testCreateMoreVehicles_WithNullArray_ReturnsTrue() {

        boolean result = vehicleService.createMoreVehicles(null);

        assertFalse(result, "The method should return true for a null array");
        verify(vehicleService, never()).createVehicle(any(VehicleRequestDTO.class));
    }


    //-------

    @Test
    public void testUpdateVehicle_SuccessfulUpdate_ReturnsUpdatedVehicleDTO() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);
        when(imagesService.setImageToVehicle(vehicle1, vehicleRequestDTO_IMG.img())).thenReturn(new Image());

        // Act
        VehicleDTO result = vehicleService.updateVehicle(vehicleId, vehicleRequestDTO_IMG);

        // Assert
        assertNotNull(result);
        assertEquals(vehicleRequestDTO_IMG.carType(), result.carType());
        assertEquals(vehicleRequestDTO_IMG.brand(), result.brand());
        assertEquals(vehicleRequestDTO_IMG.model(), result.model());
        assertEquals(vehicleRequestDTO_IMG.seats(), result.seats());
        verify(vehicleRepository).save(vehicle1);
    }

    @Test
    public void testUpdateVehicle_VehicleNotFound_ThrowsEntityNotFoundException() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.updateVehicle(vehicleId, vehicleRequestDTO_NoIMG));
    }

    @Test
    public void testUpdateVehicle_WithImage_CallsSetImageToVehicle() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(imagesService.setImageToVehicle(vehicle1, vehicleRequestDTO_IMG.img())).thenReturn(image);
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        vehicleService.updateVehicle(vehicleId, vehicleRequestDTO_IMG);

        verify(imagesService).setImageToVehicle(vehicle1, vehicleRequestDTO_IMG.img());
    }

    @Test
    public void testUpdateVehicle_WithoutImage_DoesNotCallSetImageToVehicle() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        vehicleService.updateVehicle(vehicleId, vehicleRequestDTO_NoIMG);

        verify(imagesService, never()).setImageToVehicle(any(Vehicle.class), any());
    }
    
    //-------
    
    @Test
    public void testDeleteVehicle_SuccessfulDeletion() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));

        assertDoesNotThrow(() -> vehicleService.deleteVehicle(vehicleId));
        verify(vehicleRepository).delete(any(Vehicle.class));
    }

    @Test
    public void testDeleteVehicle_VehicleNotFound() {
        long vehicleId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.deleteVehicle(vehicleId));
    }

    //-------

    @Test
    public void testCreateVehicleExemplar_SuccessfulCreation() {
        int quantity = 3;
        long vehicleId = 1L;
        long storeId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(storeService.findById(storeId)).thenReturn(store);

        List<VehicleExemplarDto> result = vehicleService.createVehicleExemplar(vehicleId, storeId, quantity, price);

        assertNotNull(result);
        assertEquals(quantity, result.size(), "The method should return the correct number of VehicleExemplarDto objects.");
        verify(vehicleExemplarRepository, times(quantity)).save(any(VehicleExemplar.class));
    }

    @Test
    public void testCreateVehicleExemplar_VehicleNotFound() {
        long vehicleId = 1L;
        long storeId = 1L;
        int quantity = 3;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.createVehicleExemplar(vehicleId, storeId, quantity, price));
        verify(vehicleExemplarRepository, never()).save(any(VehicleExemplar.class));
    }

    @Test
    public void testCreateVehicleExemplar_StoreNotFound() {
        long vehicleId = 1L;
        long storeId = 1L;
        int quantity = 3;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(storeService.findById(storeId)).thenThrow(new EntityNotFoundException("Store with id: " + storeId + " not found"));

        assertThrows(EntityNotFoundException.class, () -> vehicleService.createVehicleExemplar(vehicleId, storeId, quantity, price));
        verify(vehicleExemplarRepository, never()).save(any(VehicleExemplar.class));
    }

    @Test
    public void testCreateVehicleExemplar_VerifiesExemplarProperties() {
        int quantity = 1;
        long vehicleId = 1L;
        long storeId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(storeService.findById(storeId)).thenReturn(store);

        List<VehicleExemplarDto> result = vehicleService.createVehicleExemplar(vehicleId, storeId, quantity, price);

        assertNotNull(result);
        VehicleExemplarDto exemplarDto = result.get(0);
        assertEquals(price, exemplarDto.pricePerDay(), "Price per day should be set correctly.");
        assertEquals(0, exemplarDto.mileage(), "Mileage should be set to 0.");
        assertEquals(VehicleStatus.EXCELLENT.getStatusName(), exemplarDto.status(), "Vehicle status should be set to EXCELLENT.");
        verify(vehicleExemplarRepository, times(quantity)).save(any(VehicleExemplar.class));
    }

    @Test
    public void testCreateVehicleExemplar_ZeroQuantity() {
        int quantity = 0;
        long vehicleId = 1L;
        long storeId = 1L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle1));
        when(storeService.findById(storeId)).thenReturn(store);

        List<VehicleExemplarDto> result = vehicleService.createVehicleExemplar(vehicleId, storeId, quantity, price);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "The result list should be empty when quantity is 0.");
        verify(vehicleExemplarRepository, never()).save(any(VehicleExemplar.class));
    }


    //--------

    @Test
    public void testGetFilteredCars_WithCarTypeAndFuelTypeFilter_ReturnsFilteredVehicles() {
        int pageNo = 0;
        int recordCount = 10;
        List<VehicleExemplar> exemplars = List.of(exemplar1);
        Page<VehicleExemplar> exemplarPage = new PageImpl<>(exemplars);

        when(vehicleExemplarRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(exemplarPage);
        when(bookingService.autoIsAvailable(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        List<VehicleExemplarDto> result = vehicleService.getFilteredCars(filteringDto_noFilter, pageNo, recordCount);

        assertNotNull(result);
        assertEquals(1, result.size(), "The result should contain one vehicle");

        VehicleExemplarDto actualExemplarDto = result.get(0);
        assertEquals(exemplar1.getId(), actualExemplarDto.id());
        assertEquals(exemplar1.getPricePerDay(), actualExemplarDto.pricePerDay());
        assertEquals(exemplar1.getMileage(), actualExemplarDto.mileage());
        assertEquals(exemplar1.getVehicleStatus().getStatusName(), actualExemplarDto.status());
        assertNotNull(actualExemplarDto.vehicle(), "Vehicle should not be null");

        VehicleDTO actualVehicleDTO = actualExemplarDto.vehicle();
        Vehicle expectedVehicle = exemplar1.getVehicle();
        assertEquals(expectedVehicle.getId(), actualVehicleDTO.id());
        assertEquals(expectedVehicle.getBrand(), actualVehicleDTO.brand());
        assertEquals(expectedVehicle.getModel(), actualVehicleDTO.model());
        assertEquals(expectedVehicle.getSeats(), actualVehicleDTO.seats());
        assertEquals(expectedVehicle.getFuelType(), actualVehicleDTO.fuelType());
    }

    @Test
    public void testGetFilteredCars_WithMaxPriceFilter_ReturnsVehiclesUnderPrice() {
        FilteringDto filteringDto = new FilteringDto(
                store.getId(),
                LocalDate.of(2024,8,1),
                LocalDate.of(2024,9,1),
                null,
                null,
                null,
                new BigDecimal(300)
        );
        int pageNo = 0;
        int recordCount = 10;
        List<VehicleExemplar> exemplars = List.of(exemplar1);
        Page<VehicleExemplar> exemplarPage = new PageImpl<>(exemplars);

        when(vehicleExemplarRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(exemplarPage);
        when(bookingService.autoIsAvailable(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        List<VehicleExemplarDto> result = vehicleService.getFilteredCars(filteringDto, pageNo, recordCount);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "The result should not be empty with a price filter applied");
    }

    @Test
    public void testGetFilteredCars_WithNoMatchingVehicles() {
        FilteringDto filteringDto = new FilteringDto(
                store.getId(),
                LocalDate.of(2024,8,1),
                LocalDate.of(2024,9,1),
                null,
                null,
                null,
                new BigDecimal(300)
        );
        int pageNo = 0;
        int recordCount = 10;
        Page<VehicleExemplar> emptyPage = new PageImpl<>(new ArrayList<>());

        when(vehicleExemplarRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        List<VehicleExemplarDto> result = vehicleService.getFilteredCars(filteringDto, pageNo, recordCount);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "The result should be empty if no vehicles match the filter criteria");
    }

    @Test
    public void testGetFilteredCars_VehicleNotAvailable() {
        int pageNo = 0;
        int recordCount = 10;
        List<VehicleExemplar> exemplars = List.of(exemplar1);
        Page<VehicleExemplar> exemplarPage = new PageImpl<>(exemplars);

        when(vehicleExemplarRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(exemplarPage);
        when(bookingService.autoIsAvailable(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);

        List<VehicleExemplarDto> result = vehicleService.getFilteredCars(filteringDto_noFilter, pageNo, recordCount);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "The result should be empty if the vehicle is not available in the given date range");
    }

    @Test
    public void testGetFilteredCars_WithPagination() {
        FilteringDto filteringDto = new FilteringDto(
                store.getId(),
                LocalDate.of(2024,8,1),
                LocalDate.of(2024,9,1),
                null,
                null,
                List.of(2),
                new BigDecimal(300)
        );
        int pageNo = 1;
        int recordCount = 5;
        List<VehicleExemplar> exemplars = List.of(exemplar1);
        Page<VehicleExemplar> exemplarPage = new PageImpl<>(exemplars, PageRequest.of(pageNo, recordCount), 1);

        when(vehicleExemplarRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(exemplarPage);
        when(bookingService.autoIsAvailable(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        List<VehicleExemplarDto> result = vehicleService.getFilteredCars(filteringDto, pageNo, recordCount);

        assertNotNull(result);
        assertEquals(1, result.size(), "The result should contain one vehicle based on the pagination settings");
    }



    //------

    @Test
    public void testFindVehicleExemplarById_Found() {
        long vehicleExemplarId = 1L;
        when(vehicleExemplarRepository.findById(vehicleExemplarId)).thenReturn(Optional.of(exemplar1));

        VehicleExemplarDto result = vehicleService.findVehicleExemplarById(vehicleExemplarId);

        assertNotNull(result, "The result should not be null if the vehicle exemplar is found.");
        assertEquals(exemplarDto.id(), result.id(), "The returned DTO should have the correct ID.");
        assertEquals(exemplarDto.pricePerDay(), result.pricePerDay(), "The price per day should match.");
        assertEquals(exemplarDto.mileage(), result.mileage(), "The mileage should match.");
        assertEquals(exemplarDto.status(), result.status(), "The status should match.");

        VehicleDTO resultVehicle = result.vehicle();
        Vehicle expectedVehicle = exemplar1.getVehicle();
        assertEquals(expectedVehicle.getId(), resultVehicle.id(), "Vehicle ID should match.");
        assertEquals(expectedVehicle.getBrand(), resultVehicle.brand(), "Vehicle brand should match.");
        assertEquals(expectedVehicle.getModel(), resultVehicle.model(), "Vehicle model should match.");
    }

    @Test
    public void testFindVehicleExemplarById_NotFound() {
        long vehicleExemplarId = 1L;
        when(vehicleExemplarRepository.findById(vehicleExemplarId)).thenReturn(Optional.empty());

        assertThrows(EntityExistsException.class, () -> vehicleService.findVehicleExemplarById(vehicleExemplarId),
                "An EntityExistsException should be thrown if the vehicle exemplar is not found.");
    }


    //------

    @Test
    public void testFindEntityVehicleExemplarById_ExemplarFind() {
        long vehicleExemplarId = 1L;
        when(vehicleExemplarRepository.findById(vehicleExemplarId)).thenReturn(Optional.of(exemplar1));

        VehicleExemplar result = vehicleService.findEntityVehicleExemplarById(vehicleExemplarId);

        assertNotNull(result, "The result should not be null if the vehicle exemplar is found.");
        assertEquals(exemplar1.getId(), result.getId(), "The returned entity should have the correct ID.");
        assertEquals(exemplar1.getMileage(), result.getMileage(), "The mileage should match.");
    }

    @Test
    public void testFindEntityVehicleExemplarById_ExemplarNotFound() {
        long vehicleExemplarId = 1L;
        when(vehicleExemplarRepository.findById(vehicleExemplarId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.findEntityVehicleExemplarById(vehicleExemplarId),
                "An EntityNotFoundException should be thrown if the vehicle exemplar is not found.");
    }


    //------

    @Test
    public void testCountVehiclesPerType_WithAvailableVehicles_ReturnsCorrectCounts() {
        long storeId = autoCountRequestDto.storeId();
        when(vehicleExemplarRepository.findByStoreId(storeId)).thenReturn(List.of(exemplar1, exemplar2));
        when(bookingService.autoIsAvailable(eq(exemplar1.getId()), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);
        when(bookingService.autoIsAvailable(eq(exemplar2.getId()), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        AutoCountDto result = vehicleService.countVehiclesPerType(autoCountRequestDto);

        assertNotNull(result);
        assertEquals(2, result.cartypes().size(), "There should be two car types counted.");
        assertEquals(1L, result.cartypes().stream().filter(dto -> dto.label().equals("Sport")).count(), "SPORT type count should be 1.");
        assertEquals(1L, result.cartypes().stream().filter(dto -> dto.label().equals("SUV")).count(), "SUV type count should be 1.");

        assertEquals(2, result.fuelType().size(), "There should be two fuel types counted.");
        assertEquals(1L, result.fuelType().stream().filter(dto -> dto.label().equals("Benzin")).count(), "DIESEL type count should be 1.");
        assertEquals(1L, result.fuelType().stream().filter(dto -> dto.label().equals("Diesel")).count(), "PETROL type count should be 1.");
    }

    @Test
    public void testCountVehiclesPerType_WithNoAvailableVehicles() {
        long storeId = autoCountRequestDto.storeId();
        when(vehicleExemplarRepository.findByStoreId(storeId)).thenReturn(List.of(exemplar1, exemplar2));
        when(bookingService.autoIsAvailable(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);

        AutoCountDto result = vehicleService.countVehiclesPerType(autoCountRequestDto);

        assertNotNull(result);
        assertTrue(result.cartypes().isEmpty(), "Car types should be empty if no vehicles are available.");
        assertTrue(result.fuelType().isEmpty(), "Fuel types should be empty if no vehicles are available.");
    }

    @Test
    public void testCountVehiclesPerType_WithMixedAvailability() {
        long storeId = autoCountRequestDto.storeId();
        when(vehicleExemplarRepository.findByStoreId(storeId)).thenReturn(List.of(exemplar1, exemplar2));
        when(bookingService.autoIsAvailable(eq(exemplar1.getId()), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);
        when(bookingService.autoIsAvailable(eq(exemplar2.getId()), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);

        AutoCountDto result = vehicleService.countVehiclesPerType(autoCountRequestDto);

        assertNotNull(result);
        assertEquals(1, result.cartypes().size(), "Only one car type should be counted.");
        assertEquals("Sport", result.cartypes().getFirst().label(), "The counted car type should be SPORT.");
        assertEquals(1L, result.cartypes().getFirst().count(), "SPORT type count should be 1.");

        assertEquals(1, result.fuelType().size(), "Only one fuel type should be counted.");
        assertEquals("Benzin", result.fuelType().getFirst().label(), "The counted fuel type should be DIESEL.");
        assertEquals(1L, result.fuelType().getFirst().count(), "DIESEL type count should be 1.");
    }

    @Test
    public void testCountVehiclesPerType_WithNoVehiclesInStore_ReturnsEmptyCounts() {
        long storeId = autoCountRequestDto.storeId();
        when(vehicleExemplarRepository.findByStoreId(storeId)).thenReturn(List.of());

        AutoCountDto result = vehicleService.countVehiclesPerType(autoCountRequestDto);

        assertNotNull(result);
        assertTrue(result.cartypes().isEmpty(), "Car types should be empty if there are no vehicles in the store.");
        assertTrue(result.fuelType().isEmpty(), "Fuel types should be empty if there are no vehicles in the store.");
    }
}
