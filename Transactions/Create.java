package Transactions;

import StoreUtils.InfoFinderAndModifier;
import Users.*;

/**
 * Class Create is a transaction class which implements Transaction interface. It will implement "create" action
 * for all kinds of users. However Create is the feature that only Admin User-AA can use.
 */
public class Create implements Transactions{

    private User newUser;

    /**
     * Constructs a Delete transaction which will contain the user about to be created.
     * @param user the user about to be created
     */
    public Create(User user) {
        this.newUser = user;
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
     * Admin Users-AA will create the user "newUser" then record the information in database and a message
     * will be printed including the information of the user created.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        InfoFinderAndModifier.addNewUser(newUser);
        System.out.println("New User: " + newUser.toString() + " is created");
    }
}
