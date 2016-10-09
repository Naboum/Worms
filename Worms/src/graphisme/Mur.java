package graphisme;

import arme.Arme;
import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.forme.Color;
import joueur.Personnage;

public class Mur implements Drawer{

	@Override
	public void draw(Drawing d, int x, int y, int tailleCarre) {
		Color colorMur = MapDrawer.colorMur;
		d.setColor(colorMur.r, colorMur.g, colorMur.b);
		d.rect(x, y, tailleCarre, tailleCarre);
	}
	
	@Override
	public boolean isReplaceable() {
		return false;
	}
	
	@Override
	public boolean canStopProjectile() {
		return true;
	}

	@Override
	public void manageArmeEffect(Arme arme, Personnage ennemi, boolean onHit, GameManager gameManager) {

	}
}
