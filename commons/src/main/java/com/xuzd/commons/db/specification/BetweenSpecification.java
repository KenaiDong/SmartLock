package com.xuzd.commons.db.specification;//package com.uxsino.commons.db.specification;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.From;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import org.springframework.data.domain.Range;
//
//@SuppressWarnings("rawtypes")
//public class BetweenSpecification<T> extends AbstractSpecification<T> {
//
//    private static final long serialVersionUID = 1L;
//
//    private final String property;
//
//    private final Range range;
//
//    public BetweenSpecification(String property, Range range) {
//        this.property = property;
//        this.range = range;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//        From<?, ?> from = getRoot(property, root);
//        String field = getProperty(property);
//        return cb.between(from.get(field), range.getLowerBound(), range.getUpperBound());
//    }
//}
