package TransactionReader;

import StoreUtils.Game;
import StoreUtils.Store;
import java.io.*;

/**
 * Represents the back end of a game distribution system. It process transactions from the front end and modify the
 * database accordingly.
 *
 */

public class TransactionReader {

    private static File transactions;
    private static String filename = "daily.txt";

    /**
     * Opens the daily.txt file.
     */
    private static void fileOpener(){
        transactions = new File(filename);
        if (!transactions.exists()) {
            try {
                if (transactions.createNewFile())
                    System.out.println("An empty daily transaction file is created, please put it data and restart");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The main function opens the daily.txt, initialize a store and process transactions line by line.
     * After processing all transaction, the system updates the game database.
     *
     */
    public static void main(String[] args) {
        fileOpener();
        try {
            Store.initialize();
            BufferedReader br = new BufferedReader(new FileReader(transactions));
            String code;
            while ((code = br.readLine()) != null)
                CodeExecutor.executeCode(code);
            Store.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
