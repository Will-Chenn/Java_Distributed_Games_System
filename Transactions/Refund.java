package Transactions;

import StoreUtils.InfoFinderAndModifier;
import Users.*;

/**
 * Class Refund is a transaction class which implements Transaction interface. It will implement "refund" action
 * for all kinds of users. However Refund is the feature that only Admin User-AA can use.
 */
public class Refund implements Transactions{

    private User buyer;
    private User seller;
    private double creditRefund;

    /**
     * Constructs a Refund transaction which will contain the name of buyer, the name of seller and the credit
     * that seller need to pay.
     * @param buyer a buyer that get refund credit
     * @param seller a seller that pay refund credit
     * @param creditRefund the credit number
     */
    public Refund(User buyer, User seller, double creditRefund) {
        this.buyer = buyer;
        this.seller = seller;
        this.creditRefund = creditRefund;
    }

    /**
     * Standard Buyers-BS can not use refund.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {}

    /**
     * Standard Sellers-SS can not use refund.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {}

    /**
     * Full Standard Users-FS can not use refund.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {}

    /**
     * Admin Users-AA will add credit to "buyer" and minus credit of "seller". After that the information saved in
     * database will be update. Finally, it will print a message about it including admin user's name, number of
     * credit refunded, "buyer" credit, "seller" credit, "buyer's" name and "seller's" name.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        buyer.credit += creditRefund;
        seller.credit -= creditRefund;
        InfoFinderAndModifier.userFileUpdater(buyer, "update");
        InfoFinderAndModifier.userFileUpdater(seller, "update");
        System.out.println("User: " + adminUser.name + " processed a refund "
                + buyer.name + " gets a refund of: " + creditRefund + " credits "
                + "new credit: " + buyer.credit + " " + seller.name + " reduces " + creditRefund + " credits "
                + "new credit: " + seller.credit);
    }
}
