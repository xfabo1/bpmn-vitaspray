package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.camunda.zeebe.client.ZeebeClient;

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
				.variables("{}")
				.send()
				.join();

		return ResponseEntity.ok().build();
	}

	@PostMapping("/process_payment")
	public ResponseEntity<Void> processPayment(@RequestParam("fail") boolean shouldFail) {
		// Implementation
		return ResponseEntity.ok().build();
	}
}
