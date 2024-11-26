package ru.shop.makstore.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shop.makstore.model.ElectronicCigarettes;
import ru.shop.makstore.model.Image;
import ru.shop.makstore.repositories.ElectronicCigarettesRepository;
import ru.shop.makstore.service.ElectronicCigarettesService;
import ru.shop.makstore.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("el/cigarettes")
public class ElectronicCigarettesController {
    private final ElectronicCigarettesService electronicCigarettesService;
    private final ElectronicCigarettesRepository electronicCigarettesRepository;
    private final ImageService imageService;

    public ElectronicCigarettesController(ElectronicCigarettesService electronicCigarettesService, ElectronicCigarettesRepository electronicCigarettesRepository,
                                          ImageService imageService) {
        this.electronicCigarettesService = electronicCigarettesService;
        this.electronicCigarettesRepository = electronicCigarettesRepository;
        this.imageService = imageService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ElectronicCigarettes> getElectronicCigarettesInfo(@PathVariable Long id) {
        ElectronicCigarettes electronicCigarettes =
                electronicCigarettesService.findElectronicCigarette(id);
        if (electronicCigarettes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(electronicCigarettes);
    }

    @GetMapping
    public ResponseEntity findElectronicCigarettes(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String description,
                                                   @RequestParam(required = false) Integer price) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(electronicCigarettesService.findByName(name));
        }
        if (description != null && !description.isBlank()) {
            return ResponseEntity.ok(electronicCigarettesService.findByDescription(description));
        }
        if (price != null) {
            return ResponseEntity.ok(electronicCigarettesService.findByPrice(price));
        }
        return ResponseEntity.ok(electronicCigarettesService.getElectronicCigarettes());
    }

    @PostMapping
    public ElectronicCigarettes addElectronicCigarettes
            (ElectronicCigarettes electronicCigarettes) {
        return electronicCigarettesService
                .createElectronicCigarettes(electronicCigarettes);
    }

    @PutMapping
    public ResponseEntity<ElectronicCigarettes> editElectronicCigarettes
            (ElectronicCigarettes electronicCigarettes) {
        ElectronicCigarettes electronicCigarettes1 =
                electronicCigarettesService.editElectronicCigarettes(electronicCigarettes);
        if (electronicCigarettes1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(electronicCigarettes1);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteElectronicCigarettes(@PathVariable Long id) {
        electronicCigarettesService.deleteElectronicCigarettes(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addCover(@PathVariable Long id,
                                           @RequestParam MultipartFile image) throws IOException {
        if (image.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("Cover file size is too large.");
        }
        imageService.uploadImage(id, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image/save")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        Image image = imageService.findImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getSavesDataInDb().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getSavesDataInDb());
    }

    @GetMapping(value = "/{id}/image")
    public void downloadCover(@PathVariable Long id, HttpServletResponse response) throws IOException{
        Image image = imageService.findImage(id);
        Path path = Path.of(image.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setContentType(image.getMediaType());
            response.setContentLength((int) image.getFileSize());
            is.transferTo(os);
        }
    }
}
