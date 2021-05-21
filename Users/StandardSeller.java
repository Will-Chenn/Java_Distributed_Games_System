package Users;

import Transactions.Transactions;
import TransactionReader.Standards;

/**
 * A StandardBuyer class is a child class of User. It represents a Standard Seller-SS.
 */
public class StandardSeller extends User{

    /**
     * Construct a standard seller with given name, password, credit, creditAddToday and updates inventory form
     * available games in Store. The type of it is SS which represents standard seller.
     * @param name the name of this user
     * @param password the password of this user
     * @param credit the credit of this user
     * @param creditAddToday how much credit has user added today
     */
    public StandardSeller(String name, String password, double credit, double creditAddToday) {
        super(name, password, credit, creditAddToday);
        this.type = Standards.USERTYPE_SELLER;
    }

    /**
     * Allows given transaction could use visit() function and pass a StandardSeller as the parameter of it.
     * @param transaction a instance of any Transaction class or its child class
     */
    @Override
    public void accept(Transactions transaction) {
        transaction.visit(this);
    }

    /**
     * Returns a string representation of a StandardSeller, which includes name, password, credit, and type.
     * @return a string representation of a StandardSeller, which includes name, password, credit, and type.
     */
    @Override
    public String toString() {
        return "StandardSeller{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", credit=" + credit +
                ", type='" + type + '\'' +
                '}';
    }
}
