1) login - start a Front End session
   no back end implementation

2) logout - end a Front End session
   no back end implementation

3) create - creates a new user with buying and/or selling privileges
   from font end: username, type, initial account balance

            Can only be used by admin users

            name: at most 15 characters
                  can contain special characters
                  can contain numbers
                  case sensitive: "AAA" amd "aAA" are different
                  cannot be all spaces, cannot end with a space
                  two users cannot have same name
                  cannot start with white space

            initial account balance: at most 999,999.99
                                     cannot be negative
                                     when a transaction reaches the max limit, just max out the balance
                                     and prompt a warning(except for refund, see below)



4) delete - cancel any games for sale by the user and remove the user account
   form front end: username

           Can only be used by admin users

           name: must be existing user
                 cannot be the current admin
                 can delete any type of user

           can delete a user with positive balance
           no further transactions should be accepted on a deleted user's behalf
           nor should other users be able to purchase their games for sale

5) sell – put up a game for sale
   from front end: game name, price, discount(during auctionsale)

          buyer cannot sell

          game name: maximum 25 characters
                     can contain special characters
                     can contain numbers
                     case sensitive

          price: maximum 999.99
                 cannot be negative

          discount: maximum 90

          one seller cannot sell a game twice
          cannot put a game up for sale for some other user
          a game can only be purchased on the next day after put up for sale
          Different sellers can sell the same game at a different price and at a different discount.
          cannot resell the game you bought


6) buy -  purchase an available game for sale
   from front end: game name, seller's username

          transfer the amount of price from buyer to seller and add the game to buyer's inventory

          seller cannot buy

          cannot purchase a game already in the user's inventory

          must have enough funds to buy

          cannot purchase a game that put up for sale by the owner themselves


7) refund - issue a credit to a buyer’s account from a seller’s account
   form front end: buyer username, seller username, amount   

          do not affect inventory
          does not need to match anything
          no refund limit
          admin can refund themselves
          seller must have sufficient balance
          if the refund reaches buyer's limit, do not process

8) addcredit - add credit into the system for the purchase of accounts
   from front end:
   if admin: amount and username
   if standard account: amount

          amount: maximum 1000/day for an account
          amount must be positive

9) auctionsale - change the prices of all games for sale to incorporate a seasonal discount
   only admin can do this
   can be turned on and off in the same day
   
           *round down when the price changes more than two decimals

           auctionsale has a 07 transaction code and uses the same format as login/logout/etc.


10) removegame - remove a game from a user's inventory or from being sold
from front end:
if admin: game name and game's owner
if non-admin: game name only

          cannot remove a game that was purchased or put up to sale on the same day

11) gift - give a user a game from another user
from front end:
if admin: game name, receiver, owner
if non-admin: game name, receiver

          game must be in the owner's inventory or put up for sale by the owner
          remove from inventory if the game was purchased
          cannot gift a game that was purchased or put up to sale on the same day
          cannot gift a game to a user who already owns a game
          Admin can gift any existed games
          a seller(not full standard user) cannot receive gift



12) Transaction format


      XX UUUUUUUUUUUUUUU TT CCCCCCCCC

      Where:

      XX
      is a two-digit transaction code: 00-login, 01-create, 02-delete, 06-addcredit, 10-logout

      UUUUUUUUUUUUUUU
      is the username

      TT
      is the user type (AA=admin, FS=full-standard, BS=buy-standard, SS=sell-standard)

      CCCCCCCCC
      is the available credit
    

      XX UUUUUUUUUUUUUUU SSSSSSSSSSSSSSS CCCCCCCCC

      Where:

      XX
      is a two-digit transaction code: 05-refund

      UUUUUUUUUUUUUUU
      is the buyer’s username

      SSSSSSSSSSSSSSS
      is the seller’s username

      CCCCCCCCC
      is the refund credit
    
      XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSS DDDDD PPPPPP

      Where:

      XX
      is a two-digit transaction code: 03-sell.

      IIIIIIIIIIIIIIIIIII
      is the game name

      SSSSSSSSSSSSSS
      is the seller’s username

      DDDDD
      Is the discount percentage

      PPPPPP
      is the sale price

      XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSSSS UUUUUUUUUUUUUU

      Where:

      XX
      is a two-digit transaction code: 04-buy.

      IIIIIIIIIIIIIIIIIII
      is the game name

      SSSSSSSSSSSSSSS
      is the seller’s username

      UUUUUUUUUUUUUUU
      is the buyer's username

      XX IIIIIIIIIIIIIIIIIII UUUUUUUUUUUUUUU SSSSSSSSSSSSSSS

      Where:

      XX
      is a two-digit transaction code: 08-removegame, 09-gift.

      IIIIIIIIIIIIIIIIIII
      is the game name

      UUUUUUUUUUUUUUU
      is the owner's username

      SSSSSSSSSSSSSSSS
      is the receiver's username

      Constraints:

      numeric fields are right justified, filled with zeroes (e.g., 005.00 for a 5$ game)

      alphabetic fields are left justified, filled with spaces (e.g. John Doe for account holder John Doe)

      unused numeric fields are filled with zeros (e.g., 0000)

      In a numeric field that is used to represent a monetary value or percentage, “.00” is appended to the end of the value 
      (e.g. 00110.00 for 110)

      unused alphabetic fields are filled with spaces (blanks) (e.g., Mike M         )

      all sequences of transactions begin with a login (00) transaction code and end with a logout (10) transaction code


Other: 

       upon fatal errors, report a waring and continue to the next transaction. Do not shut down
       the entire daily.txt

       we can assume the file will always be named daily.txt,
       and it will simply reside in our project directory waiting to be accessed

       for delete transactions, If the username is correct but the credit is wrong,
       they should still be deleted

       Difference between constraint error and fatal error: point out the constraint being violated, but the kinds of
       fatal error do not need to be identified

       A constraint error is something like an buy-standard user trying to sell something.
       A fatal error is something like reading a line in your daily transaction file that says "hello world"
