DROP TABLE IF EXISTS document CASCADE;

CREATE TABLE if not exists document (
  /* Unique identifier of the document */
  id serial UNIQUE PRIMARY KEY,

  /* Foreign key of the project this document is associated with */
  project_id int REFERENCES project(id) ON DELETE CASCADE,

  /* Name (fully qualified namespace) of the document */
  name text NOT NULL,

  /* Type of the document */
  type varchar(128),

  /* JSON blob for this document */
  blob text,

  UNIQUE (project_id, name)
);

DROP TABLE IF EXISTS entity_analyst CASCADE;

CREATE TABLE entity_analyst (
  entity_id int REFERENCES document(id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,

  CONSTRAINT entity_analyst_pkey PRIMARY KEY (entity_id, person_id)
);