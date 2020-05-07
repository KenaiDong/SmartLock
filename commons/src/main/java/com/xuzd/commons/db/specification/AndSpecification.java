package com.xuzd.commons.db.specification;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class AndSpecification<T> implements Specification<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private Specification<T> specification;

    public AndSpecification(Specification<T> specification) {
        this.specification = specification;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return specification.toPredicate(root, query, cb);
    }
}
