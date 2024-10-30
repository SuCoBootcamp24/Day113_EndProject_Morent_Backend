package de.morent.backend.services;

import de.morent.backend.dtos.images.ImgbbDTO;
import de.morent.backend.entities.Image;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.repositories.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagesService {

    @Value("${imgbb.key}")
    private String IMGBB_KEY;

    @Value("${imgbb.url}")
    private String IMGBB_URL;


    private VehicleService vehicleService;
    private ImageRepository imageRepository;

    public ImagesService(VehicleService vehicleService, ImageRepository imageRepository) {
        this.vehicleService = vehicleService;
        this.imageRepository = imageRepository;
    }

    public boolean upgradeImageFromVehicle(long vehicleId, MultipartFile file) {
        Optional<Vehicle> existingVehicle = vehicleService.findVehicleById(vehicleId);
        if (existingVehicle.isEmpty()) throw new EntityNotFoundException("Vehicle not found");


        Image img = setImageToVehicle(existingVehicle.get(), file);
        vehicleService.setNewImageToVehicle(vehicleId, img);
        return true;

    }
    public Image setImageToVehicle(Vehicle vehicle, MultipartFile file) {

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }

        String imgName = vehicle.getBrand() + "_" + vehicle.getModel();
        if (vehicle.isAutomatic()) imgName += "_Automatik";
        else imgName += "_Schalter";

        ResponseEntity<ImgbbDTO> response = uploadImageToBudget(file);

        Image img = new Image();
        img.setImagesName(imgName);
        img.setImageUrl(response.getBody().data().url());
        img.setThumbnailUrl(response.getBody().thumb().thumbnailUrl());

        img = imageRepository.save(img);

        return img;
    }


    //setImageToVehicle




    ResponseEntity<ImgbbDTO> uploadImageToBudget(MultipartFile file) {
        RestClient restClient = RestClient.create(IMGBB_URL);

        MultiValueMap<String, Resource> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        ResponseEntity<ImgbbDTO> response = restClient.post()
                .uri("?key=" + IMGBB_KEY)
                .body(body)
                .retrieve()
                .toEntity(ImgbbDTO.class);

        return response;
    }


}
