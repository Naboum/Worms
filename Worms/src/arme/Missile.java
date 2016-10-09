package arme;

import joueur.Personnage;

/**
 * Classe représentant un lance-missile
 * @author Ghislain, Nabil
 *
 */
public class Missile extends ArmeNormale{

	private static final String name = "Missile";
	private static final Double precision = 0.3;
	private static final Integer damageOnHit = 60;
	private static final Integer damageOnArea = 20;
	private static final Integer munition = 3;

	
	public Missile() {
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
		return false;
	}


}
