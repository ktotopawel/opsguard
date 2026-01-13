package com.ktotopawel.opsguard.config;

import com.ktotopawel.opsguard.audit.AuditRevisionEntity;
import com.ktotopawel.opsguard.security.UserContext;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public class AuditRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        try {
            audit.setUserId(UserContext.get().id());
        } catch (AuthenticationCredentialsNotFoundException ignored) {
            // Anonymous user
        }
    }
}
