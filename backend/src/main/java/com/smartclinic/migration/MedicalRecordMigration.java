package com.smartclinic.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@ChangeUnit(id = "create-medical-records-collection", order = "001", author = "developer")
public class MedicalRecordMigration {

    private static final String COLLECTION_NAME = "medical_records";

    @Execution
    public void execution(MongoTemplate mongoTemplate) {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }

        // Tạo Index cho booking_id
        mongoTemplate.indexOps(COLLECTION_NAME).ensureIndex(
                new Index().on("booking_id", Sort.Direction.ASC)
        );

        // Tạo Index cho patient_id
        mongoTemplate.indexOps(COLLECTION_NAME).ensureIndex(
                new Index().on("patient_id", Sort.Direction.ASC)
        );
    }

    @RollbackExecution
    public void rollbackExecution(MongoTemplate mongoTemplate) {
        if (mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.dropCollection(COLLECTION_NAME);
        }
    }
}
