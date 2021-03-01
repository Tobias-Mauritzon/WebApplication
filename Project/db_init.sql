/*Run the application once before you run this sql file*/

INSERT INTO game (name, description, javascript) VALUES  ('chat','chat game a very good game where you can chat with yourself', 'js/chat_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('rotation', 'rotation game a very average game', 'js/rotation_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('addition', 'Here you can experience addition like you never have before', 'js/addition_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('glider', 'Glide like you have never glidden before', 'js/glider_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('matchstick', 'Outsmart the AI in this high octane game', 'js/matchstick_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('runner', 'Orkar ej', 'js/runner_script.js');

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin@mail.se', 'admin', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','ADMIN');