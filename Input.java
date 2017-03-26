import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Input implements KeyListener{


  public static boolean anyKey = false;
  public void keyTyped(KeyEvent e) {

  }


  public void keyPressed(KeyEvent e) {
    anyKey = true;
  }


  public void keyReleased(KeyEvent e) {
    anyKey = false;
  }

}
