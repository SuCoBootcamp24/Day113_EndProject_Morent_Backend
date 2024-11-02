package de.morent.backend.mappers;

import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.entities.Vehicle;

public class VehicleMapper {

/*    public static Vehicle mapToEntity(VehicleRequestDTO dto) {
        return new Vehicle(
                dto.carType(),
                dto.brand(),
                dto.model(),
                dto.seats(),
                dto.engineCapacity(),
                dto.isAutomatic(),
                dto.fuelType(),
                dto.img(),
                dto.consumption()
        );
    }*/

    public static VehicleDTO mapToDto(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getCarType(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getSeats(),
                vehicle.getEngineCapacity(),
                vehicle.isAutomatic(),
                vehicle.getFuelType(),
                vehicle.getImage().getImageUrl(),
                vehicle.getConsumption()
        );
    }
}
