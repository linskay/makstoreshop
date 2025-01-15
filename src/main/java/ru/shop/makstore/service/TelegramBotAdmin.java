package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Component
public class TelegramBotAdmin extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String adminBotUsername;
    @Value("${telegram.bot.token}")
    private String adminBotToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendExcelFile(String chatId, File file) {
        try {
            // URL для отправки файла
            String url = "https://api.telegram.org/bot" + adminBotToken + "/sendDocument";

            // Создаем экземпляр RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Добавляем ID чата и документ как FileSystemResource
            body.add("chat_id", chatId);
            body.add("document", new FileSystemResource(file));

            // Отправляем запрос
            restTemplate.postForObject(url, body, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotToken() {
        return adminBotToken;
    }

    @Override
    public String getBotUsername() {
        return adminBotUsername;
    }
}
