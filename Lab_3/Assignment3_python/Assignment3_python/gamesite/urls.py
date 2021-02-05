from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='gamesite-home'),  #if blank after localhost:8000 will respond with view home
    path('about/', views.about, name='gamesite-about'), #if /about after localhost:8000 will respond with view about
]