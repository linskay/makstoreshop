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
    private long lastRequestTime = 0; // Время последнего запроса
    private static final long REQUEST_COOLDOWN = 10000; // 10 секунд задержки

    public ContactUsController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @GetMapping
    public String showContactUsPage() {
        return "contact"; // Имя шаблона Thymeleaf (contact.html)
    }

    @PostMapping("/submit")
    public String submitContactRequest(ContactRequest contactRequest) {
        long currentTime = System.currentTimeMillis();

        // Проверяем, прошло ли достаточно времени с последнего запроса
        if (currentTime - lastRequestTime < REQUEST_COOLDOWN) {
            throw new TooManyRequestsException("Пожалуйста, подождите перед отправкой следующего запроса.");
        }

        // Обновляем время последнего запроса
        lastRequestTime = currentTime;

        // Формируем сообщение для отправки в Telegram
        String messageText = String.format(
                "Поступила новая форма обратной связи:\nИмя: %s\nТелефон: %s\nTelegram: %s\nСообщение: %s",
                contactRequest.getName(),
                contactRequest.getPhone(),
                contactRequest.getTelegram(),
                contactRequest.getMessage()
        );

        // Отправляем сообщение через бота
        telegramBot.sendContactRequest(messageText);

        // Перенаправление на страницу с сообщением об успешной отправке
        return "redirect:/contact-us?success";
    }
}