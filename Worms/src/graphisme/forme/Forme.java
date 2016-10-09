package graphisme.forme;

import fr.lacl.cpo.Drawing;

public abstract class Forme {

	private int epaisseur;
	private String nom;
	private Color c;
	
	public Forme(String nom, Color c, int epaisseur) {
		this.epaisseur = epaisseur;
		this.nom = nom;
		this.c = c;
	}
	
	public Forme(String nom) {
		this.epaisseur = 1;
		this.c = Color.white;
		this.nom = nom;
	}
	
	public void draw(Drawing d){
		d.setColor(this.c.r, this.c.g, this.c.b);
		d.setWidth(this.epaisseur);
	}
	
	public void register(Group g){
		g.add(this);
	}
	
	public int getEpaisseur() {
		return epaisseur;
	}
	public void setEpaisseur(int epaisseur) {
		this.epaisseur = epaisseur;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Color getC() {
		return c;
	}
	public void setC(Color c) {
		this.c = c;
	}
}
