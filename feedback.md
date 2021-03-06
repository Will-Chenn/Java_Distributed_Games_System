# A2 Feedback

## Marking Scheme

For each criteria, you will get either a **[Y/N]**.

**Y => Yes**, as in you met the criteria.

**N => No**, as in you did not meet the criteria.

For the code design portion, we will be looking at the following checklist.
It is not limited to these criteria, however this is a guideline for the things we looked for.

## Code Design General Checklist:

We will be **looking for code smells** in this section as the purpose of this course is to teach **good code design** principles.

The reason we teach **design patterns** is that they typically help to **reduce code smells** (except Singleton, we don’t mess with that one).

However, having **design patterns is not mandatory** for this assignment, if they have **zero design patterns but at the same time their code is splendid**, they should get full marks (this may be difficult to achieve).

### Code Smells List

-   Duplicate Code (most important to avoid)

-   Magic Numbers

-   Bad variable names (if they're egregious; 'i' for a loop counter is fine, 'i' for a variable that tracks balance is not)

-   Encapsulation (shouldn’t have everything public)

-   Bad Formatting (indentation, line length, inconsistent brackets, etc)

-   Useless comments

-   Overly complex logic (more for loops, ifs than needed, not using loops when possible)

-   Inheritance (should make use of parent/child classes when appropriate)

-   Generally not clean and concise code

## Functionality [ 21 / 25 marks ]

For this section, we have prepared some `daily.txt` files to run on your program to
test for functionality.

This will not be publicly available, as this assignment may get re-used in future offerings, however we will give you feedback on what went wrong to justify your mark.

**Each transaction code is worth 2 marks**, with the **exception of Login and Logout** which is worth 2 marks together.

Apart from the transactions running and modifying the database as we expect it to,
we will also be checking for appropriate error messages upon **invalid transactions**.

These error messages are worth **5 marks**.

> ### Login/Logout [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks]

> ### Create [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks

> ### Delete [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks]

> ### Sell [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks]

> ### Buy [ 1 / 2 marks ]
>
> -   The transaction works as expected [2 marks]
> -   Some parts of the transaction work as expected [1 mark] [ Y ]
> -   It doesn’t work properly. [0 marks]

> ### Refund [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks]

> ### Addcredit [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks]

> ### Auctionsale [ 2 / 2 marks ]
>
> -   The transaction works as expected [2 marks] [ Y ]
> -   Some parts of the transaction work as expected [1 mark]
> -   It doesn’t work properly. [0 marks]

> ### Removegame [ 1 / 2 marks ]
>
> -   The transaction works as expected [2 marks]
> -   Some parts of the transaction work as expected [1 mark] [ Y ]
> -   It doesn’t work properly. [0 marks]

> ### Gift [ 1 / 2 marks ]
>
> -   The transaction works as expected [2 marks]
> -   Some parts of the transaction work as expected [1 mark] [ Y ]
> -   It doesn’t work properly. [0 marks]

> ### Appropriate Error Messages [ 4 / 5 marks ]
>
> -   Good and detailed error messages for the user. [5 marks] [ Y ]
> -   Decent error messages for the user, but could be improved for better User Experience. [2.5 mark]
> -   Missing or ambiguous error messages which are not helpful. [0 marks]

### Feedback:

-   Buy
    - Brought a game that was already in the inventory
    
-   Removegame
    - Game wasn't removed from DB
    
-   Gift
    - Was able to send a gift to a seller
    
-   Error Messages
    - Kept seeing "Invalid transaction code". It would have been nice to see
    what exactly was wrong with it, taking off **1 mark**.

## Code Architecture [ 5 / 5 marks ]:

> For this one, pick one of the following to mark as [Y]
>
> -   **Perfect**, they grasp the teachings of Michael and Sadia senpais. [5 marks] [ Y ]
> -   **Almost perfect**, slight areas where it can be improved [4 marks] 
> -   **Mediocre** job at code design, **notable code smells.** [3 marks] 
> -   **Poor job** at code design, **lots of code smells.** [2 marks] 
> -   Did not really try to make their code look nice, did the **minimal effort.** [1 mark] 
> -   **NaNi?????** If it hurts so bad, just hit ‘em with the **0.** [0 marks] 

### Feedback:

-   A lot of the methods in `AddInfo.java` are quite lengthy. This can be put into helper functions to reduce code length. 
-   Uses literal strings in many places, this should be replaced with constants in order to avoid the _magic number_ code smell. 
-   Good job on `CodeExecutor.java`, made use of regex effectively to parse the file. 
-   Good job on using constants in `Standards.java`
-   Amazing use of the visitor design pattern for the transactions! This is a great way to solve the LSP problem. 
-   Awesome job overall, you deserve the full marks despite the few things you can improve. 

## Proper Test Suite [ 9 / 10 marks ]:

For each transaction, we want to check for testing of **valid and invalid** inputs.
Particularly, if there are **no tests for invalid inputs**, the **maximum mark** they can get is **5 marks.**

**Test cases for each transaction code is worth 1 marks**, with the **exception of Login and Logout** which is worth 1 mark together.

A general guideline for good testing is that they tested each different input which leads to a different output, and asserts them accordingly.

> ### Login/Logout [ 0 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ N ]
> -   Tests invalid inputs [0.5 marks] [ N ]

> ### Create [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Delete [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Sell [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Buy [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Refund [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Addcredit [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Auctionsale [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Removegame [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

> ### Gift [ 1 / 1 mark ]
>
> -   Tests valid inputs [0.5 marks] [ Y ]
> -   Tests invalid inputs [0.5 marks] [ Y ]

### Feedback:

-   **Amazing test suite**
-   Just did not see any tests for logging in and logging out, so no marks for that one. 

## Requirements [ 9 / 10 marks ]:

There were a total of 70+ **good clarifications** made on Piazza, so a reasonable total number of clarifications for full marks is **20 good clarifications.**

If there are less than 20 good clarifications, the mark will be **X // 2** where **X** is the number of **good clarifications made**.

We have **5 important clarifications** which must be included.

**For each one** of the following clarifications which is not included, **deduct 1 mark.**

1. The Auctionsale transaction code is **07**.

2. For any mismatched length of transaction lines (ex. Incorrect number of XXX) => Use the numeric specifications outlined in the README, not the character representations of each transaction format.

3. A new 'day' occurs when your backend is executed, not based on any actual date/time

4. Inventories of _items that are bought_ **are treated differently** than lists of _items up for sale._

5. A full standard user cannot sell a game that they have purchased; the games that they have bought and sold must be tracked separately.

> -   Contains at least **20 good clarifications.** [ 10 / 10 marks ] [ Y ]
> -   Contains at least **X < 20 good clarifications.** [ X // 2 marks ] 

### Feedback:

-   More than **20 good clarifications**, so full marks 
-   Contains **1, 2, 4, 5**
-   Does not contain **3**, one mark deduction.   

## Documentation [ 5 / 5 marks ]:

Contains complete and well written JavaDocs.

> -   Mostly **well-done**, with minor issues. [5 marks] [ Y ]
> -   **Some parts well-done** or at least a **good attempt**, other parts have major issues (missing or incorrect format). [2.5 marks] 
> -   All or almost all javadocs are **completely wrong or missing.** [0 marks] 

### Feedback:

-   Great documentation throughout!
-   Only nitpick is to consider adding some comments throughout your code to explain some of the complex code blocks. 


## Use of Scrum [ 5 / 5 marks ]:

> -   A generally well-organized product backlog, that clearly follows scrum and has **all or almost all** the required info, may have minor issues but overall is good. [5 marks] [ Y ]
> -   Clear attempt to do the above (beyond starter example), but **has issues** [2.5 marks] 
> -   **No attempt or extremely poor** attempt to do the above [0 marks] 

### Feedback:

-   Amazing scrum backlog, great job

## Use of Git [ 5 / 5 marks ]:

> -   **Perfectly** follows the workflow in the A2 README handout [5 marks] [ Y ]
>     -   Uses properly named branches for each task
>     -   Has useful commit messages
>     -   Has multiple commits throughout the assignment work period rather than all at the end
>     -   May have some minor issues, but overall pretty gucci.
> -   Clear attempt to do the above, but has notable issues. [2.5 marks] 
> -   Git is not used well at all. [0 marks] 

### Feedback:

-   Amazing use of git, good commits and good use of branches and PRs.

## Bonus [ 0 / 10 marks ]:

### Extra Features [ 0 / 5 marks ]:

> -   At least **one good and unique** bonus feature which works as specified in FEATURES.md
> -   If the bonus feature is incomplete or does not function as expected, no marks will be given.

### Frontend [ 0 / 10 marks ]:

> -   The frontend is **“awe inspiring”, GUI, and generates a valid daily.txt** for the backend to process [10 marks]
> -   A fully functioning frontend which can either be a GUI or CLI.
> -   Must generate a daily.txt (**0 marks if it does not**)
> -   **Error checks inputs** when appropriate
> -   Provides **user feedback** upon valid and invalid (if possible) inputs.
> -   Database manipulation directly from the frontend is **0 marks**

### Feedback:

-   [insert feedback here]

## Final Adjustments (up to 20% boost/deduction)

As we know, group assignments can be difficult and as such, we had students fill out Peer Evaluations to see if there was a clear imbalance in workload.

If we deem that some group members were not productive and did not contribute to the group project, we will make adjustments as necessary.

The **maximum total adjustment is 20%**, this means if 1 person did not do work, and 3 people did, then the person who did not do work will be **deducted 20%** and the 3 people who did do work will be **boosted by 20/3 %** each.

The total deduction and boost must match.

### Adjusted marks

#### Total Unadjusted: 98/100

##### [baijiaha] : 98/100

##### [xujiefu] : 98/100

##### [yuanhanx] : 98/100

##### [chenw242] : 98/100
