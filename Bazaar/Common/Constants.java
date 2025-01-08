package Common;

// Represents constants for the game
public final class Constants {
  // Private constructor to prevent instantiation
  private Constants() {
    throw new UnsupportedOperationException("Cannot instantiate Constants class");
  }

  public static final int MIN_NUM_PLAYERS = 2;
  public static final int MAX_NUM_PLAYERS = 6;

  public static final int MIN_PEBBLES_PER_EQUATION_SIDE = 1;
  public static final int MAX_PEBBLES_PER_EQUATION_SIDE = 4;
  public static final int NUM_EQUATIONS = 10;

  public static final int NUM_PEBBLES_ON_CARD = 5;
  public static final int NUM_CARDS = 20;
  public static final int NUM_VISIBLE_CARDS = 4;
  public static final int STARTING_BANK_NUM_PEBBLES_PER_COLOR = 20;

  public static final int POINTS_TO_WIN = 20;

  public static final int POINTS_FROM_PLAIN_CARD_AND_3_OR_MORE_PEBBLES_LEFT = 1;
  public static final int POINTS_FROM_PLAIN_CARD_AND_2_LEFT = 2;
  public static final int POINTS_FROM_PLAIN_CARD_AND_1_LEFT = 3;
  public static final int POINTS_FROM_PLAIN_CARD_AND_0_LEFT = 5;
  public static final int POINTS_FROM_SMILEY_CARD_AND_3_OR_MORE_PEBBLES_LEFT = 2;
  public static final int POINTS_FROM_SMILEY_CARD_AND_2_LEFT = 3;
  public static final int POINTS_FROM_SMILEY_CARD_AND_1_LEFT = 5;
  public static final int POINTS_FROM_SMILEY_CARD_AND_0_LEFT = 8;

  public static final int DEFAULT_BONUS_POINTS = 0;
  public static final int USA_BONUS_POINTS = 10;
  public static final int SEY_BONUS_POINTS = 50;

  public static final int MOVE_TIMEOUT_SEC = 4;
  public static final int SIGN_UP_WAIT_SEC = 20;
  public static final int SIGN_UP_WAIT_PERIOD_TIMEOUT = 2;
  public static final int NAME_WAIT_SEC = 3;
  public static final int CLIENT_RETRY_FOR_SERVER_SEC = 5;
  public static final int CLIENT_CONNECT_MAX_RETRY_ATTEMPTS = 10;
}
