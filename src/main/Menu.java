package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {

    private static Clip clip; // คลิปเสียง
    private static boolean isPlaying = true; // สถานะเสียง
    public static void main(String[] args) {

        //กรอบ(JFrame)
        JFrame f = new JFrame("Tetris");
        Container cp = f.getContentPane();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 600);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.getContentPane().setBackground(Color.BLACK);

        // โหลดรูปภาพพื้นหลัง
         ImageIcon backgroundImage = new ImageIcon("back.gif"); // ใส่พาธของรูปภาพ
         Image scaledImage = backgroundImage.getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage)); // ใช้ภาพที่ถูกปรับขนาด
         backgroundLabel.setBounds(0, 0, f.getWidth(), f.getHeight()); // กำหนดตำแหน่งและขนาดของ JLabel
    


          // สร้าง JPanel สำหรับใส่เนื้อหาทั้งหมด
        JPanel panel = new JPanel();
        panel.setLayout(null); // ตั้ง layout แบบ null เพื่อควบคุมตำแหน่งได้อิสระ
        panel.setBounds(0, 0, f.getWidth(), f.getHeight());
        panel.setOpaque(false); // ตั้งให้โปร่งใสเพื่อให้เห็นพื้นหลัง

        //Tertis
       // JLabel label = new JLabel("TETRIS!!");
       // label.setForeground(Color.WHITE);
       // label.setFont(new Font("Arial", Font.BOLD, 80));
       // label.setBounds(290, -400, 1000, 1000);


        //ปุ่ม
        JButton x1 = new JButton("start");
        JButton x2 = new JButton("Click me!");
        JButton x3 = new JButton("Exit");

        x1.addActionListener(new ActionListener() {
        
            

        public void actionPerformed(ActionEvent e) {
            if (clip != null && clip.isRunning()) {
                clip.stop(); // หยุดเพลงปัจจุบัน
            }
    
        f.dispose();
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
        });
        x3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // หยุดเสียงและออกจากโปรแกรม
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                }
                System.exit(0); // ปิดโปรแกรม
            }
        });
        x2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // หยุดเสียงและออกจากโปรแกรม
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                }
                System.exit(0); // ปิดโปรแกรม
            }
        });




        JButton soundButton = new JButton("Mute");
        soundButton.setBounds(800, 520, 65, 30); 
        soundButton.setBackground(Color.BLACK);
        f.add(soundButton);
        

         String text = "TETRIS!!";
        JLabel[] letters = new JLabel[text.length()];

         // สร้าง JLabel สำหรับแต่ละตัวอักษร
         for (int i = 0; i < text.length(); i++) {
            letters[i] = new JLabel(String.valueOf(text.charAt(i)));
            letters[i].setForeground(Color.WHITE);
            letters[i].setFont(new Font("Arial", Font.BOLD, 80));
            letters[i].setBounds(290 + i * 80, 100, 50, 100); // ตั้งตำแหน่งเริ่มต้น
            f.add(letters[i]);
        }

        // Timer สำหรับแอนิเมชันการกระโดดของตัวอักษร
        Timer timer = new Timer(100, new ActionListener() {
            Random random = new Random();
            int[] yPos = new int[text.length()]; // ตัวแปรเพื่อเก็บตำแหน่งยกขึ้นหรือลง
            int[] direction = new int[text.length()]; // ทิศทางการกระโดด
            int maxJumpHeight = 30; // ความสูงสูงสุดที่สามารถโดดได้

            {
                // ตั้งค่าทิศทางเริ่มต้นให้เป็น 1 (ขึ้น)
                for (int i = 0; i < text.length(); i++) {
                    direction[i] = 1;
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < text.length(); i++) {
                    // ตรวจสอบทิศทางการกระโดด
                    if (yPos[i] >= maxJumpHeight) {
                        direction[i] = -1; // ถ้าอยู่ที่สูงสุดให้ลง
                    } else if (yPos[i] <= 0) {
                        direction[i] = 1; // ถ้าอยู่ที่ต่ำสุดให้ขึ้น
                    }
                    yPos[i] += direction[i] * 5; // เปลี่ยนตำแหน่งตามทิศทาง

                    // ตั้งตำแหน่งใหม่
                    letters[i].setBounds(150 + i * 80, 80 - yPos[i], 80, 100);

                    // เปลี่ยนสีแบบสุ่ม
                    letters[i].setForeground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                }
            }
        });

        timer.start(); // เริ่มแอนิเมชัน

        // ปุ่มเสียง
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    if (clip != null && clip.isRunning()) {
                        clip.stop(); // หยุดเสียง
                    }
                    soundButton.setText("Play"); // เปลี่ยนชื่อปุ่มเป็น "Play"
                } else {
                    playBackgroundMusic("bgm.wav"); // เล่นเสียง
                    soundButton.setText("Mute"); // เปลี่ยนชื่อปุ่มเป็น "Mute"
                }
                isPlaying = !isPlaying; // เปลี่ยนสถานะ
            }
        });

        //ตำเเหน่งปุ่ม
        x1.setBounds(350,200,200,50);
        x2.setBounds(375, 270, 150, 40);
        x3.setBounds(400, 330, 100, 40);

        //สีปุ่ม
        x1.setBackground(new Color(245, 245, 245));
        x2.setBackground(new Color(192, 192, 192));
        x3.setBackground(new Color(192, 192, 192));

        //เเสดงปุ่ม
        cp.add(x1);
        cp.add(x2);
        cp.add(x3);
        //f.add(label);
        panel.setBackground(new Color(10, 0, 0));
        playBackgroundMusic("bgm.wav");

        // เพิ่มพื้นหลังและ panel ลงใน JFrame
         f.add(backgroundLabel); // ใส่พื้นหลัง
         f.add(panel); // ใส่เนื้อหาด้านบนพื้นหลัง
         f.setVisible(true);


}

            // เมธอดสำหรับเล่นเพลง BGM
    public static void playBackgroundMusic(String filePath) {
        try {
            // โหลดไฟล์เสียง
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // สร้างคลิปเพื่อเล่นเสียง
            clip = AudioSystem.getClip(); // ใช้ตัวแปรระดับคลาส
            clip.open(audioStream);

            // เล่นเสียงในลูป
            clip.loop(Clip.LOOP_CONTINUOUSLY); // เล่นในลูปไม่มีที่สิ้นสุด
            clip.start(); // เริ่มเล่นเสียง

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    

    
}

