package hw2;

/**
 * Model of a Monopoly-like game. Two players take turns
 * rolling dice to move around a board. The game ends
 * when one of the players has at least MONEY_TO_WIN
 * money or one of the players goes bankrupt (they have
 * negative money).
 * 
 * @author John Murphy
 */
public class CyGame { 
	/**
	 * The endzone square type.
	 */
	public static final int ENDZONE = 0;
	/**
	 * The CyTown square type.
	 */
	public static final int CYTOWN = 1;
	/**
	 * The pay rent square type.
	 */
	public static final int PAY_RENT = 2;
	/**
	 * The fall behind square type.
	 */
	public static final int FALL_BEHIND = 3;
	/**
	 * The blizzard square type.
	 */
	public static final int BLIZZARD = 4;
	/**
	 * The pass class square type.
	 */
	public static final int PASS_CLASS = 5;
	/**
	 * Points awarded when landing on or passing over the endzone square.
	 */
	public static final int ENDZONE_PRIZE = 200;
	/**
	 * The standard rent payed to the other player when landing on a
	 * pay rent square.
	 */
	public static final int STANDARD_RENT_PAYMENT = 80;
	/**
	 * The cost to by CyTown.
	 */
	public static final int CYTOWN_COST = 200;
	/**
	 * The amount of money required to win.
	 */
	public static final int MONEY_TO_WIN = 400;
	/**
	 * An integer representing player 1's cash balance
	 */
	private int player1Cash = 0;
	/**
	 * An integer representing player 2's cash balance
	 */
	private int player2Cash = 0;
	/**
	 * An integer representing player 1's position on the board. 
	 */
	private int player1Pos = 0;
	/**
	 * An integer representing player 2's position on the board. 
	 */
	private int player2Pos = 0;
	/**
	 * An integer representing whose turn it is, 1 or 2 for players one and two. 
	 */
	private int turn = 1;
	/**
	 * An integer representing the ownership of cyTown. 0 for un-owned, and 1 or 2 for players 1 and 2. 
	 */
	private int cyTownOwnership = 0;
	/**
	 * An integer representing the total length of the board from endzone back to endzone. 
	 */
	private int boardSize;
	/**
	 * A boolean for whether the player has rolled the dice yet. 
	 */
	private boolean hasRolled;
	/**
	 * Constructor for the CyGame class. It initializes the boardsize and initial balance. 
	 * @param boardSize Integer for the length of the board from endzone to endzone. 
	 * @param balance Integer for the starting cash player 1 and 2 will start with. 
	 */
	public CyGame(int boardSize, int balance) {
		player1Cash = balance;
		player2Cash = balance;
		this.boardSize = boardSize;
	}
	/**
	 * buyCyTown is a void method used for when the player has enough money and decides to buy cytown. 
	 * cyTown can only be purchased if the player has already rolled, its not owned, and the player has enough cash. 
	 * There are two separate branches for player 1 and 2, but they are identical in logic. 
	 * After cyTown is purchased, the turn is automatically ended. 
	 */
	public void buyCyTown() {
		if(turn ==1 &&player1Cash>=CYTOWN_COST && cyTownOwnership == 0&&hasRolled){
			cyTownOwnership = 1;
			player1Cash-=CYTOWN_COST;
		}
		else if(turn == 2 && player2Cash >= CYTOWN_COST && cyTownOwnership ==0&&hasRolled) {
			cyTownOwnership = 2;
			player2Cash-=CYTOWN_COST;
		}
	endTurn();
	}
	/**
	 * Action is a private void method created for the cyGame class that is used
	 *  to handle most of the squares interactions with the player. 
	 *  It checks and pays the player for passing the endzone.
	 *   It also handles paying rent including paying double if cyTown is owned. 
	 *   It has two conditional branches, one for each player, but they are the same in logic. 
	 * @param moving is a boolean passed in indicating whether the player
	 *  is still traversing the board, or if it is the players intended position. 
	 */
	private void action(boolean moving) {
		if(turn == 1) {	
					 if(getSquareType(player1Pos) == ENDZONE&&moving) {
						 player1Cash+=ENDZONE_PRIZE;
					 }
				
				if(moving == false) {
				 if(getSquareType(player1Pos)== PAY_RENT) {
					 if(cyTownOwnership == 2) {
						 player1Cash -= 2*STANDARD_RENT_PAYMENT;
						 player2Cash+=2*STANDARD_RENT_PAYMENT;
					 }
					 else {
						 player1Cash-=STANDARD_RENT_PAYMENT;
						 player2Cash+=STANDARD_RENT_PAYMENT;
					 }
				 }
				}
			}
		else if(turn == 2) {	
			 if(getSquareType(player2Pos) == ENDZONE&&moving) {
				 player2Cash+=ENDZONE_PRIZE;
			 }
		if(moving == false) {
		 if(getSquareType(player2Pos)== PAY_RENT) {
			 if(cyTownOwnership == 1) {
				 player2Cash -= 2*STANDARD_RENT_PAYMENT;
				 player1Cash+=2*STANDARD_RENT_PAYMENT;
			 }
			 else {
				 player2Cash-=STANDARD_RENT_PAYMENT;
				 player1Cash+=STANDARD_RENT_PAYMENT;
			 }
		 }

		 
		}
	}
		}
	/**
	 * Roll is a method for CyGame that handles the dice rolling and movement of the player.
	 * It will take the roll and check if the plater is in a blizzard, if not, then the player will move one space at a time
	 * forward along the cyGame board. As it's traversing, action is being called in order to check for the endzone. 
	 * After the player has moved, it handles the cases where the player hit either the pass, or fall behind spaces. 
	 * The player is then moved according to the spaces rules, and then action is called with a non moving player to check for the rent space. 
	 * The turn will always end as long as the player is not on a cyTown space. Otherwise the player has to choose between endTurn() and buyCyTown() manually. 
	 * The method has two nearly identical sections for player 1 and player 2. 
	 * The method will do nothing if the game is over. 
	 * @param diceRoll an integer given as input representing the dice roll 1-6 for the player to move. 
	 */
	public void roll(int diceRoll) {
		hasRolled=true;
		if(!isGameEnded()) { 
			boolean pass = false;
			boolean moving = true;
			if(turn == 1) {
				if(getSquareType(player1Pos) ==BLIZZARD && diceRoll%2 == 0) {//-style: checks if the player is unable to move from a blizzard
					diceRoll = 0;
				}
			for(int i = 0; i<diceRoll;i++) { //style- for loop to move the player across the board one space at a time for a given roll. 
				player1Pos++;
				player1Pos %=boardSize;
				action(moving);	
			}		
			while((getSquareType(player1Pos) == PASS_CLASS && !pass) ||getSquareType(player1Pos) == FALL_BEHIND) {
				//-style This while loop is for when the player is either on pass or fall behind. the loop will move the player as intended until it is either no longer on a moving space, or it has already done a pass class space. 
				if(getSquareType(player1Pos) == PASS_CLASS) {
					for(int i =0; i<4;i++) {
						player1Pos++;
						player1Pos%=boardSize;
						action(moving);
					}
					pass = true;
				}
				if(getSquareType(player1Pos) == FALL_BEHIND) {
					player1Pos--;
				}
			}
		  moving = false;
		action(moving);	
		if(getSquareType(player1Pos) != CYTOWN) {	
			//-style ends turn unless player is on the cyTown square. 
			endTurn();
		}

		}
		else if(turn == 2) {
			if(getSquareType(player2Pos) ==BLIZZARD && diceRoll%2 == 0) {
				diceRoll = 0;
			}	
			for(int i = 0; i<diceRoll;i++) {
				player2Pos++;
				player2Pos %=boardSize;
				action(moving);		
			}	
			while((getSquareType(player2Pos) == PASS_CLASS && !pass) ||getSquareType(player2Pos) == FALL_BEHIND) {
				if(getSquareType(player2Pos) == PASS_CLASS) {
					for(int i =0;i<4;i++) {
						player2Pos++;
						player2Pos%=boardSize;
						action(moving);
					}
					pass = true;
				}
			if(getSquareType(player2Pos) == FALL_BEHIND) {
				player2Pos--;
			}
		}
		moving = false;
		action(moving);	
		if(getSquareType(player2Pos)!=CYTOWN) {
			endTurn();
		}
			
		}	
		}

	}
	/**
	 * getSquareType method takes in an integer that represents a players position.
	 * Using the rules given about where to find each type of square, this method converts the position of the player to its corresponding type of square
	 * 
	 * @param space integer representing the players position on the board. 
	 * @return returns one of the static integer variables defined that represent the different squares on the board. 
	 */
	public int getSquareType(int space) {
		if(space == 0) {
			return ENDZONE;
		}
		else if(boardSize - space <=1) {
			return CYTOWN;
		}
		else if(space%5==0) {
			return PAY_RENT;
		}
		else if(space % 7 == 0 || space % 11 == 0) {
			return FALL_BEHIND;
		}
		else if(space%3==0) {
			return BLIZZARD;
		}
		else {
			return PASS_CLASS;
			}
	}
	/**
	 * getCurrentPlayer returns whose turn it is in the ongoing cyGame as an integer. 
	 * @return returns whose turn it is in the game as an integer, either 1 or 2. 
	 */
	public int getCurrentPlayer() {
		return turn;
	}
	/**
	 * getPlayerSquare returns an integer representing the position of the player. 
	 * The player is decided by passing in a 1 or 2 into the integer parameter.
	 * @param player is the integer for which players information to return. 
	 * @return returns the type of square the player is standing on. 
	 */
	public int getPlayerSquare(int player) {
		if(player == 1) {
			return player1Pos;
		}
		else return player2Pos;
	}
	/**
	 * isPlayer1CyTownOwner indicates whether or not player 1 owns cytown.
	 * @return returns a boolean representing if player 1 owns cytown. 
	 */
	public boolean isPlayer1CyTownOwner() {
		if(cyTownOwnership == 1) return true;
		else return false;
	}
	/**
	 * isPlayer2CyTownOwner indicates whether or not player 2 owns cytown.
	 * @return returns a boolean representing if player 2 owns cytown. 
	 */
	public boolean isPlayer2CyTownOwner() {
		if(cyTownOwnership == 2) return true;
		else return false;
	}
	/**
	 * getPlayerMoney method returns the current balance of a given player. 
	 * @param player is an integer taken as input for which player to get their balance. 
	 * @return returns the given players cash balance as an integer. 
	 */
	public int getPlayerMoney(int player) {
		if(player ==1) return player1Cash;
		else if(player ==2) return player2Cash;
		else return 0;
	}
	/**
	 * isGameEnded is a method that determines whether the game has ended or not. 
	 * The game ends if either player reaches a balance of either "money_to_win" or a negative balance. 
	 * @return returns a boolean representing if the game has ended or not. 
	 */
	public boolean isGameEnded() {
		if(player1Cash >= MONEY_TO_WIN || player2Cash <0) {
			
			return true;
		}
		else if(player2Cash >= MONEY_TO_WIN || player1Cash <0) {
			return true;
		}
		else return false;
	}
	/**
	 * getOtherPlayer get's the player that isn't turning. for example, if its player 1's turn, the output is 2. 
	 * @return an int representing which player isn't doing a turn. 
	 */
	public int getOtherPlayer() {
		if(turn == 1) {
			return 2;
		}
		else if(turn ==2) {
			return 1;
		}
		else return 0;
	}
	/**
	 * endTurn is a void method that ends the players turn. It is automatically called as long as the player doesn't land on cyTown, or it can be called if the player cannot buy cyTown. 
	 * it simply flips the turns and changes the boolean to say the next player hasn't rolled yet. 
	 */
	public void endTurn() {
		hasRolled = false;
		if(turn==1) {
			turn=2;
		}
		else if(turn ==2) {
			turn = 1;
		}
	}
	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * 
	 * Player 1*: (0, false, $0) Player 2: (0, false, $0)
	 * 
	 * The asterisks next to the player's name indicates which players turn it
	 * is. The values (0, false, $0) indicate which square the player is on,
	 * if the player is the owner of CyTown, and how much money the player has
	 * respectively.
	 * 
	 * @return one-line string representation of the game state
	 */
	public String toString() {
		String fmt = "Player 1%s: (%d, %b, $%d) Player 2%s: (%d, %b, $%d)";
		String player1Turn = "";
		String player2Turn = "";
		if (getCurrentPlayer() == 1) {
			player1Turn = "*";
		} else {
			player2Turn = "*";
		}
		return String.format(fmt,player1Turn, getPlayerSquare(1), isPlayer1CyTownOwner(), getPlayerMoney(1),player2Turn, getPlayerSquare(2), isPlayer2CyTownOwner(), getPlayerMoney(2));
	}
}
