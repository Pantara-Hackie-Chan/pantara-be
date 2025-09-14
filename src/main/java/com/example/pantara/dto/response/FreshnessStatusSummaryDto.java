package com.example.pantara.dto.response;

import com.example.pantara.entity.Batch;

public class FreshnessStatusSummaryDto {
    private Batch.FreshnessStatus status;
    private long count;

    public FreshnessStatusSummaryDto(Batch.FreshnessStatus status, long count) {
        this.status = status;
        this.count = count;
    }

    public Batch.FreshnessStatus getStatus() {
        return status;
    }

    public long getCount() {
        return count;
    }
}
