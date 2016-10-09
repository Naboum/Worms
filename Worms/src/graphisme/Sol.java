package graphisme;

import arme.Arme;
import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.forme.Color;
import joueur.Personnage;

public class Sol implements Drawer{
	
	@Override
	public void draw(Drawing d, int x, int y, int tailleCarre) {
		Color colorSol = MapDrawer.colorSol;
		d.setColor(colorSol.r, colorSol.g, colorSol.b);
		d.rect(x, y, tailleCarre, tailleCarre);
	}
	
	@Override
	public boolean isReplaceable() {
		return true;
	}

	@Override
	public boolean canStopProjectile() {
		return false;
	}

	@Override
	public void manageArmeEffect(Arme arme, Personnage ennemi, boolean onHit, GameManager gameManager) {
	}

}
