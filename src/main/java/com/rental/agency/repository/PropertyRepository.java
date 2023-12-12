package com.rental.agency.repository;

import com.rental.agency.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.capacity > SIZE(p.tenants)")
    List<Property> findAvailableProperties();

    Optional<Property> findByEirCodeOrAddress(String eirCode, String address);

    @Query("SELECT p FROM Property p WHERE SIZE(p.tenants) > 0")
    List<Property> findByTenantsIsNotEmpty();

}
