package de.morent.backend.mappers;

import de.morent.backend.dtos.bookings.DamageDto;
import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleExemplarDto;
import de.morent.backend.entities.VehicleExemplar;

import java.util.List;

public class VehicleExemplarMapper {
    public static VehicleExemplarDto mapToDto(VehicleExemplar car) {
        VehicleDTO vehicleDTO = VehicleMapper.mapToDto(car.getVehicle());

        List<DamageDto> damages = car.getDamageProfile().getDamages().stream().map(damage -> new DamageDto(
                damage.getDamagePosition(),
                damage.getDamageDescription(),
                damage.getCreated(),
                damage.isRepaired()
        )).toList();
        return new VehicleExemplarDto(
                car.getId(),
                vehicleDTO,
                car.getPricePerDay(),
                car.getMileage(),
                car.getVehicleStatus().getStatusName(),
                car.getCreated(),
                damages,
                car.getConstYear()
        );
    }
}
