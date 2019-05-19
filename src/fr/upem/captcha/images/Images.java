package fr.upem.captcha.images;
import java.util.List;
import java.net.URL;

public interface Images {
	/**
	 * Interface des images
	 * @author CRUVEILLIER Marie - DONADIO Axel
	 * @version 1.0 
	 */
	public List<URL> getPhotos();
	public List<URL> getRandomPhotosURL(int number);
	public URL getRandomPhotoURL();
	public boolean isPhotoCorrect(URL path);
}
