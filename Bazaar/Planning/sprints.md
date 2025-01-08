# MEMORANDUM

**TO:** CEO of Bazaar.com  
**FROM:** Mellow Chameleons!!!! (Ryan Tsai and Eric Sun)  
**DATE:** September 12, 2024  
**SUBJECT:** Bazaar Sprint Planning

### Sprint 1: Make Game Logic
First, set up all the data representations (interfaces/classes) and all of their methods. Then implement the game logic/rules. Finally put them all together to make the game run. This is where the *referee* will be implemented. Throughout this sprint, write unit tests for each method that we make.

### Sprint 2: Make Server/Client Communication and Player
Create the server and the communication logic. Write the interfaces for the server and the client to allow them to communicate. Also create the AI *player* and write integrations tests. At the end, there will be a *player-referee interface*. This player will allow us to test the game logic from Sprint 1 from the start of the game to the end of the game. 

### Sprint 3: Make Game Interface
First, create a basic UI and work on getting it connected to the controller by using the observer pattern. After, set up a way for *observers* to watch the game. Now, instead of just testing via code/through the terminal, we can use the UI to see if the game/player from Sprint 1 & 2 is behaving the way we expect.



