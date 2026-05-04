package com.smartclinic.models.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medical_records")
public class MedicalRecord {

    @Id
    private UUID id;

    @Field("patient_id")
    private UUID patientId;

    @Field("booking_id")
    private UUID bookingId;

    @Field("clinical_notes")
    private ClinicalNotes clinicalNotes;

    @Field("prescriptions")
    private List<Prescription> prescriptions;

    @Field("test_results")
    private List<TestResult> testResults;

    @Field("ai_analysis")
    private AiAnalysis aiAnalysis;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClinicalNotes {
        private String diagnosis;
        private String symptoms;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Prescription {
        private String medicationName;
        private String dosage;
        private String frequency;
        private String duration;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResult {
        private String testName;
        private String result;
        private String unit;
        private Instant testDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiAnalysis {
        private String predictedDiagnosis;
        private Double confidenceScore;
        private String recommendations;
    }
}
