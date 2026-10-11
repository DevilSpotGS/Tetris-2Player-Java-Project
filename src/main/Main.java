package main;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("TETRIS");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ปิดปุ่ม x
        window.setResizable(false); //ปรับขนาดไม่ได้

        //เพิ่ม JPanel --> JFrame
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();  //ขนาด Panel มีขนาดเท่ากับ ขนาด window
       
        window.setLocationRelativeTo(null); //จออยู่ตรงกลาง
        window.setVisible(true); //ทำให้มองเห็น

        gp.LaunchGame();   
    }
    
}
