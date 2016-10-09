package arme;

/**
 * 
 * Exception lorsque l'on ne trouve pas d'arme demandée
 */
@SuppressWarnings("serial")
public class ArmeNotFoundException extends Exception{

	public ArmeNotFoundException(){}
	public ArmeNotFoundException(String message){
		super(message);
	}

}
