package com.example.pantara.repository;

import com.example.pantara.entity.IoTSensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IoTSensorDataRepository extends JpaRepository<IoTSensorData, UUID> {

    Optional<IoTSensorData> findFirstByDeviceIdOrderByReceivedAtDesc(String deviceId);

    List<IoTSensorData> findTop2ByDeviceIdOrderByReceivedAtDesc(String deviceId);

    @Query("SELECT DISTINCT i.deviceId FROM IoTSensorData i")
    List<String> findAllDistinctDeviceIds();

    @Query("SELECT i FROM IoTSensorData i WHERE i.deviceId = :deviceId AND i.receivedAt >= :since ORDER BY i.receivedAt DESC")
    List<IoTSensorData> findByDeviceIdAndReceivedAtAfter(@Param("deviceId") String deviceId, @Param("since") Instant since);

    List<IoTSensorData> findByDeviceIdOrderByReceivedAtDesc(String deviceId);
}