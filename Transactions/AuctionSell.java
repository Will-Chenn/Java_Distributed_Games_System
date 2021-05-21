package Transactions;

import StoreUtils.Game;
import StoreUtils.Store;
import TransactionReader.Standards;
import Users.AdminUser;
import Users.FullStandardUser;
import Users.StandardBuyer;
import Users.StandardSeller;

/**
 * Class AuctionSell is a transaction class which implements Transaction interface. It will implement "auction sell"
 * action for all kinds of users. However AuctionSell is the feature that only Admin User-AA can use.
 */
public class AuctionSell implements Transactions{

    /**
     * Standard Buyers-BS can not use AuctionSell.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {}

    /**
     * Standard Sellers-SS can not use AuctionSell.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {}

    /**
     * Full Standard Users-FS can not use AuctionSell.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {}

    /**
     * Admin Users-AA will open the AuctionSell for all games that have a non-zero discount rate. The status of
     * isAuctionSaleOn in Store will become true. After all, it will print a message about it to tell that auction sell
     * opened.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        if (!Store.isAuctionSaleOn){
            for (Game game: Store.allAvailableGames)
                if (game.getDiscountRate() != Standards.MIN_DISCOUNT_RATE)
                    game.setPrice(game.getPrice() * (100 - game.getDiscountRate()) / 100);
            Store.isAuctionSaleOn = true;
            System.out.println("User: " + adminUser.name + " just opened auction sell! :)");
        }
        else {
            for (Game game: Store.allAvailableGames)
                if (game.getDiscountRate() != Standards.MIN_DISCOUNT_RATE)
                    game.setPrice(game.getPrice() * 100 / (100 - game.getDiscountRate()));
            Store.isAuctionSaleOn = false;
            System.out.println("User: " + adminUser.name + " just closed auction sell :(");
        }
    }
}
