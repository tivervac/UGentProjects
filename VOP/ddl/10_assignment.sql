DROP TABLE IF EXISTS assignment CASCADE;

CREATE TABLE assignment (
  task_id int REFERENCES task(document_id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,
  project_id int REFERENCES project(id),
  start_date timestamp WITH TIME ZONE,
  end_date timestamp WITH TIME ZONE,
  
  
  CONSTRAINT assignment_pkey PRIMARY KEY (task_id, start_date)
);