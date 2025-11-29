package com.ktotopawel.opsguard.spec;

import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Severity;
import com.ktotopawel.opsguard.entity.Tag;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class IncidentSpecification {
    public static Specification<Incident> hasTags(Set<String> tagNames) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (tagNames == null || tagNames.isEmpty()) {
                return null;
            }
            if (criteriaQuery != null) {
                criteriaQuery.distinct(true);
            }
            Join<Incident, Tag> tagsJoin = root.join("tags");
            return tagsJoin.get("name").in(tagNames);
        };
    }

    public static Specification<Incident> hasSeverities(Set<Severity> severities) {
        return (r, query, cb) -> {
            if (severities == null || severities.isEmpty()) {
                return null;
            }
            query.distinct(true);
            return r.get("severity").in(severities);
        };
    }
}
