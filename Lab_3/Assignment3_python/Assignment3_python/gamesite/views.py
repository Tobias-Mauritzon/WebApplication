from django.shortcuts import render
from django.contrib.auth.forms import UserCreationForm

def home(request):
    return render(request, 'gamesite/index.html')

def about(request):
    return render(request, 'gamesite/about.html')



