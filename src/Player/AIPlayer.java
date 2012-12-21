package Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import Controller.*;
public class AIPlayer extends Player { 
	
	//Position which computer already used.
	public static ArrayList<Integer> computer;
	
	//Position which human already used.
	public static ArrayList<Integer> human;
	
	//Constraints with size 4
	private static ArrayList<ArrayList<Integer>> constraintsBy4; 
	
	public static ArrayList<Integer> tempConstraints = new ArrayList<Integer>();
	
	public static ArrayList<Integer> tempWin = new ArrayList<Integer>();
	
	public static ArrayList<Integer> tempBlock = new ArrayList<Integer>();
	
	// static variable to keep track of number of times mostAppearedNumber appears
	// this variable is updated each getMostAppearedNumberFromConstraints() is
	// called 
	public static int mostAppearedNumberCounter;
	
	//Computer wining constraints left.
	public static ArrayList<ArrayList<Integer>> cConstraints; 
	//Human wining constraints left.
	public static ArrayList<ArrayList<Integer>> hConstraints;
	
	public static boolean gameOver = false;
	
	public static int mostAppearNumberCounter;
	
	public static int index;
	
	private static World world;
	
	public static ArrayList <Integer> positionUsed;
	
	public AIPlayer(World world){
		this.world = world;		
		cConstraints = new ArrayList<ArrayList<Integer>>();
		hConstraints = new ArrayList<ArrayList<Integer>>();
		constraintsBy4 = new ArrayList<ArrayList<Integer>>();
		positionUsed = new ArrayList <Integer>();
		computer = new ArrayList <Integer>();
		human = new ArrayList <Integer>();
		positionUsed = new ArrayList <Integer>();
		setUpConstraints(world.getDimension());
		//TESTING
		/*human.add(3);
		human.add(11);
		human.add(20);
		human.add(21);
		human.add(23);
		
		eliminateConstraints(3, cConstraints);
		eliminateConstraints(11, cConstraints);
		eliminateConstraints(20, cConstraints);
		eliminateConstraints(21, cConstraints);
		eliminateConstraints(23, cConstraints);
		
		computer.add(24);
		computer.add(6);
		computer.add(12);
		computer.add(15);
		computer.add(18);
		
		eliminateConstraints(24, hConstraints);
		eliminateConstraints(6, hConstraints);
		eliminateConstraints(12, hConstraints);
		eliminateConstraints(15, hConstraints);
		eliminateConstraints(18, hConstraints);*/
		
		for(int i = 0; i < world.getDimension()*world.getDimension(); i++)
			positionUsed.add(i);
	}
	
	public int move(){
		int preMove = Controller.getPreviousMove();
		//printout();
		if(preMove != -1){
			human.add(preMove);	
			eliminateConstraints(preMove, cConstraints);
		}
		run();
		return index;
	}
	
	private void run(){
		if(winningMove()){
			//Win
		}
		else if(blockingMove()){
			//Block success.
		}
		else if(move4()){
			//Create forkMove already
		}
		else if(block4()){
			//Avoid forkMOve already
		}
		else if(move3()){
			//Move 3
		}
		else if(block3()){
			//Block 3
		}
		else{
			if(human.size() < 1)
				randomDMove();
			else
				highPossibleMove();
		}
	}
	
	
	private void printout() {
		// TODO Auto-generated method stub
		for(int i = 0; i < computer.size(); i++)
			System.out.print(computer.get(i) + " ");
		for(int i = 0; i < human.size(); i++)
			System.out.print(human.get(i) + " ");
	}
	
