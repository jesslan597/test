import java.awt.*;
public class Platform extends GameObject{
    
    public Platform() {
        super(0.0, 0.0, (Math.random() * GameLogic.COURT_WIDTH), 
                (Math.random() * GameLogic.COURT_HEIGHT), 20,
                5, GameLogic.COURT_WIDTH, GameLogic.COURT_HEIGHT);
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int) x, (int) y, width, height);
    }
    
    @Override
    public boolean intersectBroken(GameObject obj) {
        return false;
    }
    
    @Override
    public void move() {
        y += vy;
    }

}
