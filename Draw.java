
import java.awt.Color;
import java.awt.Graphics2D;

public class Draw {


	public static void tiles(int tiles[][], Graphics2D g) {
		int w = tiles[0].length;
		int h = tiles.length;
		int tw = Main.WIDTH / w;
		int th = Main.HEIGHT / h;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				if (tiles[j][i] == 0) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(i * tw, j * th, tw, th);
					g.setColor(Color.BLACK);
					g.drawRect(i * tw, j * th, tw, th);
				}
				if (tiles[j][i] == 1) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(i * tw, j * th, tw, th);
				}
				if (tiles[j][i] == 2) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(i * tw, j * th, tw, th);
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(i * tw + 10, j * th + 10, tw - 20, th - 20);
				}
				if (tiles[j][i] == 3) {
					g.setColor(Color.ORANGE);
					g.fillRect(i * tw, j * th, tw, th);
				}
			}
		}
	}

}
