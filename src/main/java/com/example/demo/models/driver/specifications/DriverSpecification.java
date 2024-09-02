package com.example.demo.models.driver.specifications;


import com.example.demo.models.driver.Driver;
import com.example.demo.models.driver.DriverListFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public record DriverSpecification(
    @NotNull DriverListFilter filter
) implements Specification<Driver> {

    @Override
    public Predicate toPredicate(
        Root<Driver> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
    ) {
        List<Predicate> exps = new ArrayList<>();

        if (filter.status != null) {
            exps.add(criteriaBuilder.equal(root.get("status"), filter.status));
        }

        return criteriaBuilder.and(exps.toArray(new Predicate[0]));
    }
}
