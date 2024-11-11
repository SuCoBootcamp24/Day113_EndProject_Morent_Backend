package de.morent.backend.dtos.vehicle;

import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public record VehicleExemplarVehicleDTO(
        long vehicleId,
        List<Long> storeIds,
        int quantity,
        BigDecimal price
) {
}
