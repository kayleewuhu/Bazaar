**TO:** CEO of Bazaar.com  
**FROM:** Dazzling Salamanders (KAYLEE & ERIC)   
**DATE:** October 24, 2024  
**SUBJECT:** Game Observer Mechanism

### Game Observer Interface
Below are the methods for an observer to implement:  
// notifies this observer of an update to the game state  
public void updateGameState(Game_State gameState)

These are the methods that must be implemented by the referee in order to add observers:  
// add the given game observer to the game  
public void addGameObserver(gameObserver observer)

// removes the given game observer from the game    
public void removeGameObserver(gameObserver observer)  

// notifies the game observers of the updated game state  
public void notifyGameObservers(Game_State gameState)

### Referee and Game Observer interactions
- The Referee contains a list of observers who want to observe the game.
- The Referee adds an observer to the game.
- Whenever the Referee updates the game state, the Referee also calls the notifyGameObservers
method to notify the observers of the updated game state (a copy, so the actual game state cannot be affected).
  - The game observer gets to choose what to do with that information.
- If any of the game observers act badly or attempt to disrupt the game, the Referee will remove that observer.

The Referee controls the flow of the game and the interactions with all actors, so naturally it also 
handles observer interactions. A single person may wish to implement the observer interface in order
to render and view the game, make bets or predictions on the game's outcome, react to the 
game's updates, etc.
