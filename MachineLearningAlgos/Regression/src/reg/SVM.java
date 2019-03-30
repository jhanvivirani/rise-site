package reg;

public class SVM {
	
	public static double e = 10;
	public static boolean f = true;
	
	public static void main(String[] args) {
		double [][] x = {{0,0},{1,1},{1,0},{0,1}};
		double [] y = {-1,-1,1,1};
		double [] a = SVMSolver(x,y);
		for (int i = 0 ; i < a.length ; i++) {
			System.out.println(a[i]);
		}
		double [] xNew = {0,0};
		System.out.println(SVMClassifier (xNew, x, y, a));
	}
	
	public static double [] initializeA (double [] y) {
		double [] a = new double [y.length];
		double sum = 0;
		int balance = 0;
		for (int i = 1 ; i < a.length; i++) {
				a[i] = 1;
				sum = sum + y[i]*y[0];
				if ((y[i]*y[0]) < 0) {
					balance = i;
				}
		}
		if (sum > 0) {
			sum = sum - y[balance]*y[0];
			a[balance] = sum/(y[balance]*y[0]);
		}
		return a;
	}
	
	public static double function (double [] a, double [][] x, double [] y) {
		double lna = 0;
		double ayiy1 = 0;
		for (int i = 1 ; i < a.length ; i++) {
			lna = lna + Math.log(a[i]);
			ayiy1 = ayiy1 + a[i]*y[i]*y[0];
		}
		return f(a, x, y) + e*(lna + Math.log(ayiy1*-1));
	}
	
	public static double [] maximize (double [] a, double [][] x, double [] y) {
		double [] answer = new double [a.length];
		for (int i = 0 ; i < answer.length ; i++) {
			answer[i] = a[i];
		}
		double change = 0.001;
		int t = 1000;
		while (t > 0) {
			for (int i = 1 ; i < a.length ; i ++) {
				double [] aprime = new double [a.length];
				for (int j = 0 ; j < answer.length ; j++) {
					aprime[j] = a[j];
				}
				aprime[i] = aprime[i] + change;
				if (function (aprime, x, y) > function (answer, x, y) && isValid (aprime, y)) {
					answer[i] = aprime[i];
				}
				if (aprime[i] - 2*change > 0) {
					aprime[i] = aprime[i] - 2*change;
					if (function (aprime, x, y) > function (answer, x, y) && isValid (aprime, y)) {
						answer[i] = aprime[i];
					}
				}
			}
			t--;
		}
		return answer;
		
	}
	
	public static double f (double [] a, double [][] x, double [] y) {	//insert objective function here
		double ai = 0;
		double ayiy1 = 0;
		double ayx = 0;
		double ax = 0;
		for (int i = 1 ; i < a.length ; i++) {
			ai = ai + a[i];
			ayiy1 = ayiy1 + a[i]*y[i]*y[0];
			ax = ax + a[i] * y[i] * (1+ Math.pow(dotProduct(x[i],x[0]),2));
			for (int j = 1 ; j < a.length ; j++) {
				ayx = ayx + a[i]*y[i]*(1+ Math.pow(dotProduct(x[i],x[j]),2))*y[j]*a[j];
			}
		}
		ayiy1 = ayiy1 * -1;
		return (ayiy1 + ai) - 0.5 * (ayiy1 * (1 + Math.pow(dotProduct(x[0],x[0]),2)) + (2 * ayiy1 * y[0] * ax) + ayx);
	}
	
	public static double dotProduct (double [] a, double [] b) {
		double sum = 0;
		for (int i = 0 ; i < a.length ; i++) {
			sum = sum + a[i] * b[i];
		}
		return sum;
	}
	
	public static boolean isValid (double [] a, double [] y) {
		double sum = 0;
		for (int i = 0 ; i < a.length ; i++) {
			sum = sum + a[i]*y[i]*y[0];
		}
		sum = sum * -1;
		if (sum <= 0) {
			return true;
		}
		else {
			return false;
		}
		
	}
	public static double [] SVMSolver (double [][] x, double [] y) {
		double [] a = initializeA(y);
		e = 10;		//change if need be!
		int t = 0;
		while (t < 10) {
			a = maximize (a, x, y);
			t++;
			e = e * 0.75;
		}
		double a1 = 0;
		for (int i = 1 ; i < a.length ; i++) {
			a1 = a1 + a[i]*y[i]*y[0];
		}
		a1 = a1 * -1;
		a[0] = a1;
		return a;
	}
	
	public static int SVMClassifier (double [] xNew, double [][] x, double [] y, double [] a) {
		double sum = 0;
		for (int i = 0 ; i < a.length ; i++) {
			if (a[i] > 0) {
				sum = sum + a[i] * y[i] * (1+ Math.pow(dotProduct(x[i], xNew),2));
			}
		}
		if (sum >= 0) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
}
