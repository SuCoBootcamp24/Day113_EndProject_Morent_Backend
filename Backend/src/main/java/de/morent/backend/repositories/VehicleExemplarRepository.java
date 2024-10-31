package de.morent.backend.repositories;

import de.morent.backend.entities.VehicleExemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleExemplarRepository extends JpaRepository<VehicleExemplar, Long>, JpaSpecificationExecutor<VehicleExemplar> {
}
