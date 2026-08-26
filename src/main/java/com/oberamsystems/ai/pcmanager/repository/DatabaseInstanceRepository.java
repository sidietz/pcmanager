package com.oberamsystems.ai.pcmanager.repository;

import com.oberamsystems.ai.pcmanager.model.DatabaseInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseInstanceRepository extends JpaRepository<DatabaseInstance, Long> {
}
