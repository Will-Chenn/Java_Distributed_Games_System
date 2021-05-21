package Transactions;

import StoreUtils.InfoFinderAndModifier;
import TransactionReader.Standards;
import Users.*;

/**
 * Class AddCredit is a transaction class which implements Transaction interface. It will implement "add credit" action
 * for all kinds of users.
 */
public class AddCredit implements Transactions{

    private String username;
    private double creditAdded;

    /**
     * Constructs a AddCredit transaction which will contain the name of user who wants to add credit, string form of
     * the amount of the credit.
     * @param username a name of a user who wants to add credit
     * @param creditAdded a string about the number of credit
     */
    public AddCredit(String username, String creditAdded) {
        this.username = username;
        this.creditAdded = Double.parseDouble(creditAdded);
    }

    /**
     * Standard Buyers-BS can add credit to themselves.
     * @param standardBuyer a Standard Buyer-BS
     */
    @Override
    public void visit(StandardBuyer standardBuyer) {
        addCreditSelf(standardBuyer);
    }

    /**
     * Standard Sellers-SS can add credit to them selves.
     * @param standardSeller a Standard Seller-SS
     */
    @Override
    public void visit(StandardSeller standardSeller) {
        addCreditSelf(standardSeller);
    }

    /**
     * Full Standard Users-FS can add credit to them selves.
     * @param fullStandardUser a Full Standard User-FS
     */
    @Override
    public void visit(FullStandardUser fullStandardUser) {
        addCreditSelf(fullStandardUser);
    }

    /**
     * Admin Users-AA can add credit to them selves and they can add credit for other users as well.
     * @param adminUser an Admin User-AA
     */
    @Override
    public void visit(AdminUser adminUser) {
        if (!username.equals(adminUser.name)){
            User user = InfoFinderAndModifier.findRelevantUser(username);
            user.accept(this);
        }
        else
            addCreditSelf(adminUser);

    }

    /**
     * A helper function which called by every valid visit function in AddCredit class. Function will add credit for
     * every kind of users. If the credit exceeds the maximum of credit number after adding, the credit will become
     * the CREDIT_MAXIMUM. After all, it will print a message about it including user's name, credit numbers.
     * @param user a user who wants to add credit
     */
    private void addCreditSelf(User user) {
        user.credit += creditAdded;
        user.creditAddToday += creditAdded;
        if (user.credit > Standards.CREDIT_MAXIMUM)
            user.credit = Standards.CREDIT_MAXIMUM;
        InfoFinderAndModifier.userFileUpdater(user, "update");
        System.out.println("User: " + user.name + " adds " + creditAdded + " credit to " + username
                + " new credit: " + user.credit);
    }
}
