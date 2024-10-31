package de.morent.backend.repositories;

import de.morent.backend.entities.DamageProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageProfileRepository extends JpaRepository<DamageProfile, Long> {
}
