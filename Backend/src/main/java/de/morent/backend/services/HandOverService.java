package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingShortResponseDto;
import de.morent.backend.dtos.bookings.HandOverConfirmationDto;
import de.morent.backend.dtos.bookings.HandOverDto;
import de.morent.backend.entities.Booking;
import de.morent.backend.entities.Damage;
import de.morent.backend.entities.DamageProfile;
import de.morent.backend.entities.Handover;
import de.morent.backend.enums.BookingStatus;
import de.morent.backend.mappers.BookingMapper;
import de.morent.backend.repositories.HandoverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HandOverService {

    private HandoverRepository handoverRepository;
    private BookingService bookingService;

    public HandOverService(HandoverRepository handoverRepository, BookingService bookingService) {
        this.handoverRepository = handoverRepository;
        this.bookingService = bookingService;
    }

    @Transactional
    public HandOverConfirmationDto newHandOverConfirmation(HandOverDto dto) {

        Booking booking = bookingService.getBookingById(dto.bookingId());
        DamageProfile autoDamageProfile = booking.getVehicle().getDamageProfile();

        List<Damage> newDamages = dto.newDamages().stream()
                .map(damageDto -> {
                    Damage damage = new Damage();
                    damage.setDamagePosition(damageDto.position());
                    damage.setDamageDescription(damageDto.description());
                    damage.setCreated(damageDto.createdAt());
                    damage.setRepaired(false);
                    return damage;
                }).toList();

        autoDamageProfile.getDamages().addAll(newDamages);

        Handover handover = new Handover();
        handover.setDate(LocalDate.now());
        handover.setNewMileage(dto.newMileage());
        handover.setTankFull(dto.isTankFull());
        handover.setDamages(newDamages);

        handoverRepository.save(handover);
        BookingShortResponseDto bookingDto = BookingMapper.mapToShortDto(booking);

        booking.setHandover(handover);
        booking.setStatus(BookingStatus.IN_REVIEW);

        return new HandOverConfirmationDto(
                bookingDto,
                dto.newMileage(),
                dto.isTankFull(),
                dto.newDamages());
    }
}
