DROP TABLE IF EXISTS entity_analyst CASCADE;

CREATE TABLE entity_analyst (
  entity_id int REFERENCES document(id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,

  CONSTRAINT entity_analyst_pkey PRIMARY KEY (entity_id, person_id)
);