	/*public static void main (String []args)
	{
		//human.add(position);
		
		// Eliminate constraints which means eliminate object that contains 
		// that number
		//eliminateConstraints(position,cConstraints);

		 //There is the case where cConstraints == 0.
		 // Computer move
		 // Follow the rules before making move
		 // Rule 1: If I have a winning move, take it.
		 // Rule 2: If the opponent has a winning move, block it.
		 // Rule 3: If I can create a fork (two winning ways) after this move, do it.
		 // Rule 4: Do not let the opponent creating a fork after my move. (Opponent may block your winning move and create a fork.)
		 // Rule 5: Place in the position such as I may win in the most number of possible ways.
		  
		//Rule 1: If I have a winning move, take it. Done
		if(winningMove())
		{
			//Do nothing
		}
		//Rule 2: If the opponent has a winning move, block it. Done
		else if(blockingMove()){
			//Block success.
		}
		//Rule 3: If I can create a fork (two winning ways) after this move, do it. In progress
		else if(forkMove()){
			//Do nothing
		}
		//Rule 4: Do not let the opponent creating a fork after my move. Not yet
		//(Opponent may block your winning move and create a fork.)
		else if(avoidFork()){
			//Do nothing
		}
		//Rule 5: Place in the position such as I may win in the most number of possible ways.
		else{
			highPossibleMove();
			//Need edit a little of it if there is time left.
		}
	}*/
	
	private static ArrayList<Integer> commonElements(List<Integer> list1, List<Integer> list2)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (list1.size() > 0 && list2.size() > 0)
		{
			for (Integer i : list1)
				if (list2.contains(i))
					result.add(i);
		}
		
