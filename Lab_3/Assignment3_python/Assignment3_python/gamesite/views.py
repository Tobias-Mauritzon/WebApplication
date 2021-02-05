from django.shortcuts import render

def home(request):
    return render(request, 'gamesite/index.html')

def about(request):
    return render(request, 'gamesite/about.html')


