package Transactions;

import Users.AdminUser;
import Users.FullStandardUser;
import Users.StandardBuyer;
import Users.StandardSeller;

/**
 * A Transaction interface is a super class of all kinds of transactions. It is a Visitor class in the
 * Visit design pattern which will have 4 kinds of visit functions for Standard Buyer-BS, StandardSeller-SS,
 * FullStandardUser-FS and AdminUser-AA, respectively. Different transactions will lead to different results.
 */
public interface Transactions {
    /**
     * A visit function for Standard Buyer-BS implementing their transactions.
     * @param standardBuyer a Standard Buyer-BS
     */
    public void visit(StandardBuyer standardBuyer);

    /**
     * A visit function for Standard Seller-SS implementing their transactions.
     * @param standardSeller a Standard Seller-SS
     */
    public void visit(StandardSeller standardSeller);

    /**
     * A visit function for Full Standard User-FS implementing their transactions.
     * @param fullStandardUser a Full Standard User-FS
     */
    public void visit(FullStandardUser fullStandardUser);

    /**
     * A visit function for Admin User-AA implementing their transactions.
     * @param adminUser an Admin User-AA
     */
    public void visit(AdminUser adminUser);
}
