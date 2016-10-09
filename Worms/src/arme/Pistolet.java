package arme;

import joueur.Personnage;

/**
 * Classe représentant un pistolet
 * @author Ghislain, Nabil
 *
 */
public class Pistolet extends ArmeNormale{
	
	private static final String name = "Pistolet";
	private static final Double precision = 0.8;
	private static final Integer damageOnHit = 20;
	private static final Integer damageOnArea = 0;
	private static final Integer munition = 20;
	
	public Pistolet() {
		super(name, precision, damageOnHit, damageOnArea, munition);
	}

	@Override
	public void manageEffect(Personnage personnage1, Personnage personnage2, boolean onHit) {
		
		int lifeBefore = personnage2.getPointDeVie();
		
		if(onHit){
			personnage2.takeDamage(damageOnHit);
			int lifeAfter = personnage2.getPointDeVie();
			
			if(personnage2.isPersonnageAlive()){
				System.out.println("Le personnage '"+ personnage2.getNom() + "' de couleur " + personnage2.getColor() + " est touché par l'arme. "+
						(lifeBefore-lifeAfter) + " points de dégats infligés. Il lui reste " + lifeAfter + " points de vie.");
			}
		}
	}

	@Override
	public boolean noClip() {
		return false;
	}

}











