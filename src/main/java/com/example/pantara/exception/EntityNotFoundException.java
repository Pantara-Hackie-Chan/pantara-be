package com.example.pantara.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String entityName, String fieldName, Object fieldValue) {
        super(
                "ENTITY_NOT_FOUND",
                "%s not found with %s: %s".formatted(entityName, fieldName, fieldValue),
                entityName, fieldName, fieldValue
        );
    }

    public EntityNotFoundException(String entityName, Object id) {
        this(entityName, "id", id);
    }

    public static EntityNotFoundException batch(String batchCode) {
        return new EntityNotFoundException("Batch", "batchCode", batchCode);
    }

    public static EntityNotFoundException user(String identifier) {
        return new EntityNotFoundException("User", "identifier", identifier);
    }

    public static EntityNotFoundException menu(String menuId) {
        return new EntityNotFoundException("Menu", "id", menuId);
    }
}