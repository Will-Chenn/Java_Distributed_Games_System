package TransactionReader;

import StoreUtils.Game;
import StoreUtils.InfoFinderAndModifier;
import StoreUtils.Store;
import Transactions.*;
import Users.AdminUser;
import Users.StandardBuyer;
import Users.StandardSeller;
import Users.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the processor of transactions. It recognizes transactions from daily.txt and modify databases accordingly.
 *
 */
public class CodeExecutor {

    private static User currUser;

    /**
     * Recognise lines from daily.txt and execute different transactions based on its format. See requirements for
     * specification.
     * @param code a line from daily.txt
     */
    public static void executeCode(String code) {
        String pattern1 = "^(00|01|02|06|07|10) ([\\S][\\s\\S]{14}|[\\s]{15}) (AA|FS|BS|SS|\\s{2}) (\\d{6}\\.\\d{2}|000000000)$"; //login logout addCredit create delete auctionSale
        Pattern p1 = Pattern.compile(pattern1);

        String pattern2 = "^(03) ([^\\s][\\s\\S]{24}) ([\\S][\\s\\S]{14}) (\\d{2}\\.\\d{2}) (\\d{3}\\.\\d{2})$"; //Sell
        Pattern p2 = Pattern.compile(pattern2);

        String pattern3 = "^(04) ([^\\s][\\s\\S]{24}) ([\\S][\\s\\S]{14}) ([\\S][\\s\\S]{14})$"; //Buy
        Pattern p3 = Pattern.compile(pattern3);

        String pattern4 = "^(05) ([\\S][\\s\\S]{14}) ([\\S][\\s\\S]{14}) (\\d{6}\\.\\d{2})$"; //Refund
        Pattern p4 = Pattern.compile(pattern4);

        String pattern5 = "^(08|09) ([^\\s][\\s\\S]{24}) ([\\S][\\s\\S]{14}|[\\s]{15}) ([\\S][\\s\\S]{14}|[\\s]{15})$"; //removeGame gift
        Pattern p5 = Pattern.compile(pattern5);

        Matcher m1 = p1.matcher(code);
        Matcher m2 = p2.matcher(code);
        Matcher m3 = p3.matcher(code);
        Matcher m4 = p4.matcher(code);
        Matcher m5 = p5.matcher(code);

        if (m1.find())
            m1StyledCodeProcessor(m1);
        else if (m2.find())
            m2StyledCodeProcessor(m2);
        else if (m3.find())
            m3StyledCodeProcessor(m3);
        else if (m4.find())
            m4StyledCodeProcessor(m4);
        else if (m5.find())
            m5StyledCodeProcessor(m5);
        else if (code.equals(""))
            System.out.println("ERROR: <Fatal Error: Empty line in transaction file>");
        else
            System.out.println("ERROR: <Fatal Error: Invalid transaction code>");
    }

    /**
     * Process a transaction with the format XX UUUUUUUUUUUUUUU TT CCCCCCCCC
     * where x is a two-digit transaction code: 00-login, 01-create, 02-delete, 06-addcredit, 10-logout
     * Us is the username
     * TT is the user type (AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard)
     * Cs is the available credit
     *
     */
    private static void m1StyledCodeProcessor(Matcher m1) {
        String codeType = m1.group(1);
        switch (codeType){
            case "00":
                if (loginChecker(m1.group(2), m1.group(3), m1.group(4))) {
                    currUser = InfoFinderAndModifier.findRelevantUser(m1.group(2).stripTrailing());
                    System.out.println("User: " + currUser.name + " logged in");
                }
                break;

            case "01":
                Create create = createChecker(m1.group(2), m1.group(3), m1.group(4));
                if (create != null)
                    currUser.accept(create);
                break;

            case "02":
                Delete delete = deleteChecker(m1.group(2), m1.group(3), m1.group(4));
                if (delete != null)
                    currUser.accept(delete);
                break;

            case "06":
                AddCredit addCredit = addCreditChecker(m1.group(2), m1.group(4));
                if (addCredit != null)
                    currUser.accept(addCredit);
                break;

            case "07":
                AuctionSell auctionSell = auctionSaleChecker(m1);
                if (auctionSell != null)
                    currUser.accept(auctionSell);
                break;

            case "10":
                if (logoutChecker(m1.group(2).stripTrailing())) {
                    System.out.println("User: " + currUser.name + " logged out");
                    currUser = null;
                }
                break;
        }
    }

