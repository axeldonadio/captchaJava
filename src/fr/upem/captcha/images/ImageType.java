package fr.upem.captcha.images;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.net.URL;


/**
 * Classe abstraite implémentant les méthodes de l'interface Images
 * @author CRUVEILLIER Marie - DONADIO Axel
 * @version 1.0 
 */
abstract public class ImageType implements Images {
	private List<URL> photos;
	public ImageType() {
		super();
		this.photos = getPhotos();
	}
	/**
	  * Récupère les images numérotées de 0 à 9 dans le dossier de la classe et retourne leur lien dans une liste
	  * @param 
	  * @return List<URL>
	  */
	public List<URL> getPhotos(){
		// Ancienne version non automatisée (celle sur le .jar conforméméent aux explications du dossier)
		/*List<URL> list = new ArrayList<URL>();
		for(int i = 0; i < 10; i++) {
			list.add(this.getClass().getResource(i + ".jpg"));
		}
		return list;*/
		
		// Nouvelle version
		ArrayList<URL> photos =  new ArrayList<URL>();
		File folder = new File("./src/"+this.getClass().getPackageName().toString().replace(".", "/"));
		String[] files = folder.list();
		for(String path : files){
			if (path.endsWith("jpg")==true) {
				photos.add(this.getClass().getResource(path));
			}	
		}
		return photos;
	}
	/**
	  * Retourne une liste de n lien d'image au hasard parmi la liste des images de la classe
	  * @param int
	  * @return List<URL>
	  */
	public List<URL> getRandomPhotosURL(int number){
		int nb = Objects.requireNonNull(number, "nulber must not be null");
		List<URL> list = new ArrayList<URL>(photos);
		Collections.shuffle(list);
		return list.subList(0, nb);
	}
	/**
	  * Retourne un lien d'image au hasard parmi la liste des images de la classe
	  * @param 
	  * @return URL
	  */
	public URL getRandomPhotoURL() {
		Random r = new Random();
		int n = r.nextInt(photos.size());
		return photos.get(n);
	}
	/**
	  * Vérifie si une image passée en paramètre est du type de la classe courante. Retourne true si oui, false sinon.
	  * @param URL
	  * @return boolean
	  */
	public boolean isPhotoCorrect(URL path) {
		URL newPath = Objects.requireNonNull(path, "path is must not be null");
		String packageName = this.getClass().getPackage().getName().replace(".", "/");
		return newPath.toString().contains(packageName);
	}
	@Override
	public String toString() {
		return "photosListPath :" + photos;
	}
	
}
