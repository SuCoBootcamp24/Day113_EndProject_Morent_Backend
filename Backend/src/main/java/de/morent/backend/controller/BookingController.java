package de.morent.backend.controller;

import de.morent.backend.dtos.bookings.BookingRequestDto;
import de.morent.backend.dtos.bookings.BookingResponseDto;
import de.morent.backend.dtos.bookings.BookingShortResponseDto;
import de.morent.backend.exceptions.IllegalBookingException;
import de.morent.backend.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST NEW BOOKING - USER
    @PostMapping
    public ResponseEntity<BookingResponseDto> makeBooking(Authentication authentication, @RequestBody BookingRequestDto dto) throws IllegalBookingException {
        return ResponseEntity.ok(bookingService.makeBooking(dto, authentication));
    }

    // GET ALL PERSONAL BOOKINGS - USER
    @GetMapping
    public ResponseEntity<List<BookingShortResponseDto>> getAllPersonalBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getAllPersonalBookings(authentication));
    }

    // GET ONE PERSONAL BOOKING - USER
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getPersonalBookingById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(bookingService.getPersonalBookingById(id, authentication));
    }

    // CANCEL ONE BOOKING UP TO 24H BEFORE THE START DATE - USER
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {

        return ResponseEntity.noContent().build();
    }

    // GET ALL BOOKING / EVEN JUST FROM A STORE - ADMIN
    @GetMapping("/admin")
    public ResponseEntity<List<BookingShortResponseDto>> getAllBookings(@RequestParam (required = false) long storeId, @RequestParam int pageNo, @RequestParam int recordSize) {
        return ResponseEntity.ok(bookingService.getAllBookings(storeId, pageNo, recordSize));
    }






}
