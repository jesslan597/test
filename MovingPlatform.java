import java.awt.*;
public class MovingPlatform extends Platform {

    public MovingPlatform() {
        super();
        this.vx = 2;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, width, height);
    }
    
    @Override
    public boolean intersectBroken(GameObject obj) {
        return false;
    }
    
    @Override
    public void move() {
        y += vy;
        x += vx;
        if (x >= GameLogic.COURT_WIDTH) {
            vx = -vx;
        }
        if (x <= 0) {
            vx = -vx;
        }
    }
}
