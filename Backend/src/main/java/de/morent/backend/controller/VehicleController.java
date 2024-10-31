package de.morent.backend.controller;

import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleExemplarDto;
import de.morent.backend.dtos.vehicle.VehicleRequestDTO;
import de.morent.backend.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    //GET / Get all vehicles
/*    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }  */

    //GET / Get all vehicles (PAGES)
    @GetMapping("{pageNo}/{recordCount}")
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


    //GET / one Vehicle-Exemplar

    //POST / Create Vehicle-Exemplar (auto generate)
    @PostMapping("/exemplar")
    public ResponseEntity<List<VehicleExemplarDto>> createVehicleExemplar(@RequestParam long vehicleId, @RequestParam int quantity, @RequestParam BigDecimal price) {
        return ResponseEntity.ok(vehicleService.createVehicleExemplar(vehicleId, quantity, price));
    }


    //UPDATE

    //DELETE
}
