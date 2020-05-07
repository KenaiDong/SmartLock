package com.xuzd.commons.db.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LtSpecification<T> extends AbstractSpecification<T> {

    private static final long serialVersionUID = 1L;

    private final String property;

    private final Number number;

    public LtSpecification(String property, Number number) {
        this.property = property;
        this.number = number;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From<?, ?> from = getRoot(property, root);
        String field = getProperty(property);
        return cb.lt(from.get(field), number);
    }
}
