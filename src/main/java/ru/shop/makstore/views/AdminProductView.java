package ru.shop.makstore.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;

import java.util.Arrays;
import java.util.function.Supplier;

@SpringComponent
@UIScope
@Route("admin/products")
public class AdminProductView extends VerticalLayout {

    private final Logger logger = LoggerFactory.getLogger(AdminProductView.class);
    private Dialog dialog;
    private final WebClient webClient;
    private final Grid<Product> grid;
    private final Binder<Product> binder;
    private final TextField nameField;
    private final TextField descriptionField;
    private final IntegerField priceRetailField;
    private final IntegerField priceWholeField;
    private final ComboBox<ProductType> typeComboBox;

    @Autowired
    public AdminProductView(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/product").build();
        this.grid = new Grid<>(Product.class);
        this.binder = new Binder<>(Product.class);
        this.nameField = new TextField("Name");
        this.descriptionField = new TextField("Description");
        this.priceRetailField = new IntegerField("Price Retail");
        this.priceWholeField = new IntegerField("Price Whole");
        this.typeComboBox = new ComboBox<>("Type", Arrays.asList(ProductType.values()));

        setupGrid();
        setupForm();
        loadProducts();

        add(new Button("Back", e -> UI.getCurrent().navigate(MainView.class)));
        add(new Button("Get Products", e -> loadProducts()), grid, createAddButton(), createUpdateButton(), createDeleteButton());
    }

    private void setupGrid() {
        grid.addColumn(Product::getName).setHeader("Name");
        grid.addColumn(Product::getDescription).setHeader("Description");
        grid.addColumn(Product::getPriceRetail).setHeader("Price Retail");
        grid.addColumn(Product::getPriceWhole).setHeader("Price Whole");
        grid.addColumn(Product::getType).setHeader("Type");
        grid.asSingleSelect().addValueChangeListener(event -> binder.setBean(event.getValue()));
    }

    private void setupForm() {
        binder.forField(nameField)
                .bind(Product::getName, Product::setName);
        binder.forField(descriptionField)
                .bind(Product::getDescription, Product::setDescription);
        binder.forField(priceRetailField)
                .bind(Product::getPriceRetail, Product::setPriceRetail);
        binder.forField(priceWholeField)
                .bind(Product::getPriceWhole, Product::setPriceWhole);
        binder.forField(typeComboBox)
                .bind(Product::getType, Product::setType);
    }

    private void loadProducts() {
        webClient.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(Product.class)
                .subscribe(product -> logger.info("Product: {}", product),
                        error -> logger.error("Error loading products:", error));
    }

    private Button createAddButton() {
        return new Button("Add Product", event -> {
            Product newProduct = new Product();
            binder.setBean(newProduct);
            openAddDialog();
        });
    }

    private void openAddDialog() {
        dialog = new Dialog();
        FormLayout form = new FormLayout(nameField, descriptionField, priceRetailField, priceWholeField, typeComboBox);
        Button saveButton = new Button("Save", e -> saveProduct(binder.getBean()));
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.add(form, saveButton, cancelButton);
        dialog.open();
    }

    private void saveProduct(Product product) {
        boolean isValid = binder.writeBeanIfValid(product);
        if (!isValid) {
            Notification.show("Пожалуйста, заполните все обязательные поля.");
            return;
        }

        Mono<Product> resultMono;
        if (product.getId() == null) { // Добавление нового продукта
            resultMono = webClient.post()
                    .uri("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(product), Product.class)
                    .retrieve()
                    .bodyToMono(Product.class);
        } else {
            resultMono = webClient.put()
                    .uri("/{id}", product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(product), Product.class)
                    .retrieve()
                    .bodyToMono(Product.class);
        }

        resultMono.subscribe(
                savedProduct -> UI.getCurrent().access(() -> {
                    loadProducts();
                    Notification.show("Продукт успешно " + (product.getId() == null ? "добавлен" : "обновлен") + "!");
                    dialog.close();
                }),
                error -> UI.getCurrent().access(() -> {
                    Notification.show("Ошибка: " + getErrorMessage(error));
                })
        );
    }

    private String getErrorMessage(Throwable error) { // Объявление метода
        return error != null && error.getMessage() != null ? error.getMessage() : "Произошла неизвестная ошибка.";
    }

    private Button createUpdateButton() {
        return new Button("Update Product", event -> {
            Product product = grid.asSingleSelect().getValue();
            if (product != null) {
                binder.setBean(product);
                openUpdateDialog(product);
            } else {
                Notification.show("Select a product to update.");
            }
        });
    }

    private void openUpdateDialog(Product product) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Update Product");

        FormLayout form = new FormLayout(nameField, descriptionField, priceRetailField, priceWholeField, typeComboBox);
        dialog.add(form);

        Button saveButton = new Button("Save", event -> {
            boolean isValid = binder.writeBeanIfValid(product);
            if (isValid) {
                webClient.put()
                        .uri("/{id}", product.getId())
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .body(Mono.just(product), Product.class)
                        .retrieve()
                        .bodyToMono(Product.class)
                        .subscribe(updatedProduct -> UI.getCurrent().access(() -> {
                                    Notification.show("Product updated successfully!");
                                    loadProducts();
                                    dialog.close();
                                }),
                                error -> UI.getCurrent().access(() -> {
                                    handleError(error, "Error updating product");
                                }));
            } else {
                Notification.show("Please fill in all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(saveButton, cancelButton);
        dialog.open();
    }

    private Button createDeleteButton() {
        return new Button("Delete Product", event -> {
            Product product = grid.asSingleSelect().getValue();
            if (product != null && product.getId() != null) {
                webClient.delete()
                        .uri("/{id}", product.getId())
                        .retrieve()
                        .bodyToMono(Void.class)
                        .subscribe(v -> UI.getCurrent().access(() -> {
                                    Notification.show("Product deleted successfully!");
                                    loadProducts();
                                }),
                                error -> handleError(error, "Deleting product"));
            } else {
                Notification.show("Select a product to delete.");
            }
        });
    }

    private void handleError(Throwable error, String operation) {
        Supplier<String> messageSupplier = () -> {
            String message = error.getMessage();
            if (error instanceof WebClientResponseException) {
                WebClientResponseException ex = (WebClientResponseException) error;
                message = "HTTP error " + ex.getStatusCode() + ": " + ex.getMessage();
            }
            return message;
        };

        System.err.println("Error " + operation + ": " + messageSupplier.get());

        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> Notification.show("Error " + operation + ": " + messageSupplier.get()));
        }
    }
}