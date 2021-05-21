package StoreUtils;

import java.util.ArrayList;

/**
 * A Store class is used to save status of auction sale and all games users could buy and could not buy.
 */
public class Store {

    public static ArrayList<Game> allAvailableGames = new ArrayList<>();
    public static ArrayList<Game> onStockTomorrow = new ArrayList<>();
    public static boolean isAuctionSaleOn;

    /**
     * A function imports all games from game database, creates instance of Game for each game and add it
     * into allAvailableGames.
     */
    public static void initialize(){
        InfoFinderAndModifier.gameImporter();
    }

    /**
     * A function updates game database, writes the status of auction sale and information for all games
     * in allAvailableGames and onStockTomorrow.
     */
    public static void update() {
        if (onStockTomorrow.size() != 0)
            allAvailableGames.addAll(onStockTomorrow);
        InfoFinderAndModifier.gameFileUpdater(allAvailableGames, "Store");
        InfoFinderAndModifier.clearAddCreditToday();
    }
}
