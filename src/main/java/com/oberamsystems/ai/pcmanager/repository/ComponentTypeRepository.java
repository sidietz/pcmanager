package com.oberamsystems.ai.pcmanager.repository;

import com.oberamsystems.ai.pcmanager.model.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {
}
