package game.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import arme.Arme;
import arme.Pistolet;
import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.Drawer;
import graphisme.LinearFunction;
import joueur.Personnage;

/**
 * Listener pour gérer les tirs
 *
 */
public class TirListener  extends MouseAdapter  {
	
	GameManager gameManager;
	Drawing d;
	Personnage currentPersonnage;
	Iterator<Arme> iteratorOverArme;
	Arme currentArme;

	Integer tailleCarre;
	Integer offSetY; 
	
	Drawing drawing;
	
	public TirListener (GameManager gm, Drawing d, Personnage currentPersonnage){
		this.gameManager = gm;
		this.d = d;
		this.currentPersonnage = currentPersonnage;
		this.tailleCarre = gameManager.getMapDrawer().getTailleCarre();
		this.offSetY = gameManager.getMapDrawer().getOffSetY();
		this.drawing = gameManager.getMapDrawer().getD();


		
		GameManager.clearConsole();
		
		initializeArme();
		
	}
	

	
	@Override
	public void mouseClicked(MouseEvent e){

		if(e.getButton() == 1){
			manageTir(e);
		}
		else if(e.getButton() == 2){
			nextArme();
		}
	}

	/**
	 * Gestion général du tir
	 * @param e
	 */
	private void manageTir(MouseEvent e) {
		
		// si il n'a plus de munition, le tir est annulé
		if(currentArme.getMunition() <= 0){
			System.out.println("Vous n'avez plus de munitions sur cette arme.");
			return;
		}
		
		// si il rate la case, le tir est annulé
		if(!currentArme.testPrecision()){
			System.out.println("Vous avez raté le tir !");
			currentArme.removeOneMunition();
			terminate();
			return;
		}
		
		// colonne et ligne de la case du personnage
		IntPair coordPersonnage = new IntPair(currentPersonnage.getColonne(), currentPersonnage.getLigne());
		
		// coordonnées graphiques centrées du personnage 
		IntPair coordPersonnageCenter = new IntPair(
				(currentPersonnage.getColonne()*tailleCarre) + tailleCarre/2, 
				(currentPersonnage.getLigne()*tailleCarre) + gameManager.getMapDrawer().getOffSetY() + tailleCarre/2);
		
		// colonne et ligne de la case choisie pour le tir
		IntPair coordTir = new IntPair(e.getX()/tailleCarre, (e.getY()-offSetY)/tailleCarre);

		// coordonnées graphiques centrées de la case choisie pour le tir
		IntPair coordTirCenter = new IntPair(coordTir.x*tailleCarre+tailleCarre/2, coordTir.y*tailleCarre+tailleCarre/2 + gameManager.getMapDrawer().getOffSetY());
		
		// coordTir = managePrecision(coordTir);
		
		// récupère la liste des cases touchées par le projectile
		List<IntPair> trajectory = getTrajectory(coordPersonnage, coordTir, coordPersonnageCenter, coordTirCenter);
		
		if(trajectory == null || trajectory.size() == 0) return;
		
		//trajectory.forEach(x -> System.out.println(x));

		// si l'arme (comme une grenade) ne permet pas de passer à travers les objets
		if(!currentArme.noClip()){
			
			// récupère la vraie case touchée, c'est à dire en prenant en compte les murs et personnages qu'auraient pu toucher le projectile
			coordTir = getRealCoordTir(trajectory);
			coordTirCenter = new IntPair(coordTir.x*tailleCarre+tailleCarre/2, coordTir.y*tailleCarre+tailleCarre/2 + gameManager.getMapDrawer().getOffSetY());

		}
		
		// gestion des dégats
		manageDamage(coordTir);
		
		// on enlève une balle à l'arme
		currentArme.removeOneMunition();
		
		//fin du tir
		terminate();
		return;
	}



