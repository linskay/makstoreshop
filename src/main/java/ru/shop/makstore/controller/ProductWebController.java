package ru.shop.makstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.service.ProductService;

@Controller
public class ProductWebController {

    private final ProductService productService;

    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) ProductType type,
            Model model) {

        // Создаем Pageable для пагинации
        Pageable pageable = PageRequest.of(page, size);

        // Получаем страницу товаров с учетом фильтрации по типу
        Page<Product> productPage = productService.getAllProducts(pageable, type);

        // Передаем данные в шаблон
        model.addAttribute("products", productPage.getContent()); // Список товаров
        model.addAttribute("currentPage", page); // Текущая страница
        model.addAttribute("totalPages", productPage.getTotalPages()); // Общее количество страниц
        model.addAttribute("selectedType", type); // Выбранный тип фильтра
        model.addAttribute("productTypes", ProductType.values()); // Все типы товаров

        // Передаем текущий путь для подсветки активной кнопки
        model.addAttribute("currentPath", "/products");

        return "product"; // Имя шаблона Thymeleaf
    }
}