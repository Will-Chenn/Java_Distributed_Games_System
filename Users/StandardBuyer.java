package Users;

import Transactions.Transactions;
import TransactionReader.Standards;

/**
 * A StandardBuyer class is a child class of User. It represents a Standard Buyer-BS.
 */
public class StandardBuyer extends User{

    /**
     * Construct a standard buyer with given name, password, credit, creditAddToday and updates inventory form
     * available games in Store. The type of it is BS which represents standard buyer.
     * @param name the name of this user
     * @param password the password of this user
     * @param credit the credit of this user
     * @param creditAddToday how much credit has user added today
     */
    public StandardBuyer(String name, String password, double credit, double creditAddToday) {
        super(name, password, credit, creditAddToday);
        this.type = Standards.USERTYPE_BUYER;
    }

    /**
     * Allows given transaction could use visit() function and pass a StandardBuyer as the parameter of it.
     * @param transaction a instance of any Transaction class or its child class
     */
    @Override
    public void accept(Transactions transaction) {
        transaction.visit(this);
    }

    /**
     * Returns a string representation of a StandardBuyer, which includes name, password, credit, and type.
     * @return a string representation of a StandardBuyer, which includes name, password, credit, and type.
     */
    @Override
    public String toString() {
        return "StandardBuyer{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", credit=" + credit +
                ", type='" + type + '\'' +
                '}';
    }
}
