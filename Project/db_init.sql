/*Run the application once before you run this sql file*/

INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('chat','Matteus', 'A game where you can chat in a chatbox with yourself.', 'js/chat_script.js','Resources/chat_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('rotation','Matteus', 'A game where you rotate a cube by clicking a button.', 'js/rotation_script.js','Resources/rotation_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('addition','Matteus', 'A game where you add to a number by 1.', 'js/addition_script.js','Resources/addition_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('glider','Tobias', 'Glide like you have never glidden before', 'js/glider_script.js','Resources/glider_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('matchstick','Joachim', 'Outsmart the AI in this high octane game', 'js/matchstick_script.js','Resources/matchstick_image.png',CURRENT_TIMESTAMP);
INSERT INTO game (name, author, description, javascript, image, tstamp) VALUES  ('runner','Matteus', 'A game where you jump over boxes as a squirrel', 'js/runner_script.js','Resources/runner_image.png',CURRENT_TIMESTAMP);

INSERT INTO useraccount (mail, name, password, usergroup) VALUES  ('admin@mail.se', 'admin', 'PBKDF2WithHmacSHA256:2048:mTvEEovtLyI7wwZp1iOhufxAF0lydA2sX5V+2OOZMIk=:lqRGqAdnW9mU3fxkJXh92AgOp1qxWIwSKPEhV4AoIgY=','ADMIN');