package de.morent.backend.services;

import de.morent.backend.dtos.images.ImgbbDTO;
import de.morent.backend.entities.Image;
import de.morent.backend.entities.Profile;
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
    private ImageRepository imageRepository;

    public ImagesService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image setImageToVehicle(Vehicle vehicle, MultipartFile file) {

        if (vehicle == null) throw new IllegalArgumentException("Vehicle must not be null");
        if (file == null) throw new IllegalArgumentException("File must not be null");

        String imgName = vehicle.getBrand() + "_" + vehicle.getModel();
        if (vehicle.isAutomatic()) imgName += "_Automatik";
        else imgName += "_Schalter";

        return getImage(file, imgName);
    }

    public Image setImageToUserProfile(Profile userProfile, MultipartFile file) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }

        String imgName = "UserProfile-" + userProfile.getId();
        return getImage(file, imgName);
    }

    private Image getImage(MultipartFile file, String imgName) {
        ResponseEntity<ImgbbDTO> response = uploadImageToBudget(file);
        //System.out.println(response);

        Image img = new Image();
        img.setImagesName(imgName);
        img.setImageUrl(response.getBody().data().url());

        img = imageRepository.save(img);

        return img;
    }

    private ResponseEntity<ImgbbDTO> uploadImageToBudget(MultipartFile file) {
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