		return result;
	}
	
	/**
	 * @param constraints
	 * @param positionUsed
	 * @return list of numbers that appear at least twice in constraints and 
	 * not in positionUsed
	 */
	public static ArrayList<Integer> numbersAppearAtLeastTwice(ArrayList<ArrayList<Integer>> constraints,
			ArrayList<Integer> positionUsed)
			{
		//System.out.println("contraints passed: " + constraints);
		
		// HashMap for associating each value with a Value object which has a 
		// counter reference indicating how many times the value has been seen
		HashMap<Integer, Value> valueMap = new HashMap<Integer, Value>();

		// go through each value in constraints and put to the map 
		for (ArrayList<Integer> values : constraints)
		{
			for (Integer i : values)
			{
				// avoid adding the numbers (positions) which the computer 
				// has used
				if (!positionUsed.contains(i))
				{
					if (valueMap.containsKey(i))
						valueMap.get(i).incrementCounter();
					else
						valueMap.put(i, new Value(i));
				}
			}
		}

		ArrayList<Value> v = new ArrayList<Value>(valueMap.values());
		Collections.sort(v);	

		ArrayList<Integer> results = new ArrayList<Integer>();

		if (!v.isEmpty())
		{
			// put result in reverse order so that number that appear the 
			// least is at first position
			for (int i = v.size() - 1; i >= 0; i--)
			{
				if (v.get(i).counter >= 2)
					results.add(v.get(i).value);
			}
		}

		return results;
			}

	
	private static ArrayList<Integer> getMostAppearNumbersFromConstraints
											(ArrayList<ArrayList<Integer>> constraints,
											 ArrayList<Integer> positionUsed)
	{
		// HashMap for associating each value with a Value object which has a 
		// counter reference indicating how many times the value has been seen
		HashMap<Integer, Value> valueMap = new HashMap<Integer, Value>();

		// go through each value in constraints and put to the map 
		for (ArrayList<Integer> values : constraints)
		{
			for (Integer i : values)
			{
				// avoid adding the numbers (positions) which the computer 
				// has used
				if (!positionUsed.contains(i))
				{
					if (valueMap.containsKey(i))
						valueMap.get(i).incrementCounter();
					else
						valueMap.put(i, new Value(i));
				}
			}
		}

		ArrayList<Value> v = new ArrayList<Value>(valueMap.values());

		Collections.sort(v);	

		ArrayList<Integer> results = new ArrayList<Integer>();

		if (!v.isEmpty())
		{
			for (int i = 0; i < v.size(); i++)
			{
				results.add(v.get(i).value);
				if (i+1 < v.size() && v.get(i+1).counter < v.get(i).counter)
					break;
			}
		}
		
		if(results.size() > 0)
			mostAppearNumberCounter = valueMap.get(results.get(0)).getCount();
		else
			mostAppearNumberCounter = 0;
		return results;

	}

	private static class Value implements Comparable<Value>
	{
		private int counter = 1;
		private int value;

		public Value (int value)
		{
			this.value = value;
		}

		public void incrementCounter()
		{
			counter++;
		}
		
		public int getCount(){
			return counter;
		}


		@Override
		public String toString()
		{
			return "value: " + value + "\t" + "Counter: " + counter;
		}

		@Override
		public int compareTo(Value other) 
		{
			if (counter < other.counter)
				return 1;
			else if (counter > other.counter)
				return -1;
			return 0;
		}


	}
	
	//Eliminate the number appear in both constraints A and B
	private static boolean avoidFork() {
		boolean result = false;
		if(human.size() >= 6){
			//Hold all the constraints that contains in mostAppearNumber list.
			ArrayList<ArrayList<Integer>> tempAFConstraints = new ArrayList <ArrayList<Integer>>();
			//Hold all the constraints that can create a fork Move.
			ArrayList<ArrayList<Integer>> afConstraints = new ArrayList <ArrayList<Integer>>();
			ArrayList<Integer> hnumbersAppearAtLeastTwice;
			hnumbersAppearAtLeastTwice = numbersAppearAtLeastTwice(hConstraints, human);
			int hSize = hnumbersAppearAtLeastTwice.size();
			if(hSize != 0){ //Check to make sure it appear at least in 2 computer winning constraints
				Loop:
				for(int a = 0; a < hSize; a++){ //Check every number in most appear number.
					for(int b = 0; b < hConstraints.size(); b++){
						if(hConstraints.get(b).contains(hnumbersAppearAtLeastTwice.get(a)))
							tempAFConstraints.add(hConstraints.get(b));
					}
					int tSize = tempAFConstraints.size();
					for(int d = 0; d < tSize; d++){
						int count = 0;
						for(int e = 0; e < 5; e++){
							if(human.contains(tempAFConstraints.get(d).get(e))){
								count++;
							}
						}
						if(count >= 3){
							afConstraints.add(tempAFConstraints.get(d));
						}
					}
					if(afConstraints.size() >= 2){
						computer.add(hnumbersAppearAtLeastTwice.get(a));
						index = hnumbersAppearAtLeastTwice.get(a);
						eliminateConstraints(index, hConstraints);
						result = true;
						break Loop;
					}
					tempAFConstraints = new ArrayList<ArrayList<Integer>>();
					afConstraints = new ArrayList<ArrayList<Integer>>();
				}
			}
		}
		return result;
	}


	private static void eliminateConstraints(int position, ArrayList<ArrayList<Integer>> constraints) {
		// TODO Auto-generated method stub
		for(int i = constraints.size()-1; i >= 0; i--)
			if(constraints.get(i).contains(position))
				constraints.remove(i);
	}


	//Rule 3: If I can create a fork (two winning ways) after this move, do it.
	private static boolean forkMove() {
		boolean result = false;
		if(computer.size() >= 6){
			//Hold all the constraints that contains in mostAppearNumber list.
			ArrayList<ArrayList<Integer>> tempFConstraints = new ArrayList <ArrayList<Integer>>();
			//Hold all the constraints that can create a fork Move.
			ArrayList<ArrayList<Integer>> fConstraints = new ArrayList <ArrayList<Integer>>();
			ArrayList<Integer> cnumbersAppearAtLeastTwice;
			cnumbersAppearAtLeastTwice = numbersAppearAtLeastTwice(cConstraints, computer);
			int cSize = cnumbersAppearAtLeastTwice.size();
			if(cSize != 0){ //Check to make sure it appear at least in 2 computer winning constraints
				Loop:
				for(int a = 0; a < cSize; a++){ //Check every number in most appear number.
					for(int b = 0; b < cConstraints.size(); b++){
						if(cConstraints.get(b).contains(cnumbersAppearAtLeastTwice.get(a))){
							ArrayList<Integer> tmp = cConstraints.get(b);
							if(!tempFConstraints.contains(tmp))	
								tempFConstraints.add(tmp);
						}
					}
					
					int tSize = tempFConstraints.size();
					for(int d = 0; d < tSize; d++){
						int count = 0;
						for(int e = 0; e < 5; e++){
							if(computer.contains(tempFConstraints.get(d).get(e))){
								count++;
							}
						}
						if(count >= 3){
							fConstraints.add(tempFConstraints.get(d));
						}
					}
					if(fConstraints.size() >= 2){
						computer.add(cnumbersAppearAtLeastTwice.get(a));
						index = cnumbersAppearAtLeastTwice.get(a);
						eliminateConstraints(index, hConstraints);
						result = true;
						break Loop;
					}
					tempFConstraints = new ArrayList<ArrayList<Integer>>();
					fConstraints = new ArrayList<ArrayList<Integer>>();
				}
			}
		}
		return result;
	}


	private static boolean blockingMove() {
		boolean result = false;
		// Rule 2: If the opponent has a winning move, block it.
		if(human.size() >= 4){
			ArrayList<ArrayList<Integer>> attempWConstraints = new ArrayList<ArrayList<Integer>> ();
			int cSize = human.size();
			for(int a = 0; a < cSize-3; a++){
				ArrayList<Integer> setOf4 = new ArrayList <Integer>();
				for(int b = a+1; b < cSize-2; b++){
					for(int c = b+1; c < cSize-1; c++){
						for(int d = c+1; d < cSize; d++){
							setOf4.add(human.get(a));
							setOf4.add(human.get(b));
							setOf4.add(human.get(c));
							setOf4.add(human.get(d));
							attempWConstraints.add(setOf4);
							setOf4 = new ArrayList<Integer>();
						}
					}
				}
			}
			Loop: 
			for(int e = 0; e < hConstraints.size(); e++){
				for(int f = 0; f < attempWConstraints.size(); f++){
					if(hConstraints.get(e).containsAll(attempWConstraints.get(f))){
						ArrayList<Integer> temp = hConstraints.get(e);
						for(int k = 0; k < temp.size(); k++){
							if(!attempWConstraints.get(f).contains(temp.get(k))){
								Integer t = temp.get(k);
								computer.add(t);
								index = t;
								eliminateConstraints(t,hConstraints);
								result = true;
								break Loop;
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static void setUpConstraints(int dimensions) 
	{
		// TODO Auto-generated method stub
		for(int i = 0; i < dimensions*dimensions; i++)
		{
			rowSetUp(i,dimensions);
			columnSetUp(i,dimensions);
			diagonalRSetUp(i,dimensions,dimensions);
			diagonalLSetUp(i,dimensions,dimensions);
		}
	}
	
	// set up row contraints for win by 5 
	private static void rowSetUp(int position, int columns) 
	{
		// position % columns gives you the column position in a two 
		// dimensional array.  This position must be less than the numbers
		// of columns - 4 for win by 5
		if((position%columns) < columns - 4)
		{
			tempConstraints.add(position);
			tempConstraints.add(position+1);
			tempConstraints.add(position+2);
			tempConstraints.add(position+3);
			tempConstraints.add(position+4);
			cConstraints.add(tempConstraints);
			hConstraints.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}
	}

	private static void columnSetUp(int position, int rows) 
	{
		// We can use variable rows instead of columns since rows and columns
		// are the same.  
		// Position / rows give you the corresponding row number in a 2
		// dimensional board. The winning lines are the rows from 0 to 4
		if((position/rows) < rows - 4)
		{
			tempConstraints.add(position);
			tempConstraints.add(position+rows);
			tempConstraints.add(position+rows*2);
			tempConstraints.add(position+rows*3);
			tempConstraints.add(position+rows*4);
			cConstraints.add(tempConstraints);
			hConstraints.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}

	}
	
	
	private static void diagonalLSetUp(int position, int rows, int columns) 
	{
		// position % columns give you the column position in the two 
		// dimensional board. We check from left to right for the left
		// diagonal. The starting position of a left diagonal winning line must
		// be at least 4 for in a win by 5 ticTacToe game. 
		//
		// position/rows give you the row number of the position in the two
		// dimensional board.  We also need to make sure this row number
		// 
		if((position%columns > 3) && (position/rows < rows-4))
		{
			tempConstraints.add(position);
			tempConstraints.add(position+columns-1);
			tempConstraints.add(position+columns*2-2);
			tempConstraints.add(position+columns*3-3);
			tempConstraints.add(position+columns*4-4);
			cConstraints.add(tempConstraints);
			hConstraints.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}
	}


	private static void diagonalRSetUp(int position, int rows, int columns) {
		// TODO Auto-generated method stub
		if(position%columns < (columns - 4) && position/rows < (rows - 4)){
			tempConstraints.add(position);
			tempConstraints.add(position+columns+1);
			tempConstraints.add(position+columns*2+2);
			tempConstraints.add(position+columns*3+3);
			tempConstraints.add(position+columns*4+4);
			cConstraints.add(tempConstraints);
			hConstraints.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}
	}
	
	private static boolean winningMove() {
		boolean result = false;
		// Rule 1: If I have a winning move, take it.
		// Just check when size greater than or equal 4.Otherwise, no wining constraint can occur.
		if(computer.size() >= 4){
			ArrayList<ArrayList<Integer>> tempWConstraints = new ArrayList<ArrayList<Integer>> ();
			int cSize = computer.size();
			for(int a = 0; a < cSize-3; a++){
				ArrayList<Integer> setOf4 = new ArrayList <Integer>();
				for(int b = a+1; b < cSize-2; b++){
					for(int c = b+1; c < cSize-1; c++){
						for(int d = c+1; d < cSize; d++){
							setOf4.add(computer.get(a));
							setOf4.add(computer.get(b));
							setOf4.add(computer.get(c));
							setOf4.add(computer.get(d));
							tempWConstraints.add(setOf4);
							setOf4 = new ArrayList<Integer>();
						}
					}
				}
			}
			/*System.out.println(tempWConstraints.size());
			for(int i = 0; i < tempWConstraints.size(); i++)
				System.out.println(tempWConstraints.get(i));*/
			Loop:
			for(int e = 0; e < cConstraints.size(); e++){
				for(int f = 0; f < tempWConstraints.size(); f++){
					if(cConstraints.get(e).containsAll(tempWConstraints.get(f))){
						ArrayList <Integer> temp = cConstraints.get(e);
						for(int i = 0; i < temp.size(); i++)
							if(!tempWConstraints.get(f).contains(temp.get(i)))
								index = temp.get(i);
						computer.add(index);
						eliminateConstraints(index,hConstraints);
						result = true;
						break Loop;
					}
				}
			}
		}
		return result;
		// TODO Auto-generated method stub
	}

	private static int randomNumber(int number){
		Random randomGenerator = new Random();
	    return randomGenerator.nextInt(number);
	}
	
	private static void highPossibleMove(){
		//Rule 5: Place in the position such as I may win in the most number of possible ways.
		ArrayList<Integer> cMostAppearedNumbers, hMostAppearedNumbers, comonElements;
		cMostAppearedNumbers = getMostAppearNumbersFromConstraints(cConstraints, computer);
		int c = mostAppearNumberCounter;
		hMostAppearedNumbers = getMostAppearNumbersFromConstraints(hConstraints, human);
		int h = mostAppearNumberCounter;
		int cSize = cMostAppearedNumbers.size();
		int hSize = hMostAppearedNumbers.size();
		
		Integer numList = null;
		if(cSize >= 2){
			comonElements = commonElements(cMostAppearedNumbers,hMostAppearedNumbers);
			/**
			 * If: comonElement.size() >= 2 . Just random pick base on its size.
			 * else if: comonElement.size() == 1. Just pick it.
			 * Else: so lan xuat hien cua number in hConstraints and so lan xuat hien cua number in cConstraints.
			 * 		If (h > c) In progress.
			 */
			int comonSize = comonElements.size();
			if(comonSize >= 2){
				int num = randomNumber(comonSize);
			    computer.add(comonElements.get(num));
			    index = comonElements.get(num);
			    numList = comonElements.get(num);
			    eliminateConstraints(numList,hConstraints);
			}
			else if(comonSize == 1){
				computer.add(comonElements.get(0));
				index = comonElements.get(0);
			    numList = comonElements.get(0);
			    eliminateConstraints(numList,hConstraints);
			}
			else{
				/**
				 * Compare so lan xuat hien cua thang lon hon thi pick thang do.
				 */
				if(c < h || (c == h & cSize < hSize)){
					int num = randomNumber(hSize);
				    computer.add(hMostAppearedNumbers.get(num));
				    index = hMostAppearedNumbers.get(num);
				    numList = hMostAppearedNumbers.get(num);
				    eliminateConstraints(numList,hConstraints);
				}
				else{
					int num = randomNumber(cSize);
				    computer.add(cMostAppearedNumbers.get(num));
				    index = cMostAppearedNumbers.get(num);
				    numList = cMostAppearedNumbers.get(num);
				    eliminateConstraints(numList,hConstraints);
				}
			}
		}
		else if (cSize == 1){
			//If it contains then just pick it.
			if(hMostAppearedNumbers.contains(cMostAppearedNumbers.get(0))){
				computer.add(cMostAppearedNumbers.get(0));
				index = cMostAppearedNumbers.get(0);
				numList = cMostAppearedNumbers.get(0);
				eliminateConstraints(numList,hConstraints);
			}
			//Otherwise compare so lan xuat hien cua no voi so lan xuat hien cua hMostAppearNumbers.
			//Thang nao lon hon thi lay thang do.
			else{
				if(c < h || (c == h & cSize < hSize)){
					int num = randomNumber(hSize);
				    computer.add(hMostAppearedNumbers.get(num));
				    index = hMostAppearedNumbers.get(num);
				    numList = hMostAppearedNumbers.get(num);
				    eliminateConstraints(numList,hConstraints);
				}
				else{
					computer.add(cMostAppearedNumbers.get(0));
					index = cMostAppearedNumbers.get(0);
					numList = cMostAppearedNumbers.get(0);
					eliminateConstraints(numList,hConstraints);
				}
			}
		}
		//Size == 0 When there is no cConstraints left
		else{
			ArrayList<Integer> tempMostAppearedNumbers;
			tempMostAppearedNumbers = getMostAppearNumbersFromConstraints(hConstraints, human);
			int size = tempMostAppearedNumbers.size();
			int random = 0; 
			if(size == 0){
				index = uselessPick();
				computer.add(index);
				numList = index;
				eliminateConstraints(numList,hConstraints);
			}			
			else{	
				random = randomNumber(tempMostAppearedNumbers.size());
				computer.add(tempMostAppearedNumbers.get(random));
				index = tempMostAppearedNumbers.get(random);
				numList = tempMostAppearedNumbers.get(random);
				eliminateConstraints(numList,hConstraints);
			}
		}
	}
	
	public static int uselessPick(){
		ArrayList <Integer> temp = new ArrayList<Integer>();
		int number = -1;
		temp.addAll(computer);
		temp.addAll(human);
		for(int i = 0; i < positionUsed.size(); i++){
			if(!temp.contains(positionUsed.get(i))){
				number = positionUsed.get(i);
				break;
			}
				
		}
		/*if(number == -1){
			System.out.println(computer);
			System.out.println(human);
			System.out.println(temp);
			System.out.println(positionUsed);
			
		}*/
		return number; 
	}
	
	public ArrayList<ArrayList<Integer>> winning_sequences(int dimension){
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < hConstraints.size(); i++)
		{
			result.add(new ArrayList<Integer>());
			for(int j = 0; j < hConstraints.get(i).size(); j++)
			{
				int number = hConstraints.get(i).get(j);
				result.get(i).add(number);
			}
		}
		return result;
	}
	
	/**
	 * return an ArrayList<ArrayList<Integer>>.
	 * @param dimensions
	 */
	public ArrayList<ArrayList<Integer>> setUpConstraintsBy4(int dimensions){
		for(int i = 0; i < dimensions*dimensions; i++){
			rowSetUp4(i,dimensions);
			columnSetUpBy4(i,dimensions);
			diagonalRSetUpBy4(i,dimensions,dimensions);
			diagonalLSetUpBy4(i,dimensions,dimensions);
		}
		return constraintsBy4;
	}
	
	/**
	 * Row set up
	 * @param position
	 * @param columns
	 */
	private void rowSetUp4(int position, int columns) 
	{
		// position % columns gives you the column position in a two 
		// dimensional array.  This position must be less than the numbers
		// of columns - 4 for win by 5
		if((position%columns) < columns - 3)
		{
			tempConstraints.add(position);
			tempConstraints.add(position+1);
			tempConstraints.add(position+2);
			tempConstraints.add(position+3);
			constraintsBy4.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}
	}

	/**
	 * Columns set up
	 * @param position
	 * @param rows
	 */
	private static void columnSetUpBy4(int position, int rows) 
	{
		if((position/rows) < rows - 3)
		{
			tempConstraints.add(position);
			tempConstraints.add(position+rows);
			tempConstraints.add(position+rows*2);
			tempConstraints.add(position+rows*3);
			constraintsBy4.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}

	}
	
	/**
	 * Diagonal Right set up
	 * @param position
	 * @param rows
	 * @param columns
	 */
	private static void diagonalLSetUpBy4(int position, int rows, int columns) 
	{
		if((position%columns > 2) && (position/rows < rows-3))
		{
			tempConstraints.add(position);
			tempConstraints.add(position+columns-1);
			tempConstraints.add(position+columns*2-2);
			tempConstraints.add(position+columns*3-3);
			constraintsBy4.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}
	}

	/**
	 * Diagonal Left set up
	 * @param position
	 * @param rows
	 * @param columns
	 */
	private static void diagonalRSetUpBy4(int position, int rows, int columns) {
		// TODO Auto-generated method stub
		if(position%columns < (columns - 3) && position/rows < (rows - 3)){
			tempConstraints.add(position);
			tempConstraints.add(position+columns+1);
			tempConstraints.add(position+columns*2+2);
			tempConstraints.add(position+columns*3+3);
			constraintsBy4.add(tempConstraints);
			tempConstraints = new ArrayList<Integer>();
		}
	}
	
	private static boolean move4(){
		boolean result = false;
		ArrayList<ArrayList<Integer>> tempWConstraints = new ArrayList<ArrayList<Integer>> ();
		if(computer.size() >= 3){
			int cSize = computer.size();
			for(int a = 0; a < cSize-2; a++){
				ArrayList<Integer> setOf3 = new ArrayList <Integer>();
				for(int b = a+1; b < cSize-1; b++){
					for(int c = b+1; c < cSize; c++){
							setOf3.add(computer.get(a));
							setOf3.add(computer.get(b));
							setOf3.add(computer.get(c));
							tempWConstraints.add(setOf3);
							setOf3 = new ArrayList<Integer>();
					}
				}
			}
		}
		Loop:
		for(int e = 0; e < cConstraints.size(); e++){
			for(int f = 0; f < tempWConstraints.size(); f++){
				if(cConstraints.get(e).containsAll(tempWConstraints.get(f))){
					ArrayList <Integer> temp = cConstraints.get(e);
					for(int i = 0; i < temp.size(); i++){
						if(!tempWConstraints.get(f).contains(temp.get(i))){
							index = temp.get(i);
							computer.add(index);
							eliminateConstraints(index,hConstraints);
							result = true;
							break Loop;
						}
					}
				}
			}
		}
		return result;
	}
	
	private static boolean block4(){
		boolean result = false;
		ArrayList<ArrayList<Integer>> tempWConstraints = new ArrayList<ArrayList<Integer>> ();
		if(human.size() >= 3){
			int cSize = human.size();
			for(int a = 0; a < cSize-2; a++){
				ArrayList<Integer> setOf3 = new ArrayList <Integer>();
				for(int b = a+1; b < cSize-1; b++){
					for(int c = b+1; c < cSize; c++){
							setOf3.add(human.get(a));
							setOf3.add(human.get(b));
							setOf3.add(human.get(c));
							tempWConstraints.add(setOf3);
							setOf3 = new ArrayList<Integer>();
					}
				}
			}
		}
		Loop:
		for(int e = 0; e < hConstraints.size(); e++){
			for(int f = 0; f < tempWConstraints.size(); f++){
				if(hConstraints.get(e).containsAll(tempWConstraints.get(f))){
					ArrayList <Integer> temp = hConstraints.get(e);
					for(int i = 0; i < temp.size(); i++)
						if(!tempWConstraints.get(f).contains(temp.get(i)))
							index = temp.get(i);
					computer.add(index);
					eliminateConstraints(index,hConstraints);
					result = true;
					break Loop;
				}
			}
		}
		return result;
	}
	
	private static boolean move3(){
		boolean result = false;
		ArrayList<ArrayList<Integer>> tempWConstraints = new ArrayList<ArrayList<Integer>> ();
		if(computer.size() >= 2){
			int cSize = computer.size();
			for(int a = 0; a < cSize-1; a++){
				ArrayList<Integer> setOf2 = new ArrayList <Integer>();
				for(int b = a+1; b < cSize; b++){
					setOf2.add(computer.get(a));
					setOf2.add(computer.get(b));
					tempWConstraints.add(setOf2);
					setOf2 = new ArrayList<Integer>();
				}
			}
		}
		Loop:
		for(int e = 0; e < cConstraints.size(); e++){
			for(int f = 0; f < tempWConstraints.size(); f++){
				if(cConstraints.get(e).containsAll(tempWConstraints.get(f))){
					ArrayList <Integer> temp = cConstraints.get(e);
					for(int i = 0; i < temp.size(); i++){
						if(!tempWConstraints.get(f).contains(temp.get(i))){
							index = temp.get(i);
							computer.add(index);
							eliminateConstraints(index,hConstraints);
							result = true;
							break Loop;
						}
					}
				}
			}
		}
		return result;
	}
	
	private static boolean block3(){
		boolean result = false;
		ArrayList<ArrayList<Integer>> tempWConstraints = new ArrayList<ArrayList<Integer>> ();
		if(human.size() >= 2){
			int cSize = human.size();
			for(int a = 0; a < cSize-1; a++){
				ArrayList<Integer> setOf2 = new ArrayList <Integer>();
				for(int b = a+1; b < cSize; b++){
					setOf2.add(human.get(a));
					setOf2.add(human.get(b));
					tempWConstraints.add(setOf2);
					setOf2 = new ArrayList<Integer>();
				}
			}
		}
		Loop:
		for(int e = 0; e < hConstraints.size(); e++){
			for(int f = 0; f < tempWConstraints.size(); f++){
				if(hConstraints.get(e).containsAll(tempWConstraints.get(f))){
					ArrayList <Integer> temp = hConstraints.get(e);
					for(int i = 0; i < temp.size(); i++){
						if(!tempWConstraints.get(f).contains(temp.get(i))){
							index = temp.get(i);
							computer.add(index);
							eliminateConstraints(index,hConstraints);
							result = true;
							break Loop;
						}
					}
				}
			}
		}
		return result;
	}
	
	private static void randomMove(){
		//Rule 5: Place in the position such as I may win in the most number of possible ways.
		ArrayList<Integer> cMostAppearedNumbers, comonElements;
		cMostAppearedNumbers = getMostAppearNumbersFromConstraints(cConstraints, computer);
		int c = mostAppearNumberCounter;
		int cSize = cMostAppearedNumbers.size();
		if(cSize >= 2){
			int random = randomNumber(cSize);
			index = cMostAppearedNumbers.get(random);
			eliminateConstraints(index, hConstraints);
			computer.add(index);
		}
		else if(cSize == 1){
			index = cMostAppearedNumbers.get(0);
			eliminateConstraints(index, hConstraints);
			computer.add(index);
		}
		else{
			index = uselessPick();
			eliminateConstraints(index, hConstraints);
			computer.add(index);
		}
	}
	
	private static void randomDMove(){
		index = luckPick();
		/*for(int i = 0; i < positionUsed.size(); i++)
			if(!computer.contains(positionUsed.get(i)) && !human.contains(positionUsed.get(i))){
				index = positionUsed.get(i);
				break;
			}*/
		eliminateConstraints(index, hConstraints);
		computer.add(index);
	}
	
	private static int luckPick(){
		int num = -1;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.addAll(computer);
		temp.addAll(human);
		ArrayList<Integer> pTemp = new ArrayList<Integer>();
		pTemp.addAll(positionUsed);
		for(int i = pTemp.size()-1; i >= 0 ; i--){
			if(temp.contains(pTemp.get(i))){
				pTemp.remove(i);
			}
		}
		int random = randomNumber(pTemp.size());
		num = pTemp.get(random);
		return num;
	}

	@Override
	public void updateQValues(int i) {
		// TODO Auto-generated method stub
		
	}
}
