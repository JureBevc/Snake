

import java.util.LinkedList;
import java.util.Arrays;

public class Game {
	/*
	 * 0 -> Empty
	 *
	 * 1 -> Snake
	 *
	 * 2 -> Head
	 *
	 * 3 -> Prize
	 *
	 */
	public int tiles[][];

	public int w = 5, h = 5;

	Snake snake;

	public Game() {
		snake = new Snake(w * h, this);
		tiles = new int[h][w];
		createPrize();
	}

	void reset() {
		snake.reset();
		tiles = new int[h][w];
		if (!prizeExists()) {
			createPrize();
		}
	}

	public void update() {
		snake.move();
		updateTiles();

		if (!prizeExists()) {
			createPrize();
		}



		if (checkEnd() && !snake.gotBigger) {
			reset();
		}


	}

	void updateTiles() {

		if(snake.headx < 0 || snake.heady < 0 || snake.headx >= w || snake.heady >= h){
			reset();
			return;
		}

		for(int i = 0; i < w; i++){
			for(int j = 0; j < h; j++){
				// Remove snake
				if(tiles[j][i] == 1 || tiles[j][i] == 2){
					tiles[j][i] = 0;
				}
			}
		}

		if(tiles[snake.heady][snake.headx] == 3){
			snake.makeBigger();
			reset();
		}

		for(int i = 0; i < snake.tail.length; i++){
			if(snake.tail[i] != null){
				Point p = snake.tail[i];
				tiles[p.y][p.x] = 1;
			}
		}

		tiles[snake.heady][snake.headx] = 2;
	}

	boolean checkEnd() {
		for (int i = 0; i < snake.tail.length; i++) {
			if ((snake.tail[i] != null && snake.headx == snake.tail[i].x && snake.heady == snake.tail[i].y)
					|| snake.headx < 0 || snake.heady < 0 || snake.headx >= w || snake.heady >= h)
				return true;

		}
		return false;
	}

	boolean prizeExists() {

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (tiles[i][j] == 3)
					return true;
			}
		}

		return false;
	}

	boolean debug = false;
	void createPrize() {
		LinkedList<int[]> empty = new LinkedList<>();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (tiles[i][j] == 0)
					empty.add(new int[] { i, j });
			}
		}
		int[] r = empty.get((int) (Math.random() * empty.size()));
		//tiles[r[0]][r[1]] = 3;
		tiles[4][4] = 3; //TODO remove
	}

	public Point prizeLocation(){
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (tiles[i][j] == 3)
					return new Point(j, i);
			}
		}
		return null;
	}

}
