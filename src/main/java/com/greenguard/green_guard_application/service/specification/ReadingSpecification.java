package com.greenguard.green_guard_application.service.specification;

import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ReadingSpecification {

    public static Specification<Reading> withFilter(ReadingFilterDTO readingFilterDTO) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (readingFilterDTO.getSensorName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sensor").get("name"), readingFilterDTO.getSensorName()));
            }

            if (readingFilterDTO.getSensorOwnerUsername() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sensor").get("user").get("username"), readingFilterDTO.getSensorOwnerUsername()));
            }

            if (readingFilterDTO.getTemperatureFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("temperature"), readingFilterDTO.getTemperatureFrom()));
            }

            if (readingFilterDTO.getTemperatureTo() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("temperature"), readingFilterDTO.getTemperatureTo()));
            }

            if (readingFilterDTO.getHumidityFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("humidity"), readingFilterDTO.getHumidityFrom()));
            }

            if (readingFilterDTO.getHumidityTo() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("humidity"), readingFilterDTO.getHumidityTo()));
            }

            if (readingFilterDTO.getDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo
                        (root.get("timestamp"), readingFilterDTO.getDateFrom().toInstant(ZoneOffset.UTC)));
            }

            if (readingFilterDTO.getDateTo() != null) {
                predicates.add(criteriaBuilder.lessThan
                        (root.get("timestamp"), readingFilterDTO.getDateTo().toInstant(ZoneOffset.UTC)));
            }

            if (readingFilterDTO.getLocationName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sensor").get("location").get("name"), readingFilterDTO.getLocationName()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

