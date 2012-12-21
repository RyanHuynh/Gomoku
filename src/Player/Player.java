package Player;

public abstract class Player {
	private int type;
	public void setType(int pieceType)
	{
		type = pieceType;
	}
	public int getPieceType()
	{
		return type;
	}
	
	public abstract int move();
	public abstract void updateQValues(int i);
		
	
}
