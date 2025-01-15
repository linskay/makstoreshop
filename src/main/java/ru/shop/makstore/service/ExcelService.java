package ru.shop.makstore.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.shop.makstore.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    public File createExcelFile(List<Product> products) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");

        // Заголовки таблицы
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Товар");
        headerRow.createCell(1).setCellValue("Розница");
        headerRow.createCell(2).setCellValue("Опт");
        headerRow.createCell(3).setCellValue("Стоимость");

        int total = 0;
        int rowNum = 1;

        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getName());
            row.createCell(1).setCellValue(product.getRetailQuantity() + " шт");
            row.createCell(2).setCellValue(product.getWholeQuantity() + " шт");

            int retailTotal = product.getPriceRetail() * product.getRetailQuantity();
            int wholeTotal = product.getPriceWhole() * product.getWholeQuantity();
            int totalPrice = retailTotal + wholeTotal;

            row.createCell(3).setCellValue(totalPrice);
            total += totalPrice;
        }

        // Итоговая строка
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("ИТОГО:");
        totalRow.createCell(3).setCellValue(total);

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
