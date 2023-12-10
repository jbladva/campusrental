package com.campusrental.repository;

import com.campusrental.entity.Property;
import com.campusrental.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {

    Optional<Tenant> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
