import org.junit.Test;
import static org.junit.Assert.*;

public class DoodleTests {
    
    @Test
    public void testMovePlatform() {
        Platform p = new Platform();
        p.move();
        assertEquals((int) p.vy, 0);
    }
    
    @Test
    public void testMoveMovingPlatform() {
        MovingPlatform p = new MovingPlatform();
        p.move();
        assertEquals((int) p.vy, 0);
        assertEquals((int) p.vx, 2);
    }
    
    @Test
    public void testCollidePlatform() {
        Platform p = new Platform();
        Doodle d = new Doodle();
        d.x = p.x;
        d.y = p.y;
        d.vy = 5;
        assertTrue(d.collide(p));
    }
    
    @Test
    public void testCollidePlatformNotUp() {
        Platform p = new Platform();
        Doodle d = new Doodle();
        d.x = p.x;
        d.y = p.y;
        d.vy = -5;
        assertFalse(d.collide(p));
    }
    
    @Test
    public void testCollidePlatformDoodleGoingUp() {
        Platform p = new Platform();
        Doodle d = new Doodle();
        d.x = p.x;
        d.y = p.y;
        assertFalse(d.collide(p));
    }
    
    @Test
    public void testCollideMovingPlatform() {
        MovingPlatform p = new MovingPlatform();
        Doodle d = new Doodle();
        d.x = p.x;
        d.y = p.y;
        d.vy = 5;
        d.collide(p);
        assertEquals((int) d.vy, -4);
    }
    
    @Test
    public void testCollideBrokenPlatform() {
        BrokenPlatform p = new BrokenPlatform();
        Doodle d = new Doodle();
        d.x = p.x;
        d.y = p.y;
        assertFalse(d.collide(p));
    }
    
    @Test
    public void testIntersectBrokenPlatform() {
        BrokenPlatform p = new BrokenPlatform();
        Platform p1 = new Platform();
        Doodle d = new Doodle();
        d.x = p.x;
        d.y = p.y;
        d.vy = 5;
        p1.x = p.x;
        p1.y = p.y;
        assertTrue(p.intersectBroken(d));
        assertFalse(p1.intersectBroken(d));
    }
    
    
    
}
