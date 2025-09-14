# IoT API Documentation

## Endpoint IoT untuk ESP32 DHT22 Sensor

### 1. Endpoint Utama untuk Menerima Data Sensor
```
POST /api/iot/sensor-data
```

**Request Body (JSON):**
```json
{
  "device": "esp32-dht22",
  "temperature_c": 25.5,
  "humidity_pct": 60.2,
  "heat_index_c": 26.8,
  "timestamp": 1694123456
}
```

**Response Success:**
```json
{
  "status": "success",
  "message": "Sensor data received successfully",
  "data_id": "uuid-here",
  "received_at": "2023-09-14T05:32:20Z"
}
```

### 2. Konfigurasi ESP32
Update kode ESP32 Anda dengan URL endpoint:
```cpp
const char* SERVER_URL = "http://your-server-ip:port/api/iot/sensor-data";
```

### 3. Fitur Notifikasi Otomatis

#### 3.1 Penurunan Suhu Drastis
- **Trigger:** Penurunan suhu > 5°C dalam satu pembacaan
- **Priority:** HIGH
- **Firebase:** Ya
- **Contoh:** 25°C → 18°C (turun 7°C)

#### 3.2 Kenaikan Suhu Drastis
- **Trigger:** Kenaikan suhu > 5°C dalam satu pembacaan
- **Priority:** HIGH
- **Firebase:** Ya
- **Contoh:** 25°C → 32°C (naik 7°C)

#### 3.3 Perangkat Tidak Terhubung
- **Trigger:** Tidak ada data dalam 5 menit
- **Priority:** CRITICAL
- **Firebase:** Ya
- **Check:** Setiap 1 menit sekali

### 4. Endpoint Tambahan

#### Get All Devices
```
GET /api/iot/devices
```

#### Get Device Data
```
GET /api/iot/devices/{deviceId}/data
```

#### Get Recent Data
```
GET /api/iot/devices/{deviceId}/recent?hours=24
```

#### Test Notifications
```
POST /api/iot/test-notification?type=temperature_drop
POST /api/iot/test-notification?type=temperature_rise
POST /api/iot/test-notification?type=device_offline
```

### 5. Database Tables
- `iot_sensor_data` - Menyimpan semua data sensor
- `notifications` - Notifikasi IoT dengan type `IOT_ALERT`

### 6. Scheduled Tasks
- **Device Connectivity Check:** Setiap 1 menit
- **Old Notification Cleanup:** Sesuai konfigurasi existing (30 hari)

### 7. Error Handling
API akan mengembalikan error response jika:
- Data tidak valid
- Device ID kosong
- Temperature/Humidity null
- Server error

**Error Response:**
```json
{
  "status": "error",
  "message": "Failed to process sensor data: [detail]"
}
```