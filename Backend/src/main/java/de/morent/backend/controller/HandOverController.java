package de.morent.backend.controller;

import de.morent.backend.dtos.bookings.HandOverConfirmationDto;
import de.morent.backend.dtos.bookings.HandOverDto;
import de.morent.backend.services.BookingService;
import de.morent.backend.services.HandOverService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/handover")
public class HandOverController {

    private BookingService bookingService;
    private HandOverService handOverService;

    // ------- JUST FOR ADMINISTRATION
    // POST A NEW HANDOVER - RETURN A CAR - ADMIN
    @PostMapping("/return")
    public HandOverConfirmationDto handOverVehicle(@RequestBody HandOverDto dto) {
        return handOverService.newHandOverConfirmation(dto);
    }

    // GET ALL OLD DAMAGES ONE VEHICLE - ADMIN
}
