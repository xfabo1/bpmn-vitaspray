package com.example.demo;

import lombok.Getter;

@Getter
public enum SprayType {

	KIDS_MULTIVITAMIN("Kids’ Multivitamin", 50.0),
	WORKOUT_RECOVERY("Workout Recovery", 90.0),
	IMMUNITY_DEFENDER("Immunity Defender", 80.0),
	HAIR_SKIN_AND_NAILS("Hair, Skin & Nails", 70.0),
	PRENATAL_MULTIVITAMIN_SPRAY("Prenatal Multivitamin Spray", 40.0);

	private final String displayName;
	private final Double price;

	SprayType(String displayName, Double price) {
		this.displayName = displayName;
		this.price = price;
	}
}
