package Player;
import java.util.Random;

import Controller.World;


public class RandomPlayer extends Player{
	
	private World world;
	public RandomPlayer(World board)
	{
		world = board;
	}
	public int move() {
		int[] availabelMove = world.getAvailableMove();
		Random rng = new Random();
		int index = rng.nextInt(availabelMove.length);
		return availabelMove[index];
	}
	@Override
	public void updateQValues(int i) {
		// TODO Auto-generated method stub
		
	}

}
