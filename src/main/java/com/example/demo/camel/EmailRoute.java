package com.example.demo.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailRoute extends RouteBuilder {

    @Override
    public void configure() {

        // Register Camel REST to use Servlet
        restConfiguration().component("servlet");

        from("rest:post:/send-email")
                .log("📨 Received request to send email")
                .toD("smtp://smtp.gmail.com:587?username=pv207muni@gmail.com&password=xqut pvgd qtcl nfhe&mail.smtp.starttls.enable=true&to=${header.to}&subject=${header.subject}&from=${header.from}")
                .setBody(constant("")) // optional: ensure no response body
                .setHeader("CamelHttpResponseCode", constant(204))
                .log("✅ Email sent");

    }
}
