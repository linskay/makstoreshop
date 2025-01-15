package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Image;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ImageRepository;

@Service
@Transactional
public class ImageService {

    @Value("${telegram.bot.token}")
    private String botToken; // Токен бота для формирования URL

    private final ProductService productService;
    private final ImageRepository imageRepository;
    private final TelegramLongPollingBot telegramBot; // Используем конкретный тип

    public ImageService(ProductService productService, ImageRepository imageRepository,  @Lazy TelegramLongPollingBot telegramBot) {
        this.productService = productService;
        this.imageRepository = imageRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Добавляет изображение из Telegram по fileId.
     *
     * @param productId ID продукта, к которому привязывается изображение.
     * @param fileId    fileId изображения, полученное от Telegram.
     */
    public void addImageFromTelegram(int productId, String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            throw new IllegalArgumentException("File ID cannot be null or empty");
        }

        // Получаем filePath из Telegram API
        String filePath = getFilePathFromTelegram(fileId);

        if (filePath == null) {
            throw new RuntimeException("Failed to get file path from Telegram API");
        }

        // Получаем продукт по ID
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Создаем новое изображение или обновляем существующее
        Image image = imageRepository.findByProductId(productId).orElse(new Image());
        image.setFilePath(filePath);
     //   image.setFileId(fileId);
        image.setMediaType("image/jpeg"); // Указываем тип медиа
        image.setProduct(product); // Устанавливаем связь с продуктом

        // Сохраняем изображение в репозитории
        imageRepository.save(image);

        // Обновляем поле imageUrl в продукте
        product.setImageUrl("https://api.telegram.org/file/bot" + botToken + "/" + filePath);
        product.setImage(image); // Устанавливаем связь с изображением

        // Сохраняем обновленный продукт
        productService.updateProduct(product);
    }

    /**
     * Получает URL изображения для продукта.
     *
     * @param productId ID продукта.
     * @return Полный URL изображения.
     */
    public String getImageUrl(int productId) {
        Image image = imageRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (image.getFilePath() == null) {
            return null; // Или вернуть URL изображения по умолчанию
        }

        // Формируем полный URL
        return "https://api.telegram.org/file/bot" + botToken + "/" + image.getFilePath();
    }

    /**
     * Получает filePath из Telegram API по fileId.
     *
     * @param fileId fileId изображения.
     * @return filePath изображения.
     */
    private String getFilePathFromTelegram(String fileId) {
        try {
            GetFile getFile = new GetFile();
            getFile.setFileId(fileId);
            org.telegram.telegrambots.meta.api.objects.File file = telegramBot.execute(getFile); // Используем execute из TelegramBot
            return file.getFilePath();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Находит изображение по ID продукта.
     *
     * @param productId ID продукта.
     * @return Объект Image.
     */
    public Image findImage(int productId) {
        return imageRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}