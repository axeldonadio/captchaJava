package fr.upem.captcha.captchaManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.net.URL;

import fr.upem.captcha.images.ImageType;
import fr.upem.captcha.images.animal.Animal;
import fr.upem.captcha.images.panneau.Panneau;
import fr.upem.captcha.images.transport.Transport;
import fr.upem.captcha.images.vetement.Vetement;
import fr.upem.captcha.images.animal.mammifere.Mammifere;
import fr.upem.captcha.images.animal.mammifere.marin.MammifereMarin;
import fr.upem.captcha.images.animal.mammifere.terrestre.MammifereTerrestre;
import fr.upem.captcha.images.animal.oiseau.Oiseau;
import fr.upem.captcha.images.animal.oiseau.jaune.OiseauJaune;
import fr.upem.captcha.images.animal.oiseau.rouge.OiseauRouge;
import fr.upem.captcha.images.animal.oiseau.vert.OiseauVert;
import fr.upem.captcha.images.animal.poisson.Poisson;
import fr.upem.captcha.images.animal.poisson.poissonRouge.PoissonRouge;
import fr.upem.captcha.images.animal.poisson.requin.Requin;
import fr.upem.captcha.images.panneau.publicitaire.PanneauPublicitaire;
import fr.upem.captcha.images.panneau.publicitaire.autouroute.PanneauAutoroute;
import fr.upem.captcha.images.panneau.publicitaire.metro.PanneauMetro;
import fr.upem.captcha.images.panneau.publicitaire.ville.PanneauVille;
import fr.upem.captcha.images.panneau.routier.PanneauRoutier;
import fr.upem.captcha.images.panneau.routier.danger.PanneauDanger;
import fr.upem.captcha.images.panneau.routier.interdiction.PanneauInterdiction;
import fr.upem.captcha.images.panneau.routier.limitation.PanneauLimitation;
import fr.upem.captcha.images.transport.maritime.TransportMaritime;
import fr.upem.captcha.images.transport.maritime.bateau.Bateau;
import fr.upem.captcha.images.transport.maritime.kayak.Kayak;
import fr.upem.captcha.images.transport.maritime.sousMarin.SousMarin;
import fr.upem.captcha.images.transport.terrestre.TransportTerrestre;
import fr.upem.captcha.images.transport.terrestre.bus.Bus;
import fr.upem.captcha.images.transport.terrestre.moto.Moto;
import fr.upem.captcha.images.transport.terrestre.velo.Velo;
import fr.upem.captcha.images.transport.terrestre.voiture.Voiture;
import fr.upem.captcha.images.vetement.chaussure.Chaussure;
import fr.upem.captcha.images.vetement.chaussure.basket.Basket;
import fr.upem.captcha.images.vetement.chaussure.botte.Botte;
import fr.upem.captcha.images.vetement.chaussure.talon.Talon;
import fr.upem.captcha.images.vetement.haut.Haut;
import fr.upem.captcha.images.vetement.haut.debardeur.Debardeur;
import fr.upem.captcha.images.vetement.haut.manteau.Manteau;
import fr.upem.captcha.images.vetement.haut.pull.Pull;
import fr.upem.captcha.images.vetement.haut.teeShirt.TeeShirt;

/**
 * CaptchaManager
 * @author CRUVEILLIER Marie - DONADIO Axel
 * @version 1.0 
 */
public class CaptchaManager {
	private List<URL> imagesList;
	private List<URL> selected;
	private List<ImageType> listClasses;
	ImageType correctAnswer;
	private final static int NB_CLASSES = 3;
	private final static int NB_IMAGES = 9;
	private final static int MAX_DIFFICULTY_LEVEL = 3;
	private static int difficultyLevel;
	private int numberOfCorrects;
	
	public CaptchaManager() {
		super();
		this.listClasses =  new ArrayList<ImageType>();
		this.imagesList =  new ArrayList<URL>();
		this.selected =  new ArrayList<URL>();
		difficultyLevel = 0; 
	}
	 /**
	  * Initialise les différentes listes du captchaManager à chaque relance de la partie
	  * @param 
	  * @return
	  */
	public void captchaManagerInitialize() {	
		this.imagesList.clear();
		this.listClasses.clear();
		increaseDifficulty();
		setListClasses();
		setImagesList();
	}
	/**
	  * Initialise la liste des classes qui seront affichées à l'écran
	  * @param 
	  * @return
	  */
	public void setListClasses() {
		ArrayList<ImageType> completeListClasses = new ArrayList<ImageType>(listClasses);
		switch(difficultyLevel) {
			case 1:
				completeListClasses.add(new Animal());
				completeListClasses.add(new Vetement());
				completeListClasses.add(new Transport());
				completeListClasses.add(new Panneau());
			break;
			case 2:
					if(correctAnswer.getClass().getSimpleName().contentEquals("Animal")) {
						completeListClasses.add(new Mammifere());
						completeListClasses.add(new Oiseau());
						completeListClasses.add(new Poisson());
					}
					if(correctAnswer.getClass().getSimpleName().contentEquals("Panneau")) {
						completeListClasses.add(new PanneauPublicitaire());
						completeListClasses.add(new PanneauRoutier());
					}
					if(correctAnswer.getClass().getSimpleName().contentEquals("Transport")) {
						completeListClasses.add(new TransportMaritime());
						completeListClasses.add(new TransportTerrestre());					
					}
					if(correctAnswer.getClass().getSimpleName().contentEquals("Vetement")) {
						completeListClasses.add(new Chaussure());
						completeListClasses.add(new Haut());
					}	
			break;
			case 3:
				if(correctAnswer.getClass().getSimpleName().contentEquals("Mammifere")) {
					completeListClasses.add(new MammifereTerrestre());
					completeListClasses.add(new MammifereMarin());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("Oiseau")) {
					completeListClasses.add(new OiseauJaune());
					completeListClasses.add(new OiseauRouge());
					completeListClasses.add(new OiseauVert());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("Poisson")) {
					completeListClasses.add(new PoissonRouge());
					completeListClasses.add(new Requin());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("PanneauPublicitaire")) {
					completeListClasses.add(new PanneauAutoroute());
					completeListClasses.add(new PanneauMetro());
					completeListClasses.add(new PanneauVille());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("PanneauRoutier")) {
					completeListClasses.add(new PanneauDanger());
					completeListClasses.add(new PanneauInterdiction());
					completeListClasses.add(new PanneauLimitation());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("TransportMaritime")) {
					completeListClasses.add(new Bateau());
					completeListClasses.add(new Kayak());
					completeListClasses.add(new SousMarin());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("TransportTerrestre")) {
					completeListClasses.add(new Bus());
					completeListClasses.add(new Velo());
					completeListClasses.add(new Moto());
					completeListClasses.add(new Voiture());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("Chaussure")) {
					completeListClasses.add(new Talon());
					completeListClasses.add(new Basket());
					completeListClasses.add(new Botte());
				}
				if(correctAnswer.getClass().getSimpleName().contentEquals("Haut")) {
					completeListClasses.add(new TeeShirt());
					completeListClasses.add(new Pull());
					completeListClasses.add(new Manteau());
					completeListClasses.add(new Debardeur());
				}
			break;
			default:
				completeListClasses.add(new Animal());
				completeListClasses.add(new Vetement());
				completeListClasses.add(new Transport());
				completeListClasses.add(new Panneau());
			break;
			
		}
		Collections.shuffle(completeListClasses);
		try {
			this.listClasses = completeListClasses.subList(0, NB_CLASSES);
		}
		catch(IndexOutOfBoundsException e) {
			this.listClasses = completeListClasses;
		}
		
	}
	
