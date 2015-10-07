# HinkPink

An android app I've been working on as a side project.

The app is a two-person rhyming game. Two users can create accounts with the app
and then send rhyme puzzles to each other. They can complete the puzzles together,
submitting guesses and hints until a player solves the puzzle.

The backend is deployed on Google App Engine, which is a (free for now) scalable
service. (Check out https://cloud.google.com/appengine/docs for more info.)

As of now, the game is still in fairly early development with some limited functionality.
-Users can create accounts and log in.
-Add friends
-Start games with friends
-Offer hints/guesses

Future work includes:
-Authentication with facebook/google
-Improved UI/tweaks
-I think there are some weird server/client bugs that show up
when users on the same game are submitting guesses/hints at the same
time. I need to look into this a bit more.
-Push to google play
