package arme;

import joueur.Personnage;

/**
 * Classe représentant une grenade
 * @author Ghislain, Nabil
 *
 */
public class Grenade extends ArmeNormale {

	private static final String name = "Grenade";
	private static final Double precision = 0.4;
	private static final Integer damageOnHit = 50;
	private static final Integer damageOnArea = 20;
	private static final Integer munition = 5;

	public Grenade() {
		super(name, precision, damageOnHit, damageOnArea, munition);
	}

	@Override
	public void manageEffect(Personnage personnage1, Personnage personnage2, boolean onHit) {
		int lifeBefore = personnage2.getPointDeVie();
		
		if(onHit){
			personnage2.takeDamage(damageOnHit);
		}
		else{
			personnage2.takeDamage(damageOnArea);
		}
		
		int lifeAfter = personnage2.getPointDeVie();

		if(personnage2.isPersonnageAlive()){
			System.out.println("Le personnage '"+ personnage2.getNom() + "' de couleur " + personnage2.getColor() + " est touché par l'arme. "+
					(lifeBefore-lifeAfter) + " points de dégats infligés. Il lui reste " + lifeAfter + " points de vie.");
		}
	}

	@Override
	public boolean noClip() {
		return true;
	}
	

}
