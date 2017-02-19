INSERT INTO
  person
  (first_name, last_name, email, password, is_admin)
VALUES
  ('test', 'test', 'test@test.com', '$2a$12$0j/xhnNyYh6UONZ0GjyEo.ZEGX.DvOQU1rcoTVkfM7PJN8.yiqF2u', true);

INSERT INTO
  person
  (first_name, last_name, email, password, is_admin)
VALUES
  ('test2', 'test2', 'test2@test.com', '$2a$12$0j/xhnNyYh6UONZ0GjyEo.ZEGX.DvOQU1rcoTVkfM7PJN8.yiqF2u', false);


/*
$2a$12$0j/xhnNyYh6UONZ0GjyEo.ZEGX.DvOQU1rcoTVkfM7PJN8.yiqF2u is bcrypt for "lolcode"
 */