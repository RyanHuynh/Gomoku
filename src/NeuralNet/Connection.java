package NeuralNet;
import java.util.Random;


public class Connection {
	public static int counter = 0;
	public int id;
	private double weight;
	private double deltaWeight;
	Neuron leftNeuron;
	public Connection(Neuron left){	
		leftNeuron = left;
		Random rand = new Random();
		//weight = 100;
		weight = ((rand.nextDouble() * 2) - 1);
		id = counter;
		counter++;
	}
	
	public int getId()
	{
		return id;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public void setWeight(double newWeight)
	{
		weight = newWeight;
	}
	public void setDeltaWeight(double weight)
	{
		deltaWeight = weight;
	}
	public double getDeltaWeight()
	{
		return deltaWeight;
	}
}
