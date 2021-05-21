package Users;

import Transactions.Transactions;
import TransactionReader.Standards;

/**
 * A FullStandardUser class is a child class of User. It represents a Full Standard User-FS.
 */
public class FullStandardUser extends User{

    /**
     * Construct a full standard user with given name, password, credit, creditAddToday and updates inventory form
     * available games in Store. The type of it is FS which represents full standard user.
     * @param name the name of this user
     * @param password the password of this user
     * @param credit the credit of this user
     * @param creditAddToday how much credit has user added today
     */
    public FullStandardUser(String name, String password, double credit, double creditAddToday) {
        super(name, password, credit, creditAddToday);
        this.type = Standards.USERTYPE_FULL;
    }

    /**
     * Allows given transaction could use visit() function and pass a FullStandardUser as the parameter of it.
     * @param transaction a instance of any Transaction class or its child class
     */
    @Override
    public void accept(Transactions transaction) {
        transaction.visit(this);
    }

    /**
     * Returns a string representation of a FullStandardUser, which includes name, password, credit, and type.
     * @return a string representation of a FullStandardUser, which includes name, password, credit, and type.
     */
    @Override
    public String toString() {
        return "FullStandardUser{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", credit=" + credit +
                ", type='" + type + '\'' +
                '}';
    }
}