	/**
	 * Inflige les dégats pour chaque personnage touché par l'impact
	 * @param coordTir
	 */
	private void manageDamage(IntPair coordTir) {
		
		// permet de savoir si la case où on calcule les dégats a été touchée directement par l'arme ou bien est autour de celle ci
		boolean onHit = false;
		
		// on boucle tout autour de la case touchée
		for(int ligne =  coordTir.y-1; ligne != coordTir.y+2; ligne++){
			for(int colonne = coordTir.x-1; colonne != coordTir.x+2; colonne++){
				
				// si la coordonnée existe bien dans la matrice
				if(gameManager.getMapManager().isPositionInsideMap(ligne, colonne)){
					
					// si on est au milieu du carré de la zone d'effet, alors on est sur la case touchée directement par l'arme
					if(ligne == coordTir.y && colonne == coordTir.x)
						onHit = true;
					else 
						onHit = false;
						
					// on récupère le drawer de la case à tester
					Drawer drawer = gameManager.getMapManager().getPosition(ligne, colonne);
					
					// on gère l'effet de l'arme et si le personnage doit mourir ou pas
					drawer.manageArmeEffect(currentArme, currentPersonnage, onHit, gameManager);
				}
			}
		}
		
		gameManager.getMapDrawer().refreshMap(gameManager.getMapManager().getMap());
	}

	/**
	 * Récupère la première case qui contient un mur ou un personnage (ou toute case bloquante) dans la liste des cases traversées par le projectile
	 * Ceci afin d'avoir la vrai case où se situe le point d'impact
	 * @param trajectory
	 * @return
	 */
	private IntPair getRealCoordTir(List<IntPair> trajectory) {
		
		IntPair nextCase = null;
		Drawer nextDrawerObject;
		
		Iterator<IntPair> iteratorOverTrajectory = trajectory.iterator();
		
		while(iteratorOverTrajectory.hasNext()){
			
			nextCase = iteratorOverTrajectory.next();
			nextDrawerObject = gameManager.getMapManager().getPosition(nextCase.y, nextCase.x);
			
			if(nextDrawerObject.canStopProjectile()){
				return nextCase;
			}
		}
		
		return nextCase;
	}



	/**
	 * Récupère la balistique du projectile, c'est à dire l'ensemble des cases touchées par celui-ci
	 * @param coordPersonnage
	 * @param coordTir
	 * @param graphCoordPersonnage
	 * @param graphCoordTir
	 * @return
	 */
	private List<IntPair> getTrajectory(IntPair coordPersonnage, IntPair coordTir, IntPair graphCoordPersonnage, IntPair graphCoordTir) {
		
		Integer start = 0;
		Integer end = 0;
		
		// boolean pour vérifier si l'on doit inverser la liste, car il faut toujours qu'elle soit dans le sens où va le projectile
		boolean reverseListe = false;
		
		List<IntPair> trajectory = new ArrayList<IntPair>();
		
		
		if(coordPersonnage.x == coordTir.x && coordPersonnage.y == coordTir.y){
			System.out.println("Vous ne pouvez pas vous tirer dessus !");
		}
		// si la case touchée est sur la même colonne que le personnage
		else if(coordPersonnage.x == coordTir.x && coordPersonnage.y != coordTir.y){
			
			// test pour s'assurer que la boucle for prendra les cases de bas en haut
			// donc on vérifie qui des deux cases (du personnage et de la case touchée) est en haut/bas
			if(coordPersonnage.y > coordTir.y){
				start = coordTir.y;
				end = coordPersonnage.y;
				reverseListe = true;
			}
			else {
				start = coordPersonnage.y + 1;
				end = coordTir.y;
			}
			
			for(int i = start; i <= end; i++) {
				
				IntPair newCase = new IntPair(coordPersonnage.x, i);
				if(!newCase.equals(coordPersonnage)){
					trajectory.add(newCase);
				}
			}
			
		}
		// si la case touchée est sur la même ligne que le personnage
		else if(coordPersonnage.x != coordTir.x && coordPersonnage.y == coordTir.y){
			
			// test pour s'assurer que la boucle for prendra les cases de gauche à droite
			// donc on vérifie qui des deux cases (du personnage et de la case touchée) est à gauche/droite
			if(coordPersonnage.x > coordTir.x){
				start = coordTir.x;
				end = coordPersonnage.x;
				reverseListe = true;
			}
			else{
				start = coordPersonnage.x + 1;
				end = coordTir.x;
			}
			
			for(int i = start; i <= end; i++) {
				
				IntPair newCase = new IntPair(i, coordPersonnage.y);
				if(!newCase.equals(coordPersonnage)){
					trajectory.add(newCase);
				}
			}
			
		}
		// si les deux cases ne sont ni sur la même ligne ni sur la même
		else if (coordPersonnage.x != coordTir.x && coordPersonnage.y != coordTir.y){
			
			// on récupère l'équation formée par les deux points
			LinearFunction linearFunction = new LinearFunction(graphCoordPersonnage.x, graphCoordPersonnage.y, graphCoordTir.x, graphCoordTir.y);
			
			// valeur d'incrémentation pour la boucle
			Integer step = 1;
			
			// test pour s'assurer que la boucle for prendra les cases de gauche à droite
			// donc on vérifie qui des deux cases (du personnages et de la case touchée) est à gauche/droite
			if(coordPersonnage.x > coordTir.x){
				start = graphCoordTir.x;
				end = graphCoordPersonnage.x;
				reverseListe = true;
			}
			else if (coordPersonnage.x < coordTir.x){
				start = graphCoordPersonnage.x;
				end = graphCoordTir.x;
			}
			
			// si le coefficient directeur est -1, c'est à dire une diagonale, on modifie l'incrémentation
			// car au croisement des cases, la boucle aurait rajouté les cases adjacentes (ce qui rendrait la balistique éronnée)
			if(linearFunction.getA() == (double)-1){
				step = tailleCarre;
			}
			
			for(int i = start; i <= end; i+= step){

				// on récupère les coordonnées graphiques brutes, puis on les transforme en coordonnées logiques de la matrice
				IntPair graphCoord = new IntPair(i, linearFunction.getYFromXInInteger(i));
				IntPair matrixCoord = fromGraphToMatrix(graphCoord);

				// si on n'a toujours pas rajouté la case et qu'elle est différente de celle du personnage
				if(!trajectory.contains(matrixCoord) && !matrixCoord.equals(coordPersonnage)) {
					trajectory.add(matrixCoord);
				}
			}
		}
		if(reverseListe) Collections.reverse(trajectory);
		return trajectory;
	}



