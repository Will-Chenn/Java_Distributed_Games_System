package DatabaseManager;

import StoreUtils.Game;
import StoreUtils.InfoFinderAndModifier;
import TransactionReader.Standards;
import Users.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An AddInfo class is a class could add new users and new games into their own databases without reading code in
 * daily transaction file. Also it could print out all users or games in database.
 */
public class AddInfo {

    /**
     * Starts the program.
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        try {
            BufferedReader lineInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Hello, thank you for using user database manager.");
            String line;
            while (true) {
                System.out.println("What do you want to do? Please enter corresponding command"
                        + System.getProperty("line.separator") + "Q: quit program, " +
                        "AU: add new user to database, PU: print all users in database, " +
                        "AG: add new game to database, PG: print all games in database ");
                line = lineInput.readLine();
                switch (line) {
                    case "AU":
                        addUser(lineInput);
                        break;
                    case "PU":
                        InfoFinderAndModifier.printAll("users");
                        break;
                    case "AG":
                        addGame(lineInput);
                        break;
                    case "PG":
                        InfoFinderAndModifier.printAll("games");
                        break;
                    case "Q": return;
                    default:
                        System.out.println("Wrong command please enter a correct one");
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Collect information from user input, create a user based on it and add this new user into user
     * database: Users.txt.
     * @param lineInput a bufferedReader allows the program read user input.
     */
    private static void addUser(BufferedReader lineInput) {
        try {
            String password;
            String name;
            double credit;
            String type;
            String line;
            while (true) {
                System.out.println("username (15 characters maximum, no whitespace beginning and white space ending): ");
                line = lineInput.readLine();
                if (!usernameChecker(line)) continue;
                name = line;

                System.out.println("password (12 characters maximum, no whitespace, 123456 is default password): ");
                line = lineInput.readLine();
                if (!passwordChecker(line)) continue;
                if (line.equals("")) password = Standards.DEFAULT_PASSWORD;
                else password = line;

                System.out.println("credit (0.00 minimum 999999.99 maximum): ");
                line = lineInput.readLine();
                if (!numericChecker(line, "credit")) continue;
                credit = Double.parseDouble(line);

                System.out.println("user type (Standard buyer: BS, Standard Seller: SS, full standard user: FS, Admin user: AA): ");
                line = lineInput.readLine();
                if (!userTypeChecker(line)) continue;
                else type = line;

                User user = InfoFinderAndModifier.userCreator(name, password, credit, type, Standards.CREDIT_MINIMUM);
                if (user == null)
                    System.out.println("Fail to create a new user, going back to main menu");

                else {
                    System.out.println("New user: " + user.toString() + " is added into database! XD");
                    InfoFinderAndModifier.addNewUser(user);
                }

                System.out.println("Do you want to add more? (Yes No):");
                line = lineInput.readLine();
                if (line.equals("Yes"))
                    continue;

                else if (line.equals("No")) return;

                else System.out.println("Sorry I can't understand, going back to main menu");
                return;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Collect information from user input, create a game based on it and add this new game into game
     * database: Games.txt.
     * @param lineInput a bufferedReader allows the program read user input.
     */
    private static void addGame(BufferedReader lineInput) {
        try {
            String name;
            String seller;
            double price;
            double discountRate;
            String owners;
            String line;
            ArrayList<Game> games = new ArrayList<>();
            while (true) {
                System.out.println("Game name (25 characters maximum, no whitespace beginning and white space ending): ");
                line = lineInput.readLine();
                if (!gameNameChecker(line)) continue;
                name = line;

                System.out.println("Seller name (15 characters maximum, no whitespace beginning and white space ending): ");
                line = lineInput.readLine();
                if (!usernameChecker(line)) continue;
                seller = line;

                System.out.println("Price (0.00 minimum 999.99 maximum): ");
                line = lineInput.readLine();
                if (!numericChecker(line, "price")) continue;
                price = Double.parseDouble(line);

                System.out.println("Discount rate (00.00 minimum 90.00 maximum):  ");
                line = lineInput.readLine();
                if (!numericChecker(line, "discountRate")) continue;
                discountRate = Double.parseDouble(line);

                StringBuilder os = new StringBuilder();
                while (true){
                    System.out.println("Owners(please enter a valid username-15 characters maximum, " +
                                       "no whitespace beginning and white space ending Enter done to stop): ");
                    line = lineInput.readLine();
                    if (line.equals("done"))
                        break;
                    if (usernameChecker(line))
                        os.append(line).append(" ");
                }
                owners = os.toString();

                games.add(new Game(name, price, discountRate, seller, owners));
                System.out.println("Do you want to add more? (Yes No):");
                line = lineInput.readLine();
                if (line.equals("Yes"))
                    continue;

                else if (line.equals("No")) {
                    for (Game game: games)
                        System.out.println("New game: " + game.toString() + " is added into database! XD");
                    InfoFinderAndModifier.gameFileUpdater(games, "Add");
                }

                else System.out.println("Sorry I can't understand, going back to main menu");

                return;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * A function checks the validity of given name username based on constraints. Returns true if username is valid,
     * false otherwise.
     * @param name the username of user
     * @return if username valid, false otherwise.
     */
    private static boolean usernameChecker(String name) {
        String namePattern = "(^[\\S][\\s\\S]{1,13}[\\S]$|^[\\S]{1,2}$)";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.find() || name.length() > Standards.USERNAME_LENGTH) {
            System.out.println("Invalid name please enter a valid name");
            return false;
        }
        return true;
    }

    /**
     * A function checks the validity of given password based on constraints. Returns true if password is valid,
     * false otherwise.
     * @param password the password of user
     * @return true if password is valid, false otherwise.
     */
    private static boolean passwordChecker(String password) {
        String pwdPattern = "^[^\\s]{1,12}$";
        Pattern pattern = Pattern.compile(pwdPattern);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find() || password.length() > Standards.PASSWORD_LENGTH) {
            System.out.println("Invalid password please enter a valid password");
            return false;
        }
        return true;
    }

    /**
     * A function checks the validity of given user type based on constraints. Returns true if user type is valid,
     * false otherwise.
     * @param type the type of user
     * @return true if user type is valid, false otherwise.
     */
    private static boolean userTypeChecker(String type) {
        String typePattern = "(BS|SS|FS|AA)";
        Pattern pattern = Pattern.compile(typePattern);
        Matcher matcher = pattern.matcher(type);
        if (!matcher.find()) {
            System.out.println("Invalid user type please enter a valid user type");
            return false;
        }
        return true;
    }

    /**
     * A function checks the validity of given game name based on constraints. Returns true if game name is valid,
     * false otherwise.
     * @param name the name of a game
     * @return true if game name is valid, false otherwise.
     */
    private static boolean gameNameChecker(String name) {
        String namePattern = "(^[\\S][\\s\\S]{1,23}[\\S]$|^[\\S]{1,2}$)";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.find() || name.length() > Standards.GAME_NAME_LENGTH) {
            System.out.println("Invalid name please enter a valid name");
            return false;
        }
        return true;
    }

    /**
     * A function checks the validity of three kinds of given numeric value: credit(for user), price(for game),
     * and discount rate(for game) based on constraints. Return true if the numeric value is valid, false otherwise.
     * @param amount a string representation of a numeric value
     * @param type what type this numeric value is(credit, price, discount rate)
     * @return true if the numeric value is valid, false otherwise.
     */
    private static boolean numericChecker(String amount, String type) {
        try {
            String numericPattern = "(\\.[\\d]{2}$)";
            Pattern pattern = Pattern.compile(numericPattern);
            Matcher matcher = pattern.matcher(amount);
            if (!matcher.find()) {
                System.out.println("Please enter a numeric value has two decimal places ending");
                return false;
            }
            switch (type){
                case "credit":
                    if (Double.parseDouble(amount) < Standards.CREDIT_MINIMUM ||
                        Double.parseDouble(amount) > Standards.CREDIT_MAXIMUM) {
                        System.out.println("Invalid credit please enter a valid credit");
                        return false;
                    }
                    break;

                case "price":
                    if (Double.parseDouble(amount) < Standards.MIN_GAME_PRICE ||
                        Double.parseDouble(amount) > Standards.MAX_GAME_PRICE) {
                        System.out.println("Invalid price please enter a valid price");
                        return false;
                    }
                    break;

                case "discountRate":
                    if (Double.parseDouble(amount) < Standards.MIN_DISCOUNT_RATE ||
                        Double.parseDouble(amount) > Standards.MAX_DISCOUNT_RATE) {
                        System.out.println("Invalid discount rate please enter a valid discount rate");
                        return false;
                    }
                    break;
            }
            return true;
        } catch (Exception e){
            System.out.println("please enter a numeric number");
            return false;
        }
    }
}