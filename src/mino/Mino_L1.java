package mino;

import java.awt.Color;

public class Mino_L1 extends Mino{
    public Mino_L1(){
        create(Color.orange); //ตั้งค่าสีบล็อก L1
    }

    //ตั้งค่าตำแหน่งบล็อก
    public void setXY(int x,int y){
        // 0           <------ เป็นตำแหน่ง b[1] **b[0].y - Block.SIZE คือทำให้บล็อก ขึ้น มาจากบล็อก b[0]**
        // 0           <------ เป็นตำแหน่ง b[0] ทั้ง x,y  เพราะเป็นตัวแกนกลางที่สามารถหมุนได้
        // 0 0         <------ เป็นตำแหน่ง b[2],b[3] **b[0].y + Block.SIZE คือทำให้บล็อก ลง มาจากบล็อก b[0] /// ส่วน b[0].x + Block.SIZE ทำให้บล็อกอยู่ทางขวา***

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x;
        b[2].y = b[0].y + Block.SIZE;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;

        

    }
    public void getDirection1 (){
        // 0
        // 0
        // 0 0
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(1);
    }  
    public void getDirection2 (){
        // 
        // 0 0 0
        // 0 
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y; 
        tempB[2].x = b[0].x - Block.SIZE;
        tempB[2].y = b[0].y; 
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(2);
    }        
    public void getDirection3 (){
        // 0 0
        //   0
        //   0
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y + Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(3);
    }
    public void getDirection4 (){
        //     0
        // 0 0 0
        // 
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(4);
    }
}
