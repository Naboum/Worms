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
 * Listener pour g�rer les tirs
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
	 * Gestion g�n�ral du tir
	 * @param e
	 */
	private void manageTir(MouseEvent e) {
		
		// si il n'a plus de munition, le tir est annul�
		if(currentArme.getMunition() <= 0){
			System.out.println("Vous n'avez plus de munitions sur cette arme.");
			return;
		}
		
		// si il rate la case, le tir est annul�
		if(!currentArme.testPrecision()){
			System.out.println("Vous avez rat� le tir !");
			currentArme.removeOneMunition();
			terminate();
			return;
		}
		
		// colonne et ligne de la case du personnage
		IntPair coordPersonnage = new IntPair(currentPersonnage.getColonne(), currentPersonnage.getLigne());
		
		// coordonn�es graphiques centr�es du personnage 
		IntPair coordPersonnageCenter = new IntPair(
				(currentPersonnage.getColonne()*tailleCarre) + tailleCarre/2, 
				(currentPersonnage.getLigne()*tailleCarre) + gameManager.getMapDrawer().getOffSetY() + tailleCarre/2);
		
		// colonne et ligne de la case choisie pour le tir
		IntPair coordTir = new IntPair(e.getX()/tailleCarre, (e.getY()-offSetY)/tailleCarre);

		// coordonn�es graphiques centr�es de la case choisie pour le tir
		IntPair coordTirCenter = new IntPair(coordTir.x*tailleCarre+tailleCarre/2, coordTir.y*tailleCarre+tailleCarre/2 + gameManager.getMapDrawer().getOffSetY());
		
		// coordTir = managePrecision(coordTir);
		
		// r�cup�re la liste des cases touch�es par le projectile
		List<IntPair> trajectory = getTrajectory(coordPersonnage, coordTir, coordPersonnageCenter, coordTirCenter);
		
		if(trajectory == null || trajectory.size() == 0) return;
		
		//trajectory.forEach(x -> System.out.println(x));

		// si l'arme (comme une grenade) ne permet pas de passer � travers les objets
		if(!currentArme.noClip()){
			
			// r�cup�re la vraie case touch�e, c'est � dire en prenant en compte les murs et personnages qu'auraient pu toucher le projectile
			coordTir = getRealCoordTir(trajectory);
			coordTirCenter = new IntPair(coordTir.x*tailleCarre+tailleCarre/2, coordTir.y*tailleCarre+tailleCarre/2 + gameManager.getMapDrawer().getOffSetY());

		}
		
		// gestion des d�gats
		manageDamage(coordTir);
		
		// on enl�ve une balle � l'arme
		currentArme.removeOneMunition();
		
		//fin du tir
		terminate();
		return;
	}



	/**
	 * Inflige les d�gats pour chaque personnage touch� par l'impact
	 * @param coordTir
	 */
	private void manageDamage(IntPair coordTir) {
		
		// permet de savoir si la case o� on calcule les d�gats a �t� touch�e directement par l'arme ou bien est autour de celle ci
		boolean onHit = false;
		
		// on boucle tout autour de la case touch�e
		for(int ligne =  coordTir.y-1; ligne != coordTir.y+2; ligne++){
			for(int colonne = coordTir.x-1; colonne != coordTir.x+2; colonne++){
				
				// si la coordonn�e existe bien dans la matrice
				if(gameManager.getMapManager().isPositionInsideMap(ligne, colonne)){
					
					// si on est au milieu du carr� de la zone d'effet, alors on est sur la case touch�e directement par l'arme
					if(ligne == coordTir.y && colonne == coordTir.x)
						onHit = true;
					else 
						onHit = false;
						
					// on r�cup�re le drawer de la case � tester
					Drawer drawer = gameManager.getMapManager().getPosition(ligne, colonne);
					
					// on g�re l'effet de l'arme et si le personnage doit mourir ou pas
					drawer.manageArmeEffect(currentArme, currentPersonnage, onHit, gameManager);
				}
			}
		}
		
		gameManager.getMapDrawer().refreshMap(gameManager.getMapManager().getMap());
	}

	/**
	 * R�cup�re la premi�re case qui contient un mur ou un personnage (ou toute case bloquante) dans la liste des cases travers�es par le projectile
	 * Ceci afin d'avoir la vrai case o� se situe le point d'impact
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
	 * R�cup�re la balistique du projectile, c'est � dire l'ensemble des cases touch�es par celui-ci
	 * @param coordPersonnage
	 * @param coordTir
	 * @param graphCoordPersonnage
	 * @param graphCoordTir
	 * @return
	 */
	private List<IntPair> getTrajectory(IntPair coordPersonnage, IntPair coordTir, IntPair graphCoordPersonnage, IntPair graphCoordTir) {
		
		Integer start = 0;
		Integer end = 0;
		
		// boolean pour v�rifier si l'on doit inverser la liste, car il faut toujours qu'elle soit dans le sens o� va le projectile
		boolean reverseListe = false;
		
		List<IntPair> trajectory = new ArrayList<IntPair>();
		
		
		if(coordPersonnage.x == coordTir.x && coordPersonnage.y == coordTir.y){
			System.out.println("Vous ne pouvez pas vous tirer dessus !");
		}
		// si la case touch�e est sur la m�me colonne que le personnage
		else if(coordPersonnage.x == coordTir.x && coordPersonnage.y != coordTir.y){
			
			// test pour s'assurer que la boucle for prendra les cases de bas en haut
			// donc on v�rifie qui des deux cases (du personnage et de la case touch�e) est en haut/bas
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
		// si la case touch�e est sur la m�me ligne que le personnage
		else if(coordPersonnage.x != coordTir.x && coordPersonnage.y == coordTir.y){
			
			// test pour s'assurer que la boucle for prendra les cases de gauche � droite
			// donc on v�rifie qui des deux cases (du personnage et de la case touch�e) est � gauche/droite
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
		// si les deux cases ne sont ni sur la m�me ligne ni sur la m�me
		else if (coordPersonnage.x != coordTir.x && coordPersonnage.y != coordTir.y){
			
			// on r�cup�re l'�quation form�e par les deux points
			LinearFunction linearFunction = new LinearFunction(graphCoordPersonnage.x, graphCoordPersonnage.y, graphCoordTir.x, graphCoordTir.y);
			
			// valeur d'incr�mentation pour la boucle
			Integer step = 1;
			
			// test pour s'assurer que la boucle for prendra les cases de gauche � droite
			// donc on v�rifie qui des deux cases (du personnages et de la case touch�e) est � gauche/droite
			if(coordPersonnage.x > coordTir.x){
				start = graphCoordTir.x;
				end = graphCoordPersonnage.x;
				reverseListe = true;
			}
			else if (coordPersonnage.x < coordTir.x){
				start = graphCoordPersonnage.x;
				end = graphCoordTir.x;
			}
			
			// si le coefficient directeur est -1, c'est � dire une diagonale, on modifie l'incr�mentation
			// car au croisement des cases, la boucle aurait rajout� les cases adjacentes (ce qui rendrait la balistique �ronn�e)
			if(linearFunction.getA() == (double)-1){
				step = tailleCarre;
			}
			
			for(int i = start; i <= end; i+= step){

				// on r�cup�re les coordonn�es graphiques brutes, puis on les transforme en coordonn�es logiques de la matrice
				IntPair graphCoord = new IntPair(i, linearFunction.getYFromXInInteger(i));
				IntPair matrixCoord = fromGraphToMatrix(graphCoord);

				// si on n'a toujours pas rajout� la case et qu'elle est diff�rente de celle du personnage
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
	 * Reset l'it�rateur de l'arme afin de boucler � l'infini sur la liste d'armes
	 */
	private void resetIteratorArme(){
		iteratorOverArme = currentPersonnage.getListArme().iterator();
	}
	
	
	/**
	 * Transforme les coordonn�es brutes venant du clic souris en coordonn�es utilisables sur la matrice de la map
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
	 * Appel� lorsque la gestion du tir est finie
	 */
	private void terminate(){
		d.removeMouseListener(this);
		gameManager.endTurn();
	}

	/**
	 * Repr�sente une coordonn�e de la matrice ou du graphique, ou une simple paire de int
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
