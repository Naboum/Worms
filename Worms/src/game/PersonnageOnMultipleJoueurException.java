package game;

/**
 * 
 * Exception lancée si un personnage se trouve sur plusieurs joueurs
 */
@SuppressWarnings("serial")
public class PersonnageOnMultipleJoueurException extends Exception{
	
	public PersonnageOnMultipleJoueurException(){}
	public PersonnageOnMultipleJoueurException(String message){
		super(message);
	}

}
