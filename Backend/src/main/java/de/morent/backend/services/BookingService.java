package de.morent.backend.services;

import de.morent.backend.entities.Booking;
import de.morent.backend.entities.VehicleExemplar;
import de.morent.backend.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Check availability vehicle
    public List<Booking> getAllExemplarBooking (long vehicleExemplarId) {
        return bookingRepository.findAll().stream().filter(booking-> booking.getVehicleId().getId() == vehicleExemplarId).toList();
    }
}
