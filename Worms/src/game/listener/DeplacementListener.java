package game.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.Drawer;
import graphisme.forme.Color;
import joueur.Personnage;

/**
 * Listener pour gérer les déplacements
 * 
 */
public class DeplacementListener extends MouseAdapter {
	
	private Drawing d;
	private Personnage currentPersonnage;
	private GameManager gameManager;
	private Color color = new Color("bleu clair", 0.2, 0.8, 1);
	
	// nombre de déplacements encore disponibles pour le personnage
	private Integer availableDeplacement;
	
	// liste des positions disponibles pour le déplacement
	List<IntPair> listOfAvailablePosition = new ArrayList<IntPair>();
		
	public DeplacementListener(GameManager gm, Drawing d, Personnage currentPersonnage){
		this.gameManager = gm;
		this.d = d;
		this.currentPersonnage = currentPersonnage;
		this.availableDeplacement = currentPersonnage.getNombreDeplacement();
		
		System.out.println("Bouton clic gauche : déplacement");
		System.out.println("Bouton clic droit : terminer déplacement");
		System.out.println("Déplacements encore disponibles : " + availableDeplacement);

		showAvailableCase();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		
		// si c'est un clic gauche
		if(e.getButton() == 1){
			if(isCaseClickedAvailable(e)){
				setPositionOfPersonnage(e);
			}
		}
		// si c'est un clic droit, cela veut dire que le joueur ne souhaite pas continuer les déplacements
		else if (e.getButton() == 3){
			refreshMap();
			terminateDeplacement();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}
	
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	
	/**
	 * Affiche les cases disponibles pour le déplacement, en rouges
	 */
	private void showAvailableCase(){
		
		Integer currentPersonnageLigne = currentPersonnage.getLigne();
		Integer currentPersonnageColonne = currentPersonnage.getColonne();
				
		// on test toutes les cases autour du personnage courant
		for(int ligne = currentPersonnageLigne-1; ligne != currentPersonnageLigne+2; ligne++){
			for(int colonne = currentPersonnageColonne-1; colonne != currentPersonnageColonne+2; colonne++){
				
				// si la coordonnée existe bien dans la matrice
				if(gameManager.getMapManager().isPositionInsideMap(ligne, colonne)){
					
					// on récupère le drawer sur la case à tester
					Drawer drawer = gameManager.getMapManager().getPosition(ligne, colonne);
					
					// si ce drawer est remplaçeable on l'ajoute aux cases disponibles et on la dessine en rouge
					if(drawer.isReplaceable()){
						listOfAvailablePosition.add(new IntPair(colonne, ligne));
						drawCase(ligne, colonne);
					}
				}
			}
		}
	}
	
	/**
	 * Dessine la case
	 * @param ligne
	 * @param colonne
	 */
	private void drawCase(int ligne, int colonne){
		Integer tailleCarre = gameManager.getMapDrawer().getTailleCarre();
		Integer offSetY = gameManager.getMapDrawer().getOffSetY();
		d.setColor(color.r, color.g, color.b);
		d.rect(colonne*tailleCarre, (ligne*tailleCarre)+offSetY, tailleCarre, tailleCarre);
		gameManager.getMapDrawer().createGrid();
	}
	
	/**
	 * Cette méthode modifie la position du personnage à partir du clic du joueur.
	 * On retire 1 déplacement disponible et si il ne reste plus de déplacements (personnage épuisé) on passe au tir
	 * @param e
	 */
	private void setPositionOfPersonnage(MouseEvent e) {
		
		// si il reste des déplacements
		if(availableDeplacement > 0){
			
			// on convertir le clic en position de matrice
			IntPair newPosition = convertMouseEventIntoIntPair(e);
			
			// on remplace la position du personnage en le plaçeant à l'endroit cliqué (seulement SI la case cliquée a été validée par showAvailableCase())
			gameManager.getMapManager().replacePosition(currentPersonnage, newPosition.ligne, newPosition.colonne);
			
			// on actualise pour voir la nouvelle position du personnage
			refreshMap();
			
			// on reset la liste des positions disponibles
			listOfAvailablePosition.clear();
			
			// on enlève un point de déplacement disponible pour ce tour-ci
			availableDeplacement--;
		}
		
		// si il ne reste plus de points de déplacement on actualise la map et on retire le listener
		if(availableDeplacement <= 0) {
			refreshMap();
			terminateDeplacement();
		}
		// si il en reste on recommence depuis le début (affichage des cases disponibles pour la nouvelle position du personnage)
		else {
			System.out.println("Déplacements encore disponibles : " + availableDeplacement);
			showAvailableCase();
		}
	}
	
	/**
	 * Méthode qui signifie la fin du déplacement : on retire le listener du Drawing et on passe aux tirs
	 */
	private void terminateDeplacement(){
		d.removeMouseListener(this);
		gameManager.makeShot();
	}
	
	/**
	 * Transforme les coordonnées graphiques d'un clique en coordonnées logiques dans la matrice
	 * @param e
	 */
	private IntPair convertMouseEventIntoIntPair(MouseEvent e){
		Integer tailleCarre = gameManager.getMapDrawer().getTailleCarre();
		Integer offSetY = gameManager.getMapDrawer().getOffSetY();
		Integer colonne = e.getX()/tailleCarre;
		Integer ligne = (e.getY()-offSetY)/tailleCarre;
		return new IntPair(colonne, ligne);
	}
	
	/**
	 * Renvoit true si la case cliquée par le joueur est l'une des cases disponibles pour le déplacement (celles affichées en couleur)
	 * @param e
	 * @return
	 */
	private boolean isCaseClickedAvailable(MouseEvent e) {
		IntPair position = convertMouseEventIntoIntPair(e);
		return listOfAvailablePosition.contains(position);
	}
	
	private void refreshMap(){
		gameManager.getMapDrawer().refreshMap(gameManager.getMapManager().getMap());
	}
	
	/**
	 * Représente une coordonnée de la matrice
	 *
	 */
	private class IntPair {
		int colonne, ligne;
		IntPair(int colonne, int ligne){
			this.colonne = colonne;
			this.ligne = ligne;
		}
		/**
		 * Pour modifier le comportement de la méthode contains(IntPair intPair) pour la méthode isCaseClickedAvailable.
		 * Ceci permet de vérifier si les coordonnées de la case choisie par le joueur est bien dans la liste des cases disponibles pour le déplacement
		 */
		@Override
		public boolean equals(Object obj){
			if(this == obj) return true;
			if(obj == null || this.getClass() != obj.getClass()) return false;
			IntPair otherIntPair = (IntPair) obj;
			return this.colonne == otherIntPair.colonne && this.ligne == otherIntPair.ligne;
		}
	}
}
