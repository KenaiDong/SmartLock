package com.xuzd.commons.db.specification;

import java.io.Serializable;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

abstract class AbstractSpecification<T> implements Specification<T>, Serializable {

    private static final long serialVersionUID = 1L;

    public String getProperty(String property) {
        if (property.contains(".")) {
            return StringUtils.split(property, ".")[1];
        }
        return property;
    }

    public From<?, ?> getRoot(String property, Root<T> root) {
        if (property.contains(".")) {
            String joinProperty = StringUtils.split(property, ".")[0];
            return root.join(joinProperty, JoinType.LEFT);
        }
        return root;
    }
}
