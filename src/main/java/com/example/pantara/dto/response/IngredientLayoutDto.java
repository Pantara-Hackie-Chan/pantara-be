package com.example.pantara.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientLayoutDto {
    private String ingredientName;
    private List<BatchPositionDto> batchPositions;
}
