/*Run the application once before you run this sql file*/

INSERT INTO game (name, description, javascript) VALUES  ('chat','chat game a very good game where you can chat with yourself', 'js/chat_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('rotation', 'rotation game a very average game', 'js/rotation_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('addition', 'Here you can experience addition like you never have before', 'js/addition_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('glider', 'Glide like you have never glidden before', 'js/glider_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('matchstick', 'Outsmart the AI in this high octane game', 'js/matchstick_script.js');
INSERT INTO game (name, description, javascript) VALUES  ('runner', 'Orkar ej', 'js/runner_script.js');

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin@mail.se', 'admin', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','ADMIN');

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin1@mail.se', 'admin1', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin2@mail.se', 'admin2', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin3@mail.se', 'admin3', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin4@mail.se', 'admin4', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin5@mail.se', 'admin5', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin6@mail.se', 'admin6', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (3, 'runner', 'admin@mail.se', 0);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (8, 'runner', 'admin1@mail.se', 1);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (1, 'runner', 'admin2@mail.se', 2);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (4, 'runner', 'admin3@mail.se', 3);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (6, 'runner', 'admin4@mail.se', 4);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (3, 'runner', 'admin5@mail.se', 5);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (5, 'runner', 'admin5@mail.se', 6);
