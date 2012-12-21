package Controller;

import java.util.ArrayList;

public class World {

	
	private int size;
	private int[] board; //= {0,0,0,
						//   2,2,1,
	 					// 0,0,1};
	private int dimension;					 
	public boolean win = false;
	private ArrayList<Integer> winLine = new ArrayList<Integer>();
	public World(int _dimension)
	{
		size = _dimension * _dimension;
		board = new int[size];
		dimension = _dimension;
	}
	public int getDimension()
	{
		return dimension;
	}
	public int[] getBoard()
	{
		return board;
	}
	public int[] getAvailableMove()
	{
		int resultSize = 0;
		for(int i = 0; i < board.length;i++)
		{
			if(board[i] == 0)
				resultSize++;
		}
		int[] result = new int[resultSize];
		int index = 0;
		for(int i = 0; i < board.length;i++)
		{
			if(board[i] == 0)
			{
				result[index] = i;
				index++;
			}
		}
		return result;
	}
	
	public void setPiece(int pieceType, int location)
	{
		board[location] = pieceType;
	}
	public int size()
	{
		return size;
	}
	public void resetBoard()
	{
		for(int i = 0; i < board.length; i++)
		{
			board[i] = 0;
		}
		winLine = new ArrayList<Integer>();
		win = false;
	}
	public void setWinLine(ArrayList<Integer> win)
	{
		winLine = win;
	}
	public ArrayList<Integer> getWinLine()
	{
		return winLine;
	}
}
