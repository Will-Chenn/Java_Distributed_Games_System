package Transactions;

import StoreUtils.InfoFinderAndModifier;
import Users.*;

/**
 * Class Delete is a transaction class which implements Transaction interface. It will implement "delete" action
 * for all kinds of users. However Delete is the feature that only Admin User-AA can use.
 */
public class Delete implements Transactions{

    private User toDelete;

    /**
     * Constructs a Delete transaction which will contain the user about to be removed.
     * @param toDelete the user about to be removed
     */
    public Delete(User toDelete) {
        this.toDelete = toDelete;
    }

    /**
     * Standard Buyers-BS can not use delete.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {}

    /**
     * Standard Sellers-SS can not use delete.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {}

    /**
     * Full Standard Users-FS can not use delete.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {}

    /**
     * Admin Users-AA will delete the user "toDelete" from database and a message will be printed including
     * the information of the user deleted.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        System.out.println(toDelete.toString() + " is deleted");
        InfoFinderAndModifier.userFileUpdater(toDelete, "delete");
    }
}
