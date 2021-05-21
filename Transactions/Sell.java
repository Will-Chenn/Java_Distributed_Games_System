package Transactions;

import StoreUtils.Game;
import StoreUtils.InfoFinderAndModifier;
import Users.*;

/**
 * Class Sell is a transaction class which implements Transaction interface. It will implement "sell" action
 * for all kinds of users.
 */
public class Sell implements Transactions{

    private Game game;

    /**
     * Constructs a Sell transaction which will contain seller's name, what game is selling, what is the discount rate
     * of the game and what is the price of the game.
     * @param gameName the selling game's name
     * @param sellerName the name of seller
     * @param discountRate the discount rate of the game when auction sale open
     * @param price the price of the game
     */
    public Sell(String gameName, String sellerName, String discountRate, String price) {
        double discountRate_ = Double.parseDouble(discountRate);
        double price_ = Double.parseDouble(price);
        game = new Game(gameName, price_, discountRate_, sellerName, "");
    }

    /**
     * Standard Buyers-BS can not sell a game, so visit a Standard Buyer-BS will not do anything in Sell class.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {}

    /**
     * Standard Sellers-SS can put a game on sale but this game will be purchasable tomorrow.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {
        sellGame(standardSeller, game);
    }

    /**
     * Full Standard Users-FS can put a game on sale but this game will be purchasable tomorrow.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {
        sellGame(fullStandardUser, game);
    }

    /**
     * Admin Users-AA can put a game on sale but this game will be purchasable tomorrow.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        sellGame(adminUser, game);
    }

    /**
     * A helper function which called by every valid visit function in Sell class. This function will put
     * the game which seller wants to sell into onStockTomorrow of Store and prints a message about it including the
     * name of seller and the game.
     * @param user any user who can sell games
     * @param game the game that user want to sell
     */
    private void sellGame(User user, Game game) {
        InfoFinderAndModifier.addNewGame(game);
        System.out.println("User: " + user.name + " just release a new game: " + game.getName() +" which is available tomorrow");
    }
}
