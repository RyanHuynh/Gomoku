package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;
import Controller.*;

public class Board extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	public World world;
	private final int CELL_SIZE = 50;
	
	
	public Board(World _world)
	{
		world = _world;
	}
	
	public void paintComponent(Graphics g)
	{
		if(world.win)
		{
			ArrayList<Integer> result = world.getWinLine();
			int dim = world.getDimension();
			for(int i = 0; i < result.size() ;i++)
			{
				g.setColor(Color.YELLOW);
				int index = result.get(i);
				g.fillRect(index % dim * CELL_SIZE, index / dim * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
			g.setColor(Color.BLACK);
		}
		Image X = Toolkit.getDefaultToolkit().getImage("X.png");
		Image O = Toolkit.getDefaultToolkit().getImage("O.png");
		int[] board = world.getBoard();
		int dimension = world.getDimension();
		for(int row = 0;row < dimension; row++)
		{
			for(int column = 0; column < dimension; column++)
			{
				g.drawRect( column * CELL_SIZE, row * CELL_SIZE,CELL_SIZE, CELL_SIZE);
				int index = row * dimension + column;
				if(board[index] == 1)
					g.drawImage(O,column * CELL_SIZE + 5,row * CELL_SIZE + 5,null);
				else if(board[index] == 2)
					g.drawImage(X,column * CELL_SIZE +5 ,row * CELL_SIZE + 5,null);
				g.setColor(Color.black);
			}
		}
		
	}
	public Dimension getPreferredSize() 
	{ 
		return new Dimension(50 * world.getDimension() + 5, 50 * world.getDimension() + 5);
	}
}
