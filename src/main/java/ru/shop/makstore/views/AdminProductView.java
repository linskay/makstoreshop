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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;

import java.util.Arrays;
import java.util.List;

@Component
@Route("admin/products")
public class AdminProductView extends VerticalLayout {

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
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .subscribe(products -> UI.getCurrent().access(() -> grid.setItems(products)),
                        error -> UI.getCurrent().access(() -> Notification.show("Error loading products: " + error.getMessage())));
    }

    private Button createAddButton() {
        return new Button("Add Product", event -> {
            Product newProduct = new Product();
            binder.setBean(newProduct);
            openAddDialog();
        });
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add Product");

        FormLayout form = new FormLayout(nameField, descriptionField, priceRetailField, priceWholeField, typeComboBox);
        dialog.add(form);

        Button saveButton = new Button("Save", event -> {
            Product newProduct = new Product();
            boolean isValid = binder.writeBeanIfValid(newProduct);

            if (isValid) {
                webClient.post()
                        .uri("/")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .body(Mono.just(newProduct), Product.class)
                        .retrieve()
                        .bodyToMono(Product.class)
                        .subscribe(product -> UI.getCurrent().access(() -> {
                                    Notification.show("Product added successfully!");
                                    loadProducts();
                                    dialog.close();
                                }),
                                error -> UI.getCurrent().access(() -> {
                                    handleError(error, "Error adding product");
                                }));
            } else {
                Notification.show("Please fill in all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(saveButton, cancelButton);
        dialog.open();
    }

    private Button createUpdateButton() {
        return new Button("Update Product", event -> {
            Product product = grid.asSingleSelect().getValue();
            if (product != null) {
                binder.setBean(product);
                openUpdateDialog(product);
            } else {
                Notification.show("Please select a product to update.");
            }
        });
    }

    private void openUpdateDialog(Product product) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Update Product");

        FormLayout form = new FormLayout(nameField, descriptionField, priceRetailField, priceWholeField, typeComboBox);
        dialog.add(form);

        Button saveButton = new Button("Save", event -> {
            binder.writeBeanIfValid(product);

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
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(saveButton, cancelButton);
        dialog.open();
    }

    private Button createDeleteButton() {
        return new Button("Delete Product", event -> {
            Product product = grid.asSingleSelect().getValue();
            if (product != null) {
                webClient.delete()
                        .uri("/{id}", product.getId())
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(response -> UI.getCurrent().access(() -> {
                                    Notification.show("Product deleted successfully!");
                                    loadProducts();
                                }),
                                error -> UI.getCurrent().access(() -> {
                                    handleError(error, "Error deleting product");
                                }));
            } else {
                Notification.show("Please select a product to delete.");
            }
        });
    }

    private void handleError(Throwable error, String message) {
        if (error instanceof WebClientResponseException exception) {
            Notification.show(message + ": " + exception.getRawStatusCode() + " - " + exception.getMessage());
        } else {
            Notification.show(message + ": " + error.getMessage());
        }
    }
}