package fr.upem.captcha.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.captcha.captchaManager.CaptchaManager;

/**
 * MainUi
 * @author CRUVEILLIER Marie - DONADIO Axel
 * @version 1.0 
 */

public class MainUi {
	
	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	private static CaptchaManager captchaManager = new CaptchaManager();
	
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Capcha");
		drawFrame(frame);
	}
	
	/**
	  * Créer le layout du captcha
	  * @param 
	  * @return GridLayout
	  */
	private static GridLayout createLayout(){
		return new GridLayout(5,3);
	}
	
	/**
	  * Créer un bouton pour valider les choix à partir d'informations d'UI à afficher et d'une frame (pour la masquer)
	  * @param JTextArea, JFrame
	  * @return JButton
	  */
	private static JButton createOkButton(JTextArea iUser, JFrame f){
		JTextArea infosUser = Objects.requireNonNull(iUser, "infoUser is must not be null");
		JFrame frame = Objects.requireNonNull(f, "f is must not be null");
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des évènements
					
					@Override
					public void run() { // c'est un runnable
						captchaManager.setSelected(selectedImages);
						if(captchaManager.hasWon()) {
							infosUser.setText("Félicitation, tu as gagné !");
						}
						else {
							if(captchaManager.getDifficultyLevel() < captchaManager.getMaxDifficultyLevel()) {
								restart(frame);
							}
							else {
								infosUser.setText("Tu as perdu !");
							}
							
						}
					}
				});
			}
		});
	}
	
	/**
	  * Créer le label d'une image pour gérer les différents événements au clic
	  * @param URL
	  * @return JLabel
	  */
	private static JLabel createLabelImage(URL u) throws IOException{
		URL url = Objects.requireNonNull(u, "u is must not be null");
		BufferedImage img = ImageIO.read(url); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
			private boolean isSelected = false;
			
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
		
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
				EventQueue.invokeLater(new Runnable() { 
					
					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							isSelected = true;
							selectedImages.add(url);
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(url);
						}
						
					}
				});
				
			}
		});
		
		return label;
	}
	
	/**
	  * Restart la partie en nettoyant le buffer des images sélectionnées et en rappelant drawFrame
	  * @param JFrame
	  * @return 
	  */
	private static void restart(JFrame f) {
		JFrame frame = Objects.requireNonNull(f, "f is must not be null");
		selectedImages.clear();
		frame.setVisible(false);
		frame = new JFrame("Captcha");
		drawFrame(frame);
	}
	
	/**
	  * Dessine la frame prise en paramètre
	  * @param 
	  * @return Frame
	  */
	private static void drawFrame(JFrame f) {
		JFrame frame = Objects.requireNonNull(f, "f is must not be null");
		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes
		
		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.
		
		captchaManager.captchaManagerInitialize();
		
		JTextArea whiteSpaceHL = new JTextArea("");
		JTextArea whiteSpaceHR = new JTextArea("");
		JTextArea whiteSpaceDL = new JTextArea("");
		JTextArea infosUser = new JTextArea("Sélectionnez les images de " + 
				Arrays.toString(
						captchaManager.getCorrectAnswerName().
							split("(?=\\p{Upper})"))
								.replace(",", "")  
								.replace("[", "")  
								.replace("]", "")  
								.trim()
							);
		frame.add(whiteSpaceHL);
		frame.add(infosUser);
		frame.add(whiteSpaceHR);
		
		for(URL url : captchaManager.getImagesList()) {
			try {
				frame.add(createLabelImage(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JButton okButton = createOkButton(infosUser, frame);
		frame.add(whiteSpaceDL);
		frame.add(okButton);
		
		frame.setVisible(true);
	}
}


