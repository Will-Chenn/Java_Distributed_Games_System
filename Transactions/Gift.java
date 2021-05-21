package Transactions;

import StoreUtils.Game;
import Users.*;

/**
 * Class Gift is a transaction class which implements Transaction interface. It will implement "gift" action for
 * all kinds of users. And the receiver of the gift will never be a seller.
 */
public class Gift implements Transactions{

    private Game game;
    private User giver;
    private User receiver;

    /**
     * Constructs a Refund transaction which will contain the game as gift, the name of giver and the name
     * of receiver.
     * @param game a game as gift
     * @param giver the user wants to give gift
     * @param receiver the user receive the game
     */
    public Gift(Game game, User giver, User receiver) {
        this.game = game;
        this.giver = giver;
        this.receiver = receiver;
    }

    /**
     * Standard Buyers-BS will choose a game from game inventory to receiver.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {
        giftDistributor(standardBuyer, "buyer");
    }

    /**
     * Standard Sellers-SS will choose a game copy from the list of games that they sold to receiver.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {
        giftDistributor(standardSeller, "seller");
    }

    /**
     * Full Standard Users-FS will choose a game from game inventory to receiver or will choose a game copy
     * from the list of games that they sold to receiver.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {
        if (game.getSellerName().equals(fullStandardUser.name))
            giftDistributor(fullStandardUser, "seller");

        else if (game.getOwners().contains(fullStandardUser.name))
            giftDistributor(fullStandardUser, "buyer");
    }

    /**
     * Admin Users-AA will choose a game from game inventory to receiver or will choose a game copy
     * from the list of games that they sold to receiver.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        if (game.getSellerName().equals(adminUser.name))
            giftDistributor(adminUser, "seller");

        else if (game.getOwners().contains(adminUser.name))
            giftDistributor(adminUser, "buyer");

        else
            giver.accept(this);
    }

    /**
     * A helper function which called by every valid visit function in Gift class. It will move the game from giver's
     * inventory or send a copy of the game. After give the game, it will print a message about it including giver's
     * name, the game's name and receiver's game.
     * @param user the user who wants to give gift
     * @param type the type of this user determine the status of the game
     */
    private void giftDistributor(User user, String type) {
        if (type.equals("buyer"))
            game.setOwners(game.getOwners().replace(user.name, receiver.name));

        else if (type.equals("seller"))
            game.setOwners(game.getOwners() + " " + receiver.name);

        System.out.println("User: " + user.name + " sends a gift: " + game.getName() + " to " + receiver.name);
    }
}
