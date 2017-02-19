DROP TABLE IF EXISTS team_project CASCADE;

CREATE TABLE IF NOT EXISTS team_project (
  team_id int REFERENCES team(id) ON DELETE CASCADE,
  project_id int REFERENCES project(id) ON DELETE CASCADE,

  CONSTRAINT team_project_pkey PRIMARY KEY (team_id, project_id)
);