package Player;
import java.util.Random;

import Controller.World;
import NeuralNet.NeuralNet;


public class RLPlayer extends Player{

	NeuralNet nn;
	private World world;
	private int[] prevInput;
	private int[] currInput;
	private double[] prevQValues;
	private double[] currQValues;
	private int prevMove;
	private double reward;
	
	private int state;
	private double alpha = 0.9;
	private double gamma = 0.9;
	private double epsilon = 0.2;
	private boolean gameEnd = false;
	public boolean play;
	public RLPlayer(NeuralNet net, World board)
	{
		nn = net;
		world = board;
		prevInput = new int[world.size() * 2];
		currInput = new int[world.size() * 2];
		prevQValues = new double[world.size()];
		currQValues = new double[world.size()];
		state = 0;
		reward = 0;
		play = false;
	}
	public int move()
	{
		int move = -1;
		if(play)
			move = play();
		else
			move = train();
		return move;
	}
	private int train() {
		
		//Save old input, Qvalues.
		prevInput = currInput;
		prevQValues = currQValues;
		
		//Calculate QValues for the current state.
		int[] input = getNeuralInput();
		currInput = input;
		nn.setInput(input);
	
		nn.calculateOutput();
		currQValues = nn.getQValues();
		
		//Get next move
		int move = -1;
		int[] availableMove = world.getAvailableMove();
		if(!gameEnd)
		{
			if(Math.random() > epsilon)
			{
				double max = Double.MIN_VALUE;
				for(int i = 0; i < availableMove.length; i++)
				{
					int index = availableMove[i];
					if(currQValues[index] > max)
					{
						max = currQValues[index];
						move = index;
					}
				}
			}
			else 
			{
				Random rng = new Random();
				int index = rng.nextInt(availableMove.length);
				move = availableMove[index];
			}
		}
		
		/*if (state > 0) {
			double qPrev = prevQValues[prevMove];
			double deltaQPrev = reward + gamma * maxQ(currQValues) - qPrev;
			double q = qPrev + alpha * deltaQPrev;
			nn.setInput(prevInput);
			nn.calculateOutput();
			if(!play)
				nn.backPropagation(q, prevMove);
		}*/
		state++;

		
		//Save this move.
		prevMove = move;
		return move;
	}
	
	//Get input for neural net from current board's state.
	//First half is for RL player's input, other half is for other player's input.
	public int[] getNeuralInput()
	{
		int[] result = new int[world.size() * 2];
		int[] board = world.getBoard();
		for(int i = 0; i < board.length; i++)
		{
			if(board[i] == this.getPieceType())
				result[i] = 1;
			else if(board[i] != 0)
				result[i + world.size()] = 1;
		}
		return result;
	}
	
	public void updateQValues(int winner)
	{
		if(winner == 2)
			reward = 1;
		else if(winner == 1)
			reward = 0;
		else if(winner == 3)
			reward = 0.7;
		else if(winner == 4)
			reward = 0.5;
		if(winner != 0)
		{
			nn.setInput(currInput);
			for(int i = 0; i < 1; i++)
			{
				nn.calculateOutput();
				nn.backPropagation(reward, prevMove);
			}
			nn.calculateOutput();
		}
	}
	/*
	private double maxQ(double[] qVal)
	{
		if(gameEnd)
			return 0;
		double max = Double.MIN_VALUE;
		int[] freeCell = world.getAvailableMove();
		for(int i =0 ; i < freeCell.length; i++)
		{
			int index = freeCell[i];
			if(qVal[index] > max)
				max = qVal[index];
		}
		return max;
	}*/
	public void gameEnd(int winner)
	{
		gameEnd = true;
		if(winner == this.getPieceType())
			reward = 100;
		else if(winner == 1)
			reward =  -100;
		else
			reward = 0;
		train();
	}
	private int play()
	{
		int move = -1;
		int[] input = getNeuralInput();
		nn.setInput(input);
		nn.calculateOutput();
		double [] outPut = nn.getQValues();
		int[] availableMove = world.getAvailableMove();
		double max = Double.MIN_VALUE;
		for(int i = 0; i < availableMove.length; i++)
		{
			int index = availableMove[i];
			if(outPut[index] > max)
			{
				max = outPut[index];
				move = index;
			}
		}
		return move;
	}
	
}
