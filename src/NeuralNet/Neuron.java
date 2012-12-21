package NeuralNet;

import java.util.ArrayList;
import java.util.HashMap;



public class Neuron {
	public static int counter = 0;
	public int id;
	Connection biasConnection;
	final double bias = 1;
	private double output;
	public ArrayList<Connection> connections = new ArrayList<Connection>();
	HashMap<Integer,Connection> lookupTable = new HashMap<Integer,Connection>();
	
	
	public Neuron(){		
		id = counter;
		counter++;
	}
	
	//Only incoming connections
	public void addConnections(ArrayList<Neuron> upperLayer)
	{
		for(Neuron nr : upperLayer)
		{
			Connection con = new Connection(nr);
			connections.add(con);
			lookupTable.put(nr.id, con);
		}
	}
	
	public void setOutput(double newOutput)
	{
		output = newOutput;
	}
	public double getOutput()
	{
		return output;
	}
	
	public void calculateOutput()
	{
		double sum = 0;
		for(Connection con: connections)
		{
			double weight = con.getWeight();
			Neuron leftNeuron = con.leftNeuron;
			sum = sum + (weight * leftNeuron.getOutput());
		}
		
		output = sigmoid(sum);
	}
	
	//Activate function - Sigmoid
	private double sigmoid(double sum)
	{
		return 1.0 / (1.0 +  (Math.exp(-sum)));
	}
}
