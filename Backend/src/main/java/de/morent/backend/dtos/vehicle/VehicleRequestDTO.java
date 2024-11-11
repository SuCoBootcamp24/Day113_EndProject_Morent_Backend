package de.morent.backend.dtos.vehicle;

import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record VehicleRequestDTO(

        @NotBlank
        CarType carType,
        @NotBlank
        String brand,
        @NotBlank
        String model,
        @Min(1)
        int seats,
        @Min(1)
        int engineCapacity,
        @NotNull
        boolean isAutomatic,
        @NotBlank
        FuelType fuelType,
        @DecimalMin("0.1")
        float consumption
) {
}
