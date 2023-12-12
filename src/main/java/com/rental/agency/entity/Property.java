package com.rental.agency.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", unique = true)
    private String address;

    @Column(name = "eir_code", unique = true)
    private String eirCode;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "rental_cost")
    private double rentalCost;

    @Column(name = "created_date",updatable = false)
    private LocalDate createdDate;

    @Column(name = "updated_date",updatable = true)
    private LocalDate updatedDate;

    @OneToMany(mappedBy = "property", cascade = {CascadeType.ALL})
    private List<Tenant> tenants;
}
