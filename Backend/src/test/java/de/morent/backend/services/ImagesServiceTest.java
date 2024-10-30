package de.morent.backend.services;

import de.morent.backend.dtos.images.ImgbbDTO;
import de.morent.backend.dtos.images.ImgbbDataDTO;
import de.morent.backend.dtos.images.ImgbbThumbDTO;
import de.morent.backend.entities.Image;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.repositories.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImagesServiceTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ImagesService imagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Erstelle die Instanz von ImagesService mit den gemockten Abhängigkeiten
        imagesService = spy(new ImagesService(vehicleService, imageRepository));


        // Setze die Werte für die @Value-Felder
        ReflectionTestUtils.setField(imagesService, "IMGBB_KEY", "test_key");
        ReflectionTestUtils.setField(imagesService, "IMGBB_URL", "http://test.url");

        // Verwende einen Spy, um Methoden zu mocken, ohne die gesamte Klasse zu mocken
        imagesService = spy(imagesService);
    }

    @Test
    void testSetImageToVehicle_Success() {
        long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        vehicle.setModel("X5");
        vehicle.setAutomatic(true);

        // Mock für MultipartFile und Resource
        MultipartFile file = mock(MultipartFile.class);
        Resource resource = mock(Resource.class);
        when(file.getResource()).thenReturn(resource);

        // Erstelle ein Mock-ImgbbDTO
        ImgbbDataDTO dataDTO = new ImgbbDataDTO("id123", "title", "http://viewer.url", "http://image.url", "http://display.url");
        ImgbbThumbDTO thumbDTO = new ImgbbThumbDTO("http://thumbnail.url");
        ImgbbDTO imgbbDTO = new ImgbbDTO(dataDTO, thumbDTO);

        // Mock für VehicleService
        when(vehicleService.findVehicleById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Mock für uploadImageToBudget mit Argumentmatcher
        doReturn(ResponseEntity.ok(imgbbDTO)).when(imagesService).uploadImageToBudget(any(MultipartFile.class));

        boolean result = imagesService.setImageToVehicle(vehicleId, file);

        assertTrue(result);
        verify(vehicleService).findVehicleById(vehicleId);
        verify(vehicleService).setNewImageToVehicle(eq(vehicleId), any(Image.class));
    }

    @Test
    void testSetImageToVehicle_VehicleNotFound() {
        long vehicleId = 1L;

        when(vehicleService.findVehicleById(vehicleId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            imagesService.setImageToVehicle(vehicleId, file);
        });

        assertEquals("Vehicle with id " + vehicleId + " not found", exception.getMessage());
        verify(vehicleService).findVehicleById(vehicleId);
        verify(vehicleService, never()).setNewImageToVehicle(anyLong(), any(Image.class));
    }

    @Test
    void testSetImageToVehicle_UploadFails() {
        long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        vehicle.setModel("X5");
        vehicle.setAutomatic(true);

        // Mock für VehicleService
        when(vehicleService.findVehicleById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Mock für uploadImageToBudget, um eine Ausnahme zu werfen
        doThrow(new RuntimeException("Upload failed")).when(imagesService).uploadImageToBudget(file);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagesService.setImageToVehicle(vehicleId, file);
        });

        assertEquals("Upload failed", exception.getMessage());
        verify(vehicleService).findVehicleById(vehicleId);
        verify(vehicleService, never()).setNewImageToVehicle(anyLong(), any(Image.class));
    }
}
