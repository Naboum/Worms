package game;

/**
 * 
 * Exception lanc�e si un personnage se trouve sur plusieurs joueurs
 */
@SuppressWarnings("serial")
public class PersonnageOnMultipleJoueurException extends Exception{
	
	public PersonnageOnMultipleJoueurException(){}
	public PersonnageOnMultipleJoueurException(String message){
		super(message);
	}

}
