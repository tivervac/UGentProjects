DROP TABLE IF EXISTS task CASCADE;

CREATE TABLE task (
  document_id int REFERENCES document(id) ON DELETE CASCADE PRIMARY KEY,
  priority integer,
  workload bigint
);