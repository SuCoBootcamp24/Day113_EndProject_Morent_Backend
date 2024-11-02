package de.morent.backend.mappers;

import de.morent.backend.dtos.address.AddressDTO;
import de.morent.backend.dtos.bookings.BookingResponseDto;
import de.morent.backend.dtos.bookings.BookingShortResponseDto;
import de.morent.backend.dtos.store.BookingStoreResponseDto;
import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.entities.Booking;

public class BookingMapper {
    public static BookingResponseDto mapToDto(Booking newBooking) {

        AddressDTO pickUpAddress = new AddressDTO(
                newBooking.getPickUpLocation().getAddress().getStreet(),
                newBooking.getPickUpLocation().getAddress().getHouseNumber(),
                newBooking.getPickUpLocation().getAddress().getCity(),
                newBooking.getPickUpLocation().getAddress().getCountry(),
                newBooking.getPickUpLocation().getAddress().getCoordinates(),
                newBooking.getPickUpLocation().getAddress().getZipCode()
        );
        AddressDTO dropOffAddress = new AddressDTO(
                newBooking.getDropOffLocation().getAddress().getStreet(),
                newBooking.getDropOffLocation().getAddress().getHouseNumber(),
                newBooking.getDropOffLocation().getAddress().getCity(),
                newBooking.getDropOffLocation().getAddress().getCountry(),
                newBooking.getDropOffLocation().getAddress().getCoordinates(),
                newBooking.getDropOffLocation().getAddress().getZipCode()
        );

        VehicleDTO vehicleDto = VehicleMapper.mapToDto(newBooking.getVehicle().getVehicle());

        BookingStoreResponseDto pickUpStore = new BookingStoreResponseDto(
                newBooking.getPickUpLocation().getName(),
                pickUpAddress
        );
        BookingStoreResponseDto dropOffStore = new BookingStoreResponseDto(
                newBooking.getPickUpLocation().getName(),
                dropOffAddress
        );


        return new BookingResponseDto(
                newBooking.getId(),
                newBooking.getBookingNumber(),
                newBooking.getCreated(),
                newBooking.getUser().getProfile().getFirstName(),
                newBooking.getUser().getProfile().getLastName(),
                newBooking.getUser().getProfile().getDateOfBirth(),
                vehicleDto,
                newBooking.getPickUpDate(),
                newBooking.getPlannedDropOffDate(),
                pickUpStore,
                dropOffStore,
                newBooking.getVehicle().getPricePerDay(),
                newBooking.getTotalPrice(),
                newBooking.getDropOffDifferentStoreExtraCharge(),
                newBooking.getStatus()
        );

    }

    public static BookingShortResponseDto mapToShortDto(Booking booking) {
        return new BookingShortResponseDto(
                booking.getBookingNumber(),
                booking.getUser().getProfile().getFirstName(),
                booking.getUser().getProfile().getLastName(),
                booking.getPickUpLocation().getName(),
                booking.getPickUpLocation().getAddress().getCity(),
                booking.getPickUpDate(),
                booking.getStatus(),
                booking.getTotalPrice()
                );
    }
}
