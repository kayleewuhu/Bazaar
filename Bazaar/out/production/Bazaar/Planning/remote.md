**TO:** CEO of Bazaar.com  
**FROM:** Mellow Bats (KAYLEE & BAR)   
**DATE:** November 7, 2024  
**SUBJECT:** Remote Connection Protocol

Since the referee does not do the players lookup on its own, we would need a new component that will
be responsible for looking for the players, connecting with them, and passing them to the referee.
That new component is the Server.

### Phase 1: Registration (before the game)  
- Server to players: I am open for connection requests. Starting to look for players.
    This method will start the "lookup" for players, and will enable players to connect to the server
    the method will go on until 6 players have been found or a specific amount of time passes.
    The method returns a list of players and will eventually pass it to the referee.
      List<Player> startLookup();
- Player to Server: I want to connect to the game.
    this method will allow the player to connect to the server and be added to the list of players.
    void connectToGame();

- Server to Referee: After the lookup is over and the players have been gathered, the server
  hands over the list of connected players to the referee.
- Referee to Player: I am now setting up the game to start. Here is the required information.

### Phase 2: The game  
- The game is played as specified in the Logical Interactions spec. 

### Phase 3: The results  
- Referee to Player: The game has ended. You won or you lost. Here is your score.

Server protocol:
- List<Player> startLookup()
- Players can void connectToGame()
- If 6 players have been gathered OR time for lookup is up, ref starts the game if there are at least 2 players
- if there is only 1 player connected, the game won't start, or the ref creates in house players.
- After the game starts, we play the game normally and notify winners and losers normally.