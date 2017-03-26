import java.util.*;

public class Snake {

	public int headx, heady;

	public Point tail[];
	Game game;

	int maxSize;

	State last = null;
	int lastID = 0;

  boolean gotBigger = false;
	public Snake(int maxSize, Game game) {
		headx = 2;
		heady = 2;
		this.maxSize = maxSize;
		tail = new Point[maxSize];
		this.game = game;
	}

	public void reset(){
		headx = 2;
		heady = 2;
		tail = new Point[maxSize];
	}

	/*
		0 -> up
		1 -> right
		2 -> down
		3 -> left
	*/
  int dir = 1;

	public void move() {
		double reward = 0;
		//System.out.println("States: " + states.size());
		moveTail();
		int newDir = getNewDir();
		if(Math.abs(newDir - dir) == 2){
			//reward = invalidMoveReward;
		}else{
			dir = newDir;
		}

		// Move head
		switch(dir){
			case 0:
				heady--;
			break;
			case 1:
				headx++;
			break;
			case 2:
				heady++;
			break;
			case 3:
				headx--;
			break;
		}
		// Assign new q value
		if(last != null)
			newQ(reward);
	}

	void moveTail(){
		if(!gotBigger)
			for(int i = tail.length - 1; i >= 0; i--){
				if(tail[i] != null){
				 if(i == 0){
					tail[i].x = headx;
					tail[i].y = heady;
				 }else{
					tail[i].x = tail[i - 1].x;
					tail[i].y = tail[i - 1].y;
				 }
			 }
			}
			gotBigger = false;
	}

	public void makeBigger(){
		gotBigger = true;
		for(int i = tail.length - 2; i >= 0; i--){
			if(tail[i] != null){
			 tail[i + 1] = new Point(tail[i].x, tail[i].y);
		  }
		}
		tail[0] = new Point(headx, heady);
	}

	// Q-learning below

	LinkedList<State> states = new LinkedList<>();
	int getNewDir(){
		int newDir = (int)(Math.random() * 4);

		double reward = moveReward;

		//Get current state
		boolean walls[] = {
			(heady == 0 || game.tiles[heady - 1][headx] == 1),
			(headx == game.w - 1 || game.tiles[heady][headx + 1] == 1),
			(heady == game.h - 1 || game.tiles[heady + 1][headx] == 1),
			(headx == 0 || game.tiles[heady][headx - 1] == 1)
		};
		Point prize = game.prizeLocation();

		int prizePos = 0;
		if(prize == null){
			System.out.println("No prize found!");
			return dir;
		}else{
			int dx = prize.x - headx;
			int dy = prize.y - heady;
			if(Math.abs(dx) > Math.abs(dy)){
				prizePos = (dx > 0)? 1 : 3;
			}else{
				prizePos = (dy > 0)? 2 : 0;
			}
		}

		State current = new State(prize, new Point(headx, heady),walls);
		boolean newState = true;
		for(int i = 0; i < states.size(); i++){
			if(states.get(i).equals(current)){
				current = states.get(i);
				newState = false;
				break;
			}
		}
		if(newState){
			states.add(current);
		}
		last = current;

		// Get max q value (id of max q is new direction)
		newDir = current.getMax();
		lastID = newDir;

		return newDir;
	}

	double alpha = 0.8, // Learning rate
	 moveReward = -0.2,
	 invalidMoveReward = -200,
	 deathReward = -100,
	 prizeReward = 100,
	  gamma = 0.5;

	void newQ(double reward){
		//Get current state
		State current = null;
		Point prize = game.prizeLocation();
		if(reward == 0 && headx < game.w && heady < game.h && headx >= 0 && heady >= 0){
			boolean walls[] = {
				(heady <= 0 || game.tiles[heady - 1][headx] == 1),
				(headx >= game.w - 1 || game.tiles[heady][headx + 1] == 1),
				(heady >= game.h - 1 || game.tiles[heady + 1][headx] == 1),
				(headx <= 0 || game.tiles[heady][headx - 1] == 1)
			};

			int prizePos = 0;
			if(prize == null){
				System.out.println("No prize found!");
			}else{
				int dx = prize.x - headx;
				int dy = prize.y - heady;
				if(Math.abs(dx) > Math.abs(dy)){
					prizePos = (dx > 0)? 1 : 3;
				}else{
					prizePos = (dy > 0)? 2 : 0;
				}
			}

			//System.out.println("Prize pos: " + prizePos + " " + Arrays.toString(walls));
			current = new State(prize,new Point(headx, heady), walls);
			boolean newState = true;
			for(int i = 0; i < states.size(); i++){
				if(states.get(i).equals(current)){
					current = states.get(i);
					newState = false;
					break;
				}
			}
		}else if(reward == 0){
			//reward = deathReward;
		}
		if(reward == 0 && headx == prize.x && heady == prize.y){
			reward = prizeReward;
		//		System.out.println("Got prize!");
		}
		if(reward == 0){
			reward = moveReward;
		}
		//if(reward > 0)
		//	System.out.println("Good reward!");
		System.out.println(reward);
		if(current != null){
			//System.out.println(alpha * (reward + gamma * current.qValues[current.getMax()] - last.qValues[lastID]));
			last.qValues[lastID] += alpha * (reward + gamma * current.qValues[current.getMax()] - last.qValues[lastID]);
		}else{
			last.qValues[lastID] += alpha * (reward - last.qValues[lastID]);
		}
	}
}
