package arme;

/**
 * 
 * Exception lorsque l'on ne trouve pas d'arme demand�e
 */
@SuppressWarnings("serial")
public class ArmeNotFoundException extends Exception{

	public ArmeNotFoundException(){}
	public ArmeNotFoundException(String message){
		super(message);
	}

}
