package com.example.pantara.controller;

import com.example.pantara.dto.request.IoTSensorDataRequest;
import com.example.pantara.dto.response.MessageResponse;
import com.example.pantara.entity.IoTSensorData;
import com.example.pantara.service.IoTService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/iot")
public class IoTController {

    private static final Logger log = LoggerFactory.getLogger(IoTController.class);

    private final IoTService iotService;

    public IoTController(IoTService iotService) {
        this.iotService = iotService;
    }

    @PostMapping("/sensor-data")
    public ResponseEntity<Map<String, Object>> receiveSensorData(@Valid @RequestBody IoTSensorDataRequest request) {
        try {
            log.info("Received IoT sensor data from device: {} - Temp: {}°C, Humidity: {}%",
                    request.getDevice(), request.getTemperatureC(), request.getHumidityPct());

            IoTSensorData savedData = iotService.processSensorData(request);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Sensor data received successfully");
            response.put("data_id", savedData.getId().toString());
            response.put("received_at", savedData.getReceivedAt().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing IoT sensor data from device: {}", request.getDevice(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to process sensor data: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/devices")
    public ResponseEntity<List<String>> getAllDevices() {
        List<String> devices = iotService.getAllDeviceIds();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/devices/{deviceId}/data")
    public ResponseEntity<List<IoTSensorData>> getDeviceData(@PathVariable String deviceId) {
        List<IoTSensorData> data = iotService.getDeviceData(deviceId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/devices/{deviceId}/recent")
    public ResponseEntity<List<IoTSensorData>> getRecentDeviceData(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "24") int hours) {
        List<IoTSensorData> data = iotService.getRecentDeviceData(deviceId, hours);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/test-notification")
    public ResponseEntity<MessageResponse> testNotification(@RequestParam String type) {
        try {
            String deviceId = "test-device";
            switch (type.toLowerCase()) {
                case "temperature_drop":
                    iotService.sendTemperatureDropAlert(deviceId, 25.0, 18.0, 7.0);
                    break;
                case "temperature_rise":
                    iotService.sendTemperatureRiseAlert(deviceId, 25.0, 32.0, 7.0);
                    break;
                case "device_offline":
                    iotService.sendDeviceOfflineAlert(deviceId, java.time.Instant.now().minusSeconds(360));
                    break;
                default:
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Invalid test type. Use: temperature_drop, temperature_rise, or device_offline"));
            }

            return ResponseEntity.ok(new MessageResponse("Test notification sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Failed to send test notification: " + e.getMessage()));
        }
    }
}