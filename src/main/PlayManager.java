package main;
// หน้า UI และ Tetrominoes (รวมถึง ลบบรรทัด เพิ่มคะแนน)

import mino.*; // import โฟลเดอร์ mino เข้ามา
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import java.util.ArrayList;

public class PlayManager{
    //Main Play Area (พื้นที่เล่นหลัก) 
    final int WIDTH = 300; // ความยาวแถว 10 * ขนาดบล็อก 30 px
    final int HEIGHT = 600; // ความสูงแถว 20 * ขนาดบล็อก 30 px
    public static int left_x;
    public static int right_x;      
    public static int top_y;
    public static int bottom_y;

    // Mino ตัวปัจจุบัน
    Mino currentMino;
    final int Mino_START_X ;
    final int Mino_START_Y ;
    
    // Mino ตัวถัดไป
    Mino nextMino;
    final int NEXTMINO_X; 
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>(); //สร้างอาร์เรย์เพื่อเก็บ mino ที่ไม่ได้ใช้งานแล้วมาอยู่ใน staticBlocks

    // Score
    int level = 1 ;
    int lines;
    int score; 

    // อื่นๆ
    public static int dropInterval = 60; // mino จะทิ้งตัวเองในทุกๆ 60 เฟรม
    boolean gameOver;

    //effect
    boolean effectCountOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    public PlayManager(){ 
        
      
        // Main Play Area Frame (เส้นขอบ)
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2) -400; // 1280/2 - 300/2 = 490
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;


        //ตำแหน่งเริ่มต้นบล็อกที่จะหล่นลงมา (มุมบนตรงกลาง)
        Mino_START_X = left_x + (WIDTH/2) - Block.SIZE ;
        Mino_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 76;
        NEXTMINO_Y = top_y + 500;

        //จุดเริ่มต้นที่บล็อกจะหล่น
        currentMino = pickMino();                          //หยิบ mino แบบสุ่มมา
        currentMino.setXY(Mino_START_X,Mino_START_Y);
        
        nextMino = pickMino();                             //หยิบ mino ตัวถัดไปแบบสุ่มมา
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
    

    //Random Mino
    private Mino pickMino(){
        // สร้างระบบสุ่มบล็อก
        Mino mino = null;
        int i = new Random().nextInt(7);
        switch(i){
            case 0 : mino = new Mino_L1();break;
            case 1 : mino = new Mino_L2();break;
            case 2 : mino = new Mino_Square();break;
            case 3 : mino = new Mino_Bar();break;
            case 4 : mino = new Mino_T();break;
            case 5 : mino = new Mino_Z1();break;
            case 6 : mino = new Mino_Z2();break;
        }
        return mino;
    }

public void update(){ 
        
        //เช็ค ถ้า currentMino ยังใช้งานอยู่
        if(currentMino.active == false){
            // ถ้าไม่ได้ใช้งานแล้ว นำไปเก็บไว้ใน staticBlocks ของอาร์เรย์
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            if (currentMino.b[0].x == Mino_START_X && currentMino.b[0].y == Mino_START_Y) { 
                gameOver = true; 
                GamePanel.se.play(4, false);
        }

            

            currentMino.deactivating = false;

            //เปลี่ยน currentmino เป็นตัวถัดไป
            currentMino = nextMino;                            //หยิบ mino ถัดไปที่อยู่ในกล่องมาอยู่ในตำแหน่งเริ่มต้น
            currentMino.setXY(Mino_START_X,Mino_START_Y);
            nextMino = pickMino();                             //หยิบ mino ตัวถัดไปแบบสุ่มมา อยู่ใน Next ตัวถัดไป
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            //เมื่อ mino อยู่ในสถานะ inactive , ตรวจสอบว่า Line นั้น สามารถลบทิ้งได้หรือไม้
            checkDelete();
            //checkGameOver();
        
          
            }else{
            currentMino.update();    
        }
        
        
    
    
    }


