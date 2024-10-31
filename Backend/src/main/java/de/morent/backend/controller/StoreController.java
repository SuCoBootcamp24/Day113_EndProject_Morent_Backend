package de.morent.backend.controller;

import de.morent.backend.dtos.store.StoreShortDTO;
import de.morent.backend.services.StoreService;
import de.morent.backend.dtos.store.StoreRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //GET /geosearch
    @GetMapping("/geosearch")
    public ResponseEntity<List<StoreShortDTO>> getStoresByGeoSearch(String address) {
       List<StoreShortDTO> stores = storeService.getStoresCloseByAddress(address);
       if (stores.isEmpty()) return ResponseEntity.notFound().build();
       else return ResponseEntity.ok(stores);
    }


    //GET / Get all stores



    //GET /{id} Get a specific store



    //PUT /{id} Update a specific store

}
