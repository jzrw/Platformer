package platformer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;



public class Platformer implements ActionListener, KeyListener {

    public static Platformer platformer;

    public final int WIDTH = 800, HEIGHT = 800;

    public Renderer renderer;

    public Rectangle character;

    public ArrayList<Rectangle> columns;

    public int ticks, yMotion, score, speed = 5;

    public boolean gameOver, started;

    public Random rand;

    public Platformer() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();

        jframe.add(renderer);
        jframe.setTitle("Platformer");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        character = new Rectangle(WIDTH/2 - 10, HEIGHT/2 -10, 20, 20);
        columns = new ArrayList<>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start) {
        int width = 100;
        int height = 100 + rand.nextInt(500);

        if(start) {
        columns.add(new Rectangle(WIDTH + width + columns.size() * 600, HEIGHT - height, width, height));
        }
        else {
            columns.add(new Rectangle(columns.get(columns.size() -1).x + 600, HEIGHT - height, width,height));
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.white);
        g.fillRect(column.x,column.y, column.width, column.height);
    }

    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, WIDTH, HEIGHT);

        g.setColor(Color.cyan);
        g.fillRect(character.x, character.y, character.width, character.height);

        for(Rectangle column: columns) {
            paintColumn(g, column);
        }
        g.setColor(Color.cyan);
        g.setFont(new Font("Arial", Font.BOLD,100));
        if(!started) {
            g.drawString("Space to Start!",75,HEIGHT/2-50);
        }
        if(!gameOver && started) {
            g.drawString(String.valueOf(score), WIDTH/2 -25, 100);
        }
        if(gameOver) {
            g.drawString("Game Over!", 100, HEIGHT/2 -50);
        }
    }

    public static void main(String[] args) {
        platformer = new Platformer();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(score >= 50) {
            speed = 10;
        }
        if(score >= 100) {
            speed = 15;
        }
        if(score >= 150) {
            speed = 20;
        }
        if(score >= 200) {
            speed = 25;
        }
        ticks ++;

        if(started) {
            for (Rectangle column : columns) {
                column.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    addColumn(false);
                }
            }
            character.y += yMotion;

            for (Rectangle column: columns) {
                if (column.intersects(character)) {
                    if(character.y < column.y) {
                        character.y = column.y - character.height;
                    }
                    score++;

                }
                if (column.intersects(character) && character.y + character.height > column.y) {
                    gameOver =true;
                    if (character.x <= column.x) {
                        character.x = column.x - character.width;
                    }
                }
            }
            if (character.y > HEIGHT || character.y < 0) {
                gameOver =true;
            }
            if (character.y + yMotion >= HEIGHT) {
                character.y = HEIGHT - character.height;
                gameOver = true;
            }
        }
        
        renderer.repaint();
    }

    public void jump() {
        if(gameOver) {
            character = new Rectangle(WIDTH/2 - 10, HEIGHT/2 -10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;
            speed = 5;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        if(!started) {
            started = true;
        }
        else if (!gameOver) {
            if(yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            jump();
        }
    }


}
