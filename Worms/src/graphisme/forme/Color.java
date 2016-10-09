package graphisme.forme;

import fr.lacl.cpo.Drawing;

public class Color {
	public double r,g,b;
	private String nom;
	
	public Color(){
		this.r=1;
		this.g=1;
		this.b=1;
		this.nom = "Blanc";
	}
	
	public Color(String nom, double r,double g,double b){
		this.r=r;
		this.g=g;
		this.b=b;
		this.nom = nom;
	}
	
	public void apply(Drawing d){
		d.setColor(r,g,b);
	}
	
	public void blend(double r, Color mc){
		this.r = (1-r)*this.r + r*mc.r;
		this.g = (1-r)*this.g + r*mc.g;
		this.b = (1-r)*this.b + r*mc.b;
	}

	public static final Color black = new Color("Noir", 0,0,0);
	public static final Color red = new Color("Rouge", 1,0,0);
	public static final Color green = new Color("Vert", 0,1,0);
	public static final Color blue = new Color("Bleu", 0,0,1);
	public static final Color gray = new Color("Gris", 0.5,0.5,0.5);
	public static final Color white = new Color("Blanc", 1,1,1);
	public static final Color orange = new Color("Orange", 1, 0.4, 0);
	public static final Color yellow = new Color("Yellow", 1, 1, 0);
	public static final Color purple = new Color("Violet", 0.4, 0, 0.8);
	public static final Color turquoise = new Color("Turquoise", 0, 1, 0.6);

	@Override
	public String toString(){
		return nom;
	}

	public String getNom() {
		return nom;
	}
	
	

}
