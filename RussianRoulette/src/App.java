import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        try (Scanner scanner = new Scanner(System.in)) {
            Random random = new Random();
            Stack<String> chamber = new Stack<>();

            System.out.print("Enter your first name: ");
            String name = scanner.nextLine().toLowerCase().trim();
            System.out.println();
            System.out.print("Your signature " + name + ", if you want to participate in the game, " +
                    "knowing that you can die: ");
            String waiverSigned = scanner.nextLine().toLowerCase().trim();
            System.out.println();

            if (name.equals(waiverSigned)) {
                System.out.println("Game has begun\n");
                int playerLife = 4;
                int dealerLife = 4;
                int roundNum = 0;
                int bonus = 1;
                boolean isGameOver = false;
                boolean isPlayerTurn = true;
                String trigger = "";
                int score = 0;

                while (!isGameOver) {

                    if (chamber.empty()) {
                        roundNum++;
                        System.out.println("\nRound " + roundNum);
                        int numLiveShells = random.nextInt(6) + 2;
                        int numBlankShells = 8 - numLiveShells;
                        System.out.println("Live Shell(s): " + numLiveShells + ", Blank Shell(s): " + numBlankShells);
                        System.out.println("Reload Gun\n");
                        chamber = reloadGun(numLiveShells, numBlankShells);
                    }

                    System.out.println("\nYour health: " + playerLife + " Dealer health: " + dealerLife + "\n");

                    if (name.equals("god")) {
                        System.out.println(chamber + "\n"); // ! God mode
                    }

                    if (isPlayerTurn) {
                        while (true) {
                            System.out.println("\nYour turn");
                            System.out.print("To shoot yourself, use key 's' or to shoot the dealer use key 'd': ");
                            trigger = scanner.nextLine().toLowerCase().trim();

                            // Validate the input
                            if (trigger.equals("s") || trigger.equals("d")) {
                                break; // Exit the loop if input is valid
                            } else {
                                System.out.println(
                                        "Invalid input. Please enter 's' to shoot yourself or 'd' to shoot the dealer.");
                            }
                        }
                    } else {
                        System.out.println("\nDealer turn");
                        trigger = random.nextBoolean() ? "s" : "d";
                    }

                    String currentShell = chamber.pop();

                    if (trigger.equals("s") && currentShell == "liveShell") {
                        System.out.println("Gun points to you");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Hit! You loss one health");
                        playerLife--;
                        isPlayerTurn = true;
                    } else if (trigger.equals("d") && currentShell == "liveShell") {
                        System.out.println("Gun points to dealer");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Hit! Dealer loss one health");
                        dealerLife--;
                        isPlayerTurn = false;
                    } else if (trigger.equals("s") && currentShell == "blankShell") {
                        System.out.println("Gun points to you");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Miss! You survived");
                        isPlayerTurn = true;
                    } else if (trigger.equals("d") && currentShell == "blankShell") {
                        System.out.println("Gun points to dealer");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Miss! The dealer survived");
                        isPlayerTurn = false;
                    }

                    if (playerLife <= 0) {
                        System.out.println("\nGame over! You lost all lives");
                        isGameOver = true;
                    } else if (dealerLife <= 0) {
                        System.out.println("\nYou win! The dealer lost all lives");
                        score += playerLife * roundNum * bonus * 100000;
                        System.out.println("Your current score " + score);
                        System.out.print("Do you want to double your score? Enter 'y' for yes, 'n' for no: ");
                        String doubleOrNothing = scanner.nextLine().toLowerCase().trim();
                        if (doubleOrNothing.equals("y")) {
                            bonus += 1;
                            playerLife = 4;
                            dealerLife = 4;
                            chamber.clear();
                        } else {
                            isGameOver = true;
                        }
                    }
                }
            } else {
                System.out.println("Your sign doesn't match your name");
            }
        }
    }

    public static Stack<String> reloadGun(int numLiveShells, int numBlankShells) {
        Stack<String> chamber = new Stack<>();
        Random random = new Random();

        for (int i = 0; i < numLiveShells; i++) {
            chamber.push("liveShell");
        }
        for (int i = 0; i < numBlankShells; i++) {
            chamber.push("blankShell");
        }

        Collections.shuffle(chamber, random);
        return chamber;
    }

}
