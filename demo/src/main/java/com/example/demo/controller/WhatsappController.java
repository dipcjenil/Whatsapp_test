package com.example.demo.controller;

import com.example.demo.service.WhatsAppService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappController {

    private final WhatsAppService service;

    public WhatsappController(WhatsAppService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public String sendWhatsAppMessages() {
        System.out.println("WhatsApp API triggered");
        service.sendToMultipleNumbers();
        return "WhatsApp messages sent successfully";
    }
}
