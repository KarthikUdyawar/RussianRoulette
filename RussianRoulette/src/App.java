import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a Russian Roulette game where a player and a virtual dealer take turns shooting
 * themselves or each other with a revolver containing one live bullet and several blanks. The game continues
 * until one of them loses all their lives.
 */
public class RussianRouletteV4 {
    // Constants for initial player and dealer lives, live shell range, and score multiplier
    private static final int INITIAL_LIFE = 4;
    private static final int LIVE_SHELL_MIN = 2;
    private static final int LIVE_SHELL_MAX = 7;
    private static final int SCORE_MULTIPLIER = 100000;

    /**
     * Enum representing the two types of revolver shells: live shell and blank shell.
     */
    private enum ShellType {
        LIVE_SHELL, BLANK_SHELL
    }

    /**
     * Class representing the Russian Roulette game.
     */
    private static class Game {
        private final Random random = new Random();
        private final Stack<ShellType> chamber = new Stack<>();
        private int playerLife = INITIAL_LIFE;
        private int dealerLife = INITIAL_LIFE;
        private int roundNum = 0;
        private int score = 0;
        private boolean isGameOver = false;
        private boolean isPlayerTurn = true;
        private boolean godMode = false;
        private final Scanner scanner;

        /**
         * Constructs a new Game instance.
         */
        public Game() {
            this.scanner = new Scanner(System.in);
        }

        /**
         * Starts the game and continues until it is over.
         *
         * @throws InterruptedException if the game is interrupted
         */
        public void play() throws InterruptedException {
            setupGame(scanner);

            while (!isGameOver) {
                playRound(scanner);
            }
            scanner.close();
        }

        /**
         * Sets up the game by getting the player's name and signature.
         *
         * @param scanner the scanner object to read input
         */
        private void setupGame(Scanner scanner) {
            System.out.print("Enter your first name: ");
            String name = scanner.nextLine().toLowerCase().trim();
            System.out.println();
            System.out.print("Your signature " + name + ", if you want to participate in the game, " +
                    "knowing that you can die: ");
            String waiverSigned = scanner.nextLine().toLowerCase().trim();
            System.out.println();

            if (!name.equals(waiverSigned)) {
                System.out.println("Your sign doesn't match your name");
                throw new IllegalArgumentException("Name and signature don't match.");
            }

            if (name.equals("god")) {
                godMode = true;
            }

            System.out.println("Game has begun\n");
        }

        /**
         * Plays a round of the game.
         *
         * @param scanner the scanner object to read input
         * @throws InterruptedException if the game is interrupted
         */
        private void playRound(Scanner scanner) throws InterruptedException {
            if (chamber.isEmpty()) {
                roundNum++;
                System.out.println("\nRound " + roundNum);
                int numLiveShells = random.nextInt(LIVE_SHELL_MAX - LIVE_SHELL_MIN + 1) + LIVE_SHELL_MIN;
                int numBlankShells = 8 - numLiveShells;
                System.out.println("Live Shell(s): " + numLiveShells + ", Blank Shell(s): " + numBlankShells);
                TimeUnit.SECONDS.sleep(5);
                System.out.println("Reload Gun\n");
                reloadGun(numLiveShells, numBlankShells);
            }

            System.out.println("\nYour health: " + playerLife + " Dealer health: " + dealerLife + "\n");

            if (godMode) {
                System.out.println(chamber + "\n"); // ! God mode
            }

            String trigger = isPlayerTurn ? getPlayerTrigger(scanner) : getDealerTrigger();

            ShellType currentShell = chamber.pop();

            processShot(trigger, currentShell);

            checkGameOver(scanner);
        }

        /**
         * Gets the player's trigger choice.
         *
         * @param scanner the scanner object to read input
         * @return the player's trigger choice
         */
        private String getPlayerTrigger(Scanner scanner) {
            while (true) {
                System.out.println("\nYour turn");
                System.out.print("To shoot yourself, use key 's' or to shoot the dealer use key 'd': ");
                String trigger = scanner.nextLine().toLowerCase().trim();

                // Validate the input
                if (Set.of("s", "d").contains(trigger)) {
                    return trigger;
                } else {
                    System.out.println(
                            "Invalid input. Please enter 's' to shoot yourself or 'd' to shoot the dealer.");
                }
            }
        }

        /**
         * Generates the dealer's trigger choice randomly.
         *
         * @return the dealer's trigger choice
         */
        private String getDealerTrigger() {
            System.out.println("\nDealer turn");
            return random.nextBoolean() ? "s" : "d";
        }

        /**
         * Processes the shot based on the trigger choice and the current shell.
         *
         * @param trigger      the trigger choice ('s' or 'd')
         * @param currentShell the current shell type
         * @throws InterruptedException if the game is interrupted
         */
        private void processShot(String trigger, ShellType currentShell) throws InterruptedException {
            System.out.println("Gun points to " + ("s".equals(trigger) ? "you" : "the dealer"));
            TimeUnit.SECONDS.sleep(5);
            if (currentShell == ShellType.LIVE_SHELL) {
                System.out.println("Hit! " + ("s".equals(trigger) ? "You lose one health" : "Dealer loses one health"));
                if ("s".equals(trigger)) {
                    playerLife--;
                } else {
                    dealerLife--;
                }
            } else {
                System.out.println("Miss! " + ("s".equals(trigger) ? "You survived" : "The dealer survived"));
            }
            if ((isPlayerTurn && "d".equals(trigger)) || (!isPlayerTurn && "s".equals(trigger))) {
                isPlayerTurn = !isPlayerTurn;
            }
        }

        /**
         * Checks if the game is over based on the player and dealer lives.
         *
         * @param scanner the scanner object to read input
         */
        private void checkGameOver(Scanner scanner) {
            if (playerLife <= 0) {
                System.out.println("\nGame over! You lost all lives");
                isGameOver = true;
            } else if (dealerLife <= 0) {
                System.out.println("\nYou win! The dealer lost all lives");
                score += playerLife * roundNum * SCORE_MULTIPLIER;
                System.out.println("Your current score " + score);
                System.out.println("Do you want to double your score? Enter 'y' for yes, 'n' for no: ");
                String userChoice = scanner.nextLine().toLowerCase().trim();
                if ("y".equals(userChoice)) {
                    score *= 2;
                    playerLife = INITIAL_LIFE;
                    dealerLife = INITIAL_LIFE;
                    chamber.clear();
                } else {
                    isGameOver = true;
                }
            }
        }

        /**
         * Reloads the gun with the specified number of live and blank shells.
         *
         * @param numLiveShells   the number of live shells to reload
         * @param numBlankShells  the number of blank shells to reload
         */
        private void reloadGun(int numLiveShells, int numBlankShells) {
            chamber.clear();
            for (int i = 0; i < numLiveShells; i++) {
                chamber.push(ShellType.LIVE_SHELL);
            }
            for (int i = 0; i < numBlankShells; i++) {
                chamber.push(ShellType.BLANK_SHELL);
            }
            Collections.shuffle(chamber);
        }
    }

    /**
     * Main method to start the game.
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if the game is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        new Game().play();
    }
}
