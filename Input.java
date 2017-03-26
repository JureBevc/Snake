import java.awt.*;
import java.awt.event.*;
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
