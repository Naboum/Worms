package arme;

import java.util.Random;

import joueur.Personnage;

/**
 * 
 *Classe basique pour les armes utilisables
 *
 */
public abstract class Arme {
	
	// nom de l'arme
	protected String name;
	
	// precision de l'arme, représente un pourcentage
	protected Double precision;
	
	// nombre de munitions
	protected Integer munition;
	
	protected Arme(String name, Double precision, Integer munition){
		this.name = name;
		this.precision = precision;
		this.munition = munition;
	}
	
	// Gère l'effet de l'arme lorsqu'utilisée
	public abstract void manageEffect(Personnage personnage1, Personnage personnage2, boolean onHit);

	public String getName() {
		return name;
	}

	public Double getPrecision() {
		return precision;
	}

	public Integer getMunition() {
		return munition;
	}
	
	public void setMunition(Integer munition) {
		this.munition = munition;
	}
	
	/**
	 * Enleve une munition
	 */
	public void removeOneMunition(){
		// On enleve une munition seulement si il en reste
		if(this.getMunition() > 0){
			int munitionBuffer = this.getMunition() - 1;
			this.setMunition(munitionBuffer);
		}
	}
	
	/**
	 * Ajoute des munitions
	 * @param munition
	 */
	public void addMunition(int munition){
		int munitionBuffer = this.getMunition() + munition;
		this.setMunition(munitionBuffer);
	}
	
	/**
	 * Calcule si le projectile touche sa cible ou pas, en fonction de la precision
	 * @return boolean
	 */
	public boolean testPrecision(){
		Random probability = new Random();
		
		// on prend un random entre 0 et 1
		double randomNumber = probability.nextDouble();
		
		// si le random est inférieure à la valeure de précision de l'arme, c'est que le tir a touché la bonne case
		if(randomNumber <= this.getPrecision()) return true;
		
		else return false;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(obj.getClass() == this.getClass()) return true;
		return false;
	}
	
	public abstract boolean noClip();
}
