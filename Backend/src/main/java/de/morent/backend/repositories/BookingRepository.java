package de.morent.backend.repositories;

import de.morent.backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByVehicleId(Long vehicleId);

    List<Booking> findAllByPickUpLocationId(Long pickUpLocationId);
}
