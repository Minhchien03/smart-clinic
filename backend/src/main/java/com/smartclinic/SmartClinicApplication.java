package com.smartclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.smartclinic.modules.auth.entity",
    "com.smartclinic.modules.patient.entity",
    "com.smartclinic.modules.appointment.entity",
    "com.smartclinic.core.entity"
})
@EnableJpaRepositories(basePackages = {
    "com.smartclinic.modules.auth.repository",
    "com.smartclinic.modules.patient.repository",
    "com.smartclinic.modules.appointment.repository"
})
@EnableMongoRepositories(basePackages = {
    "com.smartclinic.modules.medicalrecord.repository"
})
public class SmartClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartClinicApplication.class, args);
    }

}
