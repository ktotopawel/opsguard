ALTER TABLE incidents
ADD COLUMN assigned_to_id BIGINT;

ALTER TABLE incidents
ADD CONSTRAINT fk_incident_assignee
FOREIGN KEY (assigned_to_id)
REFERENCES users (id);

ALTER TABLE incidents_aud
ADD COLUMN assigned_to_id BIGINT