    /**
     * Process a sell transaction.
     */
    private static void m2StyledCodeProcessor(Matcher m2) {
        Sell sell = sellChecker(m2.group(2).stripTrailing(), m2.group(4), m2.group(5), m2.group(3));
        if (sell != null) currUser.accept(sell);
    }

    /**
     * Process a buy transaction.
     */
    private static void m3StyledCodeProcessor(Matcher m3) {
        Buy buy = buyChecker(m3.group(2), m3.group(3), m3.group(4));
        if (buy != null) {
            ArrayList<User> relevantUsers = new ArrayList<>();
            relevantUsers.add(currUser);
            relevantUsers.add(InfoFinderAndModifier.findRelevantUser(m3.group(3)));
            for (User user: relevantUsers) user.accept(buy);
        }
    }


    /**
     * Process a refund transaction.
     */
    private static void m4StyledCodeProcessor(Matcher m4) {
        Refund refund = refundChecker(m4.group(2), m4.group(3), m4.group(4));
        if (refund != null)
            currUser.accept(refund);
    }

    /**
     * Process 08 remove-game or 09 gift-game transaction.
     */
    private static void m5StyledCodeProcessor(Matcher m5) {
        String codeType = m5.group(1);
        switch (codeType){
            case "08":
                RemoveGame removeGame = removeGameChecker(m5);
                if (removeGame != null)
                    currUser.accept(removeGame);
                break;

            case "09":
                Gift gift = giftChecker(m5.group(3), m5.group(2), m5.group(4));
                if (gift != null)
                    currUser.accept(gift);
                break;
        }
    }

    /**
     * Checks whether the login is valid.
     * A login is valid when the user is not already logged in, username is not empty, the user exists in the database.
     *
     */
    public static boolean loginChecker(String username, String userType, String credit){
        if (!(currUser == null)) {
            System.out.println("ERROR: <Constraint Error: Attempt to login while last user did not logout>");
            return false;
        }
        else if (username.stripTrailing().equals(Standards.EMPTY_USERNAME)){
            System.out.println("ERROR: <Constraint Error: Username is empty so login is failed>");
            return false;
        }
        else {
            User user = InfoFinderAndModifier.findRelevantUser(username.stripTrailing());
            if (user == null){
                System.out.println("ERROR: <Constraint Error: The user who wants to login does not exist>");
                return false;
            }
            else {
                if (!user.type.equals(userType))
                    System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: type but the transaction is processed>");
            }
        }
        if (!credit.equals(Standards.UNUSED_NUMERIC_FILED))
            System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: credit but the transaction is processed>");

        return true;
    }

