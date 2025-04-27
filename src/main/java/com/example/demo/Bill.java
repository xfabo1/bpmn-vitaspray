package com.example.demo;

import static jakarta.persistence.GenerationType.*;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bills")
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private Double totalAmount;
	private Double vat;
	@OneToOne
	private Order order;
}
