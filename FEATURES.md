# Features

Our software application is the back-end of a digital distribution system that takes in a daily.txt
(generated by the front end) file where transactions are stored, process transactions, and modify the database 
based on the given transactions. In this file, we will introduce the features of the back end and
the database.

To start the system, simply run TransactionReader.java

To manipulate data, please use DatabaseManager(run AddInfo.java), or you could hardcode Games.txt and Users.txt, just
make sure you follow the format(see below for example).

To test the system, run tests in the Test package. Notice after everytime you run a test, the databases in the test
package will be manipulated. To run a test again, copy the main database from 'a2-mcdonald-s' and past it into the 
database in the test package that you just manipulated.

1. The Back End
   
    There are four packages in the back end.
   
    1) TransactionReader
        
        After initializing a store instance, the TransactionReader will read transactions from 
        daily.txt and pass them to CodeExecutor. The CodeExecutor will recognise each transaction and process them 
        accordingly. The processing of transactions requires the involvement of transactions and users classes, which 
        will be demonstrated below. After processing all the transactions, the TransactionReader will update the game 
        database.
   
    2) StoreUtils
       
        This is a utility package.
            
        All games will be stored in the store which is initialized at the beginning of a day(daily.txt) 
        and will update the database at the end of the day based on the games' information in the store. The 
        InfoFinderAndModifier is a helper class that retrieve user or game from databases and can modify the databases
        directly.
       
    3) Transactions and Users
    
        This two packages are designed based on the visitor pattern, where transactions are visitors and 
        users are visitable. We design it in this way because it is very likely that the client will want to 
        add more transaction needs in the future, while the types of users are relatively fixed. With the visitor design
        pattern, we could easily add a new transaction feature without changing the user class, because all the user
        does is just accepting the transaction, and the transaction will act differently based on the user type.
       
        Unlike games, user are updated in a timely(per transaction) basis. User instance will be initialized everytime 
        when a user logs in, or a transaction involves another user that is not the current logged-in user. Transaction
        will modify the user database directly with the help of InfoFinderAndModifier, but if the transaction changes 
        information of a game(e.g: changing its owner), the change will be made on the game instance in the store, and
        all games will be updated at the end of day(daily.txt).
        
        In addition, the transactions class is an interface and all specific transactions(e.g: buy) implement the 
        interface. We design it this way because the transaction class tells the specific transaction what to do(visit),
        the transaction directly modifies the database, and they do not necessary share same characteristics(auctions
        does not even have an attribute). However, we design the user class using the abstract class because they all 
        share common some characteristics, such as name, password, credit. The user abstract defines what a user is
        instead of promising what a user can do.
    
    4) DataManager
        This is a tool we made to help you manipulate the database. AddInfo does not directly modify the database, it 
        calls InfoFinderAndModifier to help. Simply run AddInfo.java and follow the instructions, very straightforward. 
        For the first command, please only input the abbreviation, i.e??? Q, AU, PU, AG, PG.  Notice we assume the 
        database are at the same file location with the system.
        For credit, please include the decimal. e.g: 12.99
        
    

2. The Database
    1) User.txt
    
       This file stores all the users, including their username, password, credit, type and 
       how much credit was added today
       for example:
       
            username: admin
            password: admin
            credit: 999999.99
            type: AA
            creditAddToday: 0.00
       
    2) Game.txt
       
        This file stores all the games available in the system, including games for sale and games in user's
        inventory. Information includes its name, price, discount rate if auction season, owner, seller and whether
        the auction sale is on or not.
        for example:
            
            name: Dealer's life 2
            price: 12.99
            discountrate: 10
            owners: abc
            sellername: aaa
        
        The first line of Game.txt is:
            
            auctionSale: off/on
        
     
3. Tests
   
    Simply run the tests in the test package to test the system. After all tests are finished and no error occurs, you
    could also compare the output with the corresponding Report.txt. Notice the database in the test package are 
    distinguished from the system database.


How our system work?

1) The TransactionReader class in the TransactionReader package will open the daily.txt. (We assume the daily.txt is in
   the same file path with the back end)
   
2) The TransactionReader class will initialize a store instance. The initialization includes the InfoFinderAndModifier 
   class reading game database(Games.txt), creating game instances based on the information from database and adding the
   games to the store.
   
3) The TransactionReader continues reading the transactions from daily.txt and passes it to the CodeExecutor, which 
   process the transaction.

4) For example, if the CodeExecutor recognizes a transaction that ABC buys the game GTA from AAA, the CodeExecutor 
   will call InfoFinderAndModifier to find ABC and AAA from User.txt, creating two user instances and execute the buy 
   action on each of them.
   
5) Assume ABC is a standard buyer. ABC executes the buy action by accepting the Buy class from Transaction package to 
   visit ABC. The Buy class will identify ABC to be a standard buyer and therefore call the buygame function, which
   calls the InfoFinderAndModifier to modify (deduct) ABC's balance directly in the user database, changes the 
   game's owner to be ABC, and adds it to ABC's inventory (inventory is just an attribute of the user instance and
   will not show up in the user.txt, created for checking purpose. e,g: a buyer cannot buy a game that is already in 
   its inventory.)
   
6) Assume AAA is a standard buyer. AAA executes the buy action by accepting the Buy class from Transaction package to 
   visit AAA. The Buy class will identify AAA to be a standard seller and therefore call the receivemoney function, 
   which calls InfoFinderAndModifier to modify(add) AAA's balance directly in the user database.
   
7) After processing all the transactions in daily.txt, the TransactionReader will update the game database by calling the 
   update function in the Store class. For example, the update function will call InfoFinderAndModifier to change the
   owner of AAA's game GTA to ABC.

