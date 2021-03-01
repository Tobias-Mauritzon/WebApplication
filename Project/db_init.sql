/*Run the application once before you run this sql file*/

INSERT INTO game (name, description) VALUES  ('chat','chat game a very good game where you can chat with yourself');
INSERT INTO game (name, description) VALUES  ('rotation', 'rotation game a very average game');
INSERT INTO game (name, description) VALUES  ('addition', 'Here you can experience addition like you never have before');
INSERT INTO game (name, description) VALUES  ('glider', 'Glide like you have never glidden before');
INSERT INTO game (name, description) VALUES  ('matchstick', 'Outsmart the AI in this high octane game');
INSERT INTO game (name, description) VALUES  ('runner', 'Orkar ej');

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin@mail.se', 'admin', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','ADMIN');