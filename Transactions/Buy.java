package Transactions;

import StoreUtils.Game;
import StoreUtils.InfoFinderAndModifier;
import TransactionReader.CodeExecutor;
import TransactionReader.Standards;
import Users.*;

/**
 * Class Buy is a transaction class which implements Transaction interface. It will implement "buy" action
 * for all kinds of users. But users can not buy a game that is already in the inventory.
 */
public class Buy implements Transactions{
    private Game game;

    /**
     * Constructs a Buy transaction which will contain the game about to be bought.
     * @param game a game about to be bought
     */
    public Buy(Game game) {
        this.game = game;
    }

    /**
     * Standard Buyers-BS can buy a game from users who sell the game and add game into inventory, then pay the credit.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {
        buyGame(standardBuyer);
    }

    /**
     * Standard Sellers-SS can give a copy of game to users who buy the game, then get the credit.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {
        receiveMoney(standardSeller);
    }

    /**
     * Full Standard User-FS can give a copy of game to users who buy the game then get the credit if they sell the
     * game. Full Standard User-FS can buy a game from users who sell the game then pay the credit if they want to
     * add game into inventory.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {
        if (CodeExecutor.getCurrUser() != fullStandardUser)
            receiveMoney(fullStandardUser);

        else buyGame(fullStandardUser);
    }

    /**
     * Admin Users-AA can give a copy of game to users who buy the game then get the credit if they sell the game.
     * Admin Users-AA can buy a game from users who sell the game then pay the credit if they want to
     * add game into inventory.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        if (CodeExecutor.getCurrUser() != adminUser)
            receiveMoney(adminUser);

        else buyGame(adminUser);
    }

    /**
     * A helper function which called by the users who want to add this game into their inventories and minus
     * credit.
     * @param user a user who wants to add this game into inventory
     */
    private void buyGame(User user) {
        user.credit -= this.game.getPrice();
        InfoFinderAndModifier.userFileUpdater(user, "update");
        this.game.setNewOwners(this.game.getNewOwners() + " " + user.name);
        this.game.setOwners(this.game.getOwners() + " " + user.name);
        user.myInventory.add(this.game);
        System.out.println("User: " + user.name + " bought " + game.getName()
                + " with: " + game.getPrice() + " from " + game.getSellerName()
                + " new credit: " + user.credit);
    }

    /**
     * A helper function which called by the users who give a copy of game to buyers and add
     * credit.
     * @param user a user who give a copy of game to buyers
     */
    private void receiveMoney(User user) {
        user.credit += this.game.getPrice();
        if (user.credit > Standards.CREDIT_MAXIMUM)
            user.credit = Standards.CREDIT_MAXIMUM;
        InfoFinderAndModifier.userFileUpdater(user, "update");
        System.out.println("User: " + user.name + " sold one copy of " + game.getName()
                + " with: " + game.getPrice() + " new credit: " + user.credit);
    }

    /**
     * A getter that get the game which is purchased.
     * @return a game that is purchased
     */
    public Game getGame() {
        return game;
    }
}
