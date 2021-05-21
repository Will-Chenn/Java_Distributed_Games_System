package Users;

import Transactions.Transactions;
import TransactionReader.Standards;

/**
 * An AdminUser class is a child class of User. It represents an Admin User-AA.
 */
public class AdminUser extends User{

    /**
     * Construct am admin user with given name, password, credit, creditAddToday and updates inventory form
     * available games in Store. The type of it is AA which represents admin user.
     * @param name the name of this user
     * @param password the password of this user
     * @param credit the credit of this user
     * @param creditAddToday how much credit has user added today
     */
    public AdminUser(String name, String password, double credit, double creditAddToday) {
        super(name, password, credit, creditAddToday);
        this.type = Standards.USERTYPE_ADMIN;
    }

    /**
     * Allows given transaction could use visit() function and pass an AdminUser as the parameter of it.
     * @param transaction a instance of any Transaction class or its child class
     */
    @Override
    public void accept(Transactions transaction) {
        transaction.visit(this);
    }

    /**
     * Returns a string representation of an AdminUser, which includes name, password, credit, and type.
     * @return a string representation of an AdminUser, which includes name, password, credit, and type.
     */
    @Override
    public String toString() {
        return "AdminUser{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", credit=" + credit +
                ", type='" + type + '\'' +
                '}';
    }
}
