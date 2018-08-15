import java.awt.*;
public class Doodle extends GameObject{
    
    private int score;
    public final double GRAVITY = 0.2;
    
    public Doodle() {
        super(0, -5, GameLogic.COURT_WIDTH / 2, GameLogic.COURT_HEIGHT / 2, 
                5, 5, GameLogic.COURT_WIDTH, GameLogic.COURT_HEIGHT);
        score = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.fillOval((int) x, (int) y, width, height);
    }
    
    // override clip method so doodle can roll over from side to side
    @Override
    public void clip() {
        if (x < 0) x = max_x + x;
        else if (x > max_x) x = x - max_x;
    }
    
    //@Override
    public boolean collide(GameObject other) {
        if (this.intersect(other)) {
            score += 10;
            Game.setScoreText(score);
            if (other.intersectBroken(this)) {
                return false;
            } else {
                vy = -4;
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public void move(){
        x += vx;
        y += vy;
        vy += GRAVITY;
        clip();
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int x) {
        score = x;
    }
}
