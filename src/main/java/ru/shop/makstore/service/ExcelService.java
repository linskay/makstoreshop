package ru.shop.makstore.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.shop.makstore.model.CartItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    public File createExcelFile(List<CartItem> cartItems) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");

        // Заголовки таблицы
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Товар");
        headerRow.createCell(1).setCellValue("Розница");
        headerRow.createCell(2).setCellValue("Опт");
        headerRow.createCell(3).setCellValue("Стоимость");

        int total = 0; // Итоговая стоимость заказа
        int rowNum = 1; // Номер строки для данных

        // Обработка каждого товара в корзине
        for (CartItem cartItem : cartItems) {
            var product = cartItem.getProduct();
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getName()); // Название товара
            row.createCell(1).setCellValue(cartItem.getRetailQuantity() + " шт"); // Количество для розницы
            row.createCell(2).setCellValue(cartItem.getWholeQuantity() + " шт"); // Количество для опта

            // Подсчет стоимости для товара
            int retailTotal = product.getPriceRetail() * cartItem.getRetailQuantity(); // Розничная стоимость
            int wholeTotal = product.getPriceWhole() * cartItem.getWholeQuantity(); // Оптовая стоимость
            int totalPrice = retailTotal + wholeTotal; // Общая стоимость товара

            row.createCell(3).setCellValue(totalPrice); // Записываем стоимость товара
            total += totalPrice; // Добавляем к итоговой стоимости заказа
        }

        // Добавляем строку "ИТОГО"
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("ИТОГО:");
        totalRow.createCell(3).setCellValue(total); // Итоговая стоимость заказа

        // Сохранение файла
        File file = new File("Order.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}