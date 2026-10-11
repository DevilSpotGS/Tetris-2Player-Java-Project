package mino2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block2 extends Rectangle{

    public int x,y;
    public static final int SIZE = 30 ; // ขนาดบล็อก = 30*30 px
    public Color c;

    // ใน Constructor รับค่าสีจาก C มา (บล็อกแต่ละตัวค่าสีต่างกัน)
    public Block2(Color c){
        this.c = c;
    }

    // วาดบล็อกด้วยสีของมัน
    public void draw(Graphics2D g2){
        int margin = 2;
        g2.setColor(c);
        g2.fillRect(x + margin, y + margin,SIZE-(margin*2),SIZE-(margin*2));
    }
    
}
