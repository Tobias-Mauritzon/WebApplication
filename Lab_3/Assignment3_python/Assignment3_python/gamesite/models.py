from django.db import models
from django.utils import timezone

# Doesnt seem to be necessary since Django has built in users with email username and password
# class User(models.Model):
#     username = models.CharField(max_length=20, primary_key=True)
#     mail = models.CharField(max_length=200, unique=True)
#     password = models.CharField(max_length=20)

class Game(models.Model):
    GameName = models.CharField(max_length=50, primary_key=True)

class Comment(models.Model):
    Id = models.IntegerField(primary_key=True)
    commentText = models.TextField()
    timeStamp = models.DateTimeField(default=timezone.now)