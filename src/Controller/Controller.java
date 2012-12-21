package Controller;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.SwingUtilities;
import NeuralNet.*;
import Player.*;
import UI.*;


public class Controller extends Thread{
	
	
	private final static int O = 1;
	private final static int X = 2;
	private final static int ALTERNATE = 0;
	private Player currentPlayer;
	private Player[] playerList = new Player[2];
	private static int winner = 0;
	private int X_win = 0;
	private int O_win = 0;
	private int draw = 0;
	private int totalGame = 0;
	private static int nextPlayer = 0;
	final static String PATH = "../src/Database.data";
	public int RANDOM_PLAYER = 1;
	public int AI_PLAYER = 0;
	public int TRAINED = 0;
	public int UNTRAINED = 1;
	private World world;
	private Gomoku ui;
	private int dimension;
	private int  win_by;
	private static int prevMove;
	public boolean gameRun,reset;
	private ArrayList <ArrayList<Integer>> winning_sequences; 
	private int RLUse;
	private NeuralNet trained;
	private NeuralNet unTrained;
	public int delay = 500;
	private int trainTime = 0;
	private int firstPlayer = ALTERNATE;
	private int trainOponent;
	private int Ooponent;
	public int  totalTrain = 0;
	public boolean train = false;
	private Database database;
	private ArrayList<ArrayList<Integer>> sequence4;
	public static int errorCounter = 0;
	public Controller(Gomoku t,World _world,NeuralNet _trained,NeuralNet _unTrained,Database db, int _dimension, int winBy)
	{
		world = _world;
		ui = t;
		dimension = _dimension;
		win_by = winBy;
		gameRun = false;
		reset = false;
		RLUse = TRAINED;
		Ooponent = RANDOM_PLAYER;
		trainOponent = 1;
		trained = _trained;
		unTrained = _unTrained;
		database = db;
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				while(gameRun)
					//doTrainning();
					doPlay();
				sleep(delay);
				while(train)
					doTrainning();
				sleep(delay);
			}
		}catch(InterruptedException e){}
	}
	private void setPieceType(int pieceType, Player player)
	{
		player.setType(pieceType);
	}
	public void chooseFirstPlayer()
	{
		if(playerList[0].getPieceType() == firstPlayer)
			currentPlayer = playerList[0];
		else if(playerList[1].getPieceType() == firstPlayer)
			currentPlayer = playerList[1];
		else if(firstPlayer == ALTERNATE)
		{
			currentPlayer = playerList[nextPlayer];
			if(nextPlayer == 0)
			{
				nextPlayer = 1;
			}
			else
				nextPlayer = 0;
		}
		else
		{
			Random rng = new Random();
			currentPlayer = playerList[rng.nextInt(2)];
		}
		
	}
	public void setFirstPlayer(int first)
	{
		firstPlayer = first;
	}
	private void play()
	{
		int move = currentPlayer.move();
		int pieceType = currentPlayer.getPieceType();
		prevMove = move;
		world.setPiece(pieceType, move);
		if(currentPlayer == playerList[0])
			currentPlayer = playerList[1];
		else
			currentPlayer = playerList[0];
		
	}
	
	private boolean gameOver(Player player)
	{
		int[] availableMove = world.getAvailableMove();
		boolean found = false;
		boolean winnerFound;
		int[] _board = world.getBoard();
		for (int i = 0; i < winning_sequences.size(); i++) {
			for (int pieceType = 1; pieceType <  3; pieceType++) {
				winnerFound = true;
				boolean x = false;
				boolean o = false;
				for (int j = 0; j < win_by; j++)
				{
					int index = winning_sequences.get(i).get(j);
					if (_board[index] != pieceType)
						winnerFound = false;
					if(_board[index] == X)
						x = true;
					if(_board[index] == O)
						o = true;
				}
				//2 diff piece on same line, remove that line.
				if(x && o)
				{
					if(checkBlocking(winning_sequences.get(i)) && trainOponent == AI_PLAYER)
						player.updateQValues(2);
					winning_sequences.remove(i);
					i--;
					pieceType = 3;
				}
				if (winnerFound) {
					winner = pieceType;
					found = true;
					world.setWinLine(winning_sequences.get(i));
					world.win = true;
				}
			}
		}
		if(availableMove.length == 0)
			found = true;
		return found;
	}
	public static int getPreviousMove()
	{
		return prevMove;
	}
	
	public void setTrainTime(int times)
	{
		trainTime = times;
	}
	
	
public void selectRLPlayer(int type)
{
	RLUse = type;
}

