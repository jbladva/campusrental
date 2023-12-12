package com.rental.agency.repository;

import com.rental.agency.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {

    Optional<Tenant> findByEmailOrPhoneNumberAndPropertyId(String email, String phoneNumber,long propertyId);

    Optional<Object> findByEmailOrPhoneNumber(String email, String phoneNumber);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Tenant> findByEmail(String email);
}
