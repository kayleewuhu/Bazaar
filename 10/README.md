## Bazaar: Milestone 10 - Revised!

### Purpose:
This directory allows the user to run integration tests for milestone 9, Remote, for
the game/project Bazaar. 

### Overview:
- Other folder:
  - setup: shell script that downloads all necessary dependencies
- Makefile: 
  - compile Bazaar files and create jar
- xclients: 
  - shell script to run the clients (run by calling ./xclients)
- xserver:
  - shell script to run the server (run by calling ./xserver)

### To Run:
Type the following in the terminal:
- make command
- start up server: ./xserver [port number] 
  - takes in 3 json objects:
    - Equations
    - Game state
    - Optional bonus mode
- start up client: ./xclients [port number]
  - takes in 1 json object: list of actors
- Enter test input
