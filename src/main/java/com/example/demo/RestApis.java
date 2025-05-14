package com.example.demo;

import java.util.Map;
import java.time.Duration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;

@RestController
@RequestMapping("/api/vitaspray")
public class RestApis {

	private final ZeebeClient zeebeClient;

	@Autowired
	public RestApis(ZeebeClient zeebeClient) {
		this.zeebeClient = zeebeClient;
	}

	@PostMapping("/cancel_payment")
	public ResponseEntity<Void> publishMessage() {
		zeebeClient.newPublishMessageCommand()
				.messageName("cancel_payment")
				.correlationKey("cancel_payment")
				.timeToLive(Duration.ofSeconds(10))
				.variables("{}")
				.send()
				.join();

		return ResponseEntity.ok().build();
	}

	@PostMapping("/retry_payment")
	public ResponseEntity<Void> retryPayment() {
		zeebeClient.newPublishMessageCommand()
				.messageName("retry_payment")
				.correlationKey("retry_payment")
				.timeToLive(Duration.ofSeconds(10))
				.variables("{}")
				.send()
				.join();

		return ResponseEntity.ok().build();
	}

	@GetMapping("/process_payment")
	public ResponseEntity<String> processPayment(@RequestParam("fail") boolean shouldFail) {
		if (shouldFail) {
			return ResponseEntity.ok("Payment failed");
		}
		return ResponseEntity.ok("Payment successful");
	}
}
