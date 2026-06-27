package com.oberamsystems.ai.pcmanager.repository;

import com.oberamsystems.ai.pcmanager.model.DatabaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseProductRepository extends JpaRepository<DatabaseProduct, Long> {
}
