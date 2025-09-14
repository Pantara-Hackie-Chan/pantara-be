package com.example.pantara.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpiryAlertResponse {
    private List<BatchResponse> expiringToday;
    private List<BatchResponse> expiringIn2Days;
    private List<BatchResponse> expiringIn3Days;
}