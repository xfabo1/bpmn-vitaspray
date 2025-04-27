package com.example.demo;

import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "order_spray_types", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "spray_type")
    @Enumerated(EnumType.STRING)
    private List<SprayType> products;
}