    // ลบ line ทิ้ง เมื่อ block เต็มแถว
    private void checkDelete(){
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while(x < right_x && y < bottom_y){
            for(int i = 0 ; i < staticBlocks.size() ; i++){
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    //เพิ่ม count ถ้ามี static block อยู่
                    blockCount++;
                }
            }
            
            x += Block.SIZE;
            
            if (x == right_x){
                    

                    // ถ้า blockCount ถึง 10 หมายถึง เส้น y ถูกเติมด้วย blocks เต็มแถว = ลบได้ 
                    if(blockCount == 10){
                        
                        //effect ลบบล็อก
                        effectCountOn = true;
                        effectY.add(y);

                        for(int i = staticBlocks.size()-1; i > -1 ; i-- ){
                            // remove all blocks in the current y line
                            if(staticBlocks.get(i).y == y){
                                staticBlocks.remove(i);
                            }
                        }

                        lineCount++; //เมื่อลบ line จะเพิ่ม lineCount
                        lines++;

                        // Drop speed(ความเร็วการทิ้งบล็อก)
                        // ถ้าลบเส้น ถึงค่าที่กำหนด จะเพิ่ม Drop speed 
                        // 1 คือเร็วสุด
                        if(lines % 5 == 0 && dropInterval > 1){   //ทุกๆ 5 Line ที่ลบไป เพิ่ม level 1 ระดับ
                            level++;
                            if(dropInterval > 10){
                                dropInterval -=10;
                            } else {
                                dropInterval -= 1;
                            }
                        }

                        // เมื่อลบบล็อกออกไปแล้ว บล็อกด้านบน ต้องขยับลงมา
                        for(int i = 0 ; i < staticBlocks.size() ; i++){
                            //ถ้าบล็อกอยู่ด้านบนแกน y ปัจจุบัน , ขยับลงมาบล็อกนึง
                            if(staticBlocks.get(i).y < y){
                                staticBlocks.get(i).y += Block.SIZE;
                            }
                            
                        }
                    }

                blockCount = 0 ;
                x = left_x;
                y += Block.SIZE;
            }
        }
        
        // เพิ่ม score
        if(lineCount > 0){
            GamePanel.se.play(0, false);
            int singleLineScore = 10 * level;
  
            score += singleLineScore * lineCount;
        }
        
    }
    public int getScore() {
        return score;
    }

    public void draw(Graphics2D g2){
        // วาด Frame เส้นขอบหลัก
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f)); //ตั้งค่าความหนาของเส้น 4 px สำหรับความกว้างของ Frame
        g2.drawRect(left_x-4 , top_y-4 , WIDTH+8 , HEIGHT+8);

        // วาด Frame (ห้องรอและบอกตัวถัดไป)
        int x = right_x + 4;
        int y = bottom_y - 196 ;
        g2.drawRect(x,y,200,200);
        //จัดฟอนต์ และ เขียน next
        g2.setFont(new Font("Arial",Font.PLAIN,30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT",x+60, y+60);
        g2.drawString("Player 1", WIDTH, HEIGHT+100);

        // วาด Scoreboard และ level
        g2.drawRect(x,top_y-4,250,300);
        x += 30;
        y = top_y + 90;
        g2.drawString("LEVEL: "+level,x,y) ; y+=70;
        g2.drawString("LINES: "+lines,x,y) ; y+=70;
        g2.drawString("SCORE: "+score,x,y) ;

        // วาด currentMino 
        if(currentMino !=null){
            currentMino.draw(g2);
        }

        // วาด nextMino
        nextMino.draw(g2);
        


        // วาด staticBlocks
        for(int i=0 ; i < staticBlocks.size() ; i++){
            staticBlocks.get(i).draw(g2);
        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(y));    
        }        

        //effect ลบบล็อก
        if(effectCountOn){
            effectCounter++;

            g2.setColor(Color.red);
            for(int i = 0;i < effectY.size();i++){
                g2.fillRect(left_x, effectY.get(i),WIDTH, Block.SIZE);
            }
            if(effectCounter == 10){
                effectCountOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }
        //game over

        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(50f));    
        if(gameOver){
        x = left_x + 25;
        y = top_y + 320 ;
        g2.drawString("Game Over", x, y);
        }
    
        
    }
    
}
