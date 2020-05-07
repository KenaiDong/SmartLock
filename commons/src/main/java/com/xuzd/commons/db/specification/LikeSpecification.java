package com.xuzd.commons.db.specification;

import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LikeSpecification<T> extends AbstractSpecification<T> {

    private static final long serialVersionUID = 1L;

    private final String property;

    private final String[] patterns;

    private boolean ignoreCase = false;

    public LikeSpecification(String property, String... patterns) {
        this.property = property;
        this.patterns = patterns;
    }

    public LikeSpecification<T> ignoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From<? extends Object, ? extends Object> from = getRoot(property, root);
        String field = getProperty(property);
        if (patterns.length == 1) {
            return cb.like(ignoreCase ? cb.lower(from.get(field)) : from.get(field), patterns[0]);
        }
        Predicate[] predicates = Arrays.stream(patterns)
            .map(value -> cb.like(ignoreCase ? cb.lower(from.get(field)) : from.get(field), value))
            .toArray(Predicate[]::new);
        return cb.or(predicates);
    }
}
