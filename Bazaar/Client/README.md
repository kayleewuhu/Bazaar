# Client
This folder contains all files corresponding to a client of the game. A client will connect a player
to the game that is run on the server.

## Overview
**Client**: starts up on the client/player machine, attempts to connect to the server and register
a player to play the game, and handles shut down on the client machine
**Referee**: serves as a proxy referee for the client, acting similarly to the referee on the server
in that it asks the player on the client machine for its responses and communicates with the 
referee on the server
**XClient**: serves as the client side for the integration test for the server client components.
the script launches a number of clients and connects them to a running server by consuming Json input.


![My Image](../Common/Assets/Remote-protocol.jpeg)

How to run tests  
run ./xtest in Tests directory


