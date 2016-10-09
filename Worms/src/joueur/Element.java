package joueur;

public class Element {
	
	// Element ou joueur précédent dans le tour
	private Element previous;
	
	// Element ou joueur suivant dans le tour
	private Element next;
	
	// joueur que représente cet element
	private Joueur joueur;

	public Element(){}
	
	public Element(Joueur joueur){
		this.joueur = joueur;
	}
	
	public Element(Element previous, Element next, Joueur joueur) {
		this.previous = previous;
		this.next = next;
		this.joueur = joueur;
	}

	public Element getPrevious() {
		return previous;
	}

	public void setPrevious(Element previous) {
		this.previous = previous;
	}

	public Element getNext() {
		return next;
	}

	public void setNext(Element next) {
		this.next = next;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setjoueur(Joueur joueur) {
		this.joueur = joueur;
	}
}
