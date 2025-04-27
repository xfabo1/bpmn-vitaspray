package com.example.demo;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String surname;
    private String email;
    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_spray_types", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "spray_type")
    @Enumerated(EnumType.STRING)
    private List<SprayType> products;
}