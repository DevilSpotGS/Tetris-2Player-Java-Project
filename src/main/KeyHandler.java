package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{ //Listener = รับข้อมูลจากคีย์บอร์ด

    public static boolean upPressed,downPressed,leftPressed,rightPressed,spacePressed; //รับค่าจริงเท็จ

    @Override //ไม่ใช้
    public void keyTyped(KeyEvent e) {
    }

    @Override //เมื่อกด
    public void keyPressed(KeyEvent e) { 
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){
            upPressed = true;
        }

        if (code == KeyEvent.VK_S){
            downPressed = true;
        }

        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE){ 
            spacePressed = true;
        }
    }


    @Override //เมื่อปล่อยปุ่ม
    public void keyReleased(KeyEvent e) { 
        int code = e.getKeyCode();
    if (code == KeyEvent.VK_SPACE) {
        spacePressed = false; // เมื่อปล่อยปุ่ม Space ให้เปลี่ยนสถานะเป็น false
    }

    }
}