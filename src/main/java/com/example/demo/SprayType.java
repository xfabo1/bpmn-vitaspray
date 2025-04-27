package com.example.demo;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum SprayType {

	KIDS_MULTIVITAMIN("kids", 50.0),
	WORKOUT_RECOVERY("workout", 90.0),
	IMMUNITY_DEFENDER("immunity", 80.0),
	HAIR_SKIN_AND_NAILS("hair", 70.0),
	PRENATAL_MULTIVITAMIN_SPRAY("prenatal", 40.0);

	private final String name;
	private final Double price;

	SprayType(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	public static SprayType findByDisplayName(String name) {
		for (SprayType value : SprayType.values()) {
			if (value.name.equals(name)) {
				return value;
			}
		}
		throw new IllegalArgumentException("No SprayType with name " + name);
	}
}
