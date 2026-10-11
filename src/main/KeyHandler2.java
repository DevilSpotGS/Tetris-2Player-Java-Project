package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler2 implements KeyListener{ //Listener = รับข้อมูลจากคีย์บอร์ด

    public static boolean upPressed,downPressed,leftPressed,rightPressed,enter; //รับค่าจริงเท็จ

    @Override //ไม่ใช้
    public void keyTyped(KeyEvent e) {
    }

    @Override //เมื่อกด
    public void keyPressed(KeyEvent e) { 
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP){
            upPressed = true;
        }

        if (code == KeyEvent.VK_DOWN){
            downPressed = true;
        }

        if (code == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        
        if (code == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER){ 
            enter = true;
        }
    }


    @Override //เมื่อปล่อยปุ่ม
    public void keyReleased(KeyEvent e) { 
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ENTER) {
            enter = false; 
        }
    }

    }