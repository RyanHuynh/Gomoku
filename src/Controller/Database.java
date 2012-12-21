package Controller;
import java.io.Serializable;
import java.util.ArrayList;


public class Database implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Data> database;
	public Database()
	{
		database = new ArrayList<Data>();
		Data data3x3 = new Data();
		database.add(data3x3);
		Data data5x5 = new Data();
		database.add(data5x5);
		Data data7x7 = new Data();
		database.add(data7x7);
		Data data9x9 = new Data();
		database.add(data9x9);
	}
	public Data getDataForDim(int dimension)
	{
		switch(dimension)
		{
			case 3:
				return database.get(0);
			case 5:
				return database.get(1);
			case 7:
				return database.get(2);
			case 9:
				return database.get(3);
			default:
				return null;
		}
		
	}
}
