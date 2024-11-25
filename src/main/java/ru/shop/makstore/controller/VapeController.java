package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.model.Vape;
import ru.shop.makstore.service.VapeService;

import java.util.List;

@RestController
@RequestMapping("/vape")
@Tag(name = "Контроллер Железо", description = "Контроллеры для управления железом")
public class VapeController {
    private final VapeService vapeService;

    public VapeController(VapeService vapeService) {
        this.vapeService = vapeService;
    }

    /**
     * @param vape железо для добавления
     * @return возвращает созданную запись
     */
    @PostMapping("/add")
    @Operation(summary = "Создает позицию",
            description = "Создает новую позицию железа",
            responses = @ApiResponse(responseCode = "200", description = "Успешно добавлено"))
    public Vape createVape(@RequestBody Vape vape) {
        return vapeService.createVape(vape);
    }

    /**
     *
     * @param id идентификатор железа, класс Vape
     * @param vape железо для обновления
     * @return объект железа обновленный
     */
    @PutMapping("{id}/update")
    @Operation(summary = "Обновляет железо",
            description = "Обновляет позицию железа и устанавливает id",
            responses = {@ApiResponse(responseCode = "404", description = "Позиция не найдена"),
                    @ApiResponse(responseCode = "200", description = "Обновлено успешно")
            })
    public Vape editVape(@PathVariable("id") int id,
                         @RequestBody Vape vape) {
        vape.setId(id);
        return vapeService.editVape(vape);
    }

    /**
     * @param id железа для удаления
     * @return возвращает удаленный объект (тип Vape) или null, если объект железа с заданным id не был найден
     */
    @DeleteMapping("{id}/delete")
    @Operation(
            summary = "Удаляет объект",
            description = "Удаляет объект по id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объект удален"),
                    @ApiResponse(responseCode = "404", description = "Объект для удаления не найден"),
                    @ApiResponse(responseCode = "500", description = "Произошла внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<Void> deleteVape(@PathVariable("id") int id) {
        try {
            vapeService.deleteVape(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Возвращает список всех Vape.
     * Этот метод возвращает список всех объектов Vape, хранящихся в базе данных
     * @return Список всех объектов Vape. Возвращает пустой список, если Vape отсутствуют. Может вернуть ошибку 500 Internal Server Error в случае возникновения непредвиденных ошибок на сервере.
     */
    @Operation(
            summary = "Получить все Vape",
            description = "Возвращает список всех Vape.  Не используйте для больших наборов данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает список Vape."),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера.")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<Vape>> getAllVapes() {
        try {
            List<Vape> vapes = vapeService.getAllVapes();
            return ResponseEntity.ok(vapes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
