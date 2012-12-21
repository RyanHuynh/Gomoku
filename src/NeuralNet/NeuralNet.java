package NeuralNet;
import java.util.ArrayList;

import Controller.Data;
import Controller.Database;


public class NeuralNet {

	public final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
	public final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	public final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
	private Data data;

	private double momentum = 0;
	private double learningRate = 0.1;
	
	//Set up an empty neural net with number of neuron in each layers.
	public NeuralNet(int _dimension)
	{
		int size = _dimension * _dimension;
		int hiddenNeuron = 7/3 * size;
		Neuron.counter = 0;
		Connection.counter = 0;
		for(int i = 0; i < size * 2; ++i)
		{
			Neuron nr = new Neuron();
			this.inputLayer.add(nr);
		}
		
		for(int i = 0; i < hiddenNeuron; ++i)
		{
			Neuron nr = new Neuron();
			nr.addConnections(inputLayer);
			this.hiddenLayer.add(nr);
		}
		for(int i = 0; i < size; ++i)
		{
			Neuron nr = new Neuron();
			nr.addConnections(hiddenLayer);
			this.outputLayer.add(nr);
		}
	}
	
	//Set up a neural net with previous data.
	public NeuralNet(Database _data, int _dimension)
	{

		int size = _dimension * _dimension;
		int hiddenNeuron = 7/3 * size;
		Neuron.counter = 0;
		Connection.counter = 0;
		for(int i = 0; i < size * 2; ++i)
		{
			Neuron nr = new Neuron();
			this.inputLayer.add(nr);
		}
		
		for(int i = 0; i < hiddenNeuron; ++i)
		{
			Neuron nr = new Neuron();
			nr.addConnections(inputLayer);
			this.hiddenLayer.add(nr);
		}
		for(int i = 0; i < size; ++i)
		{
			Neuron nr = new Neuron();
			nr.addConnections(hiddenLayer);
			this.outputLayer.add(nr);
		}
		this.data = _data.getDataForDim(_dimension);
		updateWeight(data);
	}
	public void calculateOutput()
	{
		for(Neuron nr: hiddenLayer)
		{
			nr.calculateOutput();
		}
		for(Neuron nr: outputLayer)
		{
			nr.calculateOutput();
		}
	}
	
	public void setInput(int[] input)
	{
		for(int i = 0; i < input.length;i ++)
		{
			inputLayer.get(i).setOutput(input[i]);
		}
	}
	public double[] getQValues()
	{
		double[] result = new double[outputLayer.size()];
		for(int i = 0; i < outputLayer.size(); i++)
		{
			result[i] = outputLayer.get(i).getOutput();
		}
		return result;
	}
	
	
	//Back propagation function
	public void backPropagation(double _expectedOutput, int index)
	{
		double expectedOutput = _expectedOutput;
		if (expectedOutput < 0 )
			expectedOutput = 0 ;
		else if (expectedOutput > 1 )
			expectedOutput = 1;
		
		Neuron targetN = outputLayer.get(index);
		double output = targetN.getOutput();
		double outputError = output * (1 - output) * (expectedOutput - output);
		if( Math.pow((output - expectedOutput),2) < 0.00001)
		{
			Controller.Controller.errorCounter++;
			
		}
		for(Connection con : targetN.connections)
		{
			double inputWeight = con.leftNeuron.getOutput();
			double weight = con.getWeight();
			double newWeight = weight + learningRate * outputError * inputWeight;
			double deltaWeight = con.getDeltaWeight();
			con.setDeltaWeight(learningRate * outputError * inputWeight);
			con.setWeight(newWeight + momentum * deltaWeight);
		}
		
		for(Neuron nr : hiddenLayer)
		{
			double outputH = nr.getOutput();
			Connection con = targetN.lookupTable.get(nr.id);
			double weight = con.getWeight();
			double errorHidden = outputH * (1 - outputH) * (outputError * weight);
			for(Connection c : nr.connections)
			{
				double outUpperNeuron = c.leftNeuron.getOutput();
				double w = c.getWeight();
				double newWeight = w + learningRate * errorHidden * outUpperNeuron;
				double deltaWeight = con.getDeltaWeight();
				c.setDeltaWeight(learningRate * errorHidden * outUpperNeuron);
				c.setWeight(newWeight + momentum * deltaWeight);
			}
		}
	}
		
	//Function for save/update database
	public void updateWeight(Data data)
	{
		for(Neuron nr: hiddenLayer)
		{
			for(Connection con: nr.connections)
				con.setWeight(data.getWeightForId(con.id));
		}
		for(Neuron nr: outputLayer)
		{
			for(Connection con: nr.connections)
				con.setWeight(data.getWeightForId(con.id));
		}
	}
	public void saveWeight(Data data)
	{
		for(Neuron nr: hiddenLayer)
		{
			for(Connection con: nr.connections)
				data.setWeightForId(con.id, con.getWeight());
		}
		for(Neuron nr: outputLayer)
		{
			for(Connection con: nr.connections)
				data.setWeightForId(con.id, con.getWeight());
		}
	}
}
