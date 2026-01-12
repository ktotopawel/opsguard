ALTER TABLE incidents
ADD COLUMN assigned_to_id BIGINT;

ALTER TABLE incidents
ADD CONSTRAINT fk_incident_assignee
FOREIGN KEY (assigned_to_id)
REFERENCES users (id);

ALTER TABLE incidents_aud

ADD COLUMN assigned_to_id BIGINT;
ALTER TABLE incidents
    ADD version BIGINT;

CREATE UNIQUE INDEX IX_pk_incidents_aud ON incidents_aud (rev, id);

CREATE UNIQUE INDEX IX_pk_tags_aud ON tags_aud (rev, id);

CREATE UNIQUE INDEX IX_pk_users_aud ON users_aud (rev, id);

ALTER TABLE incidents
    ADD CONSTRAINT FK_INCIDENTS_ON_CLOSED_BY FOREIGN KEY (closed_by_id) REFERENCES users (id);

ALTER TABLE users
    DROP COLUMN created_at;

ALTER TABLE users
    ALTER COLUMN password SET NOT NULL;

ALTER TABLE incidents
    ALTER COLUMN severity TYPE VARCHAR(255) USING (severity::VARCHAR(255));

ALTER TABLE incidents_aud
    ALTER COLUMN severity TYPE VARCHAR(255) USING (severity::VARCHAR(255));

ALTER TABLE incidents
    ALTER COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE incidents_aud
    ALTER COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE users
    ALTER COLUMN username DROP NOT NULL;