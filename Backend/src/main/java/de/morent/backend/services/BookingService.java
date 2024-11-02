package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingRequestDto;
import de.morent.backend.dtos.bookings.BookingResponseDto;
import de.morent.backend.dtos.bookings.BookingShortResponseDto;
import de.morent.backend.entities.Booking;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.User;
import de.morent.backend.entities.VehicleExemplar;
import de.morent.backend.enums.BookingStatus;
import de.morent.backend.exceptions.IllegalBookingException;
import de.morent.backend.mappers.BookingMapper;
import de.morent.backend.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private MailService mailService;

    public BookingService(BookingRepository bookingRepository, UserService userService, @Lazy VehicleService vehicleService, @Lazy StoreService storeService, MailService mailService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.storeService = storeService;
        this.mailService = mailService;
    }

    @Transactional
    public BookingResponseDto makeBooking(BookingRequestDto dto, Authentication authentication) throws IllegalBookingException {
        confirmBookingDataValidity(dto, authentication);

        User user = userService.findUserByEmail(authentication.getName());
        checkUserProfileIfComplete(user);
        VehicleExemplar vehicle = vehicleService.findEntityVehicleExemplarById(dto.vehicleExemplarId());
        Store pickUpStore = storeService.findById(dto.pickUpLocationId());
        Store dropOfStore = storeService.findById(dto.dropOffLocationId());

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setBookingNumber(generateUniqueBookingNumber());
        newBooking.setVehicle(vehicle);
        newBooking.setPickUpDate(dto.pickUpDate());
        newBooking.setPlannedDropOffDate(dto.planedDropOffDate());
        newBooking.setPickUpLocation(pickUpStore);
        newBooking.setDropOffLocation(dropOfStore);
        newBooking.setTotalPrice(calculateTotalPrice(dto, vehicle));
        newBooking.setDropOffDifferentStoreExtraCharge(dto.pickUpLocationId() != dto.dropOffLocationId());
        newBooking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(newBooking);

        BookingResponseDto booking = BookingMapper.mapToDto(newBooking);
        // mailService.sendBookingConfirmationEmail(user.getEmail(),booking);
        return booking;
    }

    private void confirmBookingDataValidity(BookingRequestDto dto, Authentication authentication) throws IllegalBookingException {

        if(!authentication.isAuthenticated()) throw  new SecurityException("User is not authenticated");

        if (dto.pickUpDate().isAfter(dto.planedDropOffDate()) || dto.pickUpDate().isBefore(LocalDate.now())) throw new IllegalBookingException("The dates are invalid");

        if (!autoIsAvailable(dto.vehicleExemplarId(), dto.pickUpDate(), dto.planedDropOffDate())) throw new IllegalArgumentException("Vehicle is not available for the given dates");

    }

    private BigDecimal calculateTotalPrice(BookingRequestDto dto, VehicleExemplar vehicle) {
        BigDecimal totalPrice = calculateTotalPrice(vehicle.getPricePerDay(), dto.pickUpDate(), dto.planedDropOffDate());
        // if pick-up and drop-of stores are different, charge a 150.00 € extra fee
        if(dto.pickUpLocationId() != dto.dropOffLocationId()) {
            totalPrice = totalPrice.add(BigDecimal.valueOf(150.00));
        }
        return totalPrice;
    }


    private void checkUserProfileIfComplete(User user) throws IllegalBookingException {
        StringBuilder errorMessage = new StringBuilder("Bitte vervollständigen Sie zuerst Ihr Profil. Folgende Angaben fehlen: ");

        if (user.getProfile().getAddress() == null)
            errorMessage.append("\n- Adresse");

        if (user.getProfile().getPhoneNumber() == null)
            errorMessage.append("\n- Telefonnummer");

        if (user.getProfile().getDateOfBirth() == null)
            errorMessage.append("\n- Geburtsdatum");

        if (errorMessage.length() > "Bitte vervollständigen Sie zuerst Ihr Profil. Folgende Angaben fehlen: ".length()) {
            throw new IllegalBookingException(errorMessage.toString());
        }
    }

    private String generateUniqueBookingNumber() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 16);
    }

    private BigDecimal calculateTotalPrice(BigDecimal pricePerDay, LocalDate localDate, LocalDate localDate1) {
        long daysBetween = ChronoUnit.DAYS.between(localDate, localDate1);
        return pricePerDay.multiply(BigDecimal.valueOf(daysBetween));
    }

    // Check availability vehicle and validity of dates
    public boolean autoIsAvailable(long autoId, LocalDate pickUpDate, LocalDate dropOffDate) {
        return bookingRepository.isVehicleAvailable(autoId, pickUpDate, dropOffDate);
    }

    public List<Booking> getAllExemplarBooking (long vehicleExemplarId) {
        return bookingRepository.findAllByVehicleId(vehicleExemplarId);
    }

    public List<Booking> getBookingsFromStore(long storeId) {
        return bookingRepository.findAllByPickUpLocationId(storeId);
    }

    public List<BookingShortResponseDto> getAllPersonalBookings(Authentication authentication) {
        if(!authentication.isAuthenticated()) throw new IllegalStateException("User not authenticated");
        User user = userService.findUserByEmail(authentication.getName());
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getUser().getId() == user.getId())
                .map(BookingMapper::mapToShortDto)
                .toList();
    }

    public BookingResponseDto getPersonalBookingById(Long id, Authentication authentication) {
        if(!authentication.isAuthenticated()) throw new IllegalStateException("User not authenticated");
        User user = userService.findUserByEmail(authentication.getName());
        Booking booking = bookingRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return BookingMapper.mapToDto(booking);
    }

    public void cancelBooking(Long id, Authentication authentication) {
        if(!authentication.isAuthenticated()) throw new IllegalStateException("User not authenticated");
        User user = userService.findUserByEmail(authentication.getName());
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        if(booking.getUser().getId()!= user.getId()) throw new IllegalArgumentException("You are not allowed to cancel this booking");
        if (LocalDate.now().isAfter(booking.getPickUpDate().minusDays(1))) {
            throw new IllegalArgumentException("Cancellation is only allowed up to 24 hours before the booking start date");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