    /**
     * Checks whether the create-user transaction is valid.
     * It is valid when the current user is admin, the username to create is not empty, the user is not existed
     */
    public static Create createChecker(String username, String userType, String credit) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to create users with no admin user logged in>");
            return null;
        }
        if (currUser instanceof AdminUser){
            if (username.equals(Standards.EMPTY_USERNAME))
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to create a user with a name of spaces>");

            else if (InfoFinderAndModifier.findRelevantUser(username.stripTrailing()) != null)
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to create a user already exists>");

            else{
                double credit_ = Double.parseDouble(credit);
                User newUser = InfoFinderAndModifier.userCreator(username.stripTrailing(), "123456", credit_, userType, Standards.CREDIT_MINIMUM);
                return new Create(newUser);
            }
        }
        else
            System.out.println("ERROR: <Constraint Error: Non-admin user: " + currUser.name + " attempt to create user>");

        return null;
    }

    /**
     * Check whether the delete transaction is valid.
     * It is valid when the logged in user is admin, not deleting the current user, not deleting a user that does not
     * exist.
     */
    public static Delete deleteChecker(String username, String userType, String credit) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to delete users with no admin user logged in>");
            return null;
        }
        if (currUser instanceof AdminUser) {
            if (username.equals(Standards.EMPTY_USERNAME))
                System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: username, but the transaction is processed>");

            if (!credit.equals(Standards.UNUSED_NUMERIC_FILED))
                System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: credit, but the transaction is processed>");

            if (userType.equals("  ") || !userType.equals(Standards.USERTYPE_ADMIN))
                System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: user type, but the transaction is processed>");

            if (username.stripTrailing().equals(currUser.name))
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to delete itself>");
            else {
                User toDelete = InfoFinderAndModifier.findRelevantUser(username.stripTrailing());
                if (toDelete == null)
                    System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to delete user who does not exist>");
                else return new Delete(toDelete);
            }
        }
        else
            System.out.println("ERROR: <Constraint Error: Non-admin user: " + currUser.name + " attempt to delete user>");

        return null;
    }

    /**
     * Check whether the addcredit transaction is valid.
     * It is valid when the user exists, the amount is not zero and smaller than or equal to 1000, and the credit after
     * addition does not exceed the maximum.
     */
    public static AddCredit addCreditChecker(String username, String creditAdded) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to add credits with no user logged in>");
            return null;
        }
        if (username.equals(Standards.EMPTY_USERNAME))
            username = currUser.name;

        if (!creditAdded.contains("."))
            System.out.println("ERROR: <Constraint Error: Invalid addCredit transaction code: credit field are all zero>");

        if (Double.parseDouble(creditAdded) > Standards.ADD_CREDIT_DAILY_MAXIMUM)
            System.out.println("ERROR: <Constraint Error: User: " + username.stripTrailing() + " attempt to add more than 1000 credit one time>");

        else {
            if (!username.stripTrailing().equals(Standards.EMPTY_USERNAME) && !username.stripTrailing().equals(currUser.name) &&
                !currUser.type.equals(Standards.USERTYPE_ADMIN)){
                System.out.println("ERROR: <Constraint Error: Non-admin user: " + username.stripTrailing() + " attempt to add credit to another user>");
                return null;
            }
            User user = InfoFinderAndModifier.findRelevantUser(username.stripTrailing());
            if (user == null)
                System.out.println("ERROR: <Constraint Error: User: " + username.stripTrailing() + " does not exist>");

            else if (user.creditAddToday + Double.parseDouble(creditAdded) > Standards.ADD_CREDIT_DAILY_MAXIMUM)
                System.out.println("ERROR: <Constraint Error: User: " + username.stripTrailing() + " attempt to add total more than 1000 credit in a day>");

            else if (user.credit + Double.parseDouble(creditAdded) > Standards.CREDIT_MAXIMUM) {
                System.out.println("ERROR: <Constraint Error: User: " + username.stripTrailing() + "'s credit exceeds maximum 999999.99 so credit is saved with 999999.99>");
                return new AddCredit(username, creditAdded);
            }
            else return new AddCredit(username, creditAdded);
        }
        return null;
    }

    /**
     * Check whether the auctionsell transaction is valid.
     * Only admin user can turn on auctionsale.
     */
    public static AuctionSell auctionSaleChecker(Matcher m) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to start auction sale with no user logged in>");
            return null;
        }

        if (m.group(2).equals(Standards.EMPTY_USERNAME) || !m.group(2).equals(currUser.name))
            System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: name, but the transaction is processed>");

        if (m.group(3).equals("  ") || !m.group(3).equals(Standards.USERTYPE_ADMIN))
            System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: user type, but the transaction is processed>");

        if (!m.group(4).equals(Standards.UNUSED_NUMERIC_FILED))
            System.out.println("ERROR: <Constraint Error: Extra information provided in unused field: credit, but the transaction is processed>");

        if (!(currUser instanceof AdminUser)){
            System.out.println("ERROR: <Constraint Error: Non-admin user: " + currUser.name + " attempt to open auction sale>");
            return null;
        }
        else {
            if (Store.allAvailableGames.size() == 0){
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to open auction sale but there is no game available>");
                return null;
            }
        }
        return new AuctionSell();
    }

    /**
     * Check if the sell transaction is valid.
     * It is valid when the current user is a seller, the seller is selling for itself, the discount rate is not higher
     * than 90, the price is not higher than 999.99, not selling a game twice and the game is not in the user's
     * inventory.
     */
    public static Sell sellChecker(String gameName, String discountRate, String price, String sellerName) {
        if (currUser == null){
            System.out.println("ERROR: <Constraint Error: Attempt to sell games with no user logged in>");
            return null;
        }

        if (currUser instanceof StandardBuyer) {
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to sell games with buyer account>");
            return null;
        }
        else {
            if (!sellerName.equals(Standards.EMPTY_USERNAME) && !sellerName.stripTrailing().equals(currUser.name)){
                System.out.println("ERROR: <Constraint Error: Non-admin user: " + sellerName.stripTrailing() + " attempt to sell games for other users>");
                return null;
            }
            if (Double.parseDouble(discountRate) > Standards.MAX_DISCOUNT_RATE) {
                System.out.println("ERROR: <Constraint Error: User: " + sellerName.stripTrailing() + " attempt to sell games with discount rate over 90.00>");
                return null;
            }
            if (Double.parseDouble(price) > Standards.MAX_GAME_PRICE) {
                System.out.println("ERROR: <Constraint Error: User: " + sellerName.stripTrailing() + " attempt to sell games with price over 999.99>");
                return null;
            }
            if (gameName.equals(Standards.EMPTY_GAME_NAME)) {
                System.out.println("ERROR: <Constraint Error: Incomplete transaction code, empty game name provided>");
                return null;
            }
            if (InfoFinderAndModifier.findGame(gameName.stripTrailing(), currUser.name, "new") != null
                || InfoFinderAndModifier.findGame(gameName.stripTrailing(), currUser.name, "onStock") != null){
                System.out.println("ERROR: <Constraint Error: User: " + sellerName.stripTrailing() + " attempt to sell a game you are already selling>");
                return null;
            }
            if (InfoFinderAndModifier.inventoryCheck(currUser, gameName.stripTrailing(), "inventory") != null ||
                    InfoFinderAndModifier.inventoryCheck(currUser, gameName.stripTrailing(), "new") != null) {
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to sell a game in your inventory>");
                return null;
            }
        }
        return new Sell(gameName, sellerName.stripTrailing(), discountRate, price);
    }
    /**
     * Check if the buy transaction is valid.
     * It is valid when the current logged in user is a buyer, the game is currently selling, have enough credit to buy
     * , does not have the game in inventory.
     */
    public static Buy buyChecker(String gameName, String sellerName, String buyerName) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to buy games with no user logged in>");
            return null;
        }

        if (currUser instanceof StandardSeller) {
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to buy games with seller account>");
            return null;
        }
        else {
            if (buyerName.stripTrailing().equals(Standards.EMPTY_USERNAME))
                System.out.println("ERROR: <Constraint Error: No buyer name provided, but transaction is processed>");

            if (!currUser.name.equals(Standards.EMPTY_USERNAME) && !currUser.name.equals(buyerName.stripTrailing())){
                System.out.println("ERROR: <Constraint Error: Wrong information given: buyer's name>");
                return null;
            }
            if (sellerName.stripTrailing().equals(currUser.name)){
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to buy a game this user is selling>");
                return null;
            }
            User seller = InfoFinderAndModifier.findRelevantUser(sellerName);
            if (seller == null) {
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to buy a game from a seller does not exist>");
                return null;
            }
            Game game = InfoFinderAndModifier.findGame(gameName.stripTrailing(), sellerName.stripTrailing(), "onStock");
            if (game == null) {
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to buy a game " + seller.name + " does not sell>");
                return null;
            }
            if (game.getPrice() > currUser.credit) {
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to buy a game can't afford>");
                return null;
            }
            if (game.getOwners().contains(currUser.name)) {
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to buy a game already in inventory>");
                return null;
            }
            if (seller.credit + game.getPrice() > Standards.CREDIT_MAXIMUM)
                System.out.println("ERROR: <Constraint Error: User: " + seller.name + "'s credit exceeds 999999.99, so credit is saved in 999999.99>");
            return new Buy(game);
        }
    }

    /**
     * Check if a refund transaction is valid.
     * It is valid when the current logged in user is admin, the buyer and seller exist in the database, seller has
     * enough credit and buyer's credit after refund does not exceed its limit.
     * Notice does not like other transactions, a refund that would max out the credit does not process.
     */
    public static Refund refundChecker(String buyerName, String sellerName, String credit) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to refund with no user logged in>");
            return null;
        }

        if (!(currUser instanceof AdminUser)){
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to refund but current user is not admin user>");
            return null;
        }

        User buyer = InfoFinderAndModifier.findRelevantUser(buyerName.stripTrailing());
        User seller = InfoFinderAndModifier.findRelevantUser(sellerName.stripTrailing());
        double creditRefund = Double.parseDouble(credit);
        if (buyer == null){
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to refund with a user who gets refund does not exist>");
            return null;
        }
        else if (seller == null) {
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to refund with a user who pays the refund does not exist>");
            return null;
        }
        else if (seller.credit - creditRefund < Standards.CREDIT_MINIMUM) {
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to refund with a user who pays the refund does not have enough credit>");
            return null;
        }
        else if (buyer.credit + creditRefund > Standards.CREDIT_MAXIMUM) {
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to get a refund but it would make the credit of this user exceeds 999999.99>");
            return null;
        }
        return new Refund(buyer, seller, creditRefund);
    }

    /**
     * Check whether a removegame transaction is valid.
     *
     * It is valid when the game is not bought or put on sale today, and is on stock or in inventory.
     */
    public static RemoveGame removeGameChecker(Matcher m) {
        String gameName = m.group(2);
        String username = m.group(3);

        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to remove games with no user logged in>");
            return null;
        }

        if (!(currUser instanceof AdminUser)){
            if (!username.equals(Standards.EMPTY_USERNAME) && !username.stripTrailing().equals(currUser.name)) {
                System.out.println("ERROR: <Constraint Error: Non-admin user: " + currUser.name + " attempt to remove a game for another user>");
            }
            else return removeCheckerHelper(gameName.stripTrailing(), currUser);
        }
        else {
            if (m.group(4).equals(Standards.EMPTY_USERNAME))
                System.out.println("ERROR: <Constraint Error: Your remove game transaction code unused field is filled with message but the transaction will get processed>");

            if (username.equals(Standards.EMPTY_USERNAME) || username.stripTrailing().equals(currUser.name)) {
                return removeCheckerHelper(gameName.stripTrailing(), currUser);
            }
            else {
                User user = InfoFinderAndModifier.findRelevantUser(username.stripTrailing());
                if (user == null)
                    System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to remove a game for a user does not exist>");
                else return removeCheckerHelper(gameName.stripTrailing(), user);
            }
        }
        return null;
    }

    /**
     * A helper function to validate removegame transaction.
     */
    private static RemoveGame removeCheckerHelper(String gameName, User user) {
        Game toRemoveInventory = InfoFinderAndModifier.inventoryCheck(user, gameName, "inventory");
        Game toRemoveSell = InfoFinderAndModifier.findGame(gameName, user.name, "onStock");
        Game toRemoveNew = InfoFinderAndModifier.inventoryCheck(user, gameName, "new");
        Game toRemoveSellNew = InfoFinderAndModifier.findGame(gameName, user.name, "new");

        if (toRemoveNew != null)
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to remove a game bought today>");

        else if (toRemoveSellNew != null)
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to remove a game you put on sale today>");

        else if (user instanceof StandardBuyer){
            if (toRemoveInventory == null)
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to remove a game you don't have>");
            else return new RemoveGame(toRemoveInventory, user.name);
        }
        else if (user instanceof StandardSeller){
            if (toRemoveSell == null)
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to remove a game currently not on stock in store>");
            else return new RemoveGame(toRemoveSell, user.name);
        }
        else {
            if (toRemoveInventory != null)
                if (toRemoveInventory == toRemoveSell)
                    System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " wants to remove game which is in inventory and on sale at same time>");
                else return new RemoveGame(toRemoveInventory, user.name);

            return new RemoveGame(toRemoveSell, user.name);
        }
        return null;
    }

    /**
     * Check whether the gift transaction is valid.
     * It is valid when the game is not purchased today, not put on sale today, from the correct seller, both the
     * giver and receiver exist, the giver has the game, the receiver does not have the game and the receiver is not a
     * seller.
     */
    public static Gift giftChecker(String giver, String g_name, String receiver) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to gift games with no user logged in>");
            return null;
        }
        User userReceiver = InfoFinderAndModifier.findRelevantUser(receiver.stripTrailing());
        if (!(currUser instanceof AdminUser)){
            if (!giver.equals(Standards.EMPTY_USERNAME) && !giver.stripTrailing().equals(currUser.name)){
                System.out.println("ERROR: <Constraint Error: Non-admin user: " + currUser.name + " attempt to give gift from other user>");
                return null;
            }
            else return giftCheckerHelper(g_name.stripTrailing(), currUser, userReceiver);
        }
        else {
            if (giver.equals(Standards.EMPTY_USERNAME) || giver.stripTrailing().equals(currUser.name))
                return giftCheckerHelper(g_name.stripTrailing(), currUser, userReceiver);
            else {
                User userGiver = InfoFinderAndModifier.findRelevantUser(giver.stripTrailing());
                if (userGiver == null){
                    System.out.println("ERROR: <Constraint Error: The giver does not exist>");
                    return null;
                }
                else if (userReceiver == null) {
                    System.out.println("ERROR: <Constraint Error: The Receiver does not exist>");
                    return null;
                }
                else if (userReceiver instanceof StandardSeller){
                    System.out.println("ERROR: <Constraint Error: User: " + userReceiver.name + " is the receiver of the gift is a seller>");
                    return null;
                }
                else return giftCheckerHelper(g_name.stripTrailing(), userGiver, userReceiver);
            }
        }
    }

    /**
     * A helper function that handles some constraints on the gift transactions.
     *
     */
    public static Gift giftCheckerHelper(String gameName, User giver, User receiver){
        Game toGiveInventory = InfoFinderAndModifier.inventoryCheck(giver, gameName, "inventory");
        Game toGiveGameToday = InfoFinderAndModifier.inventoryCheck(giver, gameName, "new");
        Game toGiveSell = InfoFinderAndModifier.findGame(gameName, giver.name, "onStock");
        Game toGiveSellToday = InfoFinderAndModifier.findGame(gameName, giver.name, "new");
        Game finalGame;

        if (toGiveGameToday != null){
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to give a game is purchased today>");
            return null;
        }
        else if (toGiveSellToday != null){
            System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to give a game is put on sale today>");
            return null;
        }
        else if (giver instanceof StandardSeller){
            if (toGiveSell == null){
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to give a game is not from the correct seller>");
                return null;
            }
            else finalGame = toGiveSell;
        }
        else if (giver instanceof StandardBuyer){
            if (toGiveInventory == null){
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " does not have the game>");
                return null;
            }
            else finalGame = toGiveInventory;
        }
        else {
            if (!(toGiveSell == null)) finalGame = toGiveSell;
            else if (!(toGiveInventory == null)) finalGame = toGiveInventory;
            else{
                System.out.println("ERROR: <Constraint Error: User: " + currUser.name + " attempt to give a game this user does not own or sell>");
                return null;
            }
        }
        if (finalGame.getOwners().contains(receiver.name)){
            System.out.println("ERROR: <Constraint Error: User: " + receiver.name + " has already had this game>");
            return null;
        }
        return new Gift(finalGame, giver, receiver);
    }

    /**
     * Check whether the logout is valid.
     * It is valid when the logout user is the same as the current user.
     *
     */
    public static boolean logoutChecker(String logoutName) {
        if (currUser == null) {
            System.out.println("ERROR: <Constraint Error: Attempt to logout with no user logged in>");
            return false;
        }

        if (currUser.name.equals(Standards.EMPTY_USERNAME))
            System.out.println("ERROR: <Constraint Error: Logout with no user name but the logout is processed>");

        if (!currUser.name.equals(logoutName) && !currUser.name.equals(Standards.EMPTY_USERNAME))
            System.out.println("ERROR: <Constraint Error: Logout with wrong user name but the logout is processed>");

        return true;
    }

    /**
     * Return the current logged in user.
     */
    public static User getCurrUser() {
        return currUser;
    }
}
