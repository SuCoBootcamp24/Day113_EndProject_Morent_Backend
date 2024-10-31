package de.morent.backend.specifications;

import de.morent.backend.entities.Vehicle;
import de.morent.backend.entities.VehicleExemplar;
import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class VehicleSpecification {

    public static Specification<VehicleExemplar> isCarType (List<CarType> carTypes) {
        return (root, query, criteriaBuilder) -> {
            Join<VehicleExemplar, Vehicle> vehicle = root.join("vehicle");
            return vehicle.get("carType").in(carTypes);
        };
    }

    public static Specification<VehicleExemplar> isFuelType (List<FuelType> fuelTypes) {
        return (root, query, criteriaBuilder) -> {
            Join<VehicleExemplar, Vehicle> vehicle = root.join("vehicle");
            return vehicle.get("fuelType").in(fuelTypes);
        };
    }

    public static Specification<VehicleExemplar> hasMaxPrice(BigDecimal pricePerDay) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("pricePerDay"), pricePerDay);
    }

    public static Specification<VehicleExemplar> inStore(long storeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("store").get("id"), storeId);
    }
}