	@Override
	public void mousePressed(MouseEvent e) {

	}
	
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	

	private void initializeArme() {
		if(currentPersonnage.getListArme().size() == 0){
			currentPersonnage.addArme(new Pistolet());
		}
		resetIteratorArme();
		currentArme = iteratorOverArme.next();
		clearConsole();
	}
	
	/**
	 * Remplace l'arme courante par la prochaine
	 */
	private void nextArme(){
		Arme nextArme;
		
		if(iteratorOverArme.hasNext()){
			 nextArme = iteratorOverArme.next();
			 currentArme = nextArme;
			 clearConsole();
		}
		else {
			resetIteratorArme();
			nextArme();
		}
	}
	
	/**
	 * Reset l'itérateur de l'arme afin de boucler à l'infini sur la liste d'armes
	 */
	private void resetIteratorArme(){
		iteratorOverArme = currentPersonnage.getListArme().iterator();
	}
	
	
	/**
	 * Transforme les coordonnées brutes venant du clic souris en coordonnées utilisables sur la matrice de la map
	 * @param intPair
	 * @return
	 */
	private IntPair fromGraphToMatrix(IntPair intPair){
		return new IntPair(intPair.x/tailleCarre, (intPair.y-offSetY)/tailleCarre);
	}
	

	private void clearConsole(){
		//GameManager.clearConsole();
		System.out.println("*******************************");
		System.out.println("Clic gauche sur une case : tir");
		System.out.println("Clic milieu : changement d'arme");
		System.out.println("*******************************");
		System.out.println("Liste des armes disponibles :");
		currentPersonnage.getListArme().forEach(x -> System.out.println("Arme : " + x.getName() + " | Munitions : " + x.getMunition()));
		System.out.println("*******************************");
		System.out.println("Arme actuelle : " + currentArme.getName() + " | Munitions : " + currentArme.getMunition());
		System.out.println("*******************************");
	}
	
	/**
	 * Appelé lorsque la gestion du tir est finie
	 */
	private void terminate(){
		d.removeMouseListener(this);
		gameManager.endTurn();
	}

	/**
	 * Représente une coordonnée de la matrice ou du graphique, ou une simple paire de int
	 *
	 */
	public class IntPair {
		public int x, y;
		IntPair(int x, int y){
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj){
			if(this == obj) return true;
			if(obj == null || this.getClass() != obj.getClass()) return false;
			IntPair otherIntPair = (IntPair) obj;
			return this.x == otherIntPair.x && this.y == otherIntPair.y;
		}
		
		@Override
		public String toString(){
			return "x : "+x+" | y : "+y;
		}
	}
	
}
