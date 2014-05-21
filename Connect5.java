//Jack Wang
//Final Sum
//2011, Jan, 13
//FINAL CONNECT 5 GAMMMMMEEE
//V4 with GUI and improved AI
/**
This is just an classic connect 5 game. who ever(computer/player)
gets a 5 in a row in any direction wins the game
all the GUI code is from pickup folder. i have no clue how some of the GUI code works
the save score function work
read from file and display work
ranking system work
timer
the Game will NOT work for low resolution monitor, please set it to 1000+

**/

//things to fix
//luck...

/**
all the necessary imports
**/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class Connect5 extends JFrame implements ActionListener{
	
	static int testing = 0;
	
	//most of the stuff for GUI. i have no clue how most of these works.....
	static int numOfGrid = 20; //how many grid is there
	
	private Icon blue = new ImageIcon( "Black.PNG" );//HKSKASK
	private Icon red = new ImageIcon( "Red.PNG" );
	private JLabel lbl[][] = new JLabel[numOfGrid][numOfGrid];       // number of buttons - can be changed
	private ImageIcon icon = new ImageIcon("BackGround_Final.jpg"); //background
	private Container c; //noidea
	static boolean playerWin = false, computerWin = false, tie = false; //this is for if player win or computer win.
	private JPanel btnPanel = new JPanel(){	  //for the background
		protected void paintComponent(Graphics g){
	  		Dimension d = getSize();
			g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
			super.paintComponent(g);
	  }};
	private JPanel southPanel = new JPanel(); //noidea
	private JButton actionBtn1= new JButton("New Game");     //can change aaaa   label on action buttons
	private JButton actionBtn2 = new JButton("Highscore");      //highscore
	private JButton exit = new JButton("Exit");          //label on action button
	JFrame frame = new JFrame("InputDialog Example #2"); /**This is to get user input. its not my code, for
															more info. please look at line: 
															**/
	static char gameGrid[][] = new char[numOfGrid][numOfGrid]; //actual grid
	static int playerGrid[][] = new int[numOfGrid][numOfGrid]; //point grid for player
	static int scoreGrid[][] = new int[numOfGrid][numOfGrid]; //point grid for computer
	static int numOfTurn = 0; //this is how many turn it took
	static long startTime = System.currentTimeMillis();
	static long endTime = System.currentTimeMillis();
	JScrollPane scrollPane; //no idea
	
	//actual class
	public Connect5(){
		super( "Connect 5: YOU ARE RED!!!" );//title??

		btnPanel.setLayout(new GridLayout(lbl.length,lbl[0].length)); //noidea
		southPanel.setLayout(new GridLayout(1,3));//noidea

		c = getContentPane();//noidea
		c.setLayout( new BorderLayout() );//noidea

		// create and add buttons
		paintGameTable();
		JOptionPane.showMessageDialog( null, "This is a Connect 5 Game\nYou are red and brown is computer\nGet 5 of your piece in a row and you win!\nHave fun!!!!!!!", "Welcome", JOptionPane.INFORMATION_MESSAGE );
		btnPanel.setOpaque( false );//if it can be seen
		btnPanel.setPreferredSize( new Dimension(600, 600) ); //how big the background is
		scrollPane = new JScrollPane( btnPanel ); //noidea
		getContentPane().add( scrollPane );//noidea
		this.addMouseListener (new MouseAdapter (){ //this is if the player click on the grid
			public void mousePressed (MouseEvent e){}
			public void mouseClicked (MouseEvent e)	{ //if clicked
				if(playerWin == false && computerWin == false && tie == false){ //if no one win. 
					int x = (int)Math.floor(e.getX()/40.5),y = (int)Math.floor((e.getY()-40)/40.5); //get the pos
					if(x>19)x = 19;
					if(y>19)y = 19;
					btnPanel.removeAll(); //remove all the icon
					gameGame(y,x);//input the new pos for player
					paintGameTable(); //repaint the table
				}
			}
		});
		actionBtn1.addActionListener( this );//noidea
		southPanel.add(actionBtn1);//noidea
		actionBtn2.addActionListener( this );//noidea
		southPanel.add(actionBtn2);//noidea
		exit.addActionListener( this );//noidea
		southPanel.add(exit);//noidea
	  
		c.add( southPanel, BorderLayout.SOUTH );//noidea
		c.add( btnPanel, BorderLayout.CENTER  );//noidea
		setSize(810,880); //size of the window
		setResizable(false); //if the size can be changed
		show(); //show the window??
	}
	
	//this is to paint the icon
	public void paintGameTable(){
		for (int i = 0; i < lbl.length; i++ ) { //row
			for (int j = 0; j < lbl[0].length; j++ ) { //col
				//lbl[i][j] = new JLabel("       "+gameGrid[i][j]);                    //what ever the letter is in the grid
				if(gameGrid[i][j]=='X')lbl[i][j] = new JLabel(red);
				else if(gameGrid[i][j]=='O')lbl[i][j] = new JLabel(blue);
				else lbl[i][j] = new JLabel("        ");
				btnPanel.add(  lbl[i][j] ); //add??
			}  
		}
		repaint(); //repaint
		show(); //show
		if(playerWin){
			JOptionPane.showMessageDialog( null, winAndLose("Win"), "Win", JOptionPane.INFORMATION_MESSAGE );//if Player Lost
			/**
			This part is not my code. i got this from other sites. its about getting userinputs. 
			the site i got it from is 
			http://www.devdaily.com/java/joptionpane-showinputdialog-examples
			again. its NOt my code
			**/
			String playerName = "";
			playerName = JOptionPane.showInputDialog(frame, 
                          "Please enter your name and you'll be saved on Highscore\nClick cancel to continue without saving name", 
                          "Enter Player Name", 
                          JOptionPane.WARNING_MESSAGE);
			
				if(playerName==null){}
				else if(playerName.length()!=0){
					
					saveGameFile(playerName);
				}
			
		}
		else if(computerWin)JOptionPane.showMessageDialog( null, winAndLose("Lost"), "Lost", JOptionPane.INFORMATION_MESSAGE );  //if player Won
		
		else if(tie)JOptionPane.showMessageDialog( null, "its a tie", "Tie", JOptionPane.INFORMATION_MESSAGE );
	}
	
	//button??
	public void actionPerformed( ActionEvent e ){ 
		//if click on first button 'new game'
		if (e.getSource()==actionBtn1){
			btnPanel.removeAll(); //remove all icon
			creatGameGrid(); //recreat the grid
			paintGameTable(); //repaint the table
		    JOptionPane.showMessageDialog( null, "Ok, New Game!!", "New Game", JOptionPane.INFORMATION_MESSAGE ); //message
		}
		
		//this is for highscore
		else if (e.getSource()==actionBtn2){
			//   code for button bbbb event goes here.....................
			try{
				JOptionPane.showMessageDialog( null, highScore(1,"non"), "HighScore", JOptionPane.INFORMATION_MESSAGE );
			}catch(Exception a){System.out.print(a);}
		}
		
		//if player click on exit
		else if (e.getSource()==exit){
		    System.exit(0);
		}
			   
    }// end actionPerformed      

	//the main class 
	public static void main( String args[] ){
		creatGameGrid();
		Connect5 app = new Connect5(); //noidea
		app.addWindowListener( //noidea
			new WindowAdapter() { //noidea
            public void windowClosing( WindowEvent e )//noidea
            {//noidea
               System.exit( 0 );//noidea
            }
        }
		);
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////       creat the game grid needed      ///////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static void creatGameGrid(){
		for(int i = 0; i<gameGrid.length; i++){ //this method will add "." too all the grids
			for(int a = 0; a<gameGrid.length; a++)gameGrid[i][a]='.';//s.charAt(num++);
		}
		playerWin = false;
		computerWin = false;
		numOfTurn = 0;
		startTime = endTime;
		if(Math.random()>0.7) gameGrid[10][10] = 'O';
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////         display the game and reset all the variables that needs to be rested     ////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static void displayGameAndReset(){
		for(int i = 0; i< numOfGrid; i++){ 
			for(int a = 0; a< numOfGrid; a++){
				scoreGrid[i][a] = 0; //reset the score
				playerGrid[i][a] = 0;  //reset the score
			}
		}
	}
	
//this the where the game actually starts. is it to see how win and  
	static void gameGame(int row, int col){
	
		//this is where the player make the move, if the space is not already used
		if(gameGrid[row][col]!='O'&&gameGrid[row][col]!='X'){
		
			//player's move will be put there
			playerMove(row,col);
			
			//find computer's next move
			situationFind();
			
			//turn will increase be 1
			numOfTurn++;
			
			if (numOfTurn >= 200) tie = true;
		
			if(testing == 1)playerWin = true;
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////              player's turn to go            ////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static void playerMove(int x, int y){
		int row = x, col = y; 
		gameGrid[row][col] = 'X'; //add X for player on the postions
		checkWin(row,col);//check if player won
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////                   check if computer/player won the game  /////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static void checkWin(int row, int col){
	
		//the four possible winning position
		//accross, updown, diagonal
		String srow="",scol="",strbl="",stlbr="",fString=""; 
		for(int pos = -4; pos <=4; pos++){ //this loop will test the four possible positions
			if(col+pos>=0&&col+pos<=numOfGrid-1)srow+=gameGrid[row][pos+col];
			if(row+pos>=0&&row+pos<=numOfGrid-1)scol+=gameGrid[pos+row][col];
			if(row+pos>=0&&col-pos>=0&&row+pos<numOfGrid&&col-pos<numOfGrid)strbl+=gameGrid[pos+row][col-pos];
			if(row+pos>=0&&col+pos>=0&&row+pos<numOfGrid&&col+pos<numOfGrid)stlbr+=gameGrid[pos+row][col+pos];
		}
		
		//this will add all the string together
		fString = srow+"-"+scol+"-"+strbl+"-"+stlbr;
		
		//if any of them will have a 5 in a row.
		if(fString.indexOf("XXXXX")!=-1){playerWin = true;} //if 5 in row for player
		else if(fString.indexOf("OOOOO")!=-1){computerWin = true;} //if 5 in row for player
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////        for computer to find where is best to place next move    /////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static void situationFind(){
		//strbl = string top right to bottom left, 
		//stlbr = string top left to bottom right
		//srow = string row
		//scol = string column
		int NPCrow = 0, NPCcol = 0, biggestTotal = 0, computerPoint = 0, playerPoint = 0;
		String temp="";
		//this will test all the spaces in the 2d grid
		for(int numOfRun = 0; numOfRun<=1; numOfRun++){ //# of run, run 0 is for computer, run 1 is for player
			for(int row = 0; row < numOfGrid; row++){ //row
				for(int col = 0; col < numOfGrid; col++){ //col
					if(gameGrid[row][col]!='O'&&gameGrid[row][col]!='X'){ //if the space is not occupied
						String srow = "", scol ="", strbl = "", stlbr = ""; //creat the string needed
						int prow = 4, pcol = 4,pstrbl = 4,pstlbr = 4,total = 0, numOfT = 0, numOfF = 0, numOfP=0; //and the index + other variables
						
						//this loop will run 9 times. to get all 9 position for each square
						for(int pos = -4; pos <=4; pos++){
						
							//horzontal
							if(col+pos>=0&&col+pos<=numOfGrid-1){
								if (pos==0)srow+='O'; //this will add a O for the computer to claculate
								else srow+=gameGrid[row][pos+col];
							}
							
							//vertical
							if(row+pos>=0&&row+pos<=numOfGrid-1){
								if (pos==0)scol+='O';
								else scol+=gameGrid[pos+row][col];
							}
							
							//top right to bottom left
							if(row+pos>=0&&col-pos>=0&&row+pos<numOfGrid&&col-pos<numOfGrid){
								if (pos==0) strbl+='O';
								else strbl+=gameGrid[pos+row][col-pos];
							}
							
							//top left to bottom right
							if(row+pos>=0&&col+pos>=0&&row+pos<numOfGrid&&col+pos<numOfGrid){
								if (pos==0) stlbr+='O';
								else stlbr+=gameGrid[pos+row][col+pos];
							}
						}
						
						//the first if is for index thats greater than 4
						//second if if for the actual test
						if(col<=3)prow = col;
						if(removeOutCast(srow,prow,'O')){ //if remove out cast return true, it will continue to process
							temp=analyse(srow,numOfRun); //get the points + letter
							if(temp.charAt(temp.length()-1)=='F'||temp.charAt(temp.length()-1)=='T'||temp.charAt(temp.length()-1)=='P'){ //the next 4 lines are for situations
								if (temp.charAt(temp.length()-1)=='F') numOfF++; //add the number of F
								else if (temp.charAt(temp.length()-1)=='P') numOfP++; //add the number of P
								else if (temp.charAt(temp.length()-1)=='T') numOfT++; //add the number of T
								total+=Integer.parseInt(temp.substring(0,temp.length()-1)); //add the total
							}
							else total+=Integer.parseInt(temp); //add the total
						}
						
						//same, except vertical
						//the next 3 are ABOUT the same
						//it cant be done as method at my lvl because it require the integer and add 1 to it, and there are 3 integers...so
						//i cant do it in methdos
						if(row<=3)pcol = row;
						if(removeOutCast(scol,pcol,'O')){
							temp=analyse(scol,numOfRun);
							if(temp.charAt(temp.length()-1)=='F'||temp.charAt(temp.length()-1)=='T'||temp.charAt(temp.length()-1)=='P'){
								if (temp.charAt(temp.length()-1)=='F') numOfF++;
								else if (temp.charAt(temp.length()-1)=='P') numOfP++;
								else if (temp.charAt(temp.length()-1)=='T') numOfT++;
								total+=Integer.parseInt(temp.substring(0,temp.length()-1));
							}
							else total+=Integer.parseInt(temp);
						}
						
						//same, top right bottom left
						if(numOfGrid-1-col<4||row<4)pstrbl = Math.min(numOfGrid-1-col,row);
						if(removeOutCast(strbl,pstrbl,'O')){
							temp=analyse(strbl,numOfRun);
							if(temp.charAt(temp.length()-1)=='F'||temp.charAt(temp.length()-1)=='T'||temp.charAt(temp.length()-1)=='P'){
								if (temp.charAt(temp.length()-1)=='F') numOfF++;
								else if (temp.charAt(temp.length()-1)=='P') numOfP++;
								else if (temp.charAt(temp.length()-1)=='T') numOfT++;
								total+=Integer.parseInt(temp.substring(0,temp.length()-1))+2;
							}
							else total+=Integer.parseInt(temp)+2;
						}
						
						//same top left bottom right
						if(row<5||col<5)pstlbr = Math.min(row,col);
						if(removeOutCast(stlbr,pstlbr,'O')){
							temp=analyse(stlbr,numOfRun);
							if(temp.charAt(temp.length()-1)=='F'||temp.charAt(temp.length()-1)=='T'||temp.charAt(temp.length()-1)=='P'){
								if (temp.charAt(temp.length()-1)=='F') numOfF++;
								else if (temp.charAt(temp.length()-1)=='P') numOfP++;
								else if (temp.charAt(temp.length()-1)=='T') numOfT++;
								total+=Integer.parseInt(temp.substring(0,temp.length()-1))+2;
							}
							else total+=Integer.parseInt(temp)+2;
						}
						//situation 1
						/*
						. . . . . . . . . . 
						. H . . . . . . . . 
						. O O . . . . . . . 
						. O . . . . . . . . 
						. . . . O . . . . . 
						. . . . . . . . . . 
						. . . . . . . . . .   computer should place at H 
						*/
						if(numOfT>=2&&numOfF==0)total=5000;
						
						//situation 2
						/*
						. . . . . . . . . . 
						. H . . . . . . . . 
						. O O . . . . . . . 
						. O . . . . . . . . 
						. O . . O . . . . . 
						. X . . . . . . . . 
						. . . . . . . . . .   computer should place at H 
						*/
						
						if(numOfP>=1&&numOfT>=1&&numOfF==0){total=5000;}
						
						
						//add the point to the grid
						if(numOfRun==0){scoreGrid[row][col]+=total;computerPoint+=total;}
						else {playerGrid[row][col]+=total;playerPoint+=total;}
						
					}
				}
			}
			
			//reverse the grid to calculate for player and reverse back in the end
			for(int i = 0; i<numOfGrid; i++){
				for(int a = 0; a<numOfGrid; a++){
					if(gameGrid[i][a]=='O')gameGrid[i][a] = 'X';
					else if(gameGrid[i][a]=='X')gameGrid[i][a] = 'O';
				}
			}
		}
		//this is the part where the computer finalize where to put the next move
		int posrow = 0, poscol = 0;
		double multiplier = checkAggression(computerPoint, playerPoint), highest = 0;
		
		for(int i = 0; i<numOfGrid; i++){
			for(int a = 0; a<numOfGrid; a++){
				if(scoreGrid[i][a]+playerGrid[i][a]*multiplier>=highest){
				
					/**this os the part the computer decideds where to put the next move
					if the highest score has more than 1. then the computer will
					decided where to put by random
					**/
					if(scoreGrid[i][a]+playerGrid[i][a]*multiplier==highest){
						if(Math.random()>0.7){ //this is for where to put the next move.
							highest = scoreGrid[i][a]+playerGrid[i][a]*multiplier;
							posrow = i; 
							poscol = a;
						}
					}
					else { 
						highest = scoreGrid[i][a]+playerGrid[i][a]*multiplier;
						posrow = i;
						poscol = a;
					}
				}
			}
		}
		/*
		for(int i = 0; i<numOfGrid; i++){
			for(int a = 0; a< numOfGrid; a++){
				System.out.printf("%4d",(int)(scoreGrid[i][a]+playerGrid[i][a]*multiplier));
			}
			System.out.println();
		}
		*/
		//where is where to put the next move
		gameGrid[posrow][poscol] = 'O';
		displayGameAndReset(); //reset the score board
		checkWin(posrow,poscol); //check if computer won
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////        remove any of the string that will get 0 points         /////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static boolean removeOutCast(String testCase, int pos, char turn){
		int startPos = 0, endPos = 0, numOfTurn =0;
		
		//this loop is to check to see if the space is enough for a 5 in a row
		//at the edge of the map
		for(int i = 0; i<testCase.length(); i++){
			if(testCase.charAt(i)==turn)numOfTurn++;
		}
		
		//if not. 
		if(numOfTurn<=1)return false;
		
		//to see where the opp's chess is at on the left
		for(int i = pos; i>=0; i--){
			if(i==0)startPos = 0;
			else if(testCase.charAt(i)=='X'){startPos = i;break;}
		}
		
		//to see where the opp's chess on the right
		for(int i = pos; i<=i+4; i++){
			if(i==testCase.length()-1&&testCase.charAt(i)!='X'){endPos = i+1;break;}
			else if(testCase.charAt(i)=='X'){endPos = i;break;}
		}
		
		//if the space is less than 5, return false
		if(endPos-startPos<=5)return false;
		
		//if all works
		return true;
	} 

/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////           analyse the cases and see how much the position is worth ////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static String analyse (String testCase, int turn){
	
		//the numbers are how much the string will be worth
		final int win = 90000, fourInRow = 10000, fourInRowSpace = 300, threeInRow = 600, 
					threeInRowSpace = 450, threeInRowSpace2 = 40, twoInRow = 70, twoInRowSpace = 60, fourInRowBlockSpace = 100, 
					fourInRowBlock = 1000, threeInRowBlock = 50, threeInRowBlock2 = 30, twoInRowBlock = 5, twoinRowBlock2 = 2;
		
		//this is for player part
		final int fourInRowBlock_P = 200, threeInRow_P = 300, threeInRowBlock_P = 100, fourInRowBlockSpace_P = 50, threeInRowSpace_P = 150, fourInRow_P = 5000;
		
		String total = "";
		
		//all the possible cases
		if (testCase.indexOf("OOOOO")!=-1)total+=win+"F"; 
		
		else if(testCase.indexOf("XOOOO")!=-1||testCase.indexOf("OOOOX")!=-1){
					if(turn==0)total+=fourInRowBlock+"P";
					else total+=fourInRowBlock_P+"P";
				}
		else if(testCase.indexOf("OOOO")!=-1){
			if(turn == 0)total+=fourInRow+"F";
			else total+= fourInRow_P+"F";
		}
		else if(testCase.indexOf("O.OOOX")!=-1||testCase.indexOf("OO.OOX")!=-1||testCase.indexOf("OOO.OX")!=-1||
				testCase.indexOf("XO.OOO")!=-1||testCase.indexOf("XOO.OO")!=-1||testCase.indexOf("XOOO.O")!=-1){
					if(turn==0)total+=fourInRowBlockSpace;
					else total+=fourInRowBlockSpace_P;
				}
		else if(testCase.indexOf("O.OOO")!=-1||testCase.indexOf("OO.OO")!=-1||testCase.indexOf("OOO.O")!=-1)total+=fourInRowSpace+"F";
		else if(testCase.indexOf("OO..OX")!=-1||testCase.indexOf("XOO..O")!=-1||testCase.indexOf("O..OOX")!=-1||
				testCase.indexOf("XO..OO")!=-1) total+=threeInRowBlock2;
		else if(testCase.indexOf("XOOO")!=-1||testCase.indexOf("OOOX")!=-1||testCase.indexOf("XO.OO")!=-1||
				testCase.indexOf("O.OOX")!=-1||testCase.indexOf("XOO.O")!=-1||testCase.indexOf("OO.OX")!=-1)total+=threeInRowBlock;
		else if(testCase.indexOf("OOO")!=-1){
				if(turn==0)total+=threeInRow+"T";
				else total+=threeInRow_P+"T";
				}
		else if(testCase.indexOf("O..OO")!=-1||testCase.indexOf("OO..O")!=-1)total+=threeInRowSpace2;
		else if(testCase.indexOf("OO.O")!=-1||testCase.indexOf("O.OO")!=-1){
			if(turn==0)total+=threeInRowSpace+"T";
			else total+=threeInRowSpace_P+"T";
			}
		else if(testCase.indexOf("XOO")!=-1||testCase.indexOf("OOX")!=-1||testCase.indexOf("O.OX")!=-1||
				testCase.indexOf("XO.O")!=-1)total+=twoInRowBlock;
		else if(testCase.indexOf("XO..O")!=-1||testCase.indexOf("O..OX")!=-1) total+=twoinRowBlock2;
		else if(testCase.indexOf("OO")!=-1)total+=twoInRow;
		else if(testCase.indexOf("O.O")!=-1||testCase.indexOf("O..O")!=-1||testCase.indexOf("O...O")!=-1)total+=twoInRowSpace;
		else total="0";
		
		//return
		return total;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////          set the aggression of the game          ////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	static double checkAggression(int computerPoint, int playerPoint){
		//will set aggression depended on the points
		if(playerPoint>=computerPoint*1.4)return 1.4; //if player is winning
		if(playerPoint>=computerPoint*1.2)return 1.1; //if player is winning by a bit
		if(computerPoint>=playerPoint*1.4)return 0.6; //if computer is winning
		if(computerPoint>=playerPoint*1.2)return 0.8; //if computer is winning by a bit
		else return 0.9; //defalt  
	}  
	
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////              HIGHSCORE                   //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
	static String highScore(int display, String nameOfPlayer){
	
		//enable scanner and get highscore file
		int turn = 1, score;
		String raw="", playerName, highScore;
		ArrayList <String> name = new ArrayList <String>();
		try{
			Scanner in = new Scanner(new File("DATA/HIGHSCORE.txt"));
			
			//use arraylist to store all the name and scor
			
			
			//read the file
			while(in.hasNextLine()){
				raw = in.nextLine();
				if(raw.length() == 0)break;
				
				//when at the last line of the file
				//since there is no text, a indexout fo bound will occur
				//then this will catch the error and break out of lop
				//try{ 
					name.add((raw.substring(raw.indexOf("$")+1))+""+(raw.substring(0,raw.indexOf("$"))));
				//}catch(StringIndexOutOfBoundsException e){break;}
			}
			in.close();	
		}catch(Exception e){System.out.println(e);}
		//use collection to sort the arraylist
		/**
		for collection, i found it on internet when i made my old string game
		so i have a good idea how to use it
		i got it from oracle.com
		*/
		Collections.sort(name);
		
		//if its for a new highscore
		if(display == 0) highScore = "You Are Ranked: "+(name.indexOf((nameOfPlayer.substring(nameOfPlayer.indexOf("$")+1))+""+(nameOfPlayer.substring(0,nameOfPlayer.indexOf("$"))))+1)+"\nName               Turn  \n";//defalt the first line of text
		
		//normal score display
		else highScore = "Name               Turn  \n";
		
		//loop that will go though the list
		for(int i = 0; i<name.size(); i++){
		
			//only the top 10 will be displayed
			if(i>10)break;
			
			//the score will be turned into integer
			score = Integer.parseInt(name.get(i).substring(0,3));
			playerName = name.get(i).substring(3);
			
			//this is to make the name's length at 18. 
			//but java swing is messed up. not all letter have the same width
			//so it looks kinda weird
			while(playerName.length()<18){
				playerName+="-";
			}
			
			//add the line down
			highScore += "|"+playerName+" "+score+"\n-----------------------\n";
		}
		
		//return
		return highScore;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////          WIN AND LOSE       ////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
	static String winAndLose(String type){
	
		//return a string to have all the info about how many steps play took to 
		//win or lose the game
		endTime = System.currentTimeMillis(); //get the time so you can see how ong you took
		return "You "+type+"! \nIn "+numOfTurn+" Moves\n"+"It Took You "+(endTime-startTime)/1000+" Seconds\nClick new Game To Restart";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////               SAVE TO FILE          //////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
	static void saveGameFile(String name){
		//get the scanner for the file
		//arraylist to add all the name and score into it etc
		ArrayList <String> namePlayer = new ArrayList <String> ();
		ArrayList <Integer> score = new ArrayList <Integer>();
		String raw, num, theOne = "";
		try{
			Scanner in 	= new Scanner(new File("DATA/HIGHSCORE.txt"));
		
		//add it to file
			while(in.hasNextLine()){
				//try{ //if any error occurs. most likely indexout of bound error, 
					//meaning its at the end of file. it will just break
					//since the last line of the file is empty
					raw = in.nextLine();
					if(raw.length()==0)break;
					namePlayer.add(raw.substring(0,raw.indexOf("$")));
					score.add(Integer.parseInt(raw.substring(raw.indexOf("$")+1)));
				//}catch(Exception e){System.out.println(e);}
			}
			in.close();
			}catch(Exception e){System.out.println();}/**see if the player already have a name in highscore
		if yes. it will update the score and not add a new name 
		into the score**/
		if(namePlayer.indexOf(name)!=-1){
			if(score.get(namePlayer.indexOf(name))>numOfTurn){
				score.remove(namePlayer.indexOf(name)); //remove the value
				score.add(namePlayer.indexOf(name),numOfTurn);  //add the new score into that index
			}
		}
		
		//if its a new player
		else{
			namePlayer.add(name);
			score.add(numOfTurn);
		} 
		
		
		//to write to file
		
				
		try{
			PrintWriter out = new PrintWriter("DATA/HIGHSCORE.txt");
			//this will add 0 to the num and make it a 3 digit num. example 001
			for(int i = 0; i<score.size();i++){
				num = score.get(i)+"";
				while(num.length()<3){
					num = "0" + num;
				}
				
				//this is for if the player win 
				//it will be needed for the show of the highscore
				if(namePlayer.get(i).equals(name)){
					theOne = namePlayer.get(i)+"$"+num;
				}
				
				//print to file
				out.println(namePlayer.get(i)+"$"+num);
			}
			
			//close the file writer
			out.close();
		}catch(Exception e){System.out.println(e);}
		//show the highscore
		JOptionPane.showMessageDialog( null, highScore(0, theOne), "Highscore", JOptionPane.INFORMATION_MESSAGE );
	}
}