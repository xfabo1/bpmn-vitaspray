package com.example.demo.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("rest:post:/send-email")
                .log("📨 Received request to send email")
                .setHeader("subject", constant("Test Email from Camel"))
                .setHeader("to", constant("ilkinsadikh@icloud.com"))
                .setHeader("from", constant("pv207muni@gmail.com"))
                .setBody(constant("This is a test email sent by Camel from Spring Boot"))
                .to("smtp://smtp.gmail.com:587?username=pv207muni@gmail.com&password=xqut pvgd qtcl nfhe&mail.smtp.starttls.enable=true")
                .log("✅ Email sent");
    }
}
