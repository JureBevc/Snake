import java.util.*;

public class State{

  /*
    0 -> up
    1 -> right
    2 -> down
    3 -> left
  */
  Point pointPos;
  Point headPos;

  boolean walls[] = new boolean[4];

  public double qValues[] = new double[4];

  public State(Point pointPos, Point headPos, boolean walls[]){
      this.pointPos = pointPos;
      this.walls = walls;
      this.headPos = headPos;
  }

  // Random max qValue
  public int getMax(){
    LinkedList<Integer> all = new LinkedList<>();
    all.add(0);
    double max = qValues[0];
    for(int i = 1; i < 4; i++){
      if(qValues[i] > max){
        all.clear();
        all.add(i);
        max = qValues[i];
      }else if(qValues[i] == max){
        all.add(i);
      }
    }
    return all.get((int)(all.size() * Math.random()));
  }

  public boolean equals(State state){
    if(pointPos != state.pointPos || headPos != state.headPos)
      return false;
    for(int i = 0; i < 4; i++){
      if(walls[i] != state.walls[i])
        return false;
    }
    return true;
  }

}
