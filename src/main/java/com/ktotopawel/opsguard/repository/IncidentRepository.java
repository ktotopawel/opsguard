package com.ktotopawel.opsguard.repository;

import com.ktotopawel.opsguard.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
}
