
import java.awt.Graphics;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameObject implements Comparable<GameObject> {

    /** Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object.
     * This position should always be within bounds.
     *  0 <= x <= max_x 
     *  0 <= y <= max_y 
     */
    public double x; 
    public double y;

    /** Size of object, in pixels */
    public int width;
    public int height;
    
    /** Velocity: number of pixels to move every time move() is called */
    public double vx;
    public double vy;

    /** Upper bounds of the area in which the object can be positioned.  
     *    Maximum permissible x, y positions for the upper-left 
     *    hand corner of the object
     */
    public double max_x;
    public double max_y;
    
    public static int MAX_UID = 1;
    
    public int uid;

    /**
     * Constructor
     */
    public GameObject(double vx, double vy, double x, double y, 
        int width, int height, int court_width, int court_height){
        this.vx = vx;
        this.vy = vy;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // take the width and height into account when setting the 
        // bounds for the upper left corner of the object.
        this.max_x = court_width - width;
        this.max_y = court_height - height;
        
        this.uid = MAX_UID;
        MAX_UID++;
        
        clip();

    }


    /**
     * Moves the object by its velocity.  Ensures that the object does
     * not go outside its bounds by clipping.
     */
    public void move(){
        x += vx;
        y += vy;
    }

    /**
     * Prevents the object from going outside of the bounds of the area 
     * designated for the object. (i.e. Object cannot go outside of the 
     * active area the user defines for it).
     */ 
    public void clip(){
        if (x < 0) x = 0;
        else if (x > max_x) x = max_x;

       /* if (y < 0) y = 0;
        else if (y > max_y) y = max_y;*/
    }

    /**
     * Determine whether this game object will intersect another in the
     * next time step, assuming that both objects continue with their 
     * current velocity.
     * 
     * Intersection is determined by comparing bounding boxes. If the 
     * bounding boxes (for the next time step) overlap, then an 
     * intersection is considered to occur.
     * 
     * @param obj : other object
     * @return whether an intersection will occur.
     */
    public boolean willIntersect(GameObject obj){
        double next_x = x + vx;
        double next_y = y + vy;
        double next_obj_x = obj.x + obj.vx;
        double next_obj_y = obj.y + obj.vy;
        return (next_x + width >= next_obj_x
                && next_y + height >= next_obj_y
                && next_obj_x + obj.width >= next_x 
                && next_y > y);
    }

    public boolean intersect(GameObject obj) {
        return (x + width >= obj.x
                && x <= obj.x + obj.width
                && y + height >= obj.y
                && y <= obj.y + obj.height
                && y + vy > y);
    }
    
    public boolean isBroken() {
        return false;
    }
    
    public boolean intersectBroken(GameObject obj) {
        return false;
    }

    /** Determine whether the game object will hit another 
     *  object in the next time step. If so, return the direction
     *  of the other object in relation to this game object.
     *  
     * @return if collision occurred
     */
    /*
    public boolean collide(GameObject other) {
        return false;
    }*/
    
    /**
     * Default draw method that provides how the object should be drawn 
     * in the GUI. This method does not draw anything. Subclass should 
     * override this method based on how their object should appear.
     * 
     * @param g 
     *  The <code>Graphics</code> context used for drawing the object.
     *  Remember graphics contexts that we used in OCaml, it gives the 
     *  context in which the object should be drawn (a canvas, a frame, 
     *  etc.)
     */
    public void draw(Graphics g) {
    }
    
    @Override
    public int compareTo(GameObject other) {
        return ((Integer) uid).compareTo(other.uid);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        long temp;
        temp = Double.doubleToLongBits(max_x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max_y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vx);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vy);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + width;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return false;
    }
   
    
    
}

