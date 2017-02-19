DROP TABLE IF EXISTS project_analyst CASCADE;

CREATE TABLE project_analyst (
  project_id int REFERENCES project(id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,
  workhours bigint,

  CONSTRAINT project_analyst_pkey PRIMARY KEY (project_id, person_id)
);