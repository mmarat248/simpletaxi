package com.example.demo.models.ride.specifications;



import com.example.demo.models.ride.Ride;
import com.example.demo.models.ride.RideListFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public record RideSpecification(
    @NotNull RideListFilter filter
) implements Specification<Ride> {

    @Override
    public Predicate toPredicate(
        Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
    ) {
        List<Predicate> exps = new ArrayList<>();

        if (filter.status != null) {
            exps.add(criteriaBuilder.equal(root.get("status"), filter.status));
        }

        return criteriaBuilder.and(exps.toArray(new Predicate[0]));
    }
}
