	package arme;

import joueur.Personnage;

/**
 * Permet de transformer un personnage ennemi en allié
 * @author Ghislain, Nabil
 *
 */
public class Switcher extends Arme {
	
	private static final String name = "Switcher";
	private static final Double precision = 0.5;
	private static final Integer munition = 1; 
	
	public Switcher() {
		super(name, precision, munition);
	}

	@Override
	public void manageEffect(Personnage personnage1, Personnage personnage2, boolean onHit) {
		//TODO
		personnage2.setJoueur(personnage1.getJoueur());
		personnage1.getJoueur().getListePersonnage().add(personnage2);
	}

	@Override
	public boolean noClip() {
		return false;
	}

}
