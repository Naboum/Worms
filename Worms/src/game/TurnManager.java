package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import joueur.Element;
import joueur.Joueur;
import joueur.Personnage;

/**
 * Classe g�rant l'ordre des tours
 *
 */
public class TurnManager {
	
	// liste des joueurs dans le jeu
	private List<Joueur> listeJoueur;
	
	// liste des personnages dans le jeu
	private List<Personnage> listePersonnage;
	
	// joueur courant : c'est � son tour
	private Element currentElement;
	
	GameManager gameManager;
	
	public TurnManager(GameManager gm, Joueur... joueurs) throws PersonnageOnMultipleJoueurException{
		this.gameManager = gm;
		this.listeJoueur = new ArrayList<Joueur>(Arrays.asList(joueurs));
		this.listePersonnage = new ArrayList<Personnage>();
		this.initializeTurnOrder(joueurs);
		this.addPersonnageToGame();
	}

	/**
	 * M�thode appel�e au d�but du jeu, pour cr�er la file repr�sentant un tour
	 * @param joueurs
	 */
	private void initializeTurnOrder(Joueur... joueurs){
		Element nextElementBuffer = new Element();
		Element previousElementBuffer = new Element();

		Iterator<Joueur> iteratorOnListeJoueur = this.getListeJoueur().iterator();
		
		// On met le premier joueur en element courant
		if(iteratorOnListeJoueur.hasNext()) {
			this.setCurrentElement(new Element(iteratorOnListeJoueur.next()));
			previousElementBuffer = this.getCurrentElement();
		}
		
		// On rajoute tous les joueurs � la suite
		while(iteratorOnListeJoueur.hasNext()){
			
			// On cr�e un nouvel element suivant dont le joueur est le prochain de liste
			nextElementBuffer = this.addJoueurToTurnManager(iteratorOnListeJoueur.next(), previousElementBuffer);
			
			// Le suivant devient le pr�c�dent � la prochaine it�ration
			previousElementBuffer = nextElementBuffer;
		}
		
		// On mord la queue du serpent : on set le dernier element cr�e juste derriere le premier (element courant)
		previousElementBuffer.setNext(this.getCurrentElement());
		this.getCurrentElement().setPrevious(previousElementBuffer);
	}
	
	/**
	 * Sous m�thode de initializeTurnOrder() pour rajouter un element
	 * @param personnage
	 */
	private Element addJoueurToTurnManager (Joueur joueur, Element previousElement){
		
		Element nextElement = new Element(joueur);
		previousElement.setNext(nextElement);
		nextElement.setPrevious(previousElement);
		
		return nextElement;
	}
	
	/**
	 * Ajoute tous les personnages dans une liste
	 * @throws PersonnageOnMultipleJoueurException 
	 */
	private void addPersonnageToGame() throws PersonnageOnMultipleJoueurException {
		
		// liste de personnage venant d'un joueur
		List<Personnage> listePersonnageFromJoueur;
		
		// liste venant de TurnManager, qui repr�sente la totalit� des personnages
		List<Personnage> listePersonnageFromTurnManager = this.getListePersonnage();
		
		Iterator<Joueur> iteratorOverListeJoueur = this.getListeJoueur().iterator();
		Iterator<Personnage> iteratorOverListePersonnage;
	
		Personnage personnage;
	
		// on boucle sur la liste de joueurs
		while(iteratorOverListeJoueur.hasNext()){
			
			// on r�cup�re la liste des personnages du joueur suivant
			listePersonnageFromJoueur = iteratorOverListeJoueur.next().getListePersonnage();
			iteratorOverListePersonnage = listePersonnageFromJoueur.iterator();
			
			// on boucle sur la liste de personnages du joueur
			while(iteratorOverListePersonnage.hasNext()){
				
				// on r�cup�re le personnage suivant
				personnage = iteratorOverListePersonnage.next();
				
				// si le personnage n'existe pas on l'ajoute dans la liste
				if(!listePersonnageFromTurnManager.contains(personnage)){
					listePersonnageFromTurnManager.add(personnage);
				}
				// si il existe d�j�, il y a une erreur
				else {
					throw new PersonnageOnMultipleJoueurException();	
				}
			}
		}
	}

	/**
	 * Suppression de l'element courant dans la file
	 */
	public void deleteCurrentJoueur(){
		Element elementPreviousToCurrent = this.getCurrentElement().getPrevious();
		Element elementNextToCurrent = this.getCurrentElement().getNext();
		
		elementPreviousToCurrent.setNext(elementNextToCurrent);
		elementNextToCurrent.setPrevious(elementPreviousToCurrent);
		
		// l'element suivant devient l'element courant (on passe au suivant)
		this.setCurrentElement(this.getCurrentElement().getNext());
	}
	
	/**
	 * Suppression du joueur en param�tre
	 * @param joueur
	 */
	public void deleteJoueur(Joueur joueur){
		
		// si c'est l'element courant, on le supprime directement sans m�me le chercher
		if(joueur == this.getCurrentElement().getJoueur()){
			this.deleteCurrentJoueur();
			return;
		}
		
		// on commence au joueur courant
		Element elementToFind = this.getCurrentElement();
		Joueur joueurToFind = elementToFind.getJoueur();
		boolean joueurFound = false;
		
		do{
			// si ce n'est toujours pas le bon, on passe au suivant
			if(joueurToFind != joueur){
				elementToFind = elementToFind.getNext();
				joueurToFind = elementToFind.getJoueur();
			}
			else {
				joueurFound = true;
			}
		}
		// tant que le joueur n'est pas trouv� et que l'on ne refait pas une nouvelle boucle
		while(!joueurFound && joueurToFind != this.getCurrentElement().getJoueur());
		
		// si on l'a trouv�, on le supprime
		if(joueurFound){
			Element previousElement = elementToFind.getPrevious();
			Element nextElement = elementToFind.getNext();
			previousElement.setNext(nextElement);
			nextElement.setPrevious(previousElement);
		}
	}
	
	public List<Joueur> getListeJoueur() {
		return listeJoueur;
	}

	public List<Personnage> getListePersonnage() {
		return listePersonnage;
	}

	public Element getCurrentElement() {
		return currentElement;
	}

	public void setCurrentElement(Element currentElement) {
		this.currentElement = currentElement;
	}
	
	/**
	 * Affiche l'ordre dans lequel les tours seront jou�s
	 */
	public void showTurnOrder(){
		Element elementBuffer = this.getCurrentElement();
		do{
			System.out.println(elementBuffer.getPrevious().getJoueur().getNom() 
					+ " ----> "+ elementBuffer.getJoueur().getNom() 
					+ " ----> " + elementBuffer.getNext().getJoueur().getNom());
			elementBuffer = elementBuffer.getNext();
		} while(elementBuffer != this.getCurrentElement());
	}
	
	/*
	 * On passe au joueur suivant
	 */
	public void nextTurn(){
		this.setCurrentElement(this.getCurrentElement().getNext());
	}



}
