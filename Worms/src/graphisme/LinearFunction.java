package graphisme;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 
 * Class qui représente une fonction affine
 *
 */
public class LinearFunction {
	// coefficient directeur
	Double a;
	
	// ordonnée à l'origine
	Double b;
	
	public LinearFunction(int x1, int y1, int x2, int y2){
		this.a = createA(x1, y1, x2, y2);
		this.b = createB(x1, y1);
	}
	
	/**
	 * Crée le coefficient directeur
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Double createA(int x1, int y1, int x2, int y2) {
		
		// au dessus
		Double numerateur = (double) (y1-y2);
		
		// en dessous
		Double denominateur = (double) (x1-x2);
		
		Double a = numerateur/denominateur;
		
		Double aRounded = LinearFunction.round(a, 5); 
		
		return aRounded;
	}
	
	
	/**
	 * Crée l'ordonnée à l'origine
	 * @param x1
	 * @param y1
	 * @return
	 */
	private Double createB(int x1, int y1) {
		
		Double firstPart = (double) a*x1;
		
		Double b = (double) y1 - firstPart;
		
		Double bRounded = LinearFunction.round(b, 5);
		
		return bRounded;
	}

	/**
	 * Renvoit y, l'image de x
	 * @param x
	 * @return
	 */
	public Double getYFromX(int x){
		
		Double y = (double) a*x;
		
		y += b;
		
		Double yRounded = LinearFunction.round(y, 5);
		
		return yRounded;
	}
	
	public Integer getYFromXInInteger(int x){
		Double y = (double) a*x;
		
		y += b;
		
		return y.intValue();
	}
	
	/**
	 * Renvoit une Map de tous les points dans un ensemble défini, les x étant séparés par une valeur step
	 * @param lowerRange
	 * @param superiorRange
	 * @param step
	 * @return
	 */
	public HashMap<Double, Double> getListOfPointsInDouble (int lowerRange, int superiorRange, int step){
		HashMap<Double, Double> map = new HashMap<Double, Double>();
		
		for(int i = lowerRange; i <= superiorRange; i += step){
			map.put((double)i, getYFromX(i));
		}
		
		return map;
	}
	
	/**
	 * Arrondit un double
	 * @param d
	 * @param decimalPlace
	 * @return
	 */
	public static double round(double d, int decimalPlace){

	    BigDecimal bd = new BigDecimal(Double.toString(d));
	    
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    
	    return bd.doubleValue();
  	}

	public Double getA() {
		return a;
	}

	public Double getB() {
		return b;
	}
	
	


}
