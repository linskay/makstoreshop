package ru.shop.makstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shop.makstore.model.ContactRequest;
import ru.shop.makstore.service.TelegramBot;
import ru.shop.makstore.exception.TooManyRequestsException;

@Controller
@RequestMapping("/contact-us")
public class ContactUsController {

    private final TelegramBot telegramBot;
    private long lastRequestTime = 0;
    private static final long REQUEST_COOLDOWN = 10000;

    public ContactUsController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @GetMapping
    public String showContactUsPage() {
        return "contact";
    }

    @PostMapping("/submit")
    public String submitContactRequest(ContactRequest contactRequest) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastRequestTime < REQUEST_COOLDOWN) {
            throw new TooManyRequestsException("Пожалуйста, подождите перед отправкой следующего запроса.");
        }

        lastRequestTime = currentTime;

        String messageText = String.format(
                "Поступила новая форма обратной связи:\nИмя: %s\nТелефон: %s\nTelegram: %s\nСообщение: %s",
                contactRequest.getName(),
                contactRequest.getPhone(),
                contactRequest.getTelegram(),
                contactRequest.getMessage()
        );

        telegramBot.sendContactRequest(messageText);

        return "redirect:/contact-us?success";
    }
}