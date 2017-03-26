import java.util.*;

public class Snake {

	public int headx, heady;

	public Point tail[];
	Game game;

	int maxSize;

	int lastID = -1;
	int lastQID = 0;

	boolean gotBigger = false;

	public Snake(int maxSize, Game game) {
		headx = 2;
		heady = 2;
		this.maxSize = maxSize;
		tail = new Point[maxSize];
		this.game = game;
	}

	public void reset() {
		headx = 2;
		heady = 2;
		tail = new Point[maxSize];
		dir = 1;
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

		moveTail();
		int newDir = getNewDir();
		if(states.size() % 100 == 0)
			System.out.println("States " + states.size());
		
		if (Math.abs(newDir - dir) == 2) {
			reward = invalidMoveReward;
		} else {
			dir = newDir;
		}

		// Move head
		switch (dir) {
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
		if (lastID != -1)
			newQ(reward);
	}

	void moveTail() {
		if (!gotBigger)
			for (int i = tail.length - 1; i >= 0; i--) {
				if (tail[i] != null) {
					if (i == 0) {
						tail[i].x = headx;
						tail[i].y = heady;
					} else {
						tail[i].x = tail[i - 1].x;
						tail[i].y = tail[i - 1].y;
					}
				}
			}
		gotBigger = false;
	}

	public void makeBigger() {
		gotBigger = true;
		for (int i = tail.length - 2; i >= 0; i--) {
			if (tail[i] != null) {
				tail[i + 1] = new Point(tail[i].x, tail[i].y);
			}
		}
		tail[0] = new Point(headx, heady);
	}

	LinkedList<State> states = new LinkedList<>();

	int getNewDir() {
		int newDir = (int) (Math.random() * 4);

		//Get current state
		boolean walls[] = { (heady == 0 || game.tiles[heady - 1][headx] == 1),
				(headx == game.w - 1 || game.tiles[heady][headx + 1] == 1),
				(heady == game.h - 1 || game.tiles[heady + 1][headx] == 1),
				(headx == 0 || game.tiles[heady][headx - 1] == 1) };
		Point prize = game.prizeLocation();

		if (prize == null) {
			System.out.println("No prize found!");
			return dir;
		}

		State current = new State(prize, new Point(headx, heady), walls);
		boolean newState = true;
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).equals(current)) {
				current = states.get(i);
				newState = false;
				break;
			}
		}
		if (newState) {
			//System.out.println("New state!   " + current.toString());
			states.add(current);
		}
		lastID = states.indexOf(current);

		// Get max q value (id of max q is new direction)
		newDir = current.getMax();
		lastQID = newDir;

		return newDir;
	}

	double alpha = 0.8, // Learning rate
			moveReward = -0.2, invalidMoveReward = -200, deathReward = -100, prizeReward = 100, gamma = 0.5;

	void newQ(double reward) {
		//Get current state
		State current = null;
		Point prize = game.prizeLocation();
		if (reward == 0 && headx < game.w && heady < game.h && headx >= 0 && heady >= 0) {
			boolean walls[] = { (heady <= 0 || game.tiles[heady - 1][headx] == 1),
					(headx >= game.w - 1 || game.tiles[heady][headx + 1] == 1),
					(heady >= game.h - 1 || game.tiles[heady + 1][headx] == 1),
					(headx <= 0 || game.tiles[heady][headx - 1] == 1) };

			if (prize == null) {
				System.out.println("No prize found!");
			}

			//System.out.println("Prize pos: " + prizePos + " " + Arrays.toString(walls));
			current = new State(prize, new Point(headx, heady), walls);
			boolean newState = true;
			for (int i = 0; i < states.size(); i++) {
				if (states.get(i).equals(current)) {
					current = states.get(i);
					newState = false;
					break;
				}
			}
			if (newState) {
				states.add(current);
			}
		} else if (reward == 0) {
			reward = deathReward;
		}
		if (reward == 0) {
			reward = moveReward;
		}
		//System.out.println(states.get(lastID).qValues[lastQID]);
		if (current != null) {
			states.get(lastID).qValues[lastQID] += alpha * (reward + gamma * current.qValues[current.getMax()] - states.get(lastID).qValues[lastQID]);
		} else {
			states.get(lastID).qValues[lastQID] += alpha * (reward - states.get(lastID).qValues[lastQID]);
		}
	}
}
