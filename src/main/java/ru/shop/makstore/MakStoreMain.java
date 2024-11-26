package ru.shop.makstore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.shop.makstore.model.ElectronicCigarettes;

@SpringBootApplication
@OpenAPIDefinition
public class MakStoreMain {
    public static void main(String[] args) {
        SpringApplication.run(MakStoreMain.class, args);

    }

}
