package joueur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Joueur physique (la classe Joueur représente une personne)
 */
public class Joueur {
	
	private String nom;
	
	// liste des personnages du joueur
	private List<Personnage> listePersonnage;
	
	public Joueur(String nom, Personnage...personnages){
		this.nom = nom;
		this.listePersonnage = new ArrayList<Personnage>(Arrays.asList(personnages));
		this.setJoueurForEachPersonnage();
	}
	
	/**
	 * Assigne la propriété joueur pour chaque personnage
	 */
	public void setJoueurForEachPersonnage(){
		Iterator<Personnage> iteratorListePersonne = this.getListePersonnage().iterator();
		while(iteratorListePersonne.hasNext()){
			iteratorListePersonne.next().setJoueur(this);
		}
	}

	public String getNom() {
		return nom;
	}

	public List<Personnage> getListePersonnage() {
		return listePersonnage;
	}

	public void setListePersonnage(List<Personnage> listePersonnage) {
		this.listePersonnage = listePersonnage;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Override
	public String toString(){
		return this.nom;
	}

}
