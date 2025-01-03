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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        Product product = optionalProduct.get();

        Path filePath = Path.of(imageDir, productId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        // Уменьшаем изображение
        byte[] resizedImage = resizeImage(file.getBytes(), 800, 600); // Устанавливаем размеры 800x600

        Files.write(filePath, resizedImage, CREATE_NEW);

        Image image = imageRepository.findByProductId(productId).orElse(new Image());
        image.setProduct(product);
        image.setFilePath(filePath.toString());
        image.setFileSize(resizedImage.length);
        image.setMediaType(file.getContentType());
        image.setSavesDataInDb(resizedImage);
        imageRepository.save(image);
    }

    public Image findImage(Integer productId) {
        return imageRepository.findByProductId(productId).orElse(new Image());
    }

    private byte[] resizeImage(byte[] originalImage, int width, int height) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(originalImage));
        if (bufferedImage == null) {
            throw new IOException("Could not read image");
        }

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, width, height, null);
        graphics.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}