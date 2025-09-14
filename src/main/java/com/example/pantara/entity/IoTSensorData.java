package com.example.pantara.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "iot_sensor_data")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IoTSensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "temperature_c", nullable = false)
    private Double temperatureC;

    @Column(name = "humidity_pct", nullable = false)
    private Double humidityPct;

    @Column(name = "heat_index_c", nullable = false)
    private Double heatIndexC;

    @Column(name = "device_timestamp")
    private Long deviceTimestamp;

    @CreationTimestamp
    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "connection_status", nullable = false, length = 50)
    private String connectionStatus = "ONLINE";

    // Manual getters and setters as fallback
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Long getDeviceTimestamp() {
        return deviceTimestamp;
    }

    public void setDeviceTimestamp(Long deviceTimestamp) {
        this.deviceTimestamp = deviceTimestamp;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}