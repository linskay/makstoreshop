package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Image;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class ImageService {
    @Value("${product.image.dir.path}")
    private String imageDir;

    private final ProductService productService;
    private final ImageRepository imageRepository;

    public ImageService(ProductService productService, ImageRepository imageRepository) {
        this.productService = productService;
        this.imageRepository = imageRepository;
    }

    public void additionImage(int productId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Получаем продукт по ID
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId)); // Если продукт не найден, выбрасываем исключение

        // Создаем путь для сохранения изображения
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename(), "File name cannot be null");
        Path filePath = Path.of(imageDir, productId + "." + getExtension(originalFilename));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        // Уменьшаем изображение
        byte[] resizedImage;
        try {
            resizedImage = resizeImage(file.getBytes(), 800, 600); // Устанавливаем размеры 800x600
        } catch (IOException e) {
            throw new RuntimeException("Failed to resize image", e);
        }

        // Сохраняем изображение на диск
        Files.write(filePath, resizedImage, CREATE_NEW);

        // Ищем существующее изображение или создаем новое
        Image image = imageRepository.findByProductId(productId).orElse(new Image());
        image.setProduct(product); // Передаем объект Product, а не Optional<Product>
        image.setFilePath(filePath.toString());
        image.setFileSize(resizedImage.length);
        image.setMediaType(file.getContentType());
        image.setSavesDataInDb(resizedImage);

        // Сохраняем изображение в репозитории
        imageRepository.save(image);
    }

    public Image findImage(int productId) throws IOException {
        Optional<Image> optionalImage = imageRepository.findByProductId(productId);

        if (optionalImage.isPresent()) {
            return optionalImage.get();
        } else {
            try {
                return createDefaultImage();
            } catch (IOException e) {
                throw new IOException("Default image not found", e);
            }
        }
    }

    private Image createDefaultImage() throws IOException {
        Image defaultImage = new Image();
        Path defaultImagePath = Path.of(imageDir, "default.png");

        if (!Files.exists(defaultImagePath)) {
            throw new IOException("Default image not found at: " + defaultImagePath);
        }

        // Генерируем байты из изображения по умолчанию
        defaultImage.setFilePath(defaultImagePath.toString());
        defaultImage.setSavesDataInDb(generateImage(defaultImagePath));

        // Устанавливаем параметры по умолчанию
        defaultImage.setFileSize(Files.size(defaultImagePath));
        defaultImage.setMediaType("image/png");

        return defaultImage;
    }

    private byte[] generateImage(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                throw new IOException("Could not read image from file: " + filePath);
            }

            // Создаем уменьшенную версию изображения
            int newWidth = 150;
            int newHeight = (int) ((newWidth / (double) image.getWidth()) * image.getHeight());

            BufferedImage preview = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, newWidth, newHeight, null);
            graphics.dispose();

            // Записываем изображение в ByteArrayOutputStream
            ImageIO.write(preview, "png", baos);
            return baos.toByteArray();
        }
    }

    private byte[] resizeImage(byte[] originalImage, int width, int height) throws IOException {
        if (originalImage == null) {
            throw new IllegalArgumentException("Original image bytes cannot be null");
        }

        // Чтение оригинального изображения
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(originalImage)) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            if (bufferedImage == null) {
                throw new IOException("Could not read image");
            }

            // Создание нового изображения с заданными размерами
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = resizedImage.createGraphics();

            // Масштабирование изображения
            graphics.drawImage(bufferedImage, 0, 0, width, height, null);
            graphics.dispose();

            // Запись изображения в массив байтов
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(resizedImage, "jpg", outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private String getExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            throw new IllegalArgumentException("Filename must contain an extension");
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}