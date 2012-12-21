package Controller;
import java.io.Serializable;
import java.util.HashMap;


public class Data implements Serializable{
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Double> data;
	public Data(){
		data = new HashMap<Integer, Double>();}
	
	public double getWeightForId(int id){
		return data.get(id);}
	public void setWeightForId( int id, double newWeight){
		data.put(id, newWeight);}
}
