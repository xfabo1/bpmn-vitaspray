package com.example.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class CamundaJobWorker {

	private ObjectMapper objectMapper;
	private OrderRepository orderRepository;
	private RestTemplate restTemplate;
	private BillRepository billRepository;

	@Autowired
	public CamundaJobWorker(ObjectMapper objectMapper, OrderRepository orderRepository, RestTemplate restTemplate, BillRepository billRepository) {
		this.objectMapper = objectMapper;
		this.orderRepository = orderRepository;
		this.billRepository = billRepository;
		this.restTemplate = restTemplate;
	}

	@JobWorker(type = "store_order")
	public Map<String, Object> storeOrder(final ActivatedJob job) {

		Map<String, Object> result = Map.of("userFetched", true);
		return result;
	}

	@JobWorker(type = "process_payment")
	public Map<String, Object> process_payment(final ActivatedJob job) {
		restTemplate.postForObject("http://localhost:8080/api/vitaspray/process_payment?fail=false", null, Void.class );
		return null;
	}

	@JobWorker(type = "parse_order")
	public Map<String, Object> parseOrder(final ActivatedJob job) throws JsonProcessingException {
		var orderId = objectMapper.writeValueAsString(job.getVariable("orderId"));
		var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
		return Map.of("orderFetched", order);
	}

	@JobWorker(type = "store_bill")
	public Map<String, Object> storeBill(final ActivatedJob job) {




		Map<String, Object> result = Map.of("userFetched", true);
		return result;
	}

	@JobWorker(type = "calculate_price")
	public Map<String, Object> calculatPrice(final ActivatedJob job) {

		var variables = job.getVariables();

		System.out.println("Job received: " + variables);

		Map<String, Object> result = Map.of("userFetched", true);
		return result;
	}

	@JobWorker(type = "register_complaint")
	public Map<String, Object> registerComplaint(final ActivatedJob job) {
		var variables = job.getVariables();

		System.out.println("Job received: " + variables);

		Map<String, Object> result = Map.of("userFetched", true);
		return result;
	}

	@JobWorker(type = "generate_personalized_recommendation")
	public Map<String, Object> generateRecommendation(final ActivatedJob job) {
  
		var variables = job.getVariables();
		System.out.println("Job received: " + variables);

		String recommendation = "Your personalized spray xyz";

		System.out.println("Recommendation generated for user: " + recommendation);

		return Map.of("recommendedOrderProduct", recommendation);
	}

}
