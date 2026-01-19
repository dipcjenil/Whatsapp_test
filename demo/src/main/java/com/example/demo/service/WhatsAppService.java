package com.example.demo.service;

import com.example.demo.repository.employeerepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    @Value("${whatsapp.token}")
    private String token;

    @Value("${whatsapp.phone.id}")
    private String phoneId;

    private final employeerepository employeeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public WhatsAppService(employeerepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void sendToAllEmployees() {
        employeeRepository.findAll().forEach(emp -> {
            String to = String.valueOf(emp.getNumber()); // long → String
            sendTemplateMessage(to, emp.getName());
        });
    }

    private void sendTemplateMessage(String to, String name) {

        String url = "https://graph.facebook.com/v19.0/" + phoneId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = """
                {
                 "messaging_product": "whatsapp",
                 "recipient_type": "individual",
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
                         {
                           "type": "text",
                           "parameter_name": "name",
                           "text": "%s"
                         },
                         {
                           "type": "text",
                           "parameter_name": "sender",
                           "text": "CareHQ"
                         },
                         {
                           "type": "text",
                           "parameter_name": "title",
                           "text": "Appointment"
                         }
                       ]
                     }
                   ]
                 }
                 }
        """.formatted(to, name);

        try {
            HttpEntity<String> request = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(url, request, String.class);

            System.out.println("Message sent to: " + to);

        } catch (HttpClientErrorException e) {
            System.err.println("Client error for: " + to);
            System.err.println(e.getResponseBodyAsString());

        } catch (Exception e) {
            System.err.println("Error sending to: " + to);
            e.printStackTrace();
        }
    }
}
