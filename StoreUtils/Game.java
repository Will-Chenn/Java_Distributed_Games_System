package StoreUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * A Game class contains the name, price, discount rate, name of seller and today's new owner of a game. It
 * could write all recorded infomation into Games.txt which is database of games.
 */
public class Game {

    private String name;
    private double price;
    private double discountRate;
    private String owners;
    private String sellerName;
    private String newOwners = "";

    /**
     * Constructs a game with given name, price, discount rate, name of seller, and owners of it.
     * @param name a string which is name of this game
     * @param price a double which is price of this game
     * @param discountRate a double which is discount rate of this game
     * @param sellerName a string which is the name of seller who sells this game
     * @param owners a string which is the owners of this game
     */
    public Game(String name, double price, double discountRate, String sellerName, String owners) {
        this.name = name;
        this.price = price;
        this.discountRate = discountRate;
        this.owners = owners;
        this.sellerName = sellerName;
    }

    /**
     * A function that gets the name of this game. Returns the name of this game.
     * @return a string which is name of this game.
     */
    public String getName() {
        return name;
    }

    /**
     * A function that gets the price of this game. Returns the price of this game.
     * @return a double which is price of this game.
     */
    public double getPrice() {
        return price;
    }

    /**
     * A function that gets the discount rate of this game. Returns the discount rate of this game.
     * @return a double which is discount rate of this game.
     */
    public double getDiscountRate() {
        return discountRate;
    }

    /**
     * A function that gets the owners of this game. Returns the owners of this game.
     * @return a string which is owners of this game.
     */
    public String getOwners() {
        return owners;
    }

    /**
     * A function that gets the name of seller of this game. Returns the name of seller of this game.
     * @return a string which is name of seller of this game.
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * A function that gets the names of users who bought this game today. Returns the the names of users
     * who bought this game today.
     * @return a sting which is the names of users who bought this game today.
     */
    public String getNewOwners() {
        return newOwners;
    }

    /**
     * A function sets the owners of this game.
     * @param owners a string which is the owners of this game.
     */
    public void setOwners(String owners) {
        this.owners = owners;
    }

    /**
     * A functions sets the price of this game.
     * @param price a double which is the price of this game.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * A function sets the new owners of this games. Users who bought the game today.
     * @param newOwners a string which is the username of users who bought the game today.
     */
    public void setNewOwners(String newOwners) {
        this.newOwners = newOwners;
    }

    /**
     * Write all information of this game into the user database: Games.txt.
     * @param rw an instance of RandomAccessFile
     */
    public void writeInfo(RandomAccessFile rw){
        String name = "name: " + this.name + System.getProperty("line.separator");
        String sellerName = "seller: " + this.sellerName + System.getProperty("line.separator");
        String price = "price: " + String.format("%.2f", this.price) + System.getProperty("line.separator");
        String discountRate = "discountRate: " + String.format("%.2f", this.discountRate) + System.getProperty("line.separator");
        String owners = "owners:" + this.owners + System.getProperty("line.separator");
        try {
            rw.write(name.getBytes(StandardCharsets.UTF_8));
            rw.write(sellerName.getBytes(StandardCharsets.UTF_8));
            rw.write(price.getBytes(StandardCharsets.UTF_8));
            rw.write(discountRate.getBytes(StandardCharsets.UTF_8));
            rw.write(owners.getBytes(StandardCharsets.UTF_8));
            rw.write(System.getProperty("line.separator").getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a string representation of this game, which includes name, price, discount rate, name of seller,
     * and owners' name.
     * @return a string representation of this game, which includes name, price, discount rate, name of seller,
     *         and owners' name.
     */
    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", discountRate=" + discountRate +
                ", sellerName='" + sellerName + '\'' +
                ", owners='" + owners + '\'' +
                '}';
    }
}
