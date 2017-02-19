DROP TABLE if EXISTS person CASCADE;

CREATE TABLE if not exists person (   /* user is a reserved word and should not be used as a relation name */
  id serial UNIQUE PRIMARY KEY,

  first_name text NOT NULL,
  last_name text NOT NULL,
  email text UNIQUE NOT NULL,

  password text NOT NULL,

  is_admin boolean NOT NULL DEFAULT false
);

INSERT INTO person (first_name, last_name, email, password, is_admin) VALUES ('first', 'last', 'me@example.com', 'paswoord', true);
INSERT INTO person (first_name, last_name, email, password, is_admin) VALUES ('first2', 'last2', 'me2@example.com', 'paswoord2', false);
INSERT INTO person (first_name, last_name, email, password, is_admin) VALUES ('first3', 'last3', 'me3@example.com', 'paswoord3', true);

DROP TABLE if exists project CASCADE;

CREATE TABLE if not exists project (
/* Unique identifier of the project */
  id serial UNIQUE PRIMARY KEY,

/* Name of the project */
  name text UNIQUE NOT NULL,

  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

INSERT INTO project (name, leader_id) VALUES ('project1', 1);
INSERT INTO project (name, leader_id) VALUES ('project2', 1);

DROP TABLE IF EXISTS project_analyst CASCADE;

CREATE TABLE project_analyst (
  project_id int REFERENCES project(id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,
  workhours bigint,

  CONSTRAINT project_analyst_pkey PRIMARY KEY (project_id, person_id)
);

INSERT INTO project_analyst (project_id, person_id) VALUES (1, 2);
INSERT INTO project_analyst (project_id, person_id) VALUES (1, 3);

DROP TABLE if EXISTS team CASCADE;

CREATE TABLE if not exists team (
  /* Unique identifier of the team */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the team */
  name text UNIQUE NOT NULL,

  /* Leader of the team */
  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

INSERT INTO team (name, leader_id) VALUES ('team1', 1);
INSERT INTO team (name, leader_id) VALUES ('team2', 1);
INSERT INTO team (name, leader_id) VALUES ('team3', 1);

DROP TABLE IF EXISTS team_project CASCADE;

CREATE TABLE team_project (
  team_id int REFERENCES team(id) ON DELETE CASCADE,
  project_id int REFERENCES project(id) ON DELETE CASCADE,

  CONSTRAINT team_project_pkey PRIMARY KEY (team_id, project_id)
);

INSERT INTO team_project (team_id, project_id) VALUES (1, 1);
INSERT INTO team_project (team_id, project_id) VALUES (1, 2);
INSERT INTO team_project (team_id, project_id) VALUES (2, 1);

