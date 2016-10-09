package game.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import fr.lacl.cpo.Drawing;
import game.GameManager;
import joueur.Personnage;

/**
 * Listener qui met en évidence les personnages disponibles et récupère le personnage choisi
 *
 */
public class ChoicePersonnageListener extends MouseAdapter{
	
	private MouseEvent e;
	private Drawing d;
	private List<Personnage> listePersonnage;
	GameManager gameManager;
	private Integer ligne;
	private Integer colonne;
	ChoicePersonnageHighLighterThread thread;
	private boolean personnageFound = false;
	
	public ChoicePersonnageListener(GameManager gm, Drawing d, List<Personnage> listePersonnage){
		this.gameManager = gm;
		this.d = d;
		this.listePersonnage = listePersonnage;
		showAvailablePersonnage();
		
		// lance le thread qui permet de faire clignoter les personnages
		thread = new ChoicePersonnageHighLighterThread(gameManager, d, this);
		thread.start();
	}
	
	/**
	 * Liste tous les personnages dans le terminal
	 */
	public void showAvailablePersonnage(){
		System.out.println("Liste des personnages disponibles pour le tour :");
		listePersonnage.forEach(x -> System.out.println(x));
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		//System.out.println(e);
		if(e.getButton() == 1){
			
			// si le clique a bien été fait sur un personnage disponible, on passe aux déplacements (prochaine étape du tour)
			if(isMouseClickedOnAvailablePersonnage(e)){
				personnageFound = true;
				Personnage personnageChosen = (Personnage) gameManager.getMapManager().getPosition(ligne, colonne);
				System.out.println("Personnage choisi -> " + personnageChosen);
				
				// on supprime le listener et on set le personnage
				gameManager.setPersonnageOnTurn(personnageChosen, this);
				thread = null;
			}
			
			else{
				System.out.println("Cette case ("+ligne+"/"+colonne+") n'a pas de personnage ou contient un personnage n'étant pas à vous.");
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {

	}
	
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	

	public boolean isMouseClickedOnAvailablePersonnage(MouseEvent e){
		convertClick(e);
		return listePersonnage.contains(gameManager.getMapManager().getPosition(ligne, colonne)) ? true : false;
	}

	/**
	 * Transforme les coordonnées graphiques d'un clique en coordonnées logiques dans la matrice
	 * @param e
	 */
	public void convertClick(MouseEvent e){
		Integer tailleCarre = gameManager.getMapDrawer().getTailleCarre();
		Integer offSetY = gameManager.getMapDrawer().getOffSetY();
		colonne = e.getX()/tailleCarre;
		ligne = (e.getY()-offSetY)/tailleCarre;
	}

	public MouseEvent getE() {
		return e;
	}

	public Drawing getD() {
		return d;
	}

	public List<Personnage> getListePersonnage() {
		return listePersonnage;
	}

	public boolean isPersonnageFound() {
		return personnageFound;
	}
	
	

}
