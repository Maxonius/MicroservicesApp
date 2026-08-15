package org.example.democircuitbreaker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @GetMapping("/send")
    public String sendEmail() throws InterruptedException {
        // Имитация медленной работы
        long delay = Math.random() > 0.5 ? 5000 : 10000;
        Thread.sleep(delay);
        return "Email sent after " + delay + "ms!";
    }
}