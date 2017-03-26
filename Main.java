import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends Canvas implements Runnable {

	public static int WIDTH = 600, HEIGHT = 600;

	JFrame frame;
	Thread mainThread;

	Game game;
	Input input;

	public Main() {
		frame = new JFrame("Snake");
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		game = new Game();
		input = new Input();
		addKeyListener(input);
		mainThread = new Thread(this);
	}

	public void start() {
		mainThread.start();
	}

	@Override
	public void run() {
		long time = 0;
		while (true) {
			long td = System.nanoTime() - time;
			if (td >= 1000000000 || !Input.anyKey) {
				time = System.nanoTime();
				update();
				render();
			}
		}
	}

	public void update() {
		game.update();
	}

	BufferStrategy bs = null;
	Graphics2D g = null;

	public void render() {
		if (bs == null) {
			createBufferStrategy(3);
			bs = getBufferStrategy();
		}

		g = (Graphics2D) bs.getDrawGraphics();
		WIDTH = getWidth();
		HEIGHT = getHeight();
		Draw.tiles(game.tiles, g);

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.frame.setLocation(200, 100);
		main.frame.setVisible(true);
		main.frame.add(main);
		main.frame.pack();

		main.start();
	}

}
