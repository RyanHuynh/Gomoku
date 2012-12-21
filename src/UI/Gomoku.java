package UI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import Controller.*;
import NeuralNet.NeuralNet;



public class Gomoku extends JApplet implements ActionListener, Runnable
{
	
	private static final long serialVersionUID = 1L;
	final static String PATH = "../src/Database.data";
	private World world;
	private Board board;
	private static int dimension = 5;
	private static int winBy = 5;
	private Container mainPanel;
	private Controller controller;
	private JTabbedPane tabPane;
	private JTextField trainTextField;
	public JLabel progressText;
	private JButton startB;
	private JButton stopB;
	private JButton trainB;
	private JButton resetScore;
	private JButton reset;
	private JButton changeGrid;
	private JRadioButton smart;
	private JRadioButton luck;
	private JRadioButton trained;
	private JRadioButton unTrained;
	private JRadioButton b1;
	private JRadioButton b2;
	private JRadioButton b3;
	private JRadioButton trainer1;
	private JRadioButton trainer2;
	private JRadioButton slow;
	private JRadioButton fast;
	private JLabel totalGameText;
	private JLabel O_winText;
	private JLabel X_winText;
	private JLabel draw_Text;
	private JLabel XPerct;
	private JLabel OPerct;
	private JLabel DrawPerct;
	public JLabel trainText;
	public void init()
	{
		this.resize(850,400);
		world = new World(dimension);
		board = new Board(world);
		
		mainPanel = makeMainPanel();
		tabPane = new JTabbedPane();
		Container gridChoice = makeDimensionPanel();
		tabPane.add("Grid Choice",gridChoice);
		tabPane.add("Play", mainPanel);
		
		tabPane.setSelectedIndex(0);
		tabPane.setEnabledAt(1, false);
		getContentPane().add(tabPane);
	}
	Container makeMainPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.X_AXIS));
		thisPanel.add(makeBoardPanel());
		//thisPanel.add(Box.createHorizontalGlue());
		thisPanel.add(makeSettingPanel());				
		return thisPanel;		
	}
	Container makeBoardPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.add(board);	
		thisPanel.setAlignmentY(TOP_ALIGNMENT);
		return thisPanel;
	}
	
	Container makeSettingPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		thisPanel.add(makeControlPanel());
		thisPanel.add(makeStatPanel());
		thisPanel.add(makeTrainPanel());
		thisPanel.setAlignmentY(TOP_ALIGNMENT);
		return thisPanel;
	}
	Container makeDimensionPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		Image front = Toolkit.getDefaultToolkit().getImage("front.png");
		Icon frontIcon = new ImageIcon(front);
		JLabel background = new JLabel(frontIcon);
		background.setAlignmentX(LEFT_ALIGNMENT);
		thisPanel.add(background);
		
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		subPanel.setAlignmentX(LEFT_ALIGNMENT);
	

		
		JRadioButton fiveByFive = new JRadioButton("5x5", true);
		fiveByFive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				dimension = 5;
			}
		});
		JRadioButton sevenBySeven = new JRadioButton("7x7", false);
		sevenBySeven.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				dimension = 7;
				;
			}
		});
		JRadioButton nineByNine = new JRadioButton("9x9", false);
		nineByNine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				dimension = 9;
			}
		});
		

		ButtonGroup bg = new ButtonGroup();
		bg.add(fiveByFive);
		bg.add(sevenBySeven);
		bg.add(nineByNine);
		
		
		JButton start = new JButton("	Click Here to Start		");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				startGame();
			}
		});
		
		subPanel.add(Box.createRigidArea(new Dimension(95,0)));
		subPanel.add(fiveByFive);
		subPanel.add(Box.createRigidArea(new Dimension(200,0)));
		subPanel.add(sevenBySeven);
		subPanel.add(Box.createRigidArea(new Dimension(210,0)));
		subPanel.add(nineByNine);
		
		thisPanel.add(Box.createRigidArea(new Dimension(5,0)));
		thisPanel.add(subPanel);
		
		JPanel subPane1 = new JPanel();
		
		subPane1.add(start);
		subPane1.setAlignmentX(LEFT_ALIGNMENT);
		thisPanel.add(subPane1);
		return thisPanel;
	}
    
	Container makeControlPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		thisPanel.setBorder(BorderFactory.createTitledBorder("Game Control"));	
	
		thisPanel.add(makePlayerPanel());
		thisPanel.add(makeSpeedButton());
		thisPanel.add(makeButtonPanel());
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		return thisPanel;
	}
	Container makePlayerPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		JLabel title = new JLabel("Players: ");
		title.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel subPane = new JPanel();
		subPane.setLayout(new BoxLayout(subPane, BoxLayout.X_AXIS));
		subPane.setAlignmentX(LEFT_ALIGNMENT);
		JPanel subPane2 = new JPanel();
		subPane2.setAlignmentY(TOP_ALIGNMENT);
		subPane2.setLayout(new BoxLayout(subPane2, BoxLayout.Y_AXIS));
		
		JLabel l = new JLabel("Vs.");
		l.setForeground(Color.BLACK);
		l.setFont(new Font("Serif", Font.BOLD, 15));
		
		JPanel subPane1 = new JPanel();
		subPane1.setAlignmentY(TOP_ALIGNMENT);
		subPane1.setLayout(new BoxLayout(subPane1, BoxLayout.Y_AXIS));
		
		smart = new JRadioButton("TheSmart ");
		smart.setForeground(Color.RED);
		smart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.selectOPlayer(0);
			}
		});
		
		luck = new JRadioButton("NothingButLuck", true);
		luck.setForeground(Color.RED);
		luck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.selectOPlayer(1);
			}
		});
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(luck);
		bg2.add(smart);
		
		
		
		trained = new JRadioButton("NotSoDumb ", true);
		trained.setForeground(Color.BLUE);
		trained.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.selectRLPlayer(controller.TRAINED);
			}
		});
		
		unTrained = new JRadioButton("TheDummy");
		unTrained.setForeground(Color.BLUE);
		unTrained.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.selectRLPlayer(controller.UNTRAINED);
			}
		});
		ButtonGroup bg = new ButtonGroup();
		bg.add(trained);
		bg.add(unTrained);
		subPane1.add(trained);
		subPane1.add(unTrained);
		subPane2.add(luck);
		subPane2.add(smart);
		
		subPane.add(Box.createRigidArea(new Dimension(15,0)));
		subPane.add(subPane2);
		subPane.add(Box.createRigidArea(new Dimension(20,0)));
		subPane.add(l);
		subPane.add(Box.createRigidArea(new Dimension(25,0)));
		subPane.add(subPane1);
		thisPanel.add(title);
		thisPanel.add(Box.createVerticalGlue());
		thisPanel.add(Box.createRigidArea(new Dimension(0,5)));
		thisPanel.add(subPane);
		thisPanel.add(Box.createRigidArea(new Dimension(0,10)));
		thisPanel.add(makeFirstGoPanel());
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		return thisPanel;
	}
	Container makeStatPanel()
	{
		JPanel thisPanel = new JPanel();	
		thisPanel.setBorder(BorderFactory.createTitledBorder("Statistic"));
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		
		JPanel subPanel1= new JPanel();
		subPanel1.setAlignmentX(LEFT_ALIGNMENT);
		subPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel totalGame = new JLabel("Total Games: ");
		totalGameText = new JLabel("");
		subPanel1.add(totalGame);
		subPanel1.add(totalGameText);
		//subPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		JPanel subPanel2= new JPanel();
		subPanel2.setAlignmentX(LEFT_ALIGNMENT);
		subPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel O_win = new JLabel("  O Player: ");
		O_winText = new JLabel("");	
		O_win.setForeground(Color.RED);
		OPerct = new JLabel("");
		subPanel2.add(O_win);
		subPanel2.add(O_winText);
		subPanel2.add(OPerct);
		
		JPanel subPanel3= new JPanel();
		subPanel3.setAlignmentX(LEFT_ALIGNMENT);
		subPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel X_win = new JLabel("  X Player: ");
		X_winText = new JLabel("");
		X_win.setForeground(Color.BLUE);
		XPerct = new JLabel("");
		subPanel3.add(X_win);
		subPanel3.add(X_winText);
		subPanel3.add(XPerct);
		
		JPanel subPanel4= new JPanel();
		subPanel4.setAlignmentX(LEFT_ALIGNMENT);
		subPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel draw_win = new JLabel("  Draw: ");
		draw_Text = new JLabel("");
		DrawPerct = new JLabel("");
		subPanel4.add(draw_win);
		subPanel4.add(draw_Text);
		subPanel4.add(DrawPerct);
		
		thisPanel.add(subPanel1);
		thisPanel.add(subPanel2);
		thisPanel.add(subPanel3);
		thisPanel.add(subPanel4);
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		return thisPanel;
	}
	Container makeFirstGoPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel title = new JLabel("First to go: ");
		b2 = new JRadioButton("X", false);	
		b2.setForeground(Color.BLUE);
		b2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				controller.setFirstPlayer(2);
			}
		});
		b1 = new JRadioButton("O", false);	
		b1.setForeground(Color.RED);
		b1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				controller.setFirstPlayer(1);
			}
		});
		b3 = new JRadioButton("ALTERNATE", true);		
		b3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				controller.setFirstPlayer(0);
			}
		});
		ButtonGroup bg = new ButtonGroup();
		bg.add(b1);
		bg.add(b2);
		bg.add(b3);
		thisPanel.add(title);
		thisPanel.add(b1);
		thisPanel.add(b2);
		thisPanel.add(b3);
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		return thisPanel;
	}
	Container makeTrainPanel()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		thisPanel.setBorder(BorderFactory.createTitledBorder("Training"));
		
		JPanel subPanel4 = new JPanel();
		subPanel4.setLayout(new BoxLayout(subPanel4, BoxLayout.X_AXIS));
		subPanel4.setAlignmentX(LEFT_ALIGNMENT);
		JLabel t2 = new JLabel(" Trainee: ");
		JLabel t3 = new JLabel("TheDummny");
		t3.setForeground(Color.BLUE);
		subPanel4.add(t2);
		subPanel4.add(t3);
		
		
		JPanel subPanel3 = new JPanel();
		subPanel3.setLayout(new BoxLayout(subPanel3, BoxLayout.X_AXIS));
		subPanel3.setAlignmentX(LEFT_ALIGNMENT);
		JLabel t = new JLabel(" Train against: ");
		t.setAlignmentY(TOP_ALIGNMENT);
		subPanel3.add(t);
		JPanel sub4 = new JPanel();
		sub4.setLayout(new BoxLayout(sub4, BoxLayout.Y_AXIS));
		sub4.setAlignmentY(TOP_ALIGNMENT);
		
		trainer1 = new JRadioButton("TheSmart");
		trainer1.setForeground(Color.RED);
		trainer1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.setTrainOponent(controller.AI_PLAYER);
			}
		});
		trainer2 = new JRadioButton("NothingButLuck", true);
		trainer2.setForeground(Color.RED);
		trainer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.setTrainOponent(controller.RANDOM_PLAYER);
				
			}
		});
		ButtonGroup bg = new ButtonGroup();
		bg.add(trainer2);
		bg.add(trainer1);
		sub4.add(trainer1);
		sub4.add(trainer2);
		subPanel3.add(sub4);
		
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel text = new JLabel("Number of Training: ");
		
		trainTextField = new JTextField();
		trainTextField.setText("0");
		trainTextField.setPreferredSize(new Dimension(70,20));
		trainB = new JButton("Start Trainning");
		trainB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				int numTrain = Integer.parseInt(trainTextField.getText());
				controller.setTrainTime(numTrain);
				disableAllButton();
				controller.train = true;
			}
		});
		subPanel.add(text);
		subPanel.add(trainTextField);
		subPanel.add(trainB);
		subPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel subPanel2 = new JPanel();
		subPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		subPanel2.setAlignmentX(LEFT_ALIGNMENT);
		JLabel trained = new JLabel("Trained: ");
		trainText = new JLabel("0");
		trainText.setPreferredSize(new Dimension(145,20));
		reset = new JButton("Reset Training");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.resetTrain();
				//controller.train = false;
			}
		});
		subPanel2.add(trained);
		subPanel2.add(trainText);
		subPanel2.add(reset);
		
		JPanel subPanel1= new JPanel();
		subPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		subPanel1.setAlignmentX(LEFT_ALIGNMENT);
		JLabel progress = new JLabel("Progress: ");
		progressText = new JLabel("");
		subPanel1.add(progress);
		subPanel1.add(progressText);
		
		thisPanel.add(Box.createRigidArea(new Dimension(0,10)));
		thisPanel.add(subPanel4);
		thisPanel.add(Box.createRigidArea(new Dimension(0,15)));
		thisPanel.add(subPanel3);
		thisPanel.add(subPanel);
		thisPanel.add(subPanel2);
		thisPanel.add(subPanel1);
		
		return thisPanel;
	}
	Container makeButtonPanel()
	{
		JPanel thisPanel = new JPanel();				
		thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		startB = new JButton("Start");
		stopB = new JButton("Stop");
		resetScore = new JButton("Reset Score");
		startB.setActionCommand("START");
		changeGrid = new JButton("Change Grid");
		stopB.setActionCommand("STOP");
		
		
		startB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.gameRun = true;
				startB.setEnabled(false);
				stopB.setEnabled(true);
				resetScore.setEnabled(false);
				trainB.setEnabled(false);
				reset.setEnabled(false);
				changeGrid.setEnabled(false);
			}
		});
		thisPanel.add(startB);
		
		
		stopB.setEnabled(false);
		stopB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.gameRun = false;
				startB.setEnabled(true);
				stopB.setEnabled(false);
				resetScore.setEnabled(true);
				trainB.setEnabled(true);
				reset.setEnabled(true);
				changeGrid.setEnabled(true);
			}
		});
		thisPanel.add(stopB);	
		
		
		resetScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStat(0,0,0,0);
				controller.resetScore();
			}
		});
		
		changeGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabPane.setEnabledAt(1, false);
				resize(850,400);
				resetScreen();
				tabPane.setSelectedIndex(0);
			}
		});
		thisPanel.add(resetScore);	
		thisPanel.add(changeGrid);
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		return thisPanel;
	}
	Container makeSpeedButton()
	{
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel label= new JLabel("Speed: ");
		slow = new JRadioButton("Turtle", true);
		slow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.delay = 500;
			}
		});
		
		fast = new JRadioButton("Lightning");
		fast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				controller.delay = 0;
			}
		});
		ButtonGroup bg = new ButtonGroup();
		bg.add(slow);
		bg.add(fast);
		thisPanel.add(label);
		thisPanel.add(slow);
		thisPanel.add(fast);
		thisPanel.setAlignmentX(LEFT_ALIGNMENT);
		return thisPanel;
	}
	
	private void startGame()
	{
		tabPane.setEnabledAt(0, false);
		tabPane.setEnabledAt(1,true);
		tabPane.setSelectedIndex(1);
		//Load Database
		world = new World(dimension);
		board.world = world;
		board.setPreferredSize(board.getPreferredSize());
		Database database = new Database();
		try {
			database = (Database) (Tool.loadObject(PATH));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//Choose which RLPlayer to use.
		NeuralNet trainedNN = new NeuralNet(database, dimension);
		NeuralNet unTrainedNN = new NeuralNet(dimension);
		controller = new Controller(this, world, trainedNN, unTrainedNN,database, dimension, winBy);
		controller.start();
		if(dimension > 7)
		{
			//smart.setVisible(false);
			//luck.setEnabled(true);
			//trainer1.setVisible(false);
			//trainer2.setEnabled(true);
		}
		else
		{
			smart.setVisible(true);
			trainer1.setVisible(true);
		}
		this.resize( dimension * 50 + 500, 620);
	}
	
	public void updateStat(int o, int x, int draw, int totalGame)
	{
		int game = totalGame;
		if(game == 0)
			game = 1;
		X_winText.setText("" + x);
		XPerct.setText("(" + x*100/game + "%)");
		O_winText.setText("" + o);
		OPerct.setText("(" + o*100/game + "%)");
		draw_Text.setText("" + draw);
		DrawPerct.setText("(" + draw*100/game + "%)");
		totalGameText.setText("" + totalGame);
		
	}
	public void disableAllButton() {
		startB.setEnabled(false);
		resetScore.setEnabled(false);
		reset.setEnabled(false);
		trainB.setEnabled(false);
		changeGrid.setEnabled(false);
		progressText.setText("TRAINNING!!!");
		progressText.setForeground(Color.RED);
		progressText.setFont(new Font("Serif", Font.ITALIC, 15));
	}
	public void enableButton()
	{
		startB.setEnabled(true);
		resetScore.setEnabled(true);
		reset.setEnabled(true);
		trainB.setEnabled(true);
		changeGrid.setEnabled(true);
		progressText.setText("DONE!!!");
		progressText.setForeground(Color.BLUE);
		progressText.setFont(new Font("Serif", Font.ITALIC, 15));
		trainText.setText(Integer.toString(controller.totalTrain));
	}
	public void resetScreen()
	{
		X_winText.setText("");
		O_winText.setText("");
		draw_Text.setText("");
		XPerct.setText("");
		OPerct.setText("");
		DrawPerct.setText("");
		totalGameText.setText("");
		progressText.setText("");
		trainText.setText("");
		trainTextField.setText("0");
		smart.setSelected(false);
		luck.setSelected(true);
		trainer1.setSelected(false);
		trainer2.setSelected(true);
		b1.setSelected(false);
		b2.setSelected(false);
		b3.setSelected(true);
		slow.setSelected(true);
		fast.setSelected(false);
		trained.setSelected(true);
		unTrained.setSelected(false);
	}
	
	@Override
	public void run() {
		repaint();
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// TODO Auto-generated method stub
		
	}
}
