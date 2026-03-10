package com.oberamsystems.ai.pcmanager.repository;

import com.oberamsystems.ai.pcmanager.model.PC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRepository extends JpaRepository<PC, Long> {
}
