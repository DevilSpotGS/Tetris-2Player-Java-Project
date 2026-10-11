package main;
// หน้า UI และ Tetrominoes (รวมถึง ลบบรรทัด เพิ่มคะแนน)

import mino2.*; // import โฟลเดอร์ mino เข้ามา
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import java.util.ArrayList;

public class PlayManager2{
    //Main Play Area (พื้นที่เล่นหลัก)
    final int WIDTH = 300; // ความยาวแถว 10 * ขนาดบล็อก 30 px
    final int HEIGHT = 600; // ความสูงแถว 20 * ขนาดบล็อก 30 px
    public static int left_x;
    public static int right_x;      
    public static int top_y;
    public static int bottom_y;

    // Mino ตัวปัจจุบัน
    Mino2 currentMino;
    final int Mino_START_X ;
    final int Mino_START_Y ;
    
    // Mino ตัวถัดไป 
    Mino2 nextMino;
    final int NEXTMINO_X; 
    final int NEXTMINO_Y;
    public static ArrayList<Block2> staticBlocks = new ArrayList<>(); //สร้างอาร์เรย์เพื่อเก็บ mino ที่ไม่ได้ใช้งานแล้วมาอยู่ใน staticBlocks

     // Score
     int level = 1 ;
     int lines;
     int score2; 
     public int getScore2() {
        return score2;
    }

    boolean gameOver;

     
     

     public static int dropInterval = 60; // mino จะทิ้งตัวเองในทุกๆ 60 เฟรม
     //effect
    boolean effectCountOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    public PlayManager2(){
        
        // Main Play Area Frame (เส้นขอบ)
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2) + 220; // 1280/2 - 300/2 = 490
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

       //ตำแหน่งเริ่มต้นบล็อกที่จะหล่นลงมา (มุมบนตรงกลาง)
       Mino_START_X = left_x + (WIDTH/2) - Block2.SIZE ;
       Mino_START_Y = top_y + Block2.SIZE;

       NEXTMINO_X = right_x + 76;
       NEXTMINO_Y = top_y + 500;

       //จุดเริ่มต้นที่บล็อกจะหล่น
       currentMino = pickMino();                          //หยิบ mino แบบสุ่มมา
       currentMino.setXY(Mino_START_X,Mino_START_Y);   
       nextMino = pickMino();                             //หยิบ mino ตัวถัดไปแบบสุ่มมา
       nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);                     //หยิบ mino แบบสุ่มมา
        
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

            //เปลี่ยน currentmino เป็นตัวถัดไป
            currentMino = nextMino;                            //หยิบ mino ถัดไปที่อยู่ในกล่องมาอยู่ในตำแหน่งเริ่มต้น
            currentMino.setXY(Mino_START_X,Mino_START_Y);
            nextMino = pickMino();      // สุ่มตัวบล็อกถัดไป                        
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            //เมื่อ mino อยู่ในสถานะ inactive , ตรวจสอบว่า Line นั้น สามารถลบทิ้งได้หรือไม้
            checkDelete();
            
            }else{
            currentMino.update();
        }
        
}
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
        x += Block2.SIZE;
            
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
                                staticBlocks.get(i).y += Block2.SIZE;
                            }
                        }
                          
                    
                    }
                blockCount = 0 ;
                x = left_x;
                y += Block2.SIZE;
                }
            
    }
    // เพิ่ม score
    if(lineCount > 0){
        GamePanel.se.play(1, false);
        int singleLineScore = 10 * level;
        score2 += singleLineScore * lineCount;
    }
}
public int getScore() {
    return score2;
}
private Mino2 pickMino() {
        // สร้างระบบสุ่มบล็อก
        Mino2 mino = null;
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
    
public void draw(Graphics2D g3){
    // วาด Frame เส้นขอบหลัก
    g3.setColor(Color.WHITE);
    g3.setStroke(new BasicStroke(4f)); //ตั้งค่าความหนาของเส้น 4 px สำหรับความกว้างของ Frame
    g3.drawRect(left_x-4 , top_y-4 , WIDTH+8 , HEIGHT+8);

     // วาด Frame (ห้องรอและบอกตัวถัดไป)
     int x = right_x + 4;
     int y = bottom_y - 196 ;
     g3.drawRect(x,y,200,200);
     //จัดฟอนต์ และ เขียน next
     g3.setFont(new Font("Arial",Font.PLAIN,30));
     g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
     g3.drawString("NEXT",x+60, y+60);
     g3.drawString("Player 2", WIDTH+620, HEIGHT+100);
    // วาด Scoreboard และ level
    g3.drawRect(x,top_y-4,250,300);
    x += 30;
    y = top_y + 90;
    g3.drawString("LEVEL: "+level,x,y) ; y+=70;
    g3.drawString("LINES: "+lines,x,y) ; y+=70;
    g3.drawString("SCORE: "+score2,x,y) ;
     
     if(currentMino !=null){
        currentMino.draw(g3);
    }
    
     // วาด nextMino
     nextMino.draw(g3);
     // วาด static blocks
     for(int i=0 ; i < staticBlocks.size() ; i++){
        staticBlocks.get(i).draw(g3);
       }
       //effect ลบบล็อก
       if(effectCountOn){
        effectCounter++;

        g3.setColor(Color.cyan);
        for(int i = 0;i < effectY.size();i++){
            g3.fillRect(left_x, effectY.get(i),WIDTH, Block2.SIZE);
        }
        if(effectCounter == 10){
            effectCountOn = false;
            effectCounter = 0;
            effectY.clear();
        }
    }
    g3.setColor(Color.YELLOW);
    g3.setFont(g3.getFont().deriveFont(50f));    
    if(gameOver){
        x = left_x + 25;
        y = top_y + 320 ;
        g3.drawString("Game Over", x, y);
    }  
    }
 
// ฟังก์ชันเพื่อแสดงคะแนนเมื่อเกมโอเวอร

}
