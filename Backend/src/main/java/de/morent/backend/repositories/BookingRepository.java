package de.morent.backend.repositories;

import de.morent.backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByVehicleId(Long vehicleId);

    List<Booking> findAllByPickUpLocationId(Long pickUpLocationId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN FALSE ELSE TRUE END " +
            "FROM Booking b WHERE b.vehicle.id = :autoId AND " +
            "(b.pickUpDate < :dropOffDate AND b.plannedDropOffDate > :pickUpDate " +
            "AND b.pickUpDate != :dropOffDate AND b.plannedDropOffDate != :pickUpDate)")
    boolean isVehicleAvailable(@Param("autoId") long autoId,
                               @Param("pickUpDate") LocalDate pickUpDate,
                               @Param("dropOffDate") LocalDate dropOffDate);
}
