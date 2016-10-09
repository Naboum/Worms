package game.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.forme.Color;
import joueur.Personnage;

/**
 * Thread à objectif purement visuel, met en évidence les personnages disponibles en les faisant clignoter
 *
 */
public class ChoicePersonnageHighLighterThread extends Thread {
	Drawing d;
	ChoicePersonnageListener listener;
	GameManager gameManager;
	
	public ChoicePersonnageHighLighterThread(GameManager gm, Drawing d, ChoicePersonnageListener listener){
		this.gameManager = gm;
		this.d = d;
		this.listener = listener;
	}
	
	@Override
	public void run(){
		
		List<Personnage> listePersonnge = listener.getListePersonnage();

		Personnage p;
		Color secondaryColor = Color.white;
		
		Integer tailleCarre = gameManager.getMapDrawer().getTailleCarre();
		Integer offSetY = gameManager.getMapDrawer().getOffSetY();
		
		Map<Personnage, Color> map = new HashMap<Personnage, Color>();
		
		while(true){
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// on dessine les personnages du joueur avec de nouvelles couleurs
			for(int i = 0; i != listePersonnge.size(); i++){
				p = listePersonnge.get(i);
				
				if(map.containsKey(p)){
					if(map.get(p) == p.getColor()){
						map.put(p, Color.white);
					}
					else if (map.get(p) == Color.white){
						map.put(p, p.getColor());
					}
				}
				else{
					map.put(p, p.getColor());
				}
				
				p.drawWithDifferentColor(d, p.getColonne()*tailleCarre, (p.getLigne()*tailleCarre)+offSetY, map.get(p), tailleCarre);
				gameManager.getMapDrawer().createGrid();
			}
			
			// les couleurs successives
			if(secondaryColor.getNom() == "Blanc"){
				secondaryColor = Color.black;
			}
			else if(secondaryColor.getNom() == "Noir"){
				secondaryColor = Color.white;
			}
			
			// si un personnage a été choisi, on réinitialise les cases et on stop tout
			if(listener.isPersonnageFound()){
				resetColors();
				this.interrupt();
				return;
			}
		}
	}
	
	/**
	 * Redessine les personnages avec la bonne couleur
	 */
	public void resetColors(){
		
		List<Personnage> listePersonnge = listener.getListePersonnage();
		Personnage p;
		
		Integer tailleCarre = gameManager.getMapDrawer().getTailleCarre();
		Integer offSetY = gameManager.getMapDrawer().getOffSetY();
		
		for(int i = 0; i != listePersonnge.size(); i++){
			p = listePersonnge.get(i);
			p.draw(d, p.getColonne()*tailleCarre, (p.getLigne()*tailleCarre)+offSetY, tailleCarre);
			
			// on recrée le quadrillage
			gameManager.getMapDrawer().createGrid();
		}
	}
	
	
	
}
