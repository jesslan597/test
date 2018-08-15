import java.awt.*;
public class BrokenPlatform extends Platform {

    public BrokenPlatform() {
        super();
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect((int) x, (int) y, width, height);
    }
    
    @Override
    public boolean intersect(GameObject obj) {
        return false;
    }
    
    public boolean isBroken() {
        return true;
    }
    
    @Override
    public boolean intersectBroken(GameObject doodle) {
        return (doodle.x + doodle.width >= x
                && doodle.x <= x + width
                && doodle.y + doodle.height >= y
                && doodle.y <= y + height
                && doodle.y + doodle.vy > doodle.y);
    }
    
    
   
}
