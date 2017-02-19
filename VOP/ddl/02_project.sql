DROP TABLE IF EXISTS project CASCADE;

CREATE TABLE if not exists project (
  /* Unique identifier of the project */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the project */
  name text UNIQUE NOT NULL,

  /* Leader of the project */
  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

CREATE INDEX project_ix_name ON project (name);

/* Basic tests
   -----------
INSERT INTO project (name) VALUES ('project1');
INSERT INTO project (name) VALUES ('project2');
*/