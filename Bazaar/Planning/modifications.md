### Modifications for bonus points
- Edit ***Rule_Book*** (in Common):
  - add calculateBonus method
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Common/Rule_Book.java#L104-L107
- Create ***IGameMode*** interface (in Common):
  https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Common/IGameMode.java#L1-L9
  - Create ***USAGameMode*** class (in Common)
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Common/USAGameMode.java#L10-L12
  - Create ***SEYGameMode*** class (in Common)
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Common/SEYGameMode.java#L10-L12
  - Create ***DefaultGameMode*** class (in Common)
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Common/DefaultGameMode.java#L7-L8
- edit ***Game_State*** (in Referee):
  - edit performCardPurchases method
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/Game_State.java#L106-L108
  - add calculateFinalScores method
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/Game_State.java#L194-L196
- Edit ***PlayerInfo*** (in Referee):
  - add cardsPurchases field
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/PlayerInfo.java#L16-L26
  - add addCardsPurchased method
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/PlayerInfo.java#L100-L105
  - add getPurchasedCards method
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/PlayerInfo.java#L107-L110
- Edit ***Referee*** (in Referee):
  - add gameMode field
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/Referee.java#L30-L42
  - edit notifyWinnersAndLosers method
    https://github.khoury.northeastern.edu/CS4500-F24/mellow-bats/blob/0e8c3994cd916caf193510f3daa4c11ef27bfd80/Bazaar/Referee/Referee.java#L201-L203