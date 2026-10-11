package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    //ขนาดหน้าจอ
    public static final int WIDTH = 1500;  
    public static final int HEIGHT = 720;
    

    //player1 player2
    private PlayManager pm;
    private PlayManager2 pm2;
    public static Sound music = new Sound();
    public static Sound se = new Sound();
    

    Thread gameThread; // ใช้เพื่อสร้าง loop ของเกม

    // FPS
    final int FPS = 60;

    public GamePanel(){
        // panel setting
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);
        this.addKeyListener(new KeyHandler()); // เพิ่ม keylistener
        this.addKeyListener(new KeyHandler2()); // เพิ่ม keylistenerของplayer2
        this.setFocusable(true);      // Focus ค่า input keyboard

        // สร้าง PlayManager สำหรับผู้เล่น
        pm = new PlayManager(); // ผู้เล่นคนแรก
        pm2 = new PlayManager2(); // ผู้เล่นคนที่สอง
    }

    public void LaunchGame(){
        gameThread = new Thread(this); // เริ่มเกม
        gameThread.start(); // เรียก method run มาทำงาน
        music.play(5, true);
        music.loop();
        //music.play(0,true);
        //music.loop();
    }


    @Override
    public void run() {
        /* 
        while (gameThread != null){
            long currentTime = System.nanoTime(); // เช็ค นาโนวินาที ไว้ทำ 60 FPS
            System.out.println("Time : "+currentTime);
        }  */

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                update(); //อัพเดทข้อมูลเช่น ตำแหน่งตัวละคร
                repaint(); //วาดใหม่ด้วยข้อมูลที่อัพเดทแล้ว
                delta--;
            }
        }
    }

    public void update(){ // อัพเดทตำแหน่ง x,y หรือ score
        if(pm.gameOver == false){
            pm.update();
        }
        if(pm2.gameOver == false){
            pm2.update();
        }
        
        
    }
    
  

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g ;     // เปลี่ยน Graphic g ให้เป็น Graphic2D
        pm.draw(g2);                        //เรียก method จาก PlayManager class
        Graphics2D g3 = (Graphics2D)g ;     // เปลี่ยน Graphic g ให้เป็น Graphic2D
        pm2.draw(g3);

        
    
    }
    
    
        
 }
    
    
