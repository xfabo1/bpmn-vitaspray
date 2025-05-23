package com.example.demo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;

import org.apache.camel.ProducerTemplate;

@Component
public class CamundaJobWorker {

	private ObjectMapper objectMapper;
	private OrderRepository orderRepository;
	private RestTemplate restTemplate;
	private BillRepository billRepository;
	@Autowired
	private ProducerTemplate producerTemplate;

	@Autowired
	public CamundaJobWorker(ObjectMapper objectMapper, OrderRepository orderRepository, RestTemplate restTemplate, BillRepository billRepository) {
		this.objectMapper = objectMapper;
		this.orderRepository = orderRepository;
		this.billRepository = billRepository;
		this.restTemplate = restTemplate;
	}

	@JobWorker(type = "store_order")
	public Map<String, Object> storeOrder(final ActivatedJob job) {
		var orderItems = objectMapper.convertValue(job.getVariable("orderProduct"), new TypeReference<List<String>>() {});
		var sprayTypes = orderItems.stream().map(SprayType::findByDisplayName).toList();
		var personalDetails = objectMapper.convertValue(job.getVariable("personal"), PersonalDetails.class);

		var order = new Order();
		order.setProducts(sprayTypes);
		order.setAddress(personalDetails.getAddress());
		order.setName(personalDetails.getName());
		order.setEmail(personalDetails.getEmail());
		order.setSurname(personalDetails.getSurname());

		orderRepository.save(order);

		return Map.of("orderId", order.getId().toString());
	}

	@JobWorker(type = "process_payment")
	public Map<String, Object> process_payment(final ActivatedJob job) throws InterruptedException {
		// Change this value if you want to simulate process payment throwing exception
		boolean throwsException = false;
		var paymentInfo = objectMapper.convertValue(job.getVariable("payment"), PaymentDetails.class);
		System.out.println("Job received: " + paymentInfo);
		// Change the url from fail=false to fail=true if you want to simulate payment process return failure
		var result = restTemplate.getForObject("http://localhost:8080/api/vitaspray/process_payment?fail=false", String.class );

		if (throwsException) {
			throw new ZeebeBpmnError("500", "Error", Map.of());
		}
		Thread.sleep(20000);
		if (Objects.equals(result, "Payment successful")) {
			return Map.of("paymentResult", "success");
		}
		return Map.of("paymentResult", "failure");
	}

	@JobWorker(type = "parse_order")
	public Map<String, Object> parseOrder(final ActivatedJob job) throws JsonProcessingException {
		var orderId = job.getVariable("orderId").toString();
		var order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow();
		return Map.of("orderFetched", order);
	}

	@JobWorker(type = "store_bill")
	public Map<String, Object> storeBill(final ActivatedJob job) {
		var price = Double.valueOf((String) job.getVariable("orderPrice"));
		var order = objectMapper.convertValue(job.getVariable("orderFetched"), Order.class);
		var totalPrice = price * 0.21 + price;
		var bill = new Bill();
		bill.setVat(0.21);
		bill.setTotalAmount(totalPrice);
		bill.setOrder(order);
		billRepository.save(bill);
		return Map.of("bill", bill.getId());
	}

	@JobWorker(type = "calculate_price")
	public Map<String, Object> calculatePrice(final ActivatedJob job) {
		var order = objectMapper.convertValue(job.getVariable("orderFetched"), Order.class);
		var price = order.getProducts().stream().map(SprayType::getPrice).reduce((double) 0, Double::sum);
		return Map.of("orderPrice", price.toString());
	}

	@JobWorker(type = "register_complaint")
	public Map<String, Object> registerComplaint(final ActivatedJob job) {
		var variables = job.getVariables();

		System.out.println("Job received: " + variables);

		Map<String, Object> result = Map.of("userFetched", true);
		return result;
	}

	@JobWorker(type = "process_failed_payment")
	public Map<String, Object> processFailedPayment(final ActivatedJob job) {
		System.out.println("HERE WOULD BE SOME COORDINATION WITH FE TO SHOW WHETHER TO RETRY OR NOT");
		return null;
	}

	@JobWorker(type = "generate_personalized_recommendation")
	public Map<String, Object> generatePersonalizedRecommendations(final ActivatedJob job) {
		SprayType[] allTypes = SprayType.values();

		int randomIndex = (int) (Math.random() * allTypes.length);

		SprayType recommendation = allTypes[randomIndex];
		return Map.of("orderProduct", List.of(recommendation.getName()));
	}


	@JobWorker(type = "print_form")
	public Map<String, Object> printForm(final ActivatedJob job) throws JsonProcessingException {
		var packaging = objectMapper.writeValueAsString(job.getVariable("packaging"));

		System.out.println(packaging);
		return null;
	}

	@JobWorker(type = "send_email")
	public Map<String, Object> sendEmail(final ActivatedJob job) {
		String to = (String) job.getVariablesAsMap().get("to");
		String subject = (String) job.getVariablesAsMap().get("subject");
		String body = (String) job.getVariablesAsMap().get("body");

		if (to == null || subject == null || body == null) {
			throw new RuntimeException("Missing one of the required variables: to, subject, body");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.add("to", to);
		headers.add("subject", subject);
		headers.add("from", "pv207muni@gmail.com"); // hardcoded sender

		HttpEntity<String> request = new HttpEntity<>(body, headers);

		restTemplate.postForObject("http://localhost:8080/camel/send-email", request, String.class);

		return Map.of("emailSent", true);
	}

}
