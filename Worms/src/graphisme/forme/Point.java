package graphisme.forme;

import fr.lacl.cpo.Drawing;

public class Point extends Forme {

	private int x;
	private int y;
	
	public Point(String nom, Color c, int epaisseur, int x, int y) {
		super(nom, c, epaisseur);
		this.x = x;
		this.y = y;
	}
	
	public Point(String nom, int x, int y) {
		super(nom);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void draw(Drawing d){
		super.draw(d);
		d.point(x, y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
