/*Run the application once before you run this sql file*/

INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('chat','Matteus', 'A game where you can chat in a chatbox with yourself.', 'js/chat_script.js','Resources/chat_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('rotation','Matteus', 'A game where you rotate a cube by clicking a button.', 'js/rotation_script.js','Resources/rotation_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('addition','Matteus', 'A game where you add to a number by 1.', 'js/addition_script.js','Resources/addition_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('glider','Tobias', 'Glide like you have never glidden before', 'js/glider_script.js','Resources/glider_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('matchstick','Joachim', 'Outsmart the AI in this high octane game', 'js/matchstick_script.js','Resources/matchstick_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('runner','Matteus', 'A game where you jump over boxes as a squirrel', 'js/runner_script.js','Resources/runner_image.png',CURRENT_TIMESTAMP);

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin@mail.se', 'admin', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','ADMIN');

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin1@mail.se', 'petter', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin2@mail.se', 'greta', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin3@mail.se', 'JerryTheGamer', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin4@mail.se', 'panko', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin5@mail.se', 'fisken', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin6@mail.se', 'kEk', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin7@mail.se', 'kurTan', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin8@mail.se', 'Perry', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin9@mail.se', 'Megg', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','USER');
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (3, 'runner', 'admin@mail.se', 0);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (8, 'runner', 'admin1@mail.se', 1);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (1, 'runner', 'admin2@mail.se', 2);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (4, 'runner', 'admin3@mail.se', 3);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (6, 'runner', 'admin4@mail.se', 4);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (3, 'runner', 'admin5@mail.se', 5);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (5, 'runner', 'admin6@mail.se', 6);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (3, 'runner', 'admin7@mail.se', 7);
INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (9, 'runner', 'admin8@mail.se', 8);
-- INSERT INTO highscore (highscore, game_name, userAccount_mail, id) VALUES (2, 'runner', 'admin9@mail.se', 9);
