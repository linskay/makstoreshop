package ru.shop.makstore.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.shop.makstore.controller.AdminController;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.enumtypes.ProductType;

import java.util.*;

import java.io.File;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.chat.id}")
    private String chatId;

    @Value("${telegram.bot.password}")
    private String botPassword;

    private final AdminController adminController;
    private TelegramBotsApi botsApi;
    private Map<Long, Boolean> authorizedUsers = new HashMap<>();
    private Map<Long, UserState> userStates = new HashMap<>(); // Состояния пользователей
    private Map<Long, Product> tempProducts = new HashMap<>(); // Временные данные о продукте

    public TelegramBot(AdminController adminController) {
        this.adminController = adminController;
    }

    @PostConstruct
    public void registerBot() {
        try {
            this.botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);

            // Устанавливаем команды в боковое меню
            setCommands();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setCommands() {
        try {
            List<BotCommand> commands = new ArrayList<>();
            commands.add(new BotCommand("/createproduct", "Создать новый продукт"));
            commands.add(new BotCommand("/deleteproduct", "Удалить продукт по ID"));
            commands.add(new BotCommand("/updatebot", "Обновить бота"));

            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendContactRequest(String messageText) {
        try {
            System.out.println("Попытка отправить сообщение: " + messageText);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(messageText);
            execute(sendMessage);
            System.out.println("Сообщение успешно отправлено.");
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            // Если пользователь не авторизован и это его первое сообщение
            if (!authorizedUsers.containsKey(chatId)) {
                sendMessage(chatId, "Добро пожаловать! Для управления магазином введите пароль.");
                authorizedUsers.put(chatId, false); // Помечаем, что пользователь начал взаимодействие
                return;
            }

            // Если пользователь отправляет команду /start
            if (messageText.equals("/start")) {
                sendWelcomeMessage(chatId);
                return;
            }

            // Если пользователь не авторизован
            if (!authorizedUsers.getOrDefault(chatId, false)) {
                if (messageText.equals(botPassword)) {
                    authorizedUsers.put(chatId, true);
                    sendWelcomeMessage(chatId);
                } else {
                    sendMessage(chatId, "Неверный пароль. Попробуйте снова.");
                }
            } else {
                // Обработка команд для авторизованных пользователей
                if (messageText.equals("/createproduct") || messageText.equalsIgnoreCase("Создать продукт")) {
                    startProductCreation(chatId);
                } else if (messageText.equals("/deleteproduct") || messageText.equalsIgnoreCase("Удалить продукт")) {
                    sendMessage(chatId, "Введите ID продукта для удаления:");
                    userStates.put(chatId, UserState.AWAITING_DELETE_ID); // Устанавливаем состояние
                } else if (messageText.equals("/updatebot") || messageText.equalsIgnoreCase("Обновить бота")) {
                    sendMessage(chatId, "Функция обновления бота в разработке.");
                } else if (userStates.containsKey(chatId)) {
                    handleProductCreation(chatId, messageText);
                } else {
                    sendMessage(chatId, "Неизвестная команда. Используйте /createproduct, /deleteproduct или /updatebot.");
                }
            }
        } else if (update.hasCallbackQuery()) {
            // Обработка нажатия на кнопку
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            if (callbackData.startsWith("type_")) {
                String russianName = callbackData.substring(5); // Убираем префикс "type_"
                ProductType type = ProductType.fromRussianName(russianName);

                Product product = tempProducts.get(chatId);
                product.setType(type);

                // Сохраняем продукт в базу данных
                adminController.createProduct(Map.of(
                        "name", product.getName(),
                        "description", product.getDescription(),
                        "priceRetail", product.getPriceRetail(),
                        "priceWhole", product.getPriceWhole(),
                        "type", product.getType().name()
                ));

                sendMessage(chatId, "Продукт успешно создан: " + product.getName());

                // Очищаем состояние
                userStates.remove(chatId);
                tempProducts.remove(chatId);
            }
        }
    }

    private void sendWelcomeMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");

        // Создаем клавиатуру с кнопками
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Кнопка "Создать продукт"
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Создать продукт"));
        keyboard.add(row1);

        // Кнопка "Удалить продукт"
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Удалить продукт"));
        keyboard.add(row2);

        // Кнопка "Обновить бота"
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Обновить бота"));
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true); // Автоматически изменять размер клавиатуры
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void startProductCreation(long chatId) {
        userStates.put(chatId, UserState.AWAITING_NAME);
        tempProducts.put(chatId, new Product());
        sendMessage(chatId, "Введите название продукта:");
    }

    private void handleProductCreation(long chatId, String messageText) {
        UserState currentState = userStates.get(chatId);
        Product product = tempProducts.get(chatId);

        switch (currentState) {
            case AWAITING_NAME:
                product.setName(messageText);
                userStates.put(chatId, UserState.AWAITING_DESCRIPTION);
                sendMessage(chatId, "Введите описание продукта:");
                break;

            case AWAITING_DESCRIPTION:
                product.setDescription(messageText);
                userStates.put(chatId, UserState.AWAITING_PRICE_RETAIL);
                sendMessage(chatId, "Введите розничную цену:");
                break;

            case AWAITING_PRICE_RETAIL:
                try {
                    int priceRetail = Integer.parseInt(messageText);
                    product.setPriceRetail(priceRetail);
                    userStates.put(chatId, UserState.AWAITING_PRICE_WHOLE);
                    sendMessage(chatId, "Введите оптовую цену:");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Некорректный формат цены. Введите число:");
                }
                break;

            case AWAITING_PRICE_WHOLE:
                try {
                    int priceWhole = Integer.parseInt(messageText);
                    product.setPriceWhole(priceWhole);
                    userStates.put(chatId, UserState.AWAITING_TYPE);
                    sendTypeSelection(chatId);
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Некорректный формат цены. Введите число:");
                }
                break;
            case AWAITING_DELETE_ID:
                try {
                    int id = Integer.parseInt(messageText); // Парсим ID
                    adminController.deleteProduct(id); // Вызываем метод удаления
                    sendMessage(chatId, "Продукт с ID " + id + " успешно удален.");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Некорректный формат ID. Введите число:");
                } catch (ProductNotFoundException e) {
                    sendMessage(chatId, "Продукт с ID не найден.");
                }
                userStates.remove(chatId); // Очищаем состояние
                break;
        }
    }

    private void sendTypeSelection(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите тип продукта:");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (ProductType type : ProductType.values()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(type.getRussianName());
            button.setCallbackData("type_" + type.getRussianName());
            rows.add(Collections.singletonList(button));
        }

        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String text) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(text);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}