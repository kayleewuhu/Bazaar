**TO:** CEO of Bazaar.com  
**FROM:** Dazzling Salamanders (KAYLEE & ERIC)   
**DATE:** October 17, 2024  
**SUBJECT:** Referee and Game State interaction

### Game State Interface
Below are the methods necessary for the game state that the referee can use to dictate the game.

// sets up the game and initializes the main components (will only be called in the constructor)
private void setup()

// performs the draw pebble or exchanges action and returns the new resulting game state
public GameState performDrawPebbleOrExchanges(DrawPebbleOrExchanges)

// performs the card purchases action and returns the new resulting game state
public GameState performCardPurchases(CardPurchases)

// removes the active player from the game state (banned) and returns a new game state
public GameState removeActivePlayer()

// ends the previous player's turn by making the next player active and returning a new game state
public GameState endTurn()

// returns the turn state for this game state
public Turn_State getTurnState()

// is the game over in this game state
public boolean isGameOver()

### Referee and Game State interaction protocol
1) Referee creates initial game state (set up). The ref informs the game state of the 
equations, cards, players, and bank.

***++++++++++ Loop until game over (begin)***
2) Referee asks game state for the turn state information to send to the active player.
3) After Referee sends turn state to player and player responds with drawing a pebble or pebble
exchanges: 
- Referee checks if move is legal and updates game state accordingly:
  - If move is legal:
    - Update active player's pebbles
    - Update bank's pebbles
    - Remove card from deck (if pebble exchanges)
  - If move is illegal:
    - Remove active player from game state
4) Referee informs game state of new values. 
5) Referee asks game state for the new turn state information to send to the active player. 
6) After Referee sends turn state to player and player responds with card purchases:
- Referee checks if card purchases is legal and updates game state accordingly:
  - If move is legal:
      - Update active player's pebbles
      - Update bank's pebbles
      - Update active player's points
      - Replenish visible cards
  - If move is illegal:
    - Remove active player from game state 
7) Referee informs game state of new values.
8) Referee asks game state if the game is over.
9) Game state returns response to referee determining if the game is over or not. 
- If game is over, end loop.
- If game is not over:
  - Referee changes the turn to the next player.
  - Referee notifies the game state of this change.
10) Referee asks game state to return new turn state.
***++++++++++ Loop until game over (end)***
11) Referee gets game state for information regarding winner and losers.