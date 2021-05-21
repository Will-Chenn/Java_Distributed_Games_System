package Users;

import StoreUtils.Game;
import StoreUtils.Store;
import Transactions.Transactions;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * A User class is the super class of all users (Standard Buyer-BS, Standard Seller-SS, Full Standard User-FS,
 * Admin User-AA) It saves personal information. It's the Visitable class in Visitor design pattern. It could
 * write all recorded information to User.txt which is the database for users.
 */
public abstract class User {

    public String name;
    public String password;
    public double credit;
    public String type;
    public double creditAddToday;
    public ArrayList<Game> myInventory = new ArrayList<>();

    /**
     * Construct a user with given name, password, credit, creditAddToday and updates inventory form
     * available games in Store.
     * @param name the name of this user
     * @param password the password of this user
     * @param credit the credit of this user
     * @param creditAddToday how much credit has user added today
     */
    public User(String name, String password, double credit, double creditAddToday) {
        this.name = name;
        this.password = password;
        this.credit = credit;
        this.creditAddToday = creditAddToday;
        updateInventory();
    }

    /**
     * Updates this user's inventory by looping all games which are purchasable and has this user as a owner.
     */
    public void updateInventory(){
        for (Game game : Store.allAvailableGames)
            if (game.getOwners().contains(name)) myInventory.add(game);
    }

    /**
     * Write all information of this user into the user database: Users.txt.
     * @param rw an instance of RandomAccessFile
     */
    public void writeInfo(RandomAccessFile rw){
        String name = "username: " + this.name + System.getProperty("line.separator");
        String password = "password: " + this.password + System.getProperty("line.separator");
        String credit = "credit: " + String.format("%.2f", this.credit) + System.getProperty("line.separator");
        String type = "type: " + this.type + System.getProperty("line.separator");
        String creditAddToday = "creditAddToday: " + String.format("%.2f", this.creditAddToday) + System.getProperty("line.separator");
        try {
            rw.write(name.getBytes(StandardCharsets.UTF_8));
            rw.write(password.getBytes(StandardCharsets.UTF_8));
            rw.write(credit.getBytes(StandardCharsets.UTF_8));
            rw.write(type.getBytes(StandardCharsets.UTF_8));
            rw.write(creditAddToday.getBytes(StandardCharsets.UTF_8));
            rw.write(System.getProperty("line.separator").getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * An abstract function. It allows given transaction could use visit() function and pass a user as the
     * parameter of it.
     * @param transaction a instance of any Transaction class or its child class
     */
    public abstract void accept(Transactions transaction);
}
