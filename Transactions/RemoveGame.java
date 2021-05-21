package Transactions;

import StoreUtils.Game;
import StoreUtils.InfoFinderAndModifier;
import StoreUtils.Store;
import Users.*;

/**
 * Class RemoveGame is a transaction class which implements Transaction interface. It will implement "remove games"
 * action for all kinds of users.
 */
public class RemoveGame implements Transactions{

    private Game toRemove;
    private String GameOwner;

    /**
     * Constructs a RemoveGame transaction which will contain the name of the game about to be removed, and the name
     * of game owner.
     * @param toRemove a game that is about to be removed
     * @param GameOwner a string of game owner's name
     */
    public RemoveGame(Game toRemove, String GameOwner) {
        this.toRemove = toRemove;
        this.GameOwner = GameOwner;
    }

    /**
     * Standard Buyers-BS can remove a game from their game inventory.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {
        remove(standardBuyer, "buyer");
    }

    /**
     * Standard Sellers-SS can remove a game that is sold by them.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {
        remove(standardSeller, "seller");
    }

    /**
     * Full Standard Users-FS can remove a game from their game inventory or remove a game that is sold by them.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {
        remove(fullStandardUser, "buyer");
        remove(fullStandardUser, "seller");
    }

    /**
     * Admin User-AA can remove a game from their game inventory or remove a game that is sold by them.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        if (GameOwner.equals(adminUser.name)){
            remove(adminUser, "buyer");
            remove(adminUser, "seller");
        }
        else {
            User user = InfoFinderAndModifier.findRelevantUser(GameOwner);
            user.accept(this);
        }
    }

    /**
     * A helper function which called by every valid visit function in RemoveGame class. It will remove the game from
     * the inventory or the user will no longer sell this game. After remove the game, it will print a message about
     * it including user's name and the game's name.
     * @param user any user who can remove games
     * @param type the type of this user determine the status of the game
     */
    private void remove(User user, String type) {
        if (type.equals("buyer")) {
            toRemove.setOwners(toRemove.getOwners().replace(user.name, ""));
            user.myInventory.remove(toRemove);
            System.out.println("User: " + user.name + " removes: " + toRemove.getName() + " from inventory");
        }

        else if (type.equals("seller")) {
            if (InfoFinderAndModifier.findGame(toRemove.getName(), user.name, "onStock") != null){
                Store.allAvailableGames.remove(toRemove);
                System.out.println("User: " + user.name + " stop selling: " + toRemove.getName());
            }
        }
    }
}
