package com.example.pantara.dto.response;

import com.example.pantara.entity.Batch;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FreshnessStatusSummary {
    private Batch.FreshnessStatus status;
    private long count;

    public FreshnessStatusSummary() {}

    public FreshnessStatusSummary(Batch.FreshnessStatus status, long count) {
        this.status = status;
        this.count = count;
    }

}
