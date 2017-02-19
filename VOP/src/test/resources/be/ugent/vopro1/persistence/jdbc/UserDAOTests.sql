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
