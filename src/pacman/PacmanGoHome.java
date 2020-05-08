package pacman;


import java.awt.EventQueue;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

// Provide main
@SuppressWarnings("serial")
public class PacmanGoHome extends JFrame{
	
	public PacmanGoHome() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        initUI();
    }

    private void initUI() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        add(new screen());

        setTitle("Pacman Go Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 410);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
	
	public static void main (String[] args) throws IOException {
		EventQueue.invokeLater(() -> {
			try {
				PacmanGoHome ex;
				try {
					ex = new PacmanGoHome();
					ex.setVisible(true);
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        });
		
	}
	
}
