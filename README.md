ECE 651:  Really Interesting Strategic Conquest
![pipeline](https://gitlab.oit.duke.edu/ll199/ece651-spr20-g2/badges/master/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/ll199/ece651-spr20-g2/badges/master/coverage.svg?job=test)

======================================

The code in this project is for a strategic game where players try to rule all the lands with soldiers.

The server hostname and port are stored in shared/src/main/resources/config.properties

Please change the host information as needed before starting the Gameserver program.

To run the server, type `gradle run-server` on terminal; to run a game client, type `./gradlew run-client` .

Multiple games and players are allowed in evolution 2.

## Evolution 1
Commit ID: 5b74d244

## Evolution 2
Commit ID: 9bebbe4e

## Evolution 3
Commit ID: c26dcf0b
In evolution 3, the server requires postgreSQL to run, here are the steps to install postgreSQL in the virtualbox:

1.install necessary liberaries:

**sudo apt install postgresql postgresql-contrib libpqxx-dev**

2.set up user 'postgres': (enter the following commands one by one, notice the ';' in the 3rd one)

**sudo su -postgres**
  
**psql**
  
**ALTER USER postgres with encrypted password 'abc123';**
  
**\q**
  
**exit**

3.start postgresql service:

**sudo vim /etc/postgresql/11/main/pg_hba.conf**
  
  In the file find this line:
  
**local  all  postgres  peer** 
  
  Change to:  
  
**local  all  postgres  md5**
  
  Save and exit and do:
  
**sudo service postgresql restart**

4.create a risc DB:(notice ';' in pqsl)

**psql -U postgres**
  
**CREATE DATABASE risc;**
  
**\q**

5.from now you should be able to run the server.


## Project Documentation
[Google docs](https://drive.google.com/drive/folders/1HKee59YFFU2iZOK2iDAKOgeY09z0fdPw?usp=sharing)

## Coverage
[Detailed coverage](https://ll199.pages.oit.duke.edu/ece651-spr20-g2/dashboard.html)
