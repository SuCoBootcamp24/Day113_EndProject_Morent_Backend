package de.morent.backend.controller;

import de.morent.backend.services.StoreService;
import de.morent.backend.dtos.store.StoreRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/store")
//Only ADMIN authority can use this endpoints
public class StoreController {

    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    //POST / create a new store
    @PostMapping
    public ResponseEntity<Void> createNewStore(@RequestBody @Valid StoreRequestDTO dto) {
        if (storeService.createNewStore(dto)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }


    //GET / Get all stores



    //GET /{id} Get a specific store



    //PUT /{id} Update a specific store

}
