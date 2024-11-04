package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingShortResponseDto;
import de.morent.backend.dtos.bookings.DamageDto;
import de.morent.backend.dtos.bookings.HandOverConfirmationDto;
import de.morent.backend.dtos.bookings.HandOverDto;
import de.morent.backend.entities.*;
import de.morent.backend.enums.BookingStatus;
import de.morent.backend.mappers.BookingMapper;
import de.morent.backend.repositories.DamageRepository;
import de.morent.backend.repositories.HandoverRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HandOverService {

    private HandoverRepository handoverRepository;
    private BookingService bookingService;
    private DamageRepository damageRepository;
    private MailService mailService;
    private VehicleService vehicleService;

    public HandOverService(HandoverRepository handoverRepository, BookingService bookingService, DamageRepository damageRepository, MailService mailService, @Lazy VehicleService vehicleService) {
        this.handoverRepository = handoverRepository;
        this.bookingService = bookingService;
        this.damageRepository = damageRepository;
        this.mailService = mailService;
        this.vehicleService = vehicleService;
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
        booking.setStatus(BookingStatus.COMPLETED);
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

    // ADMIN - GET AUTO OLD DAMAGES
    public List<DamageDto> getOldDamages(Long vehicleId) {
        VehicleExemplar vehicle = vehicleService.findExemplarById(vehicleId);
        List<Damage> damages = vehicle.getDamageProfile().getDamages();

        return damages.stream().map(damage -> new DamageDto(
                damage.getDamagePosition(),
                damage.getDamageDescription(),
                damage.getCreated(),
                damage.isRepaired()
        )).toList();
    }
}
