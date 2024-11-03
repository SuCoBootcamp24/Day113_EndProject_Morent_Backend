package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingShortResponseDto;
import de.morent.backend.dtos.bookings.HandOverConfirmationDto;
import de.morent.backend.dtos.bookings.HandOverDto;
import de.morent.backend.entities.*;
import de.morent.backend.enums.BookingStatus;
import de.morent.backend.mappers.BookingMapper;
import de.morent.backend.repositories.DamageRepository;
import de.morent.backend.repositories.HandoverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HandOverService {

    private HandoverRepository handoverRepository;
    private BookingService bookingService;
    private DamageRepository damageRepository;
    private MailService mailService;

    public HandOverService(HandoverRepository handoverRepository, BookingService bookingService, DamageRepository damageRepository, MailService mailService) {
        this.handoverRepository = handoverRepository;
        this.bookingService = bookingService;
        this.damageRepository = damageRepository;
        this.mailService = mailService;
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
                    damage.setCreated(LocalDate.now());
                    damage.setRepaired(false);
                    damageRepository.save(damage);
                    return damage;
                }).toList();

        autoDamageProfile.getDamages().addAll(newDamages);

        Handover handover = new Handover();
        handover.setDate(LocalDate.now());
        handover.setNewMileage(dto.newMileage());
        handover.setTankFull(dto.isTankFull());
        handover.setDamages(newDamages);

        booking.setHandover(handover);
        booking.setStatus(BookingStatus.IN_REVIEW);
        BookingShortResponseDto bookingDto = BookingMapper.mapToShortDto(booking);
        handoverRepository.save(handover);

        HandOverConfirmationDto handOverConfirmation = new HandOverConfirmationDto(bookingDto,
                dto.newMileage(),
                dto.isTankFull(),
                dto.newDamages());

        mailService.sendHandOverConfirmationEmail(booking.getUser().getEmail(), handOverConfirmation);

        return new HandOverConfirmationDto(
                bookingDto,
                dto.newMileage(),
                dto.isTankFull(),
                dto.newDamages());
    }
}
