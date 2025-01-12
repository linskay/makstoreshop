package ru.shop.makstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shop.makstore.model.ContactRequest;

@Controller
@RequestMapping("/contact-us")
public class ContactUsController {

    // Обработка GET-запроса для отображения страницы
    @GetMapping
    public String showContactUsPage() {
        return "contact"; // Имя шаблона Thymeleaf (contact.html)
    }

    // Обработка POST-запроса для отправки формы
    @PostMapping("/submit")
    public String submitContactRequest(ContactRequest contactRequest) {
        // Логика обработки заявки
        System.out.println("Новая заявка:");
        System.out.println("Имя: " + contactRequest.getName());
        System.out.println("Телефон: " + contactRequest.getPhone());
        System.out.println("Telegram: " + contactRequest.getTelegram());
        System.out.println("Сообщение: " + contactRequest.getMessage());

        // Перенаправление на страницу с сообщением об успешной отправке
        return "redirect:/contact-us?success";
    }
}