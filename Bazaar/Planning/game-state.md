# MEMORANDUM

**TO:** CEO of Bazaar.com  
**FROM:** Mellow Chameleons!!!! (Ryan Tsai and Eric Sun)  
**DATE:** September 25, 2024  
**SUBJECT:** Game State Planning

# Game State Design
We will have a Board class that contains all the needed info (Pebbles, Cards, and Equations) that the referee can use to control the flow of the game. The Referee is able to modify the info as well, such as removing a Card, or adding more pebbles into the pebble bank. Only the Referee has an instance of the Board object, so only the Referee can access it and modify fields. If any Players need info or wants to make a move, they must go through the Referee.



