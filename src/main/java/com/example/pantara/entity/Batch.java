package com.example.pantara.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "batches")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "batch_code", nullable = false, unique = true, length = 50)
    private String batchCode;

    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "weight", nullable = false, precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    @Column(name = "source", nullable = false, length = 100)
    private String source;

    @CreationTimestamp
    @Column(name = "entry_date", nullable = false)
    private Instant entryDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "storage_location", nullable = false, length = 50)
    private String storageLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "freshness_status", nullable = false)
    private FreshnessStatus freshnessStatus = FreshnessStatus.GREEN;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public enum Category {
        SAYURAN("Sayuran"),
        PROTEIN("Protein"),
        BUAH("Buah"),
        BAHAN_POKOK("Bahan Pokok");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum FreshnessStatus {
        GREEN,
        YELLOW,
        RED
    }

    // Manual getters and setters as fallback
    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}