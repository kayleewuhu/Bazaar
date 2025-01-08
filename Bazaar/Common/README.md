# Common
This implementation currently supports:  
determining whether a game is over;  
extracting the turn state from the referee’s game state representation;  
graphical rendering the referee’s game state;  
graphical rendering the turn state data transmitted from the referee to the player.


**Referee**: Handles main game logic, players have to go through referee to make a move, referee validates the move and if valid then performs the move  
**PlayerInfo**: Represents the information of a player, purely just for referee to keep track of data
**Board**: Represents the actual game that is going on that only the referee can see  
**GameState**: Represents certain data about the Board and Player of the overall game  
**TurnState**: Represents certain data about the Board and Player that a player needs to know to make a turn
**PebbleCollection**: Represents a container for Pebbles
**Pebble**: Represents a Pebble  
**AbstractEquation**: Represents a general type of Equation that a player can use to trade; has a left and right side  
**BidirectionalEquation**: Represents a specific type of equation that can be traded from left to right and right to left    
**UnidirectionalEquation**: Represents a specific type of equation that can only be traded from left to right  
**Card**: Represents a card piece in game that a player can buy for points
**Rule_Book**: Represents the rules of the game, which handles legality checking, scoring, 
and game termination

**MethodDeserializer**: deserializes method calls from the Referee to the player from JSON
**MethodSerializer**: serializes method calls from the Referee to the player to JSON
**ObjectJsonDeserializer**: deserializes data objects from JSON
**ObjectJsonSerializer**: serializes data objects to JSON
**ResponseDeserializer**: deserializes response rom the player to the Referee from JSON
**ResponseSerializer**: serializes response rom the player to the Referee to JSON

How to run tests  
run ./xtest in Tests directory


