package de.morent.backend.services;

import de.morent.backend.dtos.images.ImgbbDTO;
import de.morent.backend.dtos.images.ImgbbDataDTO;
import de.morent.backend.dtos.images.ImgbbThumbDTO;
import de.morent.backend.entities.Image;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.repositories.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ImagesServiceTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImagesService imagesService;


    private Vehicle vehicle;
    private MultipartFile file;
    private ResponseEntity<ImgbbDTO> responseEntity;
    private ImagesService imagesServiceSpy;
    private ArgumentCaptor<Image> imageCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imagesService, "IMGBB_KEY", "test-key");
        ReflectionTestUtils.setField(imagesService, "IMGBB_URL", "http://test-url.com");

        vehicle = new Vehicle();
        vehicle.setBrand("TestBrand");
        vehicle.setModel("TestModel");

        file = mock(MultipartFile.class);

        ImgbbDataDTO data = new ImgbbDataDTO(
                "123",
                "Test Image",
                "http://viewer-url.com",
                "http://image-url.com",
                "http://display-url.com"
        );

        ImgbbThumbDTO thumb = new ImgbbThumbDTO("http://thumbnail-url.com");
        ImgbbDTO imgbbDTO = new ImgbbDTO(data, thumb);

        responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(imgbbDTO);

        imagesServiceSpy = spy(imagesService);
        doReturn(responseEntity).when(imagesServiceSpy)
                .uploadImageToBudget(any(MultipartFile.class));
        imageCaptor = ArgumentCaptor.forClass(Image.class);
    }

    @Test
    public void testSetImageToVehicle_Automatic() {
        vehicle.setAutomatic(true);

        Image savedImage = new Image();
        savedImage.setId(1L);
        savedImage.setImagesName("TestBrand_TestModel_Automatik");
        savedImage.setImageUrl("http://image-url.com");
        savedImage.setThumbnailUrl("http://thumbnail-url.com");

        when(imageRepository.save(imageCaptor.capture())).thenReturn(savedImage);

        Image result = imagesServiceSpy.setImageToVehicle(vehicle, file);

        assertNotNull(result);
        assertEquals("TestBrand_TestModel_Automatik", result.getImagesName());
        assertEquals("http://image-url.com", result.getImageUrl());
        assertEquals("http://thumbnail-url.com", result.getThumbnailUrl());

        Image capturedImage = imageCaptor.getValue();
        assertEquals("TestBrand_TestModel_Automatik", capturedImage.getImagesName());
        assertEquals("http://image-url.com", capturedImage.getImageUrl());
        assertEquals("http://thumbnail-url.com", capturedImage.getThumbnailUrl());
    }

    @Test
    public void testSetImageToVehicle_Manual() {
        vehicle.setAutomatic(false);

        Image savedImage = new Image();
        savedImage.setId(2L);
        savedImage.setImagesName("TestBrand_TestModel_Schalter");
        savedImage.setImageUrl("http://image-url.com");
        savedImage.setThumbnailUrl("http://thumbnail-url.com");

        when(imageRepository.save(imageCaptor.capture())).thenReturn(savedImage);

        Image result = imagesServiceSpy.setImageToVehicle(vehicle, file);

        assertNotNull(result);
        assertEquals("TestBrand_TestModel_Schalter", result.getImagesName());
        assertEquals("http://image-url.com", result.getImageUrl());
        assertEquals("http://thumbnail-url.com", result.getThumbnailUrl());

        Image capturedImage = imageCaptor.getValue();
        assertEquals("TestBrand_TestModel_Schalter", capturedImage.getImagesName());
        assertEquals("http://image-url.com", capturedImage.getImageUrl());
        assertEquals("http://thumbnail-url.com", capturedImage.getThumbnailUrl());
    }

    @Test
    public void testSetImageToVehicle_NullVehicle() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imagesService.setImageToVehicle(null, file);
        });

        assertEquals("Vehicle must not be null", exception.getMessage());
    }

    @Test
    public void testSetImageToVehicle_NullFile() {
        // Arrange
        vehicle.setAutomatic(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imagesService.setImageToVehicle(vehicle, null);
        });

        assertEquals("File must not be null", exception.getMessage());
    }

    @Test
    public void testSetImageToVehicle_UploadFailure() {
        // Arrange
        vehicle.setAutomatic(true);

        ImagesService imagesServiceSpy = spy(imagesService);
        doThrow(new RuntimeException("Upload failed")).when(imagesServiceSpy)
                .uploadImageToBudget(any(MultipartFile.class));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            imagesServiceSpy.setImageToVehicle(vehicle, file);
        });

        assertEquals("Upload failed", exception.getMessage());
    }

}