public void setNumberOfTrainning(int numTrains)
{
	trainTime = numTrains;
}
public void doTrainning()
{
	for(int i = 0; i < trainTime; i++)
	{
		world.resetBoard();
		winner = 0;
		prevMove = -1;
		//Chose player.
		RandomPlayer p2 = new RandomPlayer(world);
		AIPlayer p1 = new AIPlayer(world);
		winning_sequences = p1.winning_sequences(dimension);
		sequence4 = p1.setUpConstraintsBy4(dimension);
		RLPlayer p3 = new RLPlayer(unTrained, world);
		if(trainOponent == AI_PLAYER)
			playerList[0] = p1;
		else 
			playerList[0] = p2;
		playerList[1] = p3;
		setPieceType(O, playerList[0]);
		setPieceType(X, p3);
		chooseFirstPlayer();
		
		while(!gameOver(p3))
		{
			check4inARow(p3);
			play();
		}
		p3.updateQValues(winner);
		
	}

	
	totalTrain = totalTrain + trainTime;
	ui.enableButton();
	train = false;
	world.resetBoard();
	winner = 0;
	prevMove = -1;
}
public void doPlay() throws InterruptedException
{
	world.resetBoard();
	prevMove = -1;
	winner = 0;

	SwingUtilities.invokeLater(ui);
	sleep(delay);
	//Chose player
	AIPlayer p1 = new AIPlayer(world);
	winning_sequences = p1.winning_sequences(dimension);
	//sequence4 = p1.setUpConstraintsBy4(dimension);
	RandomPlayer p2 = new RandomPlayer(world);
	RLPlayer p3 = new RLPlayer(trained, world);
	p3.play = true;
	RLPlayer p4 = new RLPlayer(unTrained, world);
	p4.play = true;
	if(RLUse == TRAINED)
		playerList[1] = p3;
	else
		playerList[1] = p4;
	if(Ooponent == AI_PLAYER)
		playerList[0] = p1;
	else
		playerList[0] = p2;
	
	setPieceType(O, playerList[0]);
	setPieceType(X, playerList[1]);
	
	chooseFirstPlayer();
	while(!gameOver(playerList[1]) && gameRun)
	{
		//check4inARow(p3);
		play();
		SwingUtilities.invokeLater(ui);
		sleep(delay);
		//printTest(world.getAvailableMove());
		//printResult(unTrained);
		//sleep(delay * 2);
	}
	/*if(winner == 1)
		System.out.println("LOSE");
	if(winner == 2)
		System.out.println("WIN");*/
	totalGame++;
	if(winner == X)
		X_win++;
	else if(winner == O)
		O_win++;
	else
		draw++;
	ui.updateStat(O_win,X_win,draw,totalGame);
	SwingUtilities.invokeLater(ui);
	sleep(delay + delay);
	
}
public void resetScore()
{
	totalGame = 0;
	X_win = 0;
	O_win = 0;
	draw = 0;
}
public void selectOPlayer(int type)
{
	Ooponent = type;
}
public void setTrainOponent(int type)
{
	trainOponent = type;
}
public void resetTrain()
{
	totalTrain = 0;
	unTrained = new NeuralNet(dimension);
	ui.trainText.setText("0");
	SwingUtilities.invokeLater(ui);
}
public void check4inARow(RLPlayer player)
{
	int[] _board = world.getBoard();
	for(int i = 0; i < winning_sequences.size(); i++)
	{
		int empty = 0;
		boolean x = false;
		boolean o = false;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int j = 0; j < win_by; j++)
		{
			int index = winning_sequences.get(i).get(j);
			if (_board[index] == 0)
				empty++;
			if(_board[index] == X)
			{
				temp.add(index);
				x = true;
			}
			if(_board[index] == O)
			{
				o = true;
				temp.add(index);
			}
		}
		if(empty == 1 && sequence4.contains(temp))
		{
			if(o)
			{
				//System.out.println("P");
				player.updateQValues(1);
			}
			if(x && trainOponent != AI_PLAYER)
			{
				//System.out.println("R");
				player.updateQValues(3);
			}
			sequence4.remove(temp);
		}
	}
}
	public void printBoard(int[] board) {
		for (int i = 0; i < dimension; i++) {
		System.out.print("|");
		for (int j = 0; j < dimension; j++) {
		switch (board[i * dimension + j]) {
		case 1:
			System.out.print("O");
		break;
		case 2:
			System.out.print("X");
		break;
		default:
			System.out.print(" ");
		}
			System.out.print("|");
		}
			System.out.println();
		}
			System.out.println();
}
	public  void printTest(int[] board)
	{
		for(int i =0; i < board.length; i++)
			System.out.print(board[i] + " ");
		System.out.println();
	}
	public static void printResult(NeuralNet nn)
	{
		int pos = 0;
		for(Neuron nr : nn.outputLayer)
		{
			System.out.print(pos + ": " + nr.getOutput() + "   ");
			pos++;
		}
			System.out.println();
	}
	public boolean checkBlocking(ArrayList<Integer> line)
	{
		int count = 0;
		int[] board = world.getBoard();
		for(int i = 0; i < line.size(); i++)
		{
			int index = line.get(i);
			if(board[index] == O)
				count++;
		}
		if(count >= 3)
			return true;
		else
			return false;
	}
}