	/**
	  * Initialise la liste des images à afficher en fonction des classes choisies
	  * @param 
	  * @return
	  */
	public void setImagesList() {
		if(listClasses.size() == NB_CLASSES) {
			int r = (int)((Math.random() * 5) + 1);
			this.imagesList.addAll(listClasses.get(0).getRandomPhotosURL(r));
			int r2 = (int)((Math.random() * 3) + 1);
			this.imagesList.addAll(listClasses.get(1).getRandomPhotosURL(r2));
			int r3 = (NB_IMAGES - r - r2);
			this.imagesList.addAll(listClasses.get(2).getRandomPhotosURL(r3));
			switch(setCorrectAnswer()) {
				case 0:
					this.numberOfCorrects = r;
				break;
				case 1:
					this.numberOfCorrects = r2;
				break;
				case 2:
					this.numberOfCorrects = r3;
				break;
			}
		}
		else {
			int r = (int)((Math.random() * 5) + 1);
			this.imagesList.addAll(listClasses.get(0).getRandomPhotosURL(r));
			int r2 = (NB_IMAGES - r);
			this.imagesList.addAll(listClasses.get(1).getRandomPhotosURL(r2));
			switch(setCorrectAnswer()) {
				case 0:
					this.numberOfCorrects = r;
				break;
				case 1:
					this.numberOfCorrects = r2;
				break;
			}
		}
	}
	
	/**
	  * Getter de la liste des classes à afficher
	  * @param 
	  * @return List<ImageType>
	  */
	public List<ImageType> getListClasses(){
		return this.listClasses;
	}
	/**
	  * Getter du nom de la bonne classe à trouver
	  * @param 
	  * @return String
	  */
	public String getCorrectAnswerName() {
		return this.correctAnswer.getClass().getSimpleName();
	}
	/**
	  * Getter des images à afficher
	  * @param 
	  * @return String
	  */
	public List<URL> getImagesList(){
		return this.imagesList;
	}
	/**
	  * Getter des images sélectionnées par l'utilisateur
	  * @param 
	  * @return List<URL>
	  */
	public List<URL> getSelected(){
		return this.selected;
	}
	/**
	  * Getter du niveau de difficulté actuel
	  * @param 
	  * @return int
	  */
	public int getDifficultyLevel() {
		return difficultyLevel;
	}
	/**
	  * Getter u niveau de difficulté maximal
	  * @param 
	  * @return int
	  */
	public int getMaxDifficultyLevel() {
		return MAX_DIFFICULTY_LEVEL;
	}
	/**
	  * Setter des images sélectionnées par l'utilisateur
	  * @param List<URL>
	  * @return
	  */
	public void setSelected(List<URL> selectedImages) {
		this.selected = selectedImages;
	}
	/**
	  * Setter aléatoire de la réponse que doit trouver l'utilisateur parmis les classes à afficher. Retourne l'indice de cette réponse dans la liste des classes choisies
	  * @param 
	  * @return int
	  */
	public int setCorrectAnswer() {
		Random r = new Random();
		int n = r.nextInt(this.listClasses.size());
		this.correctAnswer = this.listClasses.get(n);
		return n;
	}
	
	/**
	  * Vérifie si le joueur s'est trompé ou non. Retourne true s'il a gagné, false sinon
	  * @param 
	  * @return boolean
	  */
	public boolean hasWon() {
		for (URL url : this.selected) {
		    if(!correctAnswer.isPhotoCorrect(url))
		    		return false;
		}
		return true && this.selected.size() == numberOfCorrects;
	}
	/**
	  * Incrémente le niveau de difficulté de 1
	  * @param 
	  * @return
	  */
	public void increaseDifficulty() {
		difficultyLevel = difficultyLevel + 1;
	}
	

	
}
