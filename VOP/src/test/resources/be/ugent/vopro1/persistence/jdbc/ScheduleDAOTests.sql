DROP TABLE if EXISTS person CASCADE;
DROP TABLE if EXISTS team CASCADE;
DROP TABLE if EXISTS team_member CASCADE;

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

CREATE TABLE if not exists team (
  /* Unique identifier of the team */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the team */
  name text UNIQUE NOT NULL,

  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

CREATE TABLE team_member (
  team_id int REFERENCES team(id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,

  CONSTRAINT team_member_pkey PRIMARY KEY (team_id, person_id)
);

INSERT INTO team (name, leader_id) VALUES ('team1', 1);
INSERT INTO team (name, leader_id) VALUES ('team2', 1);

INSERT INTO team_member (team_id, person_id) VALUES (1, 1);
INSERT INTO team_member (team_id, person_id) VALUES (1, 2);
INSERT INTO team_member (team_id, person_id) VALUES (2, 1);

/**
 This SQL is executed by Spring itself when the EntityDAOTests are started, and will be reverted when the tests have
 completed.
 */

DROP TABLE if exists document CASCADE;
DROP TABLE if exists project CASCADE;
DROP TABLE IF EXISTS person CASCADE;

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

CREATE TABLE if not exists project (
  /* Unique identifier of the project */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the project */
  name text UNIQUE NOT NULL,

  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

CREATE TABLE if not exists document (
  /* Unique identifier of the document */
  id serial UNIQUE PRIMARY KEY,

  /* Foreign key of the project this document is associated with */
  project_id int REFERENCES project(id),

  /* Name (fully qualified namespace) of the document */
  name text UNIQUE NOT NULL,

  /* Type of the document */
  type varchar(128),

  /* JSON blob for this document */
  blob text
);

INSERT INTO project (name, leader_id) VALUES ('project1', 1);
INSERT INTO project (name, leader_id) VALUES ('project2', 1);

INSERT INTO document (name, project_id, type, blob) VALUES ('doc2', 2, 'usecase', '{type: "usecase", name: "myusecase"}');
INSERT INTO document (name, project_id, type, blob) VALUES ('doc3', 2, 'usecase', '{type: "usecase", name: "myusecase"}');
INSERT INTO document (name, project_id, type, blob) VALUES ('doc4', 2, 'usecase', '{type: "usecase", name: "myusecase"}');

DROP TABLE IF EXISTS task CASCADE;

CREATE TABLE task (
  document_id int REFERENCES document(id) ON DELETE CASCADE PRIMARY KEY,
  priority integer,
  workload bigint
);

INSERT INTO task (document_id, priority, workload) VALUES (1, 10, 100);
INSERT INTO task (document_id, priority, workload) VALUES (2, 20, 200);

DROP TABLE IF EXISTS assignment CASCADE;

CREATE TABLE assignment (
  task_id int REFERENCES task(document_id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,
  project_id int REFERENCES project(id),
  start_date timestamp WITH TIME ZONE,
  end_date timestamp WITH TIME ZONE,


  CONSTRAINT assignment_pkey PRIMARY KEY (task_id, start_date)
);

INSERT INTO assignment (task_id, person_id, project_id, start_date, end_date) VALUES (1, 1, 1, now(), now());
INSERT INTO assignment (task_id, person_id, project_id, start_date, end_date) VALUES (2, 2, 1, now(), now());
/* INSERT INTO assignment (task_id, person_id, project_id, start_date, end_date) VALUES (1, 3, 1, now(), now()); */