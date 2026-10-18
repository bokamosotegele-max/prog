/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Bokamoso K.Boste
 */

    import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to QuickChat.");
        System.out.print("How many messages do you wish to enter? ");
        int maxMessages = scanner.nextInt();
        scanner.nextLine(); 

        boolean running = true;
        int messageCounter = 0;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); 

            if (menuChoice == 1) {
                if (messageCounter >= maxMessages) {
                    System.out.println("You have reached the maximum number of messages allowed for this session.");
                    continue;
                }

                System.out.print("Enter recipient cell number (e.g., +27718693002): ");
                String recipient = scanner.nextLine();
                System.out.print("Enter message: ");
                String text = scanner.nextLine();

                Message msg = new Message(messageCounter + 1, recipient, text);

                String recipientStatus = msg.checkRecipientCell();
                System.out.println(recipientStatus);
                if (!recipientStatus.startsWith("Cell phone number successfully")) continue;

                String lengthStatus = msg.validateMessageLength();
                System.out.println(lengthStatus);
                if (!lengthStatus.equals("Message ready to send.")) continue;

                System.out.println("\nWhat would you like to do?");
                System.out.println("1) Send Message\n2) Disregard Message\n3) Store Message");
                int action = scanner.nextInt();
                scanner.nextLine();

                String actionResult = msg.SentMessage(action);
                System.out.println(actionResult);

                if (action == 1) {
                    System.out.println("\n--- Message Details ---");
                    System.out.println(msg.printMessages());
                    messageCounter++;
                }
            } else if (menuChoice == 2) {
                System.out.println("Coming Soon.");
            } else if (menuChoice == 3) {
                running = false;
                System.out.println("Exiting application.");
            } else {
                System.out.println("Invalid option.");
            }
        }
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessagess());
        scanner.close();
    }
}
    
    

