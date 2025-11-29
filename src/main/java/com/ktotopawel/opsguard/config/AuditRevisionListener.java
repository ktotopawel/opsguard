package com.ktotopawel.opsguard.config;

import com.ktotopawel.opsguard.audit.AuditRevisionEntity;
import com.ktotopawel.opsguard.security.UserContext;
import org.hibernate.envers.RevisionListener;

public class AuditRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        if (UserContext.get() != null) {
            audit.setUserId(UserContext.get().id());
        }
    }
}
