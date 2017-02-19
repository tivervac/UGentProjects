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

INSERT INTO document (name, project_id, type, blob) VALUES ('doc1', 1, 'actor', '{type: "actor", name: "sysadmin"}');
INSERT INTO document (name, project_id, type, blob) VALUES ('doc2', 2, 'usecase', '{type: "usecase", name: "myusecase"}');
INSERT INTO document (name, project_id, type, blob) VALUES ('doc3', 2, 'concept', '{type: "concept", name: "myconcept"}');