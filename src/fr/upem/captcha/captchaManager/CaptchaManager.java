package fr.upem.captcha.captchaManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import fr.upem.captcha.images.Images;

/**
 * CaptchaManager
 * @author CRUVEILLIER Marie - DONADIO Axel
 * @version 1.0 
 */
public class CaptchaManager {
	private List<URL> imagesList;
	private List<URL> selected;
	private List<Images> listClasses;
	private ArrayList<String> categorieNames;
	private static Images correctAnswer;
	private final static int NB_CLASSES = 3;
	private final static int NB_IMAGES = 9;
	private final static int MAX_DIFFICULTY_LEVEL = 3;
	private static int difficultyLevel;
	private int numberOfCorrects;
	
	public CaptchaManager() {
		super();
		this.categorieNames = new ArrayList<String>();
		this.categorieNames.add("images");
		this.listClasses =  new ArrayList<Images>();
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
	
	private static String getCurrentPath(ArrayList<String> categorieNames) {
		//StringBuilder fullPath = new StringBuilder("src/fr/upem/captcha");	// Chemin pour exécuter à partir d'Eclipse
		StringBuilder fullPath = new StringBuilder(System.getProperty("user.dir") + "/captcha2/src/fr/upem/captcha");	// Chemin pour exécuter en ligne de commandes depuis le dossier bin
		for(String categorie: categorieNames) {
			fullPath.append("/").append(categorie);
		}
		return fullPath.toString();
}
	
	private static ArrayList<String> getClassPath(ArrayList<String> categorieNames, List<String> categories) {
		StringBuilder fullPath = new StringBuilder("fr.upem.captcha");
		for(String categorie: categorieNames) {
			fullPath.append(".").append(categorie);
		}
		ArrayList<String> classPath = new ArrayList<String>();
		for(String categorie: categories) {
			String className = categorie.substring(0, 1).toUpperCase() + categorie.substring(1);
			String fullName = fullPath+"."+categorie+"."+className;
			classPath.add(fullName);
		}
		return classPath;
}
	
	public static ArrayList<Images> getCategories(ArrayList<String> categorieNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>(); 		// une liste de toutes nos classes
		if(difficultyLevel > 1)
			categorieNames.add(correctAnswer.getClass().getSimpleName().toLowerCase());
		// On récupére le dossier dans lequel on se trouve actuellement
		String currentPath = getCurrentPath(categorieNames);
		Path currentRelativePath = Paths.get(currentPath);
		// On récupére les sous dossiers (c'est à dire les categories)
		List<String> directories = null;
		try {
			directories = Files.walk(currentRelativePath, 1)
			        .map(Path::getFileName)
			        .map(Path::toString)
			        .filter(n -> !n.contains("."))
			        .collect(Collectors.toList());
			directories.remove(0);	// On enléve le 0 car c'est le nom du dossier courant	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// On récupére le nom des classes trouvées et on les rajoute à la liste
		ArrayList<String> classPath = getClassPath(categorieNames, directories);
		for (String s : classPath) {
			classes.add(Class.forName(s));
		}
		
		// On instancie chaque classe en objet de type Images qu'on rajoute dans notre liste
		ArrayList<Images> categories = new ArrayList<Images>();
		for (Class clazz : classes) {
			categories.add(instantiateImages(clazz));
		}
		
		
		
		return categories;
}
	public static Images instantiateImages(Class<?> category) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> cls = Class.forName(category.getTypeName());	// On récupére le type de la classe
		Object clsInstance = null;
		try {
			clsInstance = cls.getDeclaredConstructor().newInstance();
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// On instancie un objet du type de la classe
		return (Images)clsInstance;	// On le cast en Images pour pouvoir utiliser les méthodes de l'interface
}
	/**
	  * Initialise la liste des classes qui seront affichées à l'écran
	  * @param 
	  * @return
	  */
	public void setListClasses() {
		ArrayList<Images> categories = null;
		try {
			categories = getCategories(categorieNames);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Collections.shuffle(categories);
		try {
			this.listClasses = categories.subList(0, NB_CLASSES);
		}
		catch(IndexOutOfBoundsException e) {
			this.listClasses = categories;
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
	public List<Images> getListClasses(){
		return this.listClasses;
	}
	/**
	  * Getter du nom de la bonne classe à trouver
	  * @param 
	  * @return String
	  */
	public String getCorrectAnswerName() {
		return Arrays.asList(this.categorieNames.toArray()).subList(1, this.categorieNames.toArray().length) + " " + correctAnswer.getClass().getSimpleName().toLowerCase();
		
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
		this.selected = Objects.requireNonNull(selectedImages, "selectedImages must not be  null ");
	}
	/**
	  * Setter aléatoire de la réponse que doit trouver l'utilisateur parmis les classes à afficher. Retourne l'indice de cette réponse dans la liste des classes choisies
	  * @param 
	  * @return int
	  */
	public int setCorrectAnswer() {
		Random r = new Random();
		int n = r.nextInt(this.listClasses.size());
		correctAnswer = this.listClasses.get(n);
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
