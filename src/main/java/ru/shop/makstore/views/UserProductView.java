package ru.shop.makstore.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.shop.makstore.model.Product;

import java.util.List;

@Component
@Route("user/products")
public class UserProductView extends VerticalLayout {

    private final WebClient webClient;
    private final Grid<Product> grid;
    private final TextField searchField;

    @Autowired
    public UserProductView(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/product").build();
        this.grid = new Grid<>(Product.class, false); // Создаем Grid без автоматического добавления колонок
        this.searchField = new TextField("Search");

        setupGrid();
        loadProducts();

        add(new Button("Back", e -> UI.getCurrent().navigate(MainView.class)));
        add(new Button("Get Products", e -> loadProducts()), searchField, new Button("Search", e -> searchProducts()), grid);
    }

    private void setupGrid() {
        grid.addColumn(Product::getType).setHeader("Type");
        grid.addColumn(Product::getName).setHeader("Name");
        grid.addColumn(Product::getDescription).setHeader("Description");
        grid.addColumn(Product::getPriceRetail).setHeader("Price Retail");
        grid.addColumn(Product::getPriceWhole).setHeader("Price Whole");
    }

    private void loadProducts() {
        webClient.get()
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .subscribe(products -> UI.getCurrent().access(() -> grid.setItems(products)),
                        error -> UI.getCurrent().access(() -> Notification.show("Error loading products: " + error.getMessage())));
    }

    private void searchProducts() {
        String searchTerm = searchField.getValue();
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("term", searchTerm)
                        .build())
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .subscribe(products -> UI.getCurrent().access(() -> grid.setItems(products)),
                        error -> UI.getCurrent().access(() -> handleError(error, "Error searching products")));
    }

    private void handleError(Throwable error, String message) {
        if (error instanceof WebClientResponseException exception) {
            Notification.show(message + ": " + exception.getRawStatusCode() + " - " + exception.getMessage());
        } else {
            Notification.show(message + ": " + error.getMessage());
        }
    }
}