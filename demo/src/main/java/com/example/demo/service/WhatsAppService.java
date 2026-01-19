package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
public class WhatsAppService {

    @Value("${whatsapp.token}")
    private String token;

    @Value("${whatsapp.phone.id}")
    private String phoneId;

    private final RestTemplate restTemplate = new RestTemplate();
    public void sendToMultipleNumbers() {

        String[] numbers = {
                "919374884268",
                "917383011438"
        };

        for (String to : numbers) {
            sendTemplateMessage(to);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }


    private void sendTemplateMessage(String to) {

        String url = "https://graph.facebook.com/v19.0/" + phoneId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = """
    {
      "messaging_product": "whatsapp",
      "to": "%s",
      "type": "template",
      "template": {
        "name": "set_appointment",
        "language": {
          "code": "en_US"
        },
        "components": [
          {
            "type": "body",
            "parameters": [
              { "type": "text", "text": "Jenil" },
              { "type": "text", "text": "Health Warden Team" },
              { "type": "text", "text": "OHC Department" }
            ]
          }
        ]
      }
    }
    """.formatted(to);

        try {
            HttpEntity<String> request = new HttpEntity<>(payload, headers);
            var response = restTemplate.postForEntity(url, request, String.class);

            System.out.println("Message sent to: " + to);
            System.out.println("Status: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            // 4xx errors
            System.err.println("Client error for: " + to);
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Body: " + e.getResponseBodyAsString());

        } catch (HttpServerErrorException e) {
            // 5xx errors
            System.err.println("Server error for: " + to);
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Body: " + e.getMessage());

        } catch (ResourceAccessException e) {
            // Timeouts / connection issues
            System.err.println("Connection error for: " + to);
            System.err.println("Message: " + e.getMessage());

        } catch (RestClientException e) {
            // Fallback
            System.err.println("Unexpected error for: " + to);
            e.printStackTrace();
        }
    }


}
