package graphisme;

import arme.Arme;
import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.forme.Color;
import joueur.Personnage;

public class Eau implements Drawer{

	@Override
	public void draw(Drawing d, int x, int y, int tailleCarre) {
		Color colorEau = MapDrawer.colorEau;
		d.setColor(colorEau.r, colorEau.g, colorEau.b);
		d.rect(x, y, tailleCarre, tailleCarre);
	}
	
	@Override
	public boolean isReplaceable() {
		return false;
	}
	
	@Override
	public boolean canStopProjectile() {
		return false;
	}

	@Override
	public void manageArmeEffect(Arme arme, Personnage ennemi, boolean onHit, GameManager gameManager) {
	}

}

