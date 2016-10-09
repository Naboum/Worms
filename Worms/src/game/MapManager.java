package game;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;

import graphisme.Drawer;
import graphisme.Eau;
import graphisme.Mur;
import graphisme.Sol;
import joueur.Personnage;

/**
 * 
 * Classe qui cr�e la matrice � partir du fichier CSV repr�sentant le carte dans laquelle on veut jouer
 *
 */
public class MapManager {
	
	private Integer ligne;
	private Integer colonne;
	
	// taille des carr�s repr�sentant une position sur la map
	private Integer tailleCarre;
	
	// fichier csv d'o� est r�cup�r� la map
	private String csvFile;
	
	// repr�sentation logique de la map graphique
	private Drawer[][] map;
	
	// s�parateur du fichier csv, par d�faut un point-virgule
	private char separator = ';';
	
	private GameManager gameManager;
	
	public MapManager(GameManager gm, Integer tailleCarre, String csvFile) throws ClassNotFoundException, IOException {
		this.tailleCarre = tailleCarre;
		this.csvFile = csvFile;
		this.gameManager = gm;
		this.initializeGrid();
	}
	
	/**
	 * Associe la matrice avec le fichier CSV
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private boolean initializeGrid() throws IOException, ClassNotFoundException {
		int ligne = 0;
		int colonne = 0;
		String[] nextLine;
		Drawer drawer;
		
		// on cr�e le reader sur le fichier CSV avec le s�parateur
		CSVReader reader = new CSVReader(new FileReader(this.csvFile), this.getSeparator());
		
		// on r�cup�re tout le fichier dans une liste de tableau de string
		List<String[]> listCSV = reader.readAll();
		
		// on r�cup�re le nombre de lignes et de colonnes
		this.setLigne(listCSV.size());
		this.setColonne(listCSV.get(0).length);
		
		// on initialise le tableau map avec les valeurs ci-dessus
		this.map = new Drawer[this.getLigne()][this.getColonne()];
		
		// on cr�e un iterateur sur la liste
		Iterator<String[]> iteratorOverListCSV = listCSV.iterator();
		
		// tant que l'iterateur n'a pas boucl� sur toute la liste
		while(iteratorOverListCSV.hasNext()){
			
			// on r�cup�re la prochaine ligne
			nextLine = iteratorOverListCSV.next();
			
			// pour chaque valeur dans la ligne 
			for(String objectName : nextLine){
				
				// on cr�e une objet impl�mentant l'interface Drawer � partir de la valeur r�cup�r�e
				drawer = this.createDrawerFromString(objectName);
				
				// on l'ins�re dans le tableau map
				this.setPosition(ligne, colonne, drawer);
				colonne++;
			}
			
			colonne = 0;
			ligne++;
		}
		
		reader.close();
		return true;
	}
	
	/**
	 * M�thode qui frabrique une classe impl�mentant l'interface Drawer � partir d'une String
	 * On retourne une classe impl�mentant Drawer afin de dessiner chaque element dans le tableau map apr�s
	 * @param objectName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Drawer createDrawerFromString(String objectName) throws ClassNotFoundException{
		
		String objectNameToLowerCase = objectName.toLowerCase();
		switch(objectNameToLowerCase){
			case "eau" :
				return new Eau();
			case "mur" :
				return new Mur();
			case "sol" :
				return new Sol();
			default :
				throw new ClassNotFoundException();
		}
	}
	
	/**
	 * Renvoit false si la place dans le tableau map est pris par une surface graphique (sol, eau, mur)
	 * Renvoit true si la place est d�j� prise par un element jouable et non superposable (personnage par exemple)
	 * @param ligne
	 * @param colonne
	 * @return
	 */
	public boolean isPlaceTaken(int ligne, int colonne){
		return this.getMap()[ligne][colonne].isReplaceable() ? false : true;
	}
	
	/**
	 * Met l'objet impl�mentant Drawer dans le tableau map
	 * @param ligne
	 * @param colonne
	 * @param drawer
	 */
	public void setPosition(int ligne, int colonne, Drawer drawer){
		map[ligne][colonne] = drawer;
	}
	
	/**
	 * R�cup�re l'objet � la position
	 * @param ligne
	 * @param colonne
	 * @return
	 */
	public Drawer getPosition(int ligne, int colonne){
		return map[ligne][colonne];
	}
	
	public void replacePosition(Personnage currentPersonnage, int ligne, int colonne) {
		map[currentPersonnage.getLigne()][currentPersonnage.getColonne()] = new Sol();
		map[ligne][colonne] = currentPersonnage;
		currentPersonnage.setPosition(ligne, colonne);
	}
	
	/**
	 * Permet de supprimer ce qu'il y aux coordonn�es, pour rajouter une case de Sol
	 * @param ligne
	 * @param colonne
	 */
	public void resetCase(int ligne, int colonne){
		map[ligne][colonne] = new Sol();
	}
	
	/**
	 * Renvoit true si les coordonn�es sont � l'int�rieur de la matrice, donc acceptables
	 * @param colonne
	 * @return
	 */
	public boolean isPositionInsideMap(int ligne, int colonne){
		if(ligne < 0 ) return false;
		else if (ligne >= this.ligne) return false;
		if(colonne < 0) return false;
		else if (colonne >= this.colonne) return false;
		return true;
	}
	
	public void createMap(){
		
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

	public Integer getTailleCarre() {
		return tailleCarre;
	}

	public String getCsvFile() {
		return csvFile;
	}

	public Drawer[][] getMap() {
		return map;
	}

	public char getSeparator() {
		return separator;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void showMap(){
		for(Drawer[] colonne : this.getMap()){
			for(Drawer drawer : colonne){
				 System.out.print(drawer.getClass());
			}
			System.out.println();
		}
	}
}
