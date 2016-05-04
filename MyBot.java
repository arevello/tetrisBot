import java.util.Scanner;

public class MyBot {

	public int[][] board = new int[20][10];
	public int[] height = new int[10];
    private Scanner scan = new Scanner(System.in);
    public String nextMove = "";

    public void run()
    {
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            if(line.length() == 0) { continue; }
            String[] parts = line.split(" ");

            switch(parts[0]) {
                case "settings":
                    // store game settings
                    break;
                case "update":
                    switch(parts[1]){
                    case "game":
                    	switch(parts[2]){
                    	case "this_piece_type":
                    		switch(parts[3]){
                    		case "I":
                    			getNextMove(1);
                    			break;
                    		case "J":
                    			getNextMove(2);
                    			break;
                    		case "L":
                    			getNextMove(3);
                    			break;
                    		case "O":
                    			getNextMove(4);
                    			break;
                    		case "S":
                    			getNextMove(5);
                    			break;
                    		case "T":
                    			getNextMove(6);
                    			break;
                    		case "Z":
                    			getNextMove(7);
                    			break;
                    		}
                    		break;
                    	}
                    	break;
                    case "player1":
                    	switch(parts[2]){
                    	case "field":
                    		updateField(parts[3]);
                    		updateHeights();
                    		break;
                    	}
                    	break;
                    }
                    break;
                case "action":
                    System.out.println(nextMove);
                    System.out.flush();
                    nextMove = "drop";
                    break;
                default:
                    // error
            }
        }
    }
    
    public void updateField(String field){
    	String[] lines = field.split(";");
    	for(int i = 0; i < lines.length; i++){
    		String[] pieces = lines[i].split(",");
    		for (int j = 0; j < 10; j++){
    			board[19-i][0] = Integer.parseInt(pieces[j]);
    		}
    	}
    }
    
    public void updateHeights(){
    	for(int i = 0; i < 10; i++){
    		for(int j = 0; j < 20; j++){
    			if(board[i][j]  == 0){
    				height[i] = j-1;
    				break;
    			}
    		}
    	}
    }
    
    public String OScore(){
    	int[] possibleMoves = new int[9];
    	int[][] tempBoard = new int[20][10];
    	
    	//get scores of possible moves
    	for(int i = 0; i < 9; i++){
    		int tempMax = 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>tempMax)
    				tempMax = height[i+j];
    		}
    		tempBoard = board;
    		for(int j = 0; j < 2; j++){
    			tempBoard[i+j][tempMax+1] = 1;
    			tempBoard[i+j][tempMax+2] = 1;
    		}
    		possibleMoves[i] = getScore(tempBoard);
    	}
    	
    	//best move
    	int max = 0;
    	int nextMove = 0;
    	for(int i = 0; i < possibleMoves.length; i++){
    		if(possibleMoves[i] > max){
    			max = possibleMoves[i];
    			nextMove = i;
    		}
    	}
    	
    	//write next move
    	String ret = "";
    	if(nextMove < 4){
    		ret = getLefts(nextMove, 4);
    	}
    	else if(nextMove > 4){
    		ret = getRights(nextMove,4);
    	}
    	else
    		ret = "drop";
    	
    	return ret;
    }
    
    public String getLefts(int move, int num){
    	String ret = "";
    	int lefts = num - move;
		for (int i = 0;i < lefts; i++)
			ret += "left,";
		ret += "drop";
		return ret;
    }
    
    public String getRights(int move, int num){
    	String ret = "";
    	int rights = move - num;
		for (int i = 0;i < rights; i++)
			ret += "right,";
		ret += "drop";
		return ret;
    }
    
    public String JScore(){
    	int[] possibleMoves = new int[34];
    	int[][] tempBoard = new int[20][10];
    	
    	//no rot, 3 len
    	for(int i = 0; i < 8; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 3; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		tempBoard[i][tempMax+1] = 1;
    		tempBoard[i][tempMax+2] = 1;
    		tempBoard[i+1][tempMax+1] = 1;
    		tempBoard[i+2][tempMax+1] = 1;
    		
    		possibleMoves[i] = getScore(tempBoard);
    	}
    	//1 rot, 2 len
    	for(int i = 0; i< 9; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		tempBoard[i][tempMax+1] = 1;
    		tempBoard[i+1][tempMax+1] = 1;
    		tempBoard[i+1][tempMax+2] = 1;
    		tempBoard[i+1][tempMax+3] = 1;
    		
    		possibleMoves[i+8] = getScore(tempBoard);
    	}
    	//2 rot, 3 len
    	for(int i = 0; i< 8; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 3; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		if(tempMaxIdx == 2){
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    			tempBoard[i+2][tempMax+1] = 1;
    			tempBoard[i+2][tempMax+2] = 1;
    		}
    		else{
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+2][tempMax+1] = 1;
    			tempBoard[i+2][tempMax] = 1;
    		}
    		
    		possibleMoves[i+17] = getScore(tempBoard);
    	}
    	//3 rot, 2 len
    	for(int i = 0; i< 9; i++){
    		int tempMax = 0, tempMaxIdx= 0, tempMaxPos = 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    				tempMaxPos = i+j;
    			}
    		}
    		
    		tempBoard = board;
    		if(tempMaxIdx == 1){
    			if(height[tempMaxPos-1] == tempMax - 1){
    				tempBoard[i][tempMax] = 1;
    				tempBoard[i][tempMax+1] = 1;
    				tempBoard[i][tempMax+2] = 1;
    				tempBoard[i+1][tempMax+2] = 1;
    			}
    			else {
    				tempBoard[i][tempMax-1] = 1;
    				tempBoard[i][tempMax] = 1;
    				tempBoard[i][tempMax+1] = 1;
    				tempBoard[i+1][tempMax+1] = 1;
    			}
    		}
    		else{
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i][tempMax+3] = 1;
    			tempBoard[i+1][tempMax+3] = 1;
    		}
    		
    		possibleMoves[i+25] = getScore(tempBoard);
    	}
    	
    	//best move
    	int max = 0;
    	int nextMove = 0;
    	for(int i = 0; i < possibleMoves.length; i++){
    		if(possibleMoves[i] > max){
    			max = possibleMoves[i];
    			nextMove = i;
    		}
    	}
    	
    	//find move
    	String ret = "";
    	if(nextMove < 8){
    		if(nextMove > 4){
    			ret = getRights(nextMove, 4);
    		}
    		else if(nextMove < 4){
    			ret = getLefts(nextMove, 4);
    		}
    		else
    			ret = "drop";
    	}
    	else if( nextMove >= 8 && nextMove < 17){
    		ret += "turnright,";
    		if(nextMove > 7 && nextMove < 11){
    			ret += getLefts(nextMove, 11);
    		}
    		else if (nextMove > 11){
    			ret += getRights(nextMove, 11);
    		}
    		else
    			ret += "drop";
    	}
    	else if( nextMove >= 17 && nextMove < 25){
    		ret += "turnright,turnright";
    		if(nextMove > 16 && nextMove < 20){
    			ret += getLefts(nextMove, 20);
    		}
    		else if (nextMove > 20){
    			ret += getRights(nextMove, 20);
    		}
    		else
    			ret += "drop";
    	}
    	else{
    		ret += "turnright,turnright,turnright";
    		if(nextMove > 24 && nextMove < 28){
    			ret += getLefts(nextMove, 28);
    		}
    		else if (nextMove > 28){
    			ret += getRights(nextMove, 28);
    		}
    		else
    			ret += "drop";
    	}
    	return ret;
    }
    
    public String LScore(){
    	int[] possibleMoves = new int[34];
    	int[][] tempBoard = new int[20][10];
    	
    	//no rot, 3 len
    	for(int i = 0; i < 8; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 3; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		tempBoard[i][tempMax+1] = 1;
    		tempBoard[i+1][tempMax+1] = 1;
    		tempBoard[i+2][tempMax+1] = 1;
    		tempBoard[i+2][tempMax+2] = 1;
    		
    		possibleMoves[i] = getScore(tempBoard);
    	}
    	//1 rot, 2 len
    	for(int i = 0; i< 9; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		tempBoard[i][tempMax+1] = 1;
    		tempBoard[i][tempMax+2] = 1;
    		tempBoard[i][tempMax+3] = 1;
    		tempBoard[i+1][tempMax+1] = 1;
    		
    		possibleMoves[i+8] = getScore(tempBoard);
    	}
    	//2 rot, 3 len
    	for(int i = 0; i< 8; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 3; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		if(tempMaxIdx == 0){
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    			tempBoard[i+2][tempMax+2] = 1;
    		}
    		else{
    			tempBoard[i][tempMax] = 1;
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+2][tempMax+1] = 1;
    		}
    		
    		possibleMoves[i+17] = getScore(tempBoard);
    	}
    	//3 rot, 2 len
    	for(int i = 0; i< 9; i++){
    		int tempMax = 0, tempMaxIdx= 0, tempMaxPos = 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    				tempMaxPos = i+j;
    			}
    		}
    		
    		tempBoard = board;
    		if(tempMaxIdx == 0){
    			if(height[tempMaxPos+1] == tempMax - 1){
    				tempBoard[i][tempMax+2] = 1;
    				tempBoard[i+1][tempMax] = 1;
    				tempBoard[i+1][tempMax+1] = 1;
    				tempBoard[i+1][tempMax+2] = 1;
    			}
    			else {
    				tempBoard[i][tempMax+1] = 1;
    				tempBoard[i+1][tempMax-1] = 1;
    				tempBoard[i+1][tempMax] = 1;
    				tempBoard[i+1][tempMax+1] = 1;
    			}
    		}
    		else{
    			tempBoard[i][tempMax+3] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    			tempBoard[i+1][tempMax+3] = 1;
    		}

    		possibleMoves[i+25] = getScore(tempBoard);
    	}
    	
    	//best move
    	int max = 0;
    	int nextMove = 0;
    	for(int i = 0; i < possibleMoves.length; i++){
    		if(possibleMoves[i] > max){
    			max = possibleMoves[i];
    			nextMove = i;
    		}
    	}
    	
    	//find move
    	String ret = "";
    	if(nextMove < 8){
    		if(nextMove > 4){
    			ret = getRights(nextMove, 4);
    		}
    		else if(nextMove < 4){
    			ret = getLefts(nextMove, 4);
    		}
    		else
    			ret = "drop";
    	}
    	else if( nextMove >= 8 && nextMove < 17){
    		ret += "turnleft,";
    		if(nextMove > 7 && nextMove < 11){
    			ret += getLefts(nextMove, 11);
    		}
    		else if (nextMove > 11){
    			ret += getRights(nextMove, 11);
    		}
    		else
    			ret += "drop";
    	}
    	else if( nextMove >= 17 && nextMove < 25){
    		ret += "turnleft,turnleft";
    		if(nextMove > 16 && nextMove < 20){
    			ret += getLefts(nextMove, 20);
    		}
    		else if (nextMove > 20){
    			ret += getRights(nextMove, 20);
    		}
    		else
    			ret += "drop";
    	}
    	else{
    		ret += "turnleft,turnleft,turnleft";
    		if(nextMove > 24 && nextMove < 28){
    			ret += getLefts(nextMove, 28);
    		}
    		else if (nextMove > 28){
    			ret += getRights(nextMove, 28);
    		}
    		else
    			ret += "drop";
    	}
    	return ret;
    }
    
    public String SScore(){
    	int[] possibleMoves = new int[17];
    	int[][] tempBoard = new int[20][10];
    	
    	for(int i = 0; i < 8; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 3; j++){
    			if(height[i+j]>tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		if(tempMaxIdx==i+2){
    			tempBoard[i][tempMax] = 1;
    			tempBoard[i+1][tempMax] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+2][tempMax+1] = 1;
    		}
    		else{
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    			tempBoard[i+2][tempMax+2] = 1;
    		}
    		possibleMoves[i] = getScore(tempBoard);
    	}
    	for(int i = 0; i < 9; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>=tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		tempBoard = board;
    		if(tempMaxIdx == i){
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i+1][tempMax] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    		}
    		else{
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i][tempMax+3] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    		}
    		possibleMoves[i+8] = getScore(tempBoard);
    	}
    	
    	int max = 0;
    	int nextMove = 0;
    	for(int i = 0; i < possibleMoves.length; i++){
    		if(possibleMoves[i] > max){
    			max = possibleMoves[i];
    			nextMove = i;
    		}
    	}
    	
		String ret = "";
    	if(nextMove < 8){
    		if(nextMove > 4){
    			ret = getRights(nextMove, 4);
    		}
    		else if(nextMove < 4){
    			ret = getLefts(nextMove, 4);
    		}
    		else
    			ret = "drop";
    	}
    	else{
    		ret += "turnleft,";
    		if(nextMove > 7 && nextMove < 11){
    			ret += getLefts(nextMove, 11);
    		}
    		else if (nextMove > 11){
    			ret += getRights(nextMove, 11);
    		}
    		else
    			ret += "drop";
    	}
    	return ret;
    }

    public String ZScore(){
    	int[] possibleMoves = new int[17];
    	int[][] tempBoard = new int[20][10];
    	
    	for(int i = 0; i < 8; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 3; j++){
    			if(height[i+j]>=tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		
    		tempBoard = board;
    		if(tempMaxIdx==i){
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i+1][tempMax] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+2][tempMax] = 1;
    		}
    		else{
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    			tempBoard[i+2][tempMax+1] = 1;
    		}
    		possibleMoves[i] = getScore(tempBoard);
    	}
    	for(int i = 0; i < 9; i++){
    		int tempMax = 0, tempMaxIdx= 0;
    		for(int j = 0; j < 2; j++){
    			if(height[i+j]>=tempMax){
    				tempMax = height[i+j];
    				tempMaxIdx = j;
    			}
    		}
    		tempBoard = board;
    		if(tempMaxIdx == i+1){
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i][tempMax] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    			tempBoard[i+1][tempMax+1] = 1;
    		}
    		else{
    			tempBoard[i][tempMax+2] = 1;
    			tempBoard[i][tempMax+1] = 1;
    			tempBoard[i+1][tempMax+3] = 1;
    			tempBoard[i+1][tempMax+2] = 1;
    		}
    		possibleMoves[i+8] = getScore(tempBoard);
    	}
    	
    	int max = 0;
    	int nextMove = 0;
    	for(int i = 0; i < possibleMoves.length; i++){
    		if(possibleMoves[i] > max){
    			max = possibleMoves[i];
    			nextMove = i;
    		}
    	}
    	
		String ret = "";
    	if(nextMove < 8){
    		if(nextMove > 4){
    			ret = getRights(nextMove, 4);
    		}
    		else if(nextMove < 4){
    			ret = getLefts(nextMove, 4);
    		}
    		else
    			ret = "drop";
    	}
    	else{
    		ret += "turnleft,";
    		if(nextMove > 7 && nextMove < 11){
    			ret += getLefts(nextMove, 11);
    		}
    		else if (nextMove > 11){
    			ret += getRights(nextMove, 11);
    		}
    		else
    			ret += "drop";
    	}
    	return ret;
    }
    
    public String IScore(){
    	int[] possibleMoves = new int[17];
    	int[][] tempBoard = new int[20][10];
    	
    	for(int i = 0; i < 7; i++){
    		int tempMax = 0;
    		for(int j = 0; j < 4; j++){
    			if(height[i+j]>tempMax)
    				tempMax = height[i+j];
    		}
    		tempBoard = board;
    		for(int j = 0; j < 4; j++){
    			tempBoard[i+j][tempMax+1] = 1;
    		}
    		possibleMoves[i] = getScore(tempBoard);
    	}
    	
    	for(int i = 0; i< 10; i++){
    		if(height[i] > 17)
    			possibleMoves[i+7] = -100000;
    		else{
    			tempBoard = board;
    			tempBoard[i][height[i]+1] = 1;
    			tempBoard[i][height[i]+2] = 1;
    			tempBoard[i][height[i]+3] = 1;
    			tempBoard[i][height[i]+4] = 1;
    			possibleMoves[i] = getScore(tempBoard);
    		}
    	}

    	int max = 0;
    	int nextMove = 0;
    	for(int i = 0; i < possibleMoves.length; i++){
    		if(possibleMoves[i] > max){
    			max = possibleMoves[i];
    			nextMove = i;
    		}
    	}

		String ret = "";
    	if(nextMove < 7){
    		if(nextMove > 3){
    			ret = getRights(nextMove, 3);
    		}
    		else if(nextMove < 3){
    			ret = getLefts(nextMove, 3);
    		}
    		else
    			ret = "drop";
    	}
    	else{
    		ret += "turnleft,";
    		if(nextMove > 6 && nextMove < 10){
    			ret += getLefts(nextMove, 10);
    		}
    		else if (nextMove > 10){
    			ret += getRights(nextMove, 10);
    		}
    		else
    			ret += "drop";
    	}
    	return ret;
    }
    
    public void getNextMove(int pieceType){
    	switch(pieceType){
    	case 1:
    		//2 rotations, I
    		nextMove = IScore();
    		break;
    	case 2:
    		//4 rotations, J
    		nextMove = JScore();
    		break;
    	case 3:
    		//4 rotations, L
    		nextMove = LScore();
    		break;
    	case 4:
    		//0 rotations, O
    		nextMove = OScore();
    		break;
    	case 5:
    		//2 rotations, S
    		nextMove = SScore();
    		break;
    	case 6:
    		//4 rotations, T
    		break;
    	case 7:
    		//2 rotations, Z
    		nextMove = ZScore();
    		break;
    	}
    }
    
    public int getScore(int[][] tempBoard){
    	int[] tempHeight = new int[10];
    	for (int i = 0; i < 10; i++){
    		for(int j = 0; j < 20; j++){
    			if(tempBoard[i][j] == 0){
    				tempHeight[i] = j-1;
    				break;
    			}
    		}
    	}
    	
    	int[] slope = new int[9];
    	for(int i = 0; i < 9; i++){
    		slope[0] = Math.abs(tempHeight[i] - tempHeight[i+1]);
    	}
    	
    	//find holes
    	
    	//highest cell
    	int h1 = max(tempHeight);
    	
    	//number of full cells
    	int h2 = 0;
    	for(int i = 0; i < tempBoard.length;i++){
    		for(int j = 0; j < tempBoard[i].length;j++){
    			if(tempBoard[i][j]!= 0)
    				h2++;
    		}
    	}
    	
    	//highest slope
    	int h3 = max(slope);
    	
    	//sum of slopes
    	int h4 = 0;
    	for(int i = 0; i < slope.length; i++){
    		h4 += slope[i];
    	}
    	
    	//full cells wieghted by altitude
    	
    	return (int) ((h1*-10 + h2*-1 + h3*-5 + h4*-2));
    }
    
    public int max(int[] array){
    	int max = 0;
    	for(int i = 0; i < array.length; i++){
    		if(array[i] > max)
    			max = array[i];
    	}
    	return max;
    }

    public static void main(String[] args) {
        (new MyBot()).run();
    }
}