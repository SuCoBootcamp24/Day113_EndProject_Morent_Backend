package de.morent.backend.controller;

import de.morent.backend.dtos.search.AutoCountDto;
import de.morent.backend.dtos.search.AutoCountRequestDto;
import de.morent.backend.dtos.search.FilteringDto;
import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleExemplarDto;
import de.morent.backend.dtos.vehicle.VehicleExemplarVehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    //---------vehicle model---------

    //POST / Create vehicle
    @PostMapping
    public ResponseEntity<Void> createVehicle(@RequestBody VehicleRequestDTO dto) {
        if (vehicleService.createVehicle(dto)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }

    //POST / create more vehicle
    @PostMapping("/more")
    public ResponseEntity<Void> createMoreVehicles(@RequestBody VehicleRequestDTO[] dtos) {
        if (vehicleService.createMoreVehicles(dtos)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }


    //GET / Get all vehicles (PAGES)
    @GetMapping("all")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(@RequestParam int pageNo, @RequestParam (defaultValue = "10" ) int recordCount) {
        return ResponseEntity.ok(vehicleService.getAllVehicles(pageNo, recordCount));
    }

    //GET /{id} Get a specific vehicle
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable long id) {
        return ResponseEntity.ok(vehicleService.findVehicleById(id));
    }

    //PUT / Update a specific vehicle
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable long id, @RequestBody VehicleRequestDTO dto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, dto));
    }

    //DELETE /{id} Delete a specific vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    //-------VehicleExemplar-------------

    //GET / Get all Vehicle-Exemplar / Get all Vehicle-Exemplar in one Store
    @PostMapping("/exemplars")
    public ResponseEntity<List<VehicleExemplarDto>> getAllVehicleExemplarInStore(@RequestBody FilteringDto dto) {
        return ResponseEntity.ok(vehicleService.getFilteredCars(dto));
    }

    //GET / one Vehicle-Exemplar
    @GetMapping("/exemplar/{id}")
    public ResponseEntity<VehicleExemplarDto> getVehicleExemplarById(@PathVariable long id) {
        return ResponseEntity.ok(vehicleService.findVehicleExemplarById(id));
    }

    //POST / Create Vehicle-Exemplar (auto generate)
    @PostMapping("/exemplar")
    public ResponseEntity<List<VehicleExemplarDto>> createVehicleExemplar(@RequestParam long vehicleId, @RequestParam long storeId, @RequestParam int quantity, @RequestParam BigDecimal price) {
        return ResponseEntity.ok(vehicleService.createVehicleExemplar(vehicleId, storeId, quantity, price));
    }

    @PostMapping("/exemplar/more")
    public ResponseEntity<List<List<VehicleExemplarDto>>> createMoreVehicleExemplar(@RequestBody VehicleExemplarVehicleDTO dto) {
        return ResponseEntity.ok(vehicleService.createMoreVehicleExemplar(dto));
    }

    //UPDATE

    //DELETE
    @DeleteMapping("/exemplar/{id}")
    public ResponseEntity<Void> deleteVehicleExemplar(@PathVariable long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    // Get vehicles count per type
    @PostMapping("/count")
    public ResponseEntity<AutoCountDto> countVehiclesPerType(@RequestBody AutoCountRequestDto dto) {
        return ResponseEntity.ok(vehicleService.countVehiclesPerType(dto));
    }
}
