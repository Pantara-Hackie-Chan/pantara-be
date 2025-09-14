package com.example.pantara.service;

import com.example.pantara.dto.request.IoTSensorDataRequest;
import com.example.pantara.entity.IoTSensorData;
import com.example.pantara.entity.Notification;
import com.example.pantara.repository.IoTSensorDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IoTService {

    private static final Logger log = LoggerFactory.getLogger(IoTService.class);

    private static final double TEMPERATURE_CHANGE_THRESHOLD = 5.0;
    private static final int DEVICE_OFFLINE_MINUTES = 5;

    private final IoTSensorDataRepository iotRepository;
    private final NotificationService notificationService;

    private final Map<String, Instant> lastDeviceActivity = new ConcurrentHashMap<>();

    public IoTService(IoTSensorDataRepository iotRepository, NotificationService notificationService) {
        this.iotRepository = iotRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public IoTSensorData processSensorData(IoTSensorDataRequest request) {
        IoTSensorData sensorData = new IoTSensorData();
        sensorData.setDeviceId(request.getDevice());
        sensorData.setTemperatureC(request.getTemperatureC());
        sensorData.setHumidityPct(request.getHumidityPct());
        sensorData.setHeatIndexC(request.getHeatIndexC());
        sensorData.setDeviceTimestamp(request.getTimestamp());

        IoTSensorData savedData = iotRepository.save(sensorData);

        lastDeviceActivity.put(request.getDevice(), Instant.now());

        checkTemperatureChanges(request.getDevice(), request.getTemperatureC());

        log.info("Processed IoT data from device: {} - Temp: {}°C, Humidity: {}%, Heat Index: {}°C",
                request.getDevice(), request.getTemperatureC(), request.getHumidityPct(), request.getHeatIndexC());

        return savedData;
    }

    private void checkTemperatureChanges(String deviceId, double currentTemp) {
        List<IoTSensorData> recentData = iotRepository.findTop2ByDeviceIdOrderByReceivedAtDesc(deviceId);

        if (recentData.size() >= 2) {
            IoTSensorData current = recentData.get(0);
            IoTSensorData previous = recentData.get(1);

            double tempDifference = current.getTemperatureC() - previous.getTemperatureC();

            if (Math.abs(tempDifference) >= TEMPERATURE_CHANGE_THRESHOLD) {
                if (tempDifference > 0) {
                    sendTemperatureRiseAlert(deviceId, previous.getTemperatureC(), currentTemp, tempDifference);
                } else {
                    sendTemperatureDropAlert(deviceId, previous.getTemperatureC(), currentTemp, Math.abs(tempDifference));
                }
            }
        }
    }

    @Async
    public void sendTemperatureDropAlert(String deviceId, double previousTemp, double currentTemp, double difference) {
        try {
            String title = "🥶 Penurunan Suhu Drastis Terdeteksi";
            String message = "Perangkat %s mengalami penurunan suhu drastis %.1f°C (dari %.1f°C ke %.1f°C). Periksa kondisi lingkungan segera!".formatted(
                    deviceId, difference, previousTemp, currentTemp
            );

            notificationService.sendIoTAlert(title, message, deviceId, "TEMPERATURE_DROP", Notification.NotificationPriority.HIGH);

            log.warn("Temperature drop alert sent for device: {} - Drop: {}°C", deviceId, difference);
        } catch (Exception e) {
            log.error("Failed to send temperature drop alert for device: {}", deviceId, e);
        }
    }

    @Async
    public void sendTemperatureRiseAlert(String deviceId, double previousTemp, double currentTemp, double difference) {
        try {
            String title = "🔥 Kenaikan Suhu Drastis Terdeteksi";
            String message = "Perangkat %s mengalami kenaikan suhu drastis %.1f°C (dari %.1f°C ke %.1f°C). Periksa kondisi lingkungan segera!".formatted(
                    deviceId, difference, previousTemp, currentTemp
            );

            notificationService.sendIoTAlert(title, message, deviceId, "TEMPERATURE_RISE", Notification.NotificationPriority.HIGH);

            log.warn("Temperature rise alert sent for device: {} - Rise: {}°C", deviceId, difference);
        } catch (Exception e) {
            log.error("Failed to send temperature rise alert for device: {}", deviceId, e);
        }
    }

    @Async
    public void sendDeviceOfflineAlert(String deviceId, Instant lastSeen) {
        try {
            long minutesOffline = ChronoUnit.MINUTES.between(lastSeen, Instant.now());

            String title = "📡 Perangkat IoT Terputus";
            String message = "Perangkat %s tidak mengirimkan data selama %d menit. Periksa koneksi WiFi dan status perangkat.".formatted(
                    deviceId, minutesOffline
            );

            notificationService.sendIoTAlert(title, message, deviceId, "DEVICE_OFFLINE", Notification.NotificationPriority.CRITICAL);

            log.warn("Device offline alert sent for device: {} - Offline for {} minutes", deviceId, minutesOffline);
        } catch (Exception e) {
            log.error("Failed to send device offline alert for device: {}", deviceId, e);
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional(readOnly = true)
    public void checkDeviceConnectivity() {
        log.debug("Checking device connectivity...");

        List<String> allDevices = iotRepository.findAllDistinctDeviceIds();
        Instant thresholdTime = Instant.now().minus(DEVICE_OFFLINE_MINUTES, ChronoUnit.MINUTES);

        for (String deviceId : allDevices) {
            IoTSensorData lastData = iotRepository.findFirstByDeviceIdOrderByReceivedAtDesc(deviceId).orElse(null);

            if (lastData != null && lastData.getReceivedAt().isBefore(thresholdTime)) {
                Instant lastActivity = lastDeviceActivity.get(deviceId);

                if (lastActivity == null || lastActivity.isBefore(thresholdTime)) {
                    sendDeviceOfflineAlert(deviceId, lastData.getReceivedAt());
                    lastDeviceActivity.put(deviceId, Instant.now());
                }
            }
        }
    }

    public List<IoTSensorData> getDeviceData(String deviceId) {
        return iotRepository.findByDeviceIdOrderByReceivedAtDesc(deviceId);
    }

    public List<IoTSensorData> getRecentDeviceData(String deviceId, int hours) {
        Instant since = Instant.now().minus(hours, ChronoUnit.HOURS);
        return iotRepository.findByDeviceIdAndReceivedAtAfter(deviceId, since);
    }

    public List<String> getAllDeviceIds() {
        return iotRepository.findAllDistinctDeviceIds();
    }
}