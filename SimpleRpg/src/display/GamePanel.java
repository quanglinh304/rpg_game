package display;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

import state.MenuState;
import state.State;

public class GamePanel extends JPanel implements Runnable, KeyListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	State gameState;
    InputManager inputManager;    
    Thread gameThread;

    public boolean isRunning = true;

    public GamePanel(){
        gameState = new MenuState(this);        
        inputManager = new InputManager(gameState);
    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        long previousTime = System.nanoTime();
        long currentTime;
        long sleepTime;
        long period = 1000000000/100;

        while(isRunning){
            gameState.Update();
            gameState.Render();
            repaint();

            currentTime = System.nanoTime();
            sleepTime = period - (currentTime - previousTime);
            try{

                    if(sleepTime > 0)
                            Thread.sleep(sleepTime/1000000);
                    else Thread.sleep(period/2000000);

            }catch(Exception e){}
            previousTime = System.nanoTime();
        }
    }

    public void paint(Graphics g){
        g.drawImage(gameState.getBufferedImage(), 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        inputManager.setPressedButton(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputManager.setReleasedButton(e.getKeyCode());
    }

    public void setState(State state) {
        gameState = state;
        inputManager.setState(state);
    }
    
}
