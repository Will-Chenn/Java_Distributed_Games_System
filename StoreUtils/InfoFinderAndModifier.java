package StoreUtils;

import Users.*;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import TransactionReader.Standards;

/**
 * A InfoFinderAndModifier class is a static class that contains many "tool functions" which could find information
 * from databases, and modify them.
 */
public class InfoFinderAndModifier {

    private static File users;
    private static File games;
    private static String filename1 = "Users.txt";
    private static String filename2 = "Games.txt";

    /**
     * A function which could open user database and create one if there is no database in directory
     */
    public static void userFileOpener() {
        users = new File(filename1);
        if (!users.exists()) {
            try {
                if (users.createNewFile())
                    System.out.println("Your have a empty user database at: " + users.getAbsolutePath()
                                       + " please import your data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  A function which could open game database and create one if there is no database in directory
     */
    public static void gameFileOpener() {
        games = new File(filename2);
        if (!games.exists()) {
            try {
                if (games.createNewFile())
                    System.out.println("Your have a empty game database at: " + games.getAbsolutePath()
                                       + " please import your data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A function searches the user database with given username. Returns a User which relevant user type,
     * null if there is no such user.
     * @param username the name of user
     * @return a User which relevant user type, null if there is no such user.
     */
    public static User findRelevantUser(String username) {
        userFileOpener();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(users));
            String line;
            while ((line = bf.readLine()) != null) {
                if (lineParser(line).equals(username)) {
                    String name = lineParser(line);
                    String password = lineParser(bf.readLine());
                    double credit = Double.parseDouble(lineParser(bf.readLine()));
                    String userType = lineParser(bf.readLine());
                    double creditAddToday = Double.parseDouble(lineParser(bf.readLine()));
                    bf.close();
                    return userCreator(name, password, credit, userType, creditAddToday);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A function creates a user based on given username, password, credit, type, and creditAddToday. Return the user
     * instance, null if the user type does not belongs to BS(Standard Buyer), SS(Standard Seller), FS(Full Standard
     * user), and AA(Admin user).
     * @param username a string which is name of user
     * @param password a string which is  password of user
     * @param credit a double which is the credit of user
     * @param type a string which indicates the type of user
     * @param creditAddToday a double which records how much credit this user have added today
     * @return a User, null if the user type does not belongs to BS(Standard Buyer), SS(Standard Seller), FS
     *         (Full Standard user), and AA(Admin user).
     */
    public static User userCreator(String username, String password, double credit, String type, double creditAddToday) {
        switch (type) {
            case Standards.USERTYPE_BUYER:
                return new StandardBuyer(username, password, credit, creditAddToday);

            case Standards.USERTYPE_SELLER:
                return new StandardSeller(username, password, credit, creditAddToday);

            case Standards.USERTYPE_FULL:
                return new FullStandardUser(username, password, credit, creditAddToday);

            case Standards.USERTYPE_ADMIN:
                return new AdminUser(username, password, credit, creditAddToday);

            default: return null;
        }
    }

    /**
     * A function loops through inventory of given user, checks if it contains a game which has a given game name.
     * The mode: "inventory" will check for game satisfy constraints that this user bought before. "new" will check
     * for such game bought today. Returns a Game satisfy constraints, null if there is no such game.
     * @param user an instance of User which you want to check
     * @param gameName a string which is the name of game
     * @param mode a string which either is inventory or new.
     * @return a Game satisfy constraints, null if there is no such game.
     */
    public static Game inventoryCheck(User user, String gameName, String mode) {
        if (mode.equals("inventory")) {
            for (Game game : user.myInventory) {
                if (game.getName().equals(gameName))
                    return game;
            }
        }
        else if (mode.equals("new")){
            for (Game game : user.myInventory) {
                if (game.getNewOwners().contains(user.name) && game.getName().equals(gameName))
                    return game;
            }
        }
        return null;
    }

    /**
     * A function that updates user database. The mode: "update" overrides given user's information in database.
     * "delete" delete a user from user database.
     * @param user an instance of User which you want to update or delete
     * @param mode a string which either is update or delete.
     */
    public static void userFileUpdater(User user, String mode) {
        userFileOpener();
        try (RandomAccessFile rw = new RandomAccessFile(filename1, "rw");
             FileLock ignored = rw.getChannel().lock()) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rw.readLine()) != null) {
                if (line.contains(user.name)) {
                    rw.readLine(); //password
                    rw.readLine(); //credit
                    rw.readLine(); //type
                    rw.readLine(); //creditAddToday
                    rw.readLine(); //empty line
                } else
                    result.append(line).append(System.getProperty("line.separator"));
            }
            rw.setLength(0);
            rw.seek(0);
            rw.write(result.toString().getBytes(StandardCharsets.UTF_8));
            if (mode.equals("update"))
                user.writeInfo(rw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function adds a new user into database.
     * @param user an instance of User which you want to add in user database
     */
    public static void addNewUser(User user) {
        userFileOpener();
        try (RandomAccessFile rw = new RandomAccessFile(filename1, "rw");
             FileLock ignored = rw.getChannel().lock()) {
            rw.seek(rw.length());
            user.writeInfo(rw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function resets the addCreditToday to 0.00 for every user in user database.
     */
    public static void clearAddCreditToday(){
        userFileOpener();
        try (RandomAccessFile rw = new RandomAccessFile(filename1, "rw");
             FileLock ignored = rw.getChannel().lock()) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rw.readLine()) != null){
                if (line.contains("creditAddToday"))
                    result.append("creditAddToday: ").append(String.format("%.2f", Standards.CREDIT_MINIMUM))
                          .append(System.getProperty("line.separator"));
                else result.append(line).append(System.getProperty("line.separator"));
            }
            rw.setLength(0);
            rw.seek(0);
            rw.write(result.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function that read through game database and create instance of Game for each game in database and add all
     * of these instances into Store's allAvailableGames attribute.
     */
    public static void gameImporter(){
        gameFileOpener();
        try {
            BufferedReader br = new BufferedReader(new FileReader(games));
            String isAuctionSale = br.readLine();
            Store.isAuctionSaleOn = isAuctionSale.equals("on");
            String line;
            while ((line = br.readLine()) != null){
                String name = lineParser(line);
                String sellerName = lineParser(br.readLine());
                double price = Double.parseDouble(lineParser(br.readLine()));
                String discountRate = lineParser(br.readLine());
                String owners = lineParser(br.readLine());
                Store.allAvailableGames.add(new Game(name, price, Double.parseDouble(discountRate), sellerName, owners));
                br.readLine();
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function adds given game to Store's onStockTomorrow attribute.
     * @param game an instance of Game which you put on sale today.
     */
    public static void addNewGame(Game game){
        Store.onStockTomorrow.add(game);
    }

    /**
     * A function searches for a game in Store which matches given game name and seller name. The mode:
     * "new" loops through Store.onStockTomorrow which is an array list of games put on sale today. "onStock"
     * loops through Store.allAvailableGames which is an array list of games users could buy. Return the game
     * satisfy constraints, null if there is no such game.
     * @param gameName a string which is name of game.
     * @param sellerName a string which is name of user
     * @param mode a string which either is new or onStock.
     * @return the Game satisfy constraints, null if there is no such game.
     */
    public static Game findGame(String gameName, String sellerName, String mode) {
        if (mode.equals("new")){
            for (Game game: Store.onStockTomorrow){
                if(game.getName().equals(gameName) &&
                        game.getSellerName().equals(sellerName))
                    return game;
            }
        }
        else if (mode.equals("onStock")){
            for (Game game : Store.allAvailableGames) {
                if (game.getName().equals(gameName) &&
                        game.getSellerName().equals(sellerName))
                    return game;
            }
        }
        return null;
    }

    /**
     * A function updates game database with given array list of games. The mode: "Store" updates the whole file.
     * "Add" add all games at the end of file.
     * @param games an array list of games you want to update in game database.
     * @param mode a string which either is "Store" or "Add".
     */
    public static void gameFileUpdater(ArrayList<Game> games, String mode) {
        gameFileOpener();
        try (RandomAccessFile rw = new RandomAccessFile(filename2, "rw");
             FileLock ignored = rw.getChannel().lock()) {
            if (mode.equals("Store")) {
                rw.setLength(0);
                boolean isAuctionSale = Store.isAuctionSaleOn;
                if (isAuctionSale)
                    rw.write(("auctionSale: on" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
                else
                    rw.write(("auctionSale: off" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
            }
            else if (mode.equals("Add"))
                rw.seek(rw.length());

            for (Game game : games) game.writeInfo(rw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function that wipes out all parts which are not useful in a given line. Returns the processed line.
     * @param line a string.
     * @return the processed line.
     */
    public static String lineParser(String line) {
        return line.replaceAll("[a-zA-Z]+:\\s?", "");
    }

    /**
     * A function prints all information in database. The mode: "users" prints all users in user database. "games":
     * prints all games in game database.
     * @param mode a string which either is "users" or "games"
     */
    public static void printAll(String mode){
        try {
            if (mode.equals("users")){
                userFileOpener();
                BufferedReader bf = new BufferedReader(new FileReader(users));
                String line;
                while ((line = bf.readLine()) != null)
                    System.out.println(line);
            }

            else if (mode.equals("games")){
                gameFileOpener();
                BufferedReader bf = new BufferedReader(new FileReader(games));
                String line;
                while ((line = bf.readLine()) != null)
                    System.out.println(line);
            }

            else System.out.println("No such file exists");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function sets the filename1 attribute.
     * @param filename1 the attribute which indicates path of user database.
     */
    public static void setFilename1(String filename1) {
        InfoFinderAndModifier.filename1 = filename1;
    }

    /**
     * A function sets the filename2 attribute.
     * @param filename2 the attribute which indicates path of game database.
     */
    public static void setFilename2(String filename2) {
        InfoFinderAndModifier.filename2 = filename2;
    }

    /**
     * A function gets users attribute which is the file of the user database. Return the file of the user database.
     * @return the File of the user database.
     */
    public static File getUsersFile() {
        return users;
    }

    /**
     * A function gets games attribute which is the file of the game database. Return the file of the game database.
     * @return the File of the game database.
     */
    public static File getGamesFile() {
        return games;
    }
}