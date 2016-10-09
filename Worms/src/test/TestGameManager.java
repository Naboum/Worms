package test;

import game.GameManager;
import graphisme.forme.Color;
import joueur.Joueur;
import joueur.Personnage;

public class TestGameManager {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		Personnage J1P1 = new Personnage("Joueur 1 Perso 1", 100, 0, 4, 4, Color.orange);
		Personnage J1P2 = new Personnage("Joueur 1 Perso 2", 120, 0, 2, 3, Color.red);
		Personnage J2P1 = new Personnage("Joueur 2 Perso 1", 125, 0, 3, 2, Color.purple);
		Personnage J3P1 = new Personnage("Joueur 3 Perso 1", 110, 0, 5, 6, Color.yellow);
		
		Joueur J1 = new Joueur("Joueur 1", J1P1, J1P2);
		Joueur J2 = new Joueur("Joueur 2", J2P1);
		Joueur J3 = new Joueur("Joueur 3", J3P1);
	
		GameManager game = new GameManager("Mon Jeu", "map/map1.csv", 50, J1, J2, J3);

	}

}
