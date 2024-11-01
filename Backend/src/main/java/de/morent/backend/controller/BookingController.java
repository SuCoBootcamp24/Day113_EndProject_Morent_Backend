package de.morent.backend.controller;

import de.morent.backend.dtos.bookings.BookingRequestDto;
import de.morent.backend.dtos.bookings.BookingResponseDto;
import de.morent.backend.exceptions.IllegalBookingException;
import de.morent.backend.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // GET ONE PERSONAL BOOKING - USER

    // CANCEL ONE BOOKING UP TO 24H BEFORE THE START DATE - USER

    // GET ALL BOOKING / EVEN JUST FROM A STORE - ADMIN

    // POST A NEW HANDOVER - RETURN A CAR - ADMIN

    // GET ALL OLD DAMAGES ONE VEHICLE - ADMIN




}
