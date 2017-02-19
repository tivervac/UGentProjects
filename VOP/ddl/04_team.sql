DROP TABLE IF EXISTS team CASCADE;

CREATE TABLE if not exists team (
  /* Unique identifier of the team */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the team */
  name text UNIQUE NOT NULL,

  /* Leader of the team */
  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

/* Basic tests
   -----------
INSERT INTO project (name) VALUES ('project1');
INSERT INTO project (name) VALUES ('project2');
*/