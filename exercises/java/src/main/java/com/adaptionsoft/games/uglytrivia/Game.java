package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

/*TODO:
   - move string messages to constants file
   - move constant literals to top as final variables
   - use board class, player class, deck class
*/
public class Game {
    ArrayList players = new ArrayList();
    int[] playerPositions = new int[6]; // array
    int[] playerPurses = new int[6]; // array
    boolean[] playerInPenaltyBox = new boolean[6];
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public Game(){
		setUpQuestions();
    }

	private void setUpQuestions() {
		for (int questionNumber = 0; questionNumber < 50; questionNumber++) {
			popQuestions.addLast("Pop Question " + questionNumber);
			scienceQuestions.addLast(("Science Question " + questionNumber));
			sportsQuestions.addLast(("Sports Question " + questionNumber));
			rockQuestions.addLast("Rock Question " + questionNumber);
		}
	}

	public boolean addPlayer(String playerName) {
	    players.add(playerName);
		resetPlayer(howManyPlayers());

		displayMessage(playerName + " was added");
		displayMessage("They are player number " + players.size());
		return true;
	}

	private void resetPlayer(int number) {
		resetPlayerPosition(number);
		resetPlayerPurse(number);
		resetPlayerInPenaltyBox(number);
	}

	private void resetPlayerInPenaltyBox(int number) {
		playerInPenaltyBox[number] = false;
	}

	private void resetPlayerPurse(int number) {
		playerPurses[number] = 0; //0 TO VARIABLE
	}

	private void resetPlayerPosition(int number) {
		playerPositions[number] = 0; //0 TO VARIABLE
	}

	//AMOUNTOFPLAYERS
	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		displayMessage(players.get(currentPlayer) + " is the current player");
		displayMessage("They have rolled a " + roll);
		if (currentPlayerCanRole(roll)){
			playerRolls(roll);
			askQuestion();
		}
	}

	private boolean currentPlayerCanRole(int roll) {
		boolean isRoleOdd = roll % 2 != 0;
		if (playerInPenaltyBox[currentPlayer] && isRoleOdd) {
			setIsGettingOutOfPenaltyBox(true);
			displayMessage(players.get(currentPlayer) + " is getting out of the penalty box");
		} else if (playerInPenaltyBox[currentPlayer] && !isRoleOdd) {
			displayMessage(players.get(currentPlayer) + " is not getting out of the penalty box");
			setIsGettingOutOfPenaltyBox(false);
			return false;
		}
		return true;
	}

	private void playerRolls(int roll) {
		playerPositions[currentPlayer] = playerPositions[currentPlayer] + roll;
		if (playerPositions[currentPlayer] > 11) {
			playerPositions[currentPlayer] = playerPositions[currentPlayer] - 12; // EXPLANATION?
		}

		displayMessage(players.get(currentPlayer)
				+ "'s new location is "
				+ playerPositions[currentPlayer]);
		displayMessage("The category is " + currentCategory());
	}

	private void setIsGettingOutOfPenaltyBox(boolean isGettingOut) {
		isGettingOutOfPenaltyBox = isGettingOut;
	}

	private void askQuestion() {
		switch (currentCategory()){
			case Pop: displayMessage(popQuestions.removeFirst());
				break;
			case Science: displayMessage(scienceQuestions.removeFirst());
				break;
			case Sports: displayMessage(sportsQuestions.removeFirst());
				break;
			case Rock: displayMessage(rockQuestions.removeFirst());
				break;
		}
	}
	
	
	private QuestionTypes currentCategory() {
		int playerPosition = playerPositions[currentPlayer];
		if (isNumberMultipleOf4(playerPosition, 0)) {
			return QuestionTypes.Pop;
		}
		if (isNumberMultipleOf4(playerPosition, 1)) {
			return QuestionTypes.Science;
		}
		if (isNumberMultipleOf4(playerPosition, 2)) {
			return QuestionTypes.Sports;
		}
		return QuestionTypes.Rock;
	}

	private boolean isNumberMultipleOf4(int number, int offset) {
		return number % 4 == offset;
	}

	public boolean wasCorrectlyAnswered() {
		if (playerInPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				displayMessage("Answer was correct!!!!");
				return isWinnerFromCorrectAnswer();
			} else {
				changePlayer();
				return true;
			}
		} else {
			displayMessage("Answer was corrent!!!!");
			return isWinnerFromCorrectAnswer();
		}
	}

	private boolean isWinnerFromCorrectAnswer() {
		addCoinToCurrentPlayer();
		displayMessage(players.get(currentPlayer)
				+ " now has "
				+ playerPurses[currentPlayer]
				+ " Gold Coins.");

		boolean winner = isCurrentPlayerWinner();
		changePlayer();

		return winner;
	}

	public boolean wrongAnswer(){
		displayMessage("Question was incorrectly answered");
		displayMessage(players.get(currentPlayer)+ " was sent to the penalty box");
		moveCurrentPlayerInPenaltyBox();
		changePlayer();
		return true;
	}


	private boolean isCurrentPlayerWinner() {
		return playerPurses[currentPlayer] != 6;
	}

	private void addCoinToCurrentPlayer() {
		playerPurses[currentPlayer]++;
	}

	private void moveCurrentPlayerInPenaltyBox() {
		playerInPenaltyBox[currentPlayer] = true;
	}

	private void changePlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) {
			currentPlayer = 0;
		}
	}

	private void displayMessage(Object message) {
		System.out.println(message);
	}
}
