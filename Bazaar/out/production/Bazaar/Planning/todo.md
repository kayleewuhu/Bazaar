### To-do: Priorities to fix
### Completed fixes
1. Fix/Find the problem with strategy/referee/game state. (Unit tests are failing.)
   - Remove duplicate paths (when duplicate equations/cards exist) - ***a9d0b150f62cf946e9bc4a0555ba01b284f7169f "ensures no duplicate candidates in strategies"***
   - Test game with player that does nothing and player that maximizes incorrectly produces the
     result that both players win with 0 points each. - ***No issues found, after further testing (with rendering), we discovered
     the discrepancy was due to the random generation of the game components and the players' choices to do nothing in a forever. After
     our next fix (drawing a pebble deterministically), we will update the test to remove randomness.***
2. Provide the option to draw a pebble deterministically. - ***5db0d2e107f61435be55bfbfa167d32874f0c84f "functionality to draw a pebble deterministically"***
3. Remove all mutation methods. - ***e61f90d0a4755e4b132e5e25f41a647e1e4e3b15 "rewrote mutator methods to return new objects"***
4. Inform players in the game if they have won or lost when a game is over. - ***5fd77abfe60a83349ae8d7b5b061e2dd89938fd4 "notify players of win or loss"***
5. Handle exceptions thrown by the players when their methods are called. - ***6ed5e3ed6db4e357771bb15f816233ead84ffd23 "handle exceptions thrown by players"***
6. Factor out constant values. - ***57843a6349a2025a5cddee0af6ca068cad72b444 "create constants for numerical values"***
7. Remove the equations that are used in the game from the constants. - ***091fb542da36c51c8f49fd903dc040e73f550d82 "remove equations from constants class"*** 
8. Ensures there is the proper number of players in a game. - ***01efc6f8536c415f0f9ad94e114c9f9895b0a8db "ensures the game has the right number of players"***
9. Break down large functions into calls to helper functions. - ***fb9f8abb03984ceb630aba961da533024335147a "split large functions to helpers"***
10. Add complete unit test coverage for all public methods. - ***169dcdb347cef8bd7662c813bc5e70f42a4df0c3 "add unit tests"***