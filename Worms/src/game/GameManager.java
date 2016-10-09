package game;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fr.lacl.cpo.Drawing;
import game.listener.ChoicePersonnageListener;
import game.listener.DeplacementListener;
import game.listener.TirListener;
import graphisme.MapDrawer;
import joueur.Joueur;
import joueur.Personnage;


/**
 * Classe la plus importante. Utilise tous les composants principaux pour gérer le fonctionnement d'un tour.
 *
 */
public class GameManager {

	// gestion de la map, des calculs, et de l'aspect logique
	MapManager mapManager;
	
	// gestion de l'interface graphique et du visuel
	MapDrawer mapDrawer;
	
	// gestion des tours
	TurnManager turnManager;
	
	// personnage choisi
	Personnage currentPersonnage;
	
	
	public GameManager(String titleGame, String csvFile, int tailleCarre, Joueur... joueurs) {
		try {
			// on initialise la gestion des tours, des personnages et des joueurs
			this.turnManager = new TurnManager(this, joueurs);
			
			// on initialise la gestion de la map, matrice qui représente toutes les coordonnées possibles
			this.mapManager = new MapManager(this, tailleCarre, csvFile);
			
			// on rajoute tous les personnages dans la map, avec des positions aléatoires
			this.addPersonnageToMapAtRandomPosition();
			
			//on initialise l'interface graphique
			this.mapDrawer = new MapDrawer(titleGame, tailleCarre, getMapManager().getLigne(), getMapManager().getColonne(), mapManager.getMap());
			
			choosePersonnageForNextJoueur();
			
		} catch (ClassNotFoundException e) {
			System.out.println("Classe non trouvée durant le mapping : vérifiez le fichier csv.");
			return;
		} catch (IOException e) {
			System.out.println("Erreur lors de l'ouverture ou lecture du fichier CSV");
			return;
		} catch (PersonnageOnMultipleJoueurException e) {
			System.out.println("Erreur : un personnage appartient à plusieurs joueurs.");
			return;
		}
	}

	/**
	 * Fin d'un tour, passage au prochain joueur
	 */
	public void endTurn() {

		getTurnManager().nextTurn();
		
		if(turnManager.getCurrentElement() == turnManager.getCurrentElement().getNext()){
			System.out.println("La partie est terminée, le joueur '" + getTurnManager().getCurrentElement().getJoueur().getNom() + "' a gagné.");
		}
		
		choosePersonnageForNextJoueur();
	}
	
	/**
	 * Permet au joueur de choisir un personnage pour le tour
	 */
	public void choosePersonnageForNextJoueur(){

		if(turnManager.getCurrentElement().getJoueur().getListePersonnage().isEmpty()){
			turnManager.deleteCurrentJoueur();
		}
		

		// on récupère le joueur courant
		Joueur nextJoueur = turnManager.getCurrentElement().getJoueur();

		
		
		System.out.println("***************************************************************************");
		System.out.println("Tour de : " + getTurnManager().getCurrentElement().getJoueur().getNom());

		// ... et ses personnages
		List<Personnage> listePersonnage = nextJoueur.getListePersonnage();
				
		ChoicePersonnageListener choicePersonnagelistener = new ChoicePersonnageListener(this, getDrawing(), listePersonnage);
		// on ajoute un listener pour sublimer les personnages disponibles
		getDrawing().addMouseListener(choicePersonnagelistener);		
	}
	
	/**
	 * Setter qui récupère le personnage choisi par le joueur à partir du listener
	 * @param personnageChosen
	 * @param choiceListener
	 */
	public void setPersonnageOnTurn(Personnage personnageChosen, ChoicePersonnageListener choiceListener) {
		currentPersonnage = personnageChosen;
		getDrawing().removeMouseListener(choiceListener);
		makeDeplacements();
	}
	
	/**
	 * Méthode appelant un listener gérant les déplacements
	 */
	public void makeDeplacements(){
		clearConsole();
		DeplacementListener deplacementListener = new DeplacementListener(this, getDrawing(), currentPersonnage);
		getDrawing().addMouseListener(deplacementListener);
		deplacementListener = null;
		
	}
	
	/**
	 * Méthode appelant un listener gérant le tir
	 */
	public void makeShot() {
		clearConsole();
		getDrawing().addMouseListener(new TirListener(this, getDrawing(), currentPersonnage));
	}
	


	
	public static void clearConsole(){
		for (int i = 30; i != 0; i--) System.out.print('\n'); 
	}

	
	/**
	 * Méthode lancée au début du jeu, pour rajouter les personnages à des endroits aléatoires
	 */
	private void addPersonnageToMapAtRandomPosition(){
		
		Random probability = new Random();
		Personnage nextPersonnage;
		
		// les coordonnées pour placer le personnage
		int ligne;
		int colonne;
		
		// permet de savoir si une place a été trouvée pour un personnage
		boolean placeFound;
		
		// on récupère la liste + l'itérateur
		List<Personnage> listePersonnage = turnManager.getListePersonnage();
		Iterator<Personnage> iteratorOverListePersonnage = listePersonnage.iterator();

		while (iteratorOverListePersonnage.hasNext()){

			nextPersonnage = iteratorOverListePersonnage.next();
			placeFound = false;
			
			// tant qu'on ne trouve pas de place de libre
			while (!placeFound){
				
				ligne = probability.nextInt(mapManager.getLigne());
				colonne = probability.nextInt(mapManager.getColonne());
				
				// si la place n'est pas prise par un element non superposable (comme un autre personnage)
				if(!mapManager.isPlaceTaken(ligne, colonne)){
					mapManager.setPosition(ligne, colonne, nextPersonnage);
					nextPersonnage.setPosition(ligne, colonne);
					placeFound = true;
				}
			}
		}
	}


	public MapManager getMapManager() {
		return mapManager;
	}


	public TurnManager getTurnManager() {
		return turnManager;
	}
	
	public MapDrawer getMapDrawer(){
		return mapDrawer;
	}
	
	public Drawing getDrawing(){
		return getMapDrawer().getD();
	}




}
