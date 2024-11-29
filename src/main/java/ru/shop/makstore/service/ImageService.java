package ru.shop.makstore.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.shop.makstore.model.Image;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ImageRepository;
import ru.shop.makstore.repositories.ProductRepository;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ImageService {
    @Value("${product.image.dir.path}")
    private String imageDir;

    private final ProductService productService;
    private final ImageRepository imageRepository;

    public ImageService(ProductService productService,
                        ImageRepository imageRepository) {
        this.productService = productService;
        this.imageRepository = imageRepository;
    }

    public void uploadImage(int productId, MultipartFile file) throws IOException {
        Product product = productService
                .findProduct(productId);

        Path filePath = Path.of(imageDir, productId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());

        // Удаляем файл, если он существует
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Записываем новый файл
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(os)) {
            bis.transferTo(bos);
        }

        // Получаем или создаем фотку
        Image image = imageRepository.findByProductId(productId)
                .orElse(new Image());
        // задаем размеры
        image.setProduct(product);
        image.setFilePath(filePath.toString());
        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());
        image.setSavesDataInDb(generateImage(filePath, 200, 200));

        imageRepository.save(image);
    }

    public Image findImage(int productId) {
        return imageRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException("Image not found for id: " + productId));
    }

    public byte[] generateImage(Path filePath, int targetWidth, int targetHeight) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                throw new IOException("Failed to read image from file: " + filePath);
            }

            double scale = Math.min((double) targetWidth / image.getWidth(), (double) targetHeight / image.getHeight());

            int newWidth = (int) (image.getWidth() * scale);
            int newHeight = (int) (image.getHeight() * scale);

            BufferedImage preview = new BufferedImage(newWidth, newHeight, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, newWidth, newHeight, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        if (lastIndex == -1 || lastIndex == filename.length() - 1) {
            throw new IllegalArgumentException("Filename does not have a valid extension: " + filename);
        }
        return filename.substring(lastIndex + 1).toLowerCase();
    }
}

