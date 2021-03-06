package roulette;

import util.ConsoleReader;


/**
 * Plays a game of roulette.
 * 
 * @author Robert C. Duvall
 */
public class Game {
    // name of the game
    private static final String DEFAULT_NAME = "Roulette";
    // bets player can make
    private Bet[] myPossibleBets = { 
        new Bet("Red or Black", 1),
        new Bet("Odd or Even", 1),
        new Bet("Three in a Row", 11)
    };
    private Wheel myWheel;

    /**
     * Construct the game.
     */
    public Game () {
        myWheel = new Wheel();
    }

    /**
     * @return name of the game
     */
    public String getName () {
        return DEFAULT_NAME;
    }

    /**
     * Play a round of roulette.
     *
     * Prompt player to make a bet, then spin the roulette wheel, and then verify 
     * that the bet is won or lost.
     *
     * @param player one that wants to play a round of the game
     */
    public void play (Gambler player) {
        int amount = ConsoleReader.promptRange("How much do you want to bet",
                                               0, player.getBankroll());
        int whichBet = promptForBet();
        String betChoice = placeBet(whichBet);

        System.out.print("Spinning ...");
        myWheel.spin();
        System.out.println(String.format("Dropped into %s %d", myWheel.getColor(), myWheel.getNumber()));
        if (betIsMade(whichBet, betChoice)) {
            System.out.println("*** Congratulations :) You win ***");
            amount *= myPossibleBets[whichBet].getOdds();
        }
        else {
            System.out.println("*** Sorry :( You lose ***");
            amount *= -1;
        }
        player.updateBankroll(amount);
    }

    /**
     * Prompt the user to make a bet from a menu of choices.
     */
    private int promptForBet () {
        System.out.println("You can make one of the following types of bets:");
        for (int k = 0; k < myPossibleBets.length; k++) {
            System.out.println(String.format("%d) %s", (k + 1), myPossibleBets[k].getDescription()));
        }
        return ConsoleReader.promptRange("Please make a choice", 1, myPossibleBets.length) - 1;
    }

    /**
     * Place the given bet by prompting user for specific information need to complete that bet.
     *
     * @param whichBet specific bet chosen by the user
     */
    private String placeBet (int whichBet) {
        String result = "";
        if (whichBet == 0) {
            result = ConsoleReader.promptOneOf("Please bet", Wheel.BLACK, Wheel.RED);
        }
        else if (whichBet == 1) {
            result = ConsoleReader.promptOneOf("Please bet", "even", "odd");
        }
        else if (whichBet == 2) {
            result = "" + ConsoleReader.promptRange("Enter first of three consecutive numbers",
                                                    1, Wheel.NUM_SPOTS - 3);
        }
        System.out.println();
        return result;
    }

    /**
     * Checks if the given bet is won or lost given user's choice and result of spinning the wheel.
     *
     * @param whichBet specific bet chosen by the user
     * @param betChoice specific value user chose to try to win the bet
     */
    private boolean betIsMade (int whichBet, String betChoice) {
        if (whichBet == 0) {
            return myWheel.getColor().equals(betChoice);
        }
        else if (whichBet == 1) {
            return (myWheel.getNumber() % 2 == 0 && betChoice.equals("even")) ||
                   (myWheel.getNumber() % 2 == 1 && betChoice.equals("odd"));
        }
        else if (whichBet == 2) {
            int start = Integer.parseInt(betChoice);
            return (start <= myWheel.getNumber() && myWheel.getNumber() < start + 3);
        }
        else {
            return false;
        }
    }
}
