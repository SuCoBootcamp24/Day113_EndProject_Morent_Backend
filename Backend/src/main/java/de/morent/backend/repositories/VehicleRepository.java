package de.morent.backend.repositories;

import de.morent.backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByBrandAndModelAndIsAutomatic(String brand, String model, boolean isAutomatic);
}
