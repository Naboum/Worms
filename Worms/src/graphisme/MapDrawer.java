package graphisme;


import fr.lacl.cpo.Drawing;
import graphisme.forme.Color;
import graphisme.forme.Line;

/**
 * 
 * Classe qui gère l'affichage de la fenêtre de jeu
 */
public class MapDrawer{

	private Drawing d;
	private Integer ligne;
	private Integer colonne;
	private String title;
	private Integer totalSizeX;
	private Integer totalSizeY;
	
	// constante pour éviter que la 1ère ligne ne soit cachée par la barre windows de la fenêtre
	public Integer offSetY = 22;
	
	public Integer tailleCarre;
	public static Color colorLigne = Color.white;
	public static Integer epaisseurLigne = 2;
	public static Color colorEau = Color.blue;
	public static Color colorSol = Color.green;
	public static Color colorMur = Color.gray;
	public static Color highLightCase = Color.turquoise;
	
	public MapDrawer(String title, Integer tailleCarre, Integer ligne, Integer colonne, Drawer[][] map) {
		this.title = title;
		this.ligne = ligne;
		this.colonne = colonne;
		this.totalSizeX = tailleCarre*colonne;
		this.totalSizeY = tailleCarre*ligne;
		this.tailleCarre = tailleCarre;

		createGUI();
		refreshMap(map);
	}
	
	/**
	 * Rafraichit l'interface avec la matrice de MapManager
	 * @param map
	 */
	public void refreshMap(Drawer[][] map){	
		for(int i = 0; i != map.length ; i++){
			for(int j = 0; j != map[i].length; j++){
				
				// on boucle sur chaque objet dans la map et on lance la méthode draw() qu'ils ont en commun
				map[i][j].draw(d, j*tailleCarre, (i*tailleCarre)+offSetY, tailleCarre);
			}
		}
		
		// on appelle la méthode pour quadriller la map à la fin
		createGrid();
	}
	
	
	/**
	 * Crée l'interface graphique
	 */
	public void createGUI(){
		this.d = new Drawing(title, totalSizeX, totalSizeY+offSetY);
	}
	
	/**
	 * Création du quadrillage
	 */
	public void createGrid(){
		
		int x = tailleCarre;
		int y = tailleCarre;
		boolean colonneDrawn = false;
		boolean ligneDrawn = false;
		
		// tant que les colonnes et lignes ne sont pas toutes créées
		while (!colonneDrawn && !ligneDrawn){
			
			if(x <= totalSizeX){
				drawLine(x, 0, x, totalSizeY + offSetY);
				x += tailleCarre;
			}
			else{
				ligneDrawn = true;
			}
			
			if(y <= totalSizeY){
				drawLine(0, y + offSetY, totalSizeX, y + offSetY);
				y += tailleCarre;
			}
			else{
				colonneDrawn = true;
			}
		}
	}
	
	/**
	 * Construit une ligne (pour le quadrillage)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawLine(int x1, int y1, int x2, int y2){
		Line newLine = new Line("", colorLigne, epaisseurLigne, x1, y1, x2, y2);
		newLine.draw(d);
	}

	public Drawing getD() {
		return d;
	}

	public void setD(Drawing d) {
		this.d = d;
	}

	public Integer getOffSetY() {
		return offSetY;
	}

	public Integer getTailleCarre() {
		return tailleCarre;
	}

	public Integer getLigne() {
		return ligne;
	}

	public void setLigne(Integer ligne) {
		this.ligne = ligne;
	}

	public Integer getColonne() {
		return colonne;
	}

	public void setColonne(Integer colonne) {
		this.colonne = colonne;
	}
	
	
	
	
	
}
