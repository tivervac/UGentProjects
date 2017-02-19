DROP TABLE if exists project CASCADE;

CREATE TABLE if not exists project (
  /* Unique identifier of the project */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the project */
  name text UNIQUE NOT NULL,

  /* Leader of the team */
  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

INSERT INTO project (name, leader_id) VALUES ('testproject', 1);
INSERT INTO project (name, leader_id) VALUES ('testproject2', 1);