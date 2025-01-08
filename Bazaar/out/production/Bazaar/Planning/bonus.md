**TO:** CEO of Bazaar.com  
**FROM:** Mellow Bats (KAYLEE & BAR)   
**DATE:** November 21, 2024  
**SUBJECT:** Proposed Changes

### Bonus points at end of game
To implement the change where the referee will award players 10 points at the end of the game for 
having bought cards with specific pebbles, the following changes will need to be made:
- The referee's knowledge of each player (PlayerInfo) will need to include the cards that each 
player has purchased.
  - This class (PlayerInfo) will also include a method that checks through all the player's bought
    cards to determine if the player has bought a card with a red pebble, a card with a white pebble,
    and a card with a blue pebble. If so, 10 points will be added to the player's score.
    - This method will be called by the referee for every single player after the game is determined
    to be over, but before the winners and losers are calculated and notified.
      - This will occur in the Referee's runGame method.

In short, only the Referee's runGame method and the referee's knowledge of the player (PlayerInfo) 
will require additions. Both of these files (Referee.java and PlayerInfo.java) are in the Referee
folder/component.

### Glowing pebble
To implement the change where the referee will award players bonus points at the end of the game
for having purchased cards with glowing pebbles, the following changes will need to be made:
- The PebbleColor.java enum file in the Common folder will have to distinguish between regular pebbles
  and glowing pebbles. We can add a new field to the enum, a boolean called "isGlowing".
- The Card.Java will also need to handle glowing pebbles, the class will need to check if a card contains
  a glowing pebble. The code will also need to construct cards that have up to one glowing pebble, up
  to a max of N cards with glowing pebble that are allowed to exist in a game as specified in the rules.
- The referee's knowledge of each player (PlayerInfo) will need to include the cards that each
    player has purchased.
  - This class (PlayerInfo) will also include a method that checks through all the player's bought
    cards to determine if the player has bought three cards with glowing pebbles.
    If so, 10 points will be added to the player's score.
    - This method will be called by the referee for every single player after the game is determined
      to be over, but before the winners and losers are calculated and notified.
      - This will occur in the Referee's runGame method.

In short, the code will now have a pebble that can be glowing. The code will be able to construct a card
that has a glowing pebble and check if a card has a glowing pebble. The GameState will now be initialized
with up to N cards that have a glowing pebble cost to them. At the end of the game, the referee will
go over the PlayerInfo of players and check if their purchased cards contain at least 3 cards that contain
a glowing pebble, and then will award the bonus before announcing winners.