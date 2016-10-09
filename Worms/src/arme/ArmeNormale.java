package arme;

public abstract class ArmeNormale extends Arme{

	// d�gats sur la cible
	protected Integer damageOnHit;
	// d�gats aux alentours
	protected Integer damageOnArea;
	
	protected ArmeNormale(String name, double precision, int damageOnHit, int damageOnArea, int munition){
		super(name, precision, munition);
		this.damageOnHit = damageOnHit;
		this.damageOnArea = damageOnArea;
	}

	public Integer getDamageOnHit() {
		return damageOnHit;
	}

	public Integer getDamageOnArea() {
		return damageOnArea;
	}
}
