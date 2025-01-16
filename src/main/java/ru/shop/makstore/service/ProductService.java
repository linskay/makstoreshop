package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

/**
 * • Сервис для работы с продуктами.
 * • Этот сервис предоставляет методы для выполнения операций CRUD (создание, чтение, обновление, удаление)
 * • над объектами Product, а также для получения списка продуктов с поддержкой пагинации и фильтрации по типу.
 */
@Service
@Transactional
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;

    /**
     * Конструктор класса ProductService.
     *
     * @param productRepository Репозиторий для работы с продуктами.
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Находит продукт по его уникальному идентификатору.
     *
     * @param productId Идентификатор продукта.
     * @return Найденный продукт.
     * @throws ProductNotFoundException Если продукт с указанным идентификатором не найден.
     */
    @Override
    public Product findProductById(int productId) {
        return productRepository.findById((long) productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    /**
     * Создает новый продукт.
     *
     * @param product Продукт для создания.
     * @return Созданный продукт.
     */
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Обновляет существующий продукт по его идентификатору.
     *
     * @param id               Идентификатор продукта, который нужно обновить.
     * @param productForUpdate Объект Product, содержащий новые данные.
     * @return Обновленный продукт.
     * @throws ProductNotFoundException Если продукт с указанным идентификатором не найден.
     */
    @Override
    public Product updateProductId(int id, Product productForUpdate) {
        if (productRepository.existsById((long) id)) {
            productForUpdate.setId(id);
            return productRepository.save(productForUpdate);
        }
        throw new ProductNotFoundException(id);
    }


    /**
     * Обновляет существующий продукт.
     *
     * @param product Продукт с обновленными данными.
     * @return Обновленный продукт.
     */
    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Удаляет продукт по его уникальному идентификатору.
     *
     * @param id Идентификатор продукта, который нужно удалить.
     * @throws ProductNotFoundException Если продукт с указанным идентификатором не найден.
     */
    @Override
    public void deleteProduct(int id) {
        if (productRepository.existsById((long) id)) {
            productRepository.deleteById((long) id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    /**
     * Получает продукт по его уникальному идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Optional, содержащий найденный продукт, или пустой Optional, если продукт не найден.
     */
    @Override
    public Optional<Product> getProductById(int id) {
        return productRepository.findById((long) id);
    }

    /**
     * Возвращает список всех продуктов.
     *
     * @return Список всех продуктов.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Находит продукт по его идентификатору (аналог findProductById)
     *
     * @param id Идентификатор продукта.
     * @return Найденный продукт
     * @throws ProductNotFoundException если продукт не найден
     */
    @Override
    public Product findProduct(Integer id) {
        return productRepository.findById((long) id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /**
     * Возвращает страницу продуктов с учетом параметров пагинации и фильтрации.
     *
     * @param pageable Параметры пагинации.
     * @param type     Тип продукта для фильтрации (может быть null).
     * @return Страница продуктов.
     */
    @Override
    public Page<Product> getAllProducts(Pageable pageable, ProductType type) {
        if (type != null) {
            return productRepository.findByType(type, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    /**
     * Находит продукт по имени.
     *
     * @param name Имя продукта.
     * @return Optional, содержащий найденный продукт, или пустой Optional, если продукт не найден.
     */
    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }
}
