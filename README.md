<img src="https://github.com/Tobias-Mauritzon/WebApplication/blob/project/Project/src/main/webapp/Resources/weblogo2.svg" alt="logo" width="100" height="100" align="right"/>
  
# WebApplication

This is the Repository for Group 10 in the Chalmers course DAT076/DIT126 Web applications.
The Repo includes the Labs and the project for the course.

The Project is a web-app that is a social game platform called Awesome Games. You can play, rate, comment and score in games. The site includes a number of games like the fun Runner game and challenging Flappy game. It also features an Account system where you create, login and use a user account.

## Table of contents
* [Documentation](#documentation)
* [Getting Started](#getting-started)
* [Prerequisites](#prerequisites)
* [Installing](#installing)
* [Built With](#built-with)
* [Authors](#authors)
* [License](#license)
* [Acknowledgments](#acknowledgments)

## Documentation
```
The Labs from the course are in their corresponding folders.
The Project called Awesome Games is inside the project folder.
```

## Getting Started

### Prerequisites

What things you need to have to install the app

NetBeans [12.2](https://netbeans.apache.org/download/nb122/nb122.html)<br>
Payara Server [5.194](https://docs.payara.fish/community/docs/5.194/documentation/ecosystem/netbeans-plugin/payara-server.html)<br>
Derby Database [10.14.2.0](https://db.apache.org/derby/releases/release-10_14_2_0.cgi)<br>
Java JDK [11](https://jdk.java.net/archive/)

### Installing

How to build and install the project from the source code.

```
Download or clone the Repository
Open the Project folder with NetBeans
In NetBeans go to Services, Databases, right click "New Connection", have Java DB (Embedded) selected and add your Derby db.
Right click Java DB under Databases and choose "Create Database", name it AwesomeGamesDB and username "user1" and password "pass".
Do it agin but this time call it AwesomeGamesTestDB with the same username password.
Then build the project.
After that run db_init.sql(file is inside the project folder) on the lab4 database to initialize it.
Congrats you have just installed the project!
```

## Built With

* [Java](https://www.oracle.com/java/technologies/javase-downloads.html) - Programing Language for the backend
* [Bootstrap](https://getbootstrap.com/) - Used for layout and styling
* [Primefaces](https://www.primefaces.org/) - Used for its components and theming
* [JSF](https://en.wikipedia.org/wiki/Jakarta_Server_Faces) - Java component-based user interfaces
* [Arquillian](https://arquillian.org/) - Testing library
* [Javascript](https://en.wikipedia.org/wiki/JavaScript) - Programing Language for the games
* [Java DB & Derby](https://db.apache.org/derby/) - Database
* [Github](https://github.com/) - Version control and code sharing
* [CSS](https://en.wikipedia.org/wiki/CSS) - Styling Language
* [Lombok](https://projectlombok.org/) - Library that creates getters, setters and helps with storing data
* [Maven](https://maven.apache.org/) - Library/package manager
* [Jquery](https://jquery.com/) - Javascript library
* [Jacoco](https://www.jacoco.org/jacoco/) - Code coverage reporting
* [Mockito](https://site.mockito.org/) - Testing library


## Authors

* **David Anderson** - *Group Member* - [lerbyn](https://github.com/lerbyn)
* **Matteus Straznyk** - *Group Member* - [Game9954](https://github.com/Game9954)
* **Tobias Mauritzon** - *Group Member* - [Tobias-Mauritzon](https://github.com/Tobias-Mauritzon)
* **William Jönsson** - *Group Member* - [SlaughterBrute](https://github.com/SlaughterBrute)
* **Simon Terling** - *Group Member* - [SimonTer](https://github.com/SimonTer)
* **Joachim Antfolk** - *Group Member* - [joantfolk](https://github.com/joantfolk)


See also the list of [contributors](https://github.com/Tobias-Mauritzon/WebApplication/graphs/contributors) who participated in this project.
Because whole libraries and packages where pushed to this Repo the contribution screen is not an accurate show of contribution.

## License

none yet

## Acknowledgments

Matthías Páll Gissurarson as our project supervisor.

