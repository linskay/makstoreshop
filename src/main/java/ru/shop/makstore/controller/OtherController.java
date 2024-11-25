package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.model.Other;
import ru.shop.makstore.service.OtherService;


@RestController
@RequestMapping("/other")
@Tag(name = "Контроллер Other", description = "Контроллеры для управления Other")
public class OtherController {

    private final OtherService otherService;

    public OtherController(OtherService otherService) {
        this.otherService = otherService;
    }

    /**
     * Создает новый объект Other.
     * @param other Объект Other для создания.
     * @return Созданный объект Other и HTTP статус 201 (Created).  В случае ошибки возвращает 500 (Internal Server Error).
     */
    @PostMapping
    @Operation(summary = "Создать Other", description = "Создает новый Other")
    public ResponseEntity<Other> createOther(@RequestBody Other other) {
        Other createdOther = otherService.createOther(other);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOther);
    }

    /**
     * Обновляет существующий объект Other.
     * @param other Объект Other с обновленными данными.  ID объекта должен быть указан.
     * @return Обновленный объект Other и HTTP статус 200 (OK).  Возвращает 404 (Not Found), если объект не найден. В случае ошибки возвращает 500 (Internal Server Error).
     */
    @PutMapping
    @Operation(summary = "Изменить Other", description = "Изменяет существующий Other")
    public ResponseEntity<Other> editOther(@RequestBody Other other) {
        Other updatedOther = otherService.editOther(other);
        return ResponseEntity.ok(updatedOther);
    }

    /**
     * Удаляет объект Other по ID.
     * @param id ID объекта Other для удаления.
     * @return HTTP статус 204 (No Content), если объект успешно удален.  Возвращает 404 (Not Found), если объект не найден.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить Other", description = "Удаляет Other по ID", responses = {
            @ApiResponse(responseCode = "204", description = "Other успешно удален"),
            @ApiResponse(responseCode = "404", description = "Other не найден")
    })
    public ResponseEntity<Void> deleteOther(@PathVariable int id) {
        if (otherService.deleteOther(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Возвращает список всех объектов Other с пагинацией.
     * @param pageable Объект Pageable для пагинации.
     * @return Список объектов Other с пагинацией и HTTP статус 200 (OK).  В случае ошибки возвращает 500 (Internal Server Error).
     */
    @GetMapping
    @Operation(summary = "Получить все Others", description = "Возвращает список всех Others с пагинацией")
    public ResponseEntity<Page<Other>> getAllOthers(Pageable pageable) {
        Page<Other> others = otherService.getOthers(pageable);
        return ResponseEntity.ok(others);
    }
}

