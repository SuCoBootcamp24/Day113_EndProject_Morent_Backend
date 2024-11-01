package de.morent.backend.mappers;

import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.dtos.vehicle.VehicleExemplarDto;
import de.morent.backend.entities.VehicleExemplar;

public class VehicleExemplarMapper {
    public static VehicleExemplarDto mapToDto(VehicleExemplar car) {
        VehicleDTO vehicleDTO = VehicleMapper.mapToDto(car.getVehicle());
        return new VehicleExemplarDto(
                car.getId(),
                vehicleDTO,
                car.getPricePerDay(),
                car.getMileage(),
                car.getVehicleStatus().getStatusName(),
                car.getCreated(),
                car.getDamageProfile().getDamages().size(),
                car.getConstYear()
        );
    }
}
