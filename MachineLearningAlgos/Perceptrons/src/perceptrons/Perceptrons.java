package perceptrons;
import java.util.*;

public class Perceptrons {

	static int totalSteps;
	
	public static void main(String[] args) {
		/* double [][] a = genData (100, 20, 1);	//number 2
		double [] b = findWeights(a);
		for (int i = 0 ; i < b.length ; i++) {
			System.out.println(b[i]);
		} */

		
		/* for (int i = 2 ; i < 41 ; i++) {		//number 4
			double avg = 0;
			int c = 10;
			while (c > 0) {
				double [][] a = genData (1000, i, 1);
				double [] b = findWeights(a);
				avg = avg + totalSteps;
				c--;
			}
			System.out.println("when k value is " + i + ", the total steps is " + avg/10);
		} */
		
		double [][] a = genData5 (100, 20);	//number 2
		double [] b = findWeights(a);
		if (totalSteps >= 10000) {
			System.out.println("too big lol");
		}
		else {
			for (int i = 0 ; i < b.length ; i++) {
				System.out.println(b[i]);
			}
		}
	}

	public static double [][] genData (int m, int k, double e) {
		double [][] answer = new double [m][k+1];
		for (int i = 0 ; i < m ; i++) {
			for (int j = 0 ; j < k - 1 ; j++) {		//populating first k-1 vector elements w normal distibution items
				answer[i][j] = new Random().nextGaussian(); 
			}
			double random = Math.random();
			while (random == 1) {			//log of 0 cannot be defined
				random = Math.random();
			}
			random = Math.log(1-random)/(-1);	//creating exponential distribution
			random = random + e;
			double posOrNeg = Math.random();
			if (posOrNeg < 0.5) {
				answer[i][k-1] = random;
			}
			else {
				answer[i][k-1] = random * -1;
				random = random * -1;
			}
			if (random < 0) {
				answer[i][k] = -1;
			}
			else {
				answer[i][k] = 1;
			}
		}
		return answer;
	}
	
	public static double [][] genData5 (int m, int k) {
		double [][] answer = new double [m][k+1];
		double sum = 0;
		for (int i = 0 ; i < m ; i++) {
			for (int j = 0 ; j < k ; j++) {		//populating first k-1 vector elements w normal distibution items
				answer[i][j] = new Random().nextGaussian(); 
				sum = sum + Math.pow(answer[i][j], 2);
			}
			answer[i][k-1] = -1;
			if (sum >= k) {
				answer[i][k-1] = 1;
			}
		}
		return answer;
	}
	
	public static double [] findWeights (double [][] data) {
		totalSteps = 0;
		double [] weights = new double[data[0].length];
		int counter = 0;
		while (counter < data.length && totalSteps < 10000) {
			if (totalSteps%1000 == 0) {		//for number 5
				for (int x = 0 ; x < weights.length ; x++) {
					System.out.println(weights[x]);
				} 
			}
			double [] vector = data[counter];
			double y = vector[vector.length - 1];
			double fx = -1;
			double sum = 0;
			for (int i = 0 ; i < vector.length - 1 ; i++) {
				sum = sum + weights[i] * vector[i];
			}
			sum = sum + weights[vector.length - 1];
			if (sum > 0) {
				fx = 1;
			}
			if (y != fx) {
				totalSteps++;
				for (int i = 0 ; i < vector.length - 1 ; i++) {
					weights[i] = weights[i] + y * vector[i];
				}
				weights[weights.length - 1] = weights[weights.length - 1] + y;
				counter = 0;
			}
			else {
				counter++;
			}
		}
		return weights;
	}
}
