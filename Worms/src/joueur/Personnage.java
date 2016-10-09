package joueur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import arme.Arme;
import arme.Grenade;
import arme.Missile;
import arme.Pistolet;
import fr.lacl.cpo.Drawing;
import game.GameManager;
import graphisme.Drawer;
import graphisme.Sol;
import graphisme.forme.Color;

/**
 * 
 * Class représentant un personnage, qui appartient à un joueur
 */
public class Personnage implements Drawer{
	
	// joueur auquel ce personnage appartient
	private Joueur joueur;
	
	// nom du personnage
	private String nom;
	
	// points de vie
	private Integer pointDeVie;
	
	// les dégats réduits suite à un tir ennemi
	private Integer damageReduction;
	
	// nombre d'armes disponibles maximum
	private Integer tailleSac;
	
	// nombre de déplacements maximum dans un tour
	private Integer nombreDeplacement;
	
	// liste d'armes
	private List<Arme> listeArme = new ArrayList<Arme>(Arrays.asList(
			new Pistolet(),
			new Grenade(),
			new Missile()
			));
	
	private Color color;
	
	private Integer ligne;
	private Integer colonne;
		
	public Personnage(String nom){
		this.nom = nom;
	}
	
	public Personnage(String nom, Integer pointDeVie, Integer damageReduction, Integer tailleSac, Integer nombreDeplacement, Color color) {
		this.nom = nom;
		this.pointDeVie = pointDeVie;
		this.damageReduction = damageReduction;
		this.tailleSac = tailleSac;
		this.nombreDeplacement = nombreDeplacement;
		this.color = color;
	}
	
	/**
	 * Calcule les dégats
	 * @param damage
	 */
	public void takeDamage(int damage){
		
		// les vrais dégats sont ceux calculés après la réduction
		int effectiveDamage = damage - this.getDamageReduction();
		
		setPointDeVie(getPointDeVie()-effectiveDamage);
	}

	@Override
	public void manageArmeEffect(Arme arme, Personnage ennemi, boolean onHit, GameManager gameManager) {
		
		if(arme.getMunition() <= 0){
			return;
		}
		
		arme.manageEffect(ennemi, this, onHit);

		if(!isPersonnageAlive()){
			joueur.getListePersonnage().remove(this);
			gameManager.getMapManager().resetCase(ligne, colonne);
			gameManager.getTurnManager().getListePersonnage().remove(this);
			System.out.println("Le personnage '"+ nom + "' est mort.");
		}
	}
	
	/**
	 * Renvoit true si il reste au personnage de la vie
	 * @return
	 */
	public boolean isPersonnageAlive() {
		return pointDeVie > 0 ? true : false ;
	}
	
	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public String getNom() {
		return nom;
	}
	
	public List<Arme> getListArme(){
		return listeArme;
	}

	public Integer getDamageReduction() {
		return damageReduction;
	}

	public Integer getTailleSac() {
		return tailleSac;
	}

	public Integer getNombreDeplacement() {
		return nombreDeplacement;
	}

	public Integer getPointDeVie() {
		return pointDeVie;
	}

	public void setPointDeVie(Integer pointDeVie) {
		this.pointDeVie = pointDeVie;
	}

	@Override
	public void draw(Drawing d, int colonne, int ligne, int tailleCarre) {
		Sol sol = new Sol();
		sol.draw(d, colonne, ligne, tailleCarre);
		d.setColor(color.r, color.g, color.b);
		d.rect(colonne + tailleCarre/4, ligne + tailleCarre/4,  tailleCarre/2, tailleCarre/2);
	}
	
	public void drawWithDifferentColor(Drawing d, int colonne, int ligne, Color c, int tailleCarre){
		Sol sol = new Sol();
		sol.draw(d, colonne, ligne, tailleCarre);
		d.setColor(c.r, c.g, c.b);
		d.rect(colonne + tailleCarre/4, ligne + tailleCarre/4,  tailleCarre/2, tailleCarre/2);
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}
	
	@Override
	public boolean canStopProjectile() {
		return true;
	}
	
	public void addArme(Arme arme){
		if(!listeArme.contains(arme)) listeArme.add(arme);
	}
	
	public void setPosition(int ligne, int colonne){
		this.ligne = ligne;
		this.colonne = colonne;
	}
	
	public void setColor(Color c){
		this.color = c;
	}
	
	
	public Color getColor() {
		return color;
	}

	public Integer getLigne() {
		return ligne;
	}

	public Integer getColonne() {
		return colonne;
	}
	
	@Override
	public String toString(){
		return this.nom + " | Vie : " + pointDeVie + " | Couleur : " + color + " | Deplacements : " + nombreDeplacement 
				+ " | ligne/colonne = "+getLigne()+"/"+getColonne();
	}




}
