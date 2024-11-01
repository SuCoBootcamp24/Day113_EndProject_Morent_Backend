package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingRequestDto;
import de.morent.backend.dtos.bookings.BookingResponseDto;
import de.morent.backend.entities.Booking;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.User;
import de.morent.backend.entities.VehicleExemplar;
import de.morent.backend.enums.BookingStatus;
import de.morent.backend.exceptions.IllegalBookingException;
import de.morent.backend.mappers.BookingMapper;
import de.morent.backend.repositories.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private UserService userService;
    private VehicleService vehicleService;
    private StoreService storeService;

    public BookingService(BookingRepository bookingRepository, UserService userService, @Lazy VehicleService vehicleService, @Lazy StoreService storeService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.storeService = storeService;
    }

    @Transactional
    public BookingResponseDto makeBooking(BookingRequestDto dto, Authentication authentication) throws IllegalBookingException {

        // to implement yet:
        // user is not locked
        // User Profile not complete yet /maybe with an END request
        // check if dates are not in the past
        // if pick-up and drop-of are different, charge a fee

        if(!authentication.isAuthenticated()) throw  new SecurityException("User is not authenticated");
        User user = userService.findUserByEmail(authentication.getName());
        if(!isUserProfileComplete(user)) throw new IllegalBookingException("User Profile is not complete yet");
        VehicleExemplar vehicle = vehicleService.findEntityVehicleExemplarById(dto.vehicleExemplarId());
        Store pickUpStore = storeService.findById(dto.pickUpLocationId());
        Store dropOfStore = storeService.findById(dto.dropOffLocationId());

        // Check if vehicle exemplar is available
        if (!autoIsAvailable(dto.vehicleExemplarId(), dto.pickUpDate(), dto.planedDropOffDate())) throw new IllegalArgumentException("Vehicle is not available for the given dates");

        BigDecimal totalPrice = calculateTotalPrice(vehicle.getPricePerDay(),dto.pickUpDate(), dto.planedDropOffDate());

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setBookingNumber(generateUniqueBookingNumber());
        newBooking.setVehicle(vehicle);
        newBooking.setPickUpLocation(pickUpStore);
        newBooking.setDropOffLocation(dropOfStore);
        newBooking.setTotalPrice(totalPrice);
        newBooking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(newBooking);

        return BookingMapper.mapToDto(newBooking);
    }


    private boolean isUserProfileComplete(User user) {
        return true;
    }

    private String generateUniqueBookingNumber() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 16);
    }

    private BigDecimal calculateTotalPrice(BigDecimal pricePerDay, LocalDate localDate, LocalDate localDate1) {
        long daysBetween = ChronoUnit.DAYS.between(localDate, localDate1);
        return pricePerDay.multiply(BigDecimal.valueOf(daysBetween));
    }

    // Check availability vehicle
    public boolean autoIsAvailable(long autoId, LocalDate pickUpDate, LocalDate dropOffDate) {
        return bookingRepository.findAll().stream().filter(
          vehicle -> vehicle.getId() == autoId)
                .noneMatch(
                        booking -> booking.getPickUpDate().isBefore(dropOffDate) &&
                        booking.getPlannedDropOffDate().isAfter(pickUpDate));
    }

    public List<Booking> getAllExemplarBooking (long vehicleExemplarId) {
        return bookingRepository.findAllByVehicleId(vehicleExemplarId);
    }

    public List<Booking> getBookingsFromStore(long storeId) {
        return bookingRepository.findAllByPickUpLocationId(storeId);
    }
}
