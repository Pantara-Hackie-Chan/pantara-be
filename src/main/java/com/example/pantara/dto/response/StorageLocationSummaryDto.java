package com.example.pantara.dto.response;

public class StorageLocationSummaryDto {
    private String location;
    private long batchCount;

    public StorageLocationSummaryDto(String location, long batchCount) {
        this.location = location;
        this.batchCount = batchCount;
    }

    public String getLocation() {
        return location;
    }

    public long getBatchCount() {
        return batchCount;
    }
}
