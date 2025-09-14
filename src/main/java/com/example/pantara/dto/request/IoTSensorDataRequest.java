package com.example.pantara.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IoTSensorDataRequest {

    @NotBlank(message = "Device ID is required")
    private String device;

    @NotNull(message = "Temperature is required")
    @JsonProperty("temperature_c")
    private Double temperatureC;

    @NotNull(message = "Humidity is required")
    @JsonProperty("humidity_pct")
    private Double humidityPct;

    @NotNull(message = "Heat index is required")
    @JsonProperty("heat_index_c")
    private Double heatIndexC;

    private Long timestamp;

    // Manual getters and setters as fallback
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(Double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public Double getHumidityPct() {
        return humidityPct;
    }

    public void setHumidityPct(Double humidityPct) {
        this.humidityPct = humidityPct;
    }

    public Double getHeatIndexC() {
        return heatIndexC;
    }

    public void setHeatIndexC(Double heatIndexC) {
        this.heatIndexC = heatIndexC;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}