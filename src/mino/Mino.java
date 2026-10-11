package mino;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyHandler;
import main.PlayManager;

// เป็น super class ของ terminoes ทั้งหมด หรือก็คือ รูปทรงต่างๆต้อง extend จาก class นี้
public class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0; // ค่าตัวเลขของบล็อกหล่นลงมา
    public int direction = 1; //การหมุนมีทั้งหมด 4 ทิศ (1/2/3/4)

    boolean leftCollision,rightCollision,bottomCollision; // ไม่ให้ block ออกเส้นขอบ or การชนกัน
    public boolean active = true; //ทำให้บล็อกเริ่มใช้งานได้
    
    //เวลาก่อนที่ mino จะหยุดทำงานเมื่อลงพื้น
    public boolean deactivating;
    int deactiveCounter = 0;

    //สร้างบล็อกตัวอย่าง
    public void create(Color c){
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);

        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x,int y){}
    public void updateXY(int direction){

        checkRotationCollision(); // ตรวจสอบการชนเมื่อหมุน

        if(leftCollision == false && rightCollision == false && bottomCollision == false){

            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;                // เก็บค่าไว้ใน temp array เผื่อรับมือ collision
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }      
    }

    //หมุนบล็อกไปทางต่างๆ
    public void getDirection1 (){}                      // 1 บล็อกแบบ Default
    public void getDirection2 (){}                      // ค่อยไปทำต่อในรูปแบบ @override ในแต่ละ mino class
    public void getDirection3 (){}
    public void getDirection4 (){}
    
    // ตรวจชอบการชนระหว่างเคลื่อนที่
    public void checkMovementCollision(){

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // ตรวจชอบการชนระหว่างบล็อก
        checkStaticBlockCollision();

        //เช็คกรอบนอก
        //กำแพงทางซ้าย 
       for(int i = 0; i < b.length ; i++){             // สแกนอาร์เรย์จากค่า X ถ้าเท่ากับ PlayArea ทาง x ซ้าย 
        if(b[i].x == PlayManager.left_x ){           // ดังนั้น mino ชนกับกำแพงซ้าย ทำให้ collision เป็นจริง
            leftCollision = true;
        }
    }
        
    //กำแพงทางขวา
    for(int i = 0; i < b.length ; i++){                         // สแกนอาร์เรย์จากค่า X ถ้าเท่ากับ PlayArea ทาง x ขวา
        if(b[i].x + Block.SIZE >= PlayManager.right_x){   //370      // ดังนั้น mino ชนกับกำแพงซ้าย ทำให้ collision เป็นจริง
            rightCollision = true;
        }
    }

    //กำแพงส่วนล่าง
    for (int i = 0; i < b.length; i++) {
        if (b[i].y + Block.SIZE >= PlayManager.bottom_y) { // ถ้า y มากกว่าหรือเท่ากับกรอบล่าง
            bottomCollision = true;
        }
    }                      
} 

    // ตรวจชอบการชนระหว่างหมุน
    public void checkRotationCollision(){
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // ตรวจชอบการชนระหว่างบล็อก
        checkStaticBlockCollision();

        //เช็คกรอบนอก
        //กำแพงทางซ้าย 
        for(int i = 0; i < b.length ; i++){                            // สแกนอาร์เรย์จากค่า X ถ้าน้อยกว่า PlayArea ทาง x ซ้าย 
            if(tempB[i].x < PlayManager.left_x){                       // ดังนั้น mino ชนกับกำแพงซ้าย ทำให้ collision เป็นจริง
                leftCollision = true;
            }
        }

        //กำแพงทางขวา
        for(int i = 0; i < b.length ; i++){                            // สแกนอาร์เรย์จากค่า X ถ้ามากกว่า PlayArea ทาง x ขวา
            if(tempB[i].x + Block.SIZE > PlayManager.right_x){         // ดังนั้น mino ชนกับกำแพงซ้าย ทำให้ collision เป็นจริง
                rightCollision = true;
            }
        }

        //กำแพงส่วนล่าง
        for(int i = 0; i < b.length ; i++){                             // สแกนอาร์เรย์จากค่า X ถ้ามากกว่า PlayArea ทาง y ข้างล่าง 
            if(tempB[i].y + Block.SIZE > PlayManager.bottom_y){         // ดังนั้น mino ชนกับกำแพงซ้าย ทำให้ collision เป็นจริง
                bottomCollision = true;
            }
        }
    }

    // ตรวจชอบการชนระหว่างบล็อก
    public void checkStaticBlockCollision(){
        //สแกน staticBlocks array
        for(int i = 0 ; i < PlayManager.staticBlocks.size() ; i++){
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            //เช็ค ล่าง
            for(int ii = 0 ; ii < b.length ; ii++){
                if(b[ii].y + Block.SIZE == targetY && b[ii].x == targetX){
                    bottomCollision = true;
                }
            }    
            
            //เช็ค ซ้าย
            for(int ii = 0 ; ii < b.length ; ii++){
                if(b[ii].x - Block.SIZE == targetX && b[ii].y == targetY){
                    leftCollision = true;
                }
                }
            //เช็ค ขวา
            for(int ii = 0 ; ii < b.length ; ii++){
                if(b[ii].x + Block.SIZE == targetX && b[ii].y == targetY){
                    rightCollision = true;
                }
            }
        }
    } 

    public void update(){

        if(deactivating){
            deactivating();
        }

        // ขยับ Mino
        if(KeyHandler.upPressed){
            switch (direction){
                case 1 : getDirection2();break;  // ใช้ switch-case สร้างเงื่อนไขในการหมุน
                case 2 : getDirection3();break;
                case 3 : getDirection4();break;
                case 4 : getDirection1();break;
            }
            KeyHandler.upPressed = false; // จากนั้นเปลี่ยน boolean เป็น false
            GamePanel.se.play(3, false);
        } 
        if (KeyHandler.spacePressed) {
            while (!bottomCollision) {
                for (Block block : b) {
                    block.y += Block.SIZE; // ขยับบล็อกลงไป
                }
                checkMovementCollision(); // ตรวจสอบการชน
            }
            KeyHandler.spacePressed = false; // เปลี่ยนสถานะเป็น false
        }


        checkMovementCollision();

        if(KeyHandler.downPressed){    // เมื่อกด S บล็อคขยับลง 1 บล็อก
            //ถ้า mino ไม่แตะโดนส่วนข้างล่าง ก็จะลงไปต่อได้
            if (bottomCollision == false){
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;          
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                autoDropCounter = 0; // เมื่อบล็อคลงมา รีเซ็ตค่าหล่นเป็น 0
            }
            KeyHandler.downPressed = false; // จากนั้นเปลี่ยน boolean เป็น false
        }

        if(KeyHandler.leftPressed){
            if (leftCollision == false){
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;          
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;    
            }      
            KeyHandler.leftPressed = false; // จากนั้นเปลี่ยน boolean เป็น false
        } 

        if(KeyHandler.rightPressed){
            if (rightCollision == false){
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;          
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }           
            KeyHandler.rightPressed = false; // จากนั้นเปลี่ยน boolean เป็น false
        }

        if(bottomCollision){ 
            if(deactivating == false){
                GamePanel.se.play(2, false);
            }
            deactivating = true;   //ถ้าเกิดการชนกันของข้างล่าง จะปิด autoDrop
        } else {
            autoDropCounter++;     // ค่าค่อยๆเพิ่มขึ้นในทุกๆเฟรมต่อ 1 บล็อก
            
            if(autoDropCounter == PlayManager.dropInterval){
            // บล็อกหล่นลงมา
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;          // เพิ่ม Block.SIZE ในแต่ละแกน y
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;           // หลังจากนั้นรีเซ็ตค่าเป็น 0
            }
        }    
    }

    public void deactivating(){
        deactiveCounter++;

        //รอไปอีก 40 เฟรม ถึงจะปิดใช้งาน mino ที่เล่น
        if(deactiveCounter == 40){
            deactiveCounter = 0;      //ถ้าถึง 40 รีเซ็ตค่าเป็น 0 
            checkMovementCollision(); //ตรวจสอบว่าข้างล่างยังชนกันอยู่ไหม

            //ถ้าข้างล่างยังชนกันหลังผ่านไป 40 เฟรม ทำการปิด mino ที่เล่น
            if(bottomCollision){
                active = false;
            }

        }
    }

    public void draw(Graphics2D g2){
        
        int margin = 2; //เพิ่มระยะขอบให้กับบล็อก
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE-(margin*2),Block.SIZE-(margin*2));
        g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE-(margin*2),Block.SIZE-(margin*2));
        g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE-(margin*2),Block.SIZE-(margin*2));
        g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE-(margin*2),Block.SIZE-(margin*2));
    }
}
    

