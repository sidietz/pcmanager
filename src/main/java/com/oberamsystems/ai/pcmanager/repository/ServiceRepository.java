package com.oberamsystems.ai.pcmanager.repository;

import com.oberamsystems.ai.pcmanager.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findBySoftwareStackId(Long stackId);
}
