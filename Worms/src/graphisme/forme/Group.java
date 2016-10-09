package graphisme.forme;

import java.util.ArrayList;
import java.util.List;

import fr.lacl.cpo.Drawing;

public class Group extends Forme{

	private List<Forme> formeList = new ArrayList<Forme>();
	
	public Group(String nom, Color c, int epaisseur) {
		super(nom, c, epaisseur);
	}
	
	public Group(String nom) {
		super(nom);
	}
	
	public Group add(Forme f){
		this.formeList.add(f);
		return this;
	}
	
	@Override
	public void draw(Drawing d){
		super.draw(d);
		this.formeList.forEach(x -> x.draw(d));
	}

}
