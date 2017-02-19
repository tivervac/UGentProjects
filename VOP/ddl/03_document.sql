DROP TABLE IF EXISTS document CASCADE;

CREATE TABLE if not exists document (
  /* Unique identifier of the document */
  id serial UNIQUE PRIMARY KEY,

  /* Foreign key of the project this document is associated with */
  project_id int REFERENCES project(id) ON DELETE CASCADE,

  /* Name (fully qualified namespace) of the document */
  name text NOT NULL,

  /* Type of the document */
  type varchar(128),

  /* JSON blob for this document */
  blob text,

  UNIQUE (project_id, name)
);

CREATE INDEX document_ix_name ON document (name);
CREATE INDEX document_ix_project_name ON document (project_id, name);

/* Basic tests
   -----------
INSERT INTO document (name, project_id, type, blob) VALUES ('doc1', 1, 'actor', 'hello');
INSERT INTO document (name, project_id, type, blob) VALUES ('doc2', 2, 'usecase', 'yo');
*/