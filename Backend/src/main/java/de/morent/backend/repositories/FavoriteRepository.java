package de.morent.backend.repositories;

import de.morent.backend.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndVehicleId(long userId, long vehicleId);
    Optional<List<Favorite>> findAllByUserId(Long userId);
}
