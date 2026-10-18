import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessagesSent = 0;

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

        public String checkRecipientCell() {
        // Ensure length is <= 12 (for +27...) and starts with '+'
        if (this.recipient.length() <= 12 && this.recipient.matches("^\\+\\d+")) {
            return "Cell phone number successfully captured."; // Make sure the 'd' is here!
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    
    }

    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) return "Invalid Message";
        String firstTwoID = this.messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", ""); 
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        return (firstTwoID + ":" + this.messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String validateMessageLength() {
        if (this.messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = this.messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1: totalMessagesSent++; return "Message successfully sent.";
            case 2: return "Press 0 to delete the message.";
            case 3: storeMessage(); return "Message successfully stored.";
            default: return "Invalid choice.";
        }
    }

    public static int returnTotalMessagess() { return totalMessagesSent; }

    public String printMessages() {
        return "Message ID: " + this.messageID + "\n" +
               "Message Hash: " + this.messageHash + "\n" +
               "Recipient: " + this.recipient + "\n" +
               "Message: " + this.messageText;
    }

    public void storeMessage() {
        String json = "{\n  \"messageID\": \"" + this.messageID + "\",\n  \"recipient\": \"" + this.recipient + "\",\n  \"messageText\": \"" + this.messageText + "\"\n}\n";
        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json);
        } catch (IOException e) {
            System.out.println("Error storing message.");
        }
    }
    
    public String getMessageHash() { return messageHash; }

    
    // Nested Test Class
    
    public static class MessageTest {

        private Message testMessage1;
        private Message testMessage2;

        @Before
        public void setUp() {
            testMessage1 = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
            testMessage2 = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        }

        @Test
        public void testMessageLengthSuccess() {
            assertEquals("Message ready to send.", testMessage1.validateMessageLength());
        }

        @Test
        public void testMessageLengthFailure() {
            String longMessage = "A".repeat(255); 
            Message longMsg = new Message(2, "+27718693002", longMessage);
            assertEquals("Message exceeds 250 characters by 5; please reduce the size.", longMsg.validateMessageLength());
        }

        @Test
        public void testRecipientFormatSuccess() {
            assertEquals("Cell phone number successfully captured.", testMessage1.checkRecipientCell());
        }

        @Test
        public void testRecipientFormatFailure() {
            String expectedError = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
            assertEquals(expectedError, testMessage2.checkRecipientCell());
        }

        @Test
        public void testMessageHashCreation() {
            String hash = testMessage1.getMessageHash();
            assertTrue("Hash format is incorrect: " + hash, hash.matches("\\d{2}:0:HITONIGHT"));
        }

        @Test
        public void testSentMessageActions() {
            assertEquals("Message successfully sent.", testMessage1.SentMessage(1));
            assertEquals("Press 0 to delete the message.", testMessage1.SentMessage(2));
            assertEquals("Message successfully stored.", testMessage1.SentMessage(3));
        }
    }
                
        public static void main(String[] args) {
            MessageTest tester = new MessageTest();
            
            System.out.println("Starting Tests...");
            
            tester.setUp(); 
            
            
            tester.testMessageLengthSuccess();
            System.out.println("Passed: testMessageLengthSuccess");
            
            tester.testMessageLengthFailure();
            System.out.println("Passed: testMessageLengthFailure");
            
            tester.testRecipientFormatSuccess();
            System.out.println("Passed: testRecipientFormatSuccess");
            
            tester.testRecipientFormatFailure();
            System.out.println("Passed: testRecipientFormatFailure");
            
            tester.testMessageHashCreation();
            System.out.println("Passed: testMessageHashCreation");
            
            tester.testSentMessageActions();
            System.out.println("Passed: testSentMessageActions");
            
            System.out.println("All Tests Completed Successfully!");
        }

}