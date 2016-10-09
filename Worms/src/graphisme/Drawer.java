package graphisme;

import arme.Arme;
import fr.lacl.cpo.Drawing;
import game.GameManager;
import joueur.Personnage;

/**
 * Interface permettant la gestion d'affichage des différents types d'éléments du jeu, et leurs interactions
 *
 */
public interface Drawer {
	/**
	 * Permet de dessiner l'objet
	 * @param d
	 * @param y 
	 * @param x 
	 * @param tailleCarre 
	 */
	public void draw(Drawing d, int x, int y, int tailleCarre);
	
	/**
	 * Renvoit true si c'est un element remplaçeable et donc pas important, qui n'a qu'un aspect graphique (comme sol)
	 * @return
	 */
	public boolean isReplaceable();
	
	/**
	 * Renvoit true si c'est un projectile doit s'arreter sur sa trajectoire si il passe par cet element
	 * @return
	 */
	public boolean canStopProjectile();
	
	/**
	 * Gestion de l'arme si le projectile touche
	 * @param arme
	 * @param ennemi
	 * @param onHit
	 * @param gameManager
	 */
	void manageArmeEffect(Arme arme, Personnage ennemi, boolean onHit, GameManager gameManager);

}
