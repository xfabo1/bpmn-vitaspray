package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetails {
	private String name;
	private String surname;
	private String email;
	private String address;
	private String city;
	private String postal;
}
