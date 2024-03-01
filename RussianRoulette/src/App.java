import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter your first name: ");
        String name = scanner.nextLine().toLowerCase().trim();
        System.out.println();

        String sf1 = String
                .format("Your signature %s if you want to participate in the game, knowing that you can die: ", name);
        System.out.print(sf1);
        String waiverSigned = scanner.nextLine().toLowerCase().trim();
        System.out.println();

        if (name.equals(waiverSigned)) {
            System.out.println("Game has begun\n");

            while (true) {
                int emptyShells = random.nextInt(7) + 1;
                int bullets = random.nextInt(7) + 1;

                if (emptyShells + bullets == 8) {
                    System.out.println("Empty Shell(s): " + emptyShells + " Bullet(s): " + bullets + "\n");

                    while (true) {
                        int liveRound = random.nextBoolean() ? bullets : emptyShells;

                        System.out.print("To shoot yourself, use key 's' or to shoot the dealer use key 'd': ");
                        String trigger = scanner.nextLine().toLowerCase().trim();

                        if (trigger.equals("s") && liveRound == bullets) {
                            System.out.println("Game over! you shot yourself");
                            System.exit(0);
                        } else if (trigger.equals("d") && liveRound == bullets) {
                            System.out.println("Game over! you shot the dealer");
                            System.exit(0);
                        } else if (trigger.equals("s") && liveRound == emptyShells) {
                            emptyShells--;
                            System.out.println(
                                    "You survived, now empty shells left: " + emptyShells + " Bullets: " + bullets);

                            if (emptyShells == 0) {
                                System.out.println("No more empty shells left, game over");
                                System.exit(0);
                            } else {
                                System.out.println("\nDealer's turn\n");
                                char dealerChoice = random.nextBoolean() ? 's' : 'p';
                                int dealerLiveRound = random.nextBoolean() ? bullets : emptyShells;

                                if (dealerChoice == 's' && dealerLiveRound == bullets) {
                                    System.out.println("You won! The Dealer shot himself");
                                    System.exit(0);
                                } else if (dealerChoice == 'p' && dealerLiveRound == bullets) {
                                    System.out.println("You lost, The Dealer shot you");
                                    System.exit(0);
                                } else if (dealerChoice == 's' && dealerLiveRound == emptyShells) {
                                    System.out.println("The Dealer survived, now empty shells left: " + emptyShells
                                            + " Bullets: " + bullets);
                                    continue;
                                } else if (dealerChoice == 'p' && dealerLiveRound == emptyShells) {
                                    System.out.println("You survived, now empty shells left: " + emptyShells
                                            + " Bullets: " + bullets);
                                    continue;
                                }
                            }
                        } else if (trigger.equals("d") && liveRound == emptyShells) {
                            System.out.println("The dealer survived, now empty shells left: " + emptyShells
                                    + " Bullets: " + bullets);

                            if (emptyShells == 0) {
                                System.out.println("No more empty shells left, game over");
                                System.exit(0);
                            } else {
                                System.out.println("\nDealer's turn\n");
                                char dealerChoice = random.nextBoolean() ? 's' : 'p';
                                int dealerLiveRound = random.nextBoolean() ? bullets : emptyShells;

                                if (dealerChoice == 's' && dealerLiveRound == bullets) {
                                    System.out.println("You won! The Dealer shot himself");
                                    System.exit(0);
                                } else if (dealerChoice == 'p' && dealerLiveRound == bullets) {
                                    System.out.println("You lost, The Dealer shot you");
                                    System.exit(0);
                                } else if (dealerChoice == 's' && dealerLiveRound == emptyShells) {
                                    emptyShells--;
                                    System.out.println("The Dealer survived, now empty shells left: " + emptyShells
                                            + " Bullets: " + bullets);
                                    continue;
                                } else if (dealerChoice == 'p' && dealerLiveRound == emptyShells) {
                                    System.out.println("You survived, now empty shells left: " + emptyShells
                                            + " Bullets: " + bullets);
                                    continue;
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } else {
            System.out.println("Your sign doesn't match your name");
            System.exit(0);
        }
    }
}
