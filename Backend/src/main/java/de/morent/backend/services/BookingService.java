package de.morent.backend.services;

import de.morent.backend.entities.Booking;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.VehicleExemplar;
import de.morent.backend.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
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
        List<Booking> bookings = findAllBookings();
        if (bookings == null || bookings.isEmpty()) {
            return List.of();
        }
        return bookings.stream()
                .filter(booking-> booking.getVehicleId().getId() == vehicleExemplarId)
                .toList();
    }

    private List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsFromStore(long storeId) {
        List<Booking> bookings = findAllBookings();
        if (bookings == null || bookings.isEmpty()) {
            return List.of();
        }
        return bookings.stream()
                .filter(booking-> booking.getPickUpLocation().getId() == storeId)
                .toList();
    }
}
