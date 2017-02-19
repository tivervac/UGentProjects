DROP TABLE IF EXISTS team CASCADE;

CREATE TABLE IF NOT EXISTS team (
  /* Unique identifier of the team */
  id serial UNIQUE PRIMARY KEY,

  /* Name of the team */
  name text UNIQUE NOT NULL,


  /* Leader of the team */
  leader_id int REFERENCES person(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS team_member CASCADE;

CREATE TABLE IF NOT EXISTS team_member (
  team_id int REFERENCES team(id) ON DELETE CASCADE,
  person_id int REFERENCES person(id) ON DELETE CASCADE,

  CONSTRAINT team_member_pkey PRIMARY KEY (team_id, person_id)
);