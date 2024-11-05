package de.morent.backend.repositories;

import de.morent.backend.entities.VehicleExemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleExemplarRepository extends JpaRepository<VehicleExemplar, Long>, JpaSpecificationExecutor<VehicleExemplar> {
    List<VehicleExemplar> findByStoreId(long storeId);
}
