package reg;

import java.util.Random;

public class Regression {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Naive Regression
		
		/**double [][] allW = new double [100][20];
		for (int k = 0 ; k < 100 ; k++) {
			double [][] data = genData(1000);
			double [] weights = naiveReg(data);
			allW[k] = weights;
		}
		double weights[] = new double [20];
		for (int i = 0 ; i < 20 ; i++) {
			double sum = 0;
			for (int j = 0 ; j < 100 ; j++) {
				sum = sum + allW[j][i];
			}
			sum = sum / 100;
			weights[i] = sum;
		}
		for (int i = 0 ; i < weights.length ; i++) {
			System.out.println(weights[i]);
		}
		System.out.println("error: " + error(genData(500), weights)); **/
		
		//Ridge Regression
		
		/**for (int i = 0 ; i < 20 ; i++) {
			double [][] trainingData = genData(800);
			double [][] testingData = genData(200);
			double [] weights = ridgeReg(trainingData, i*0.2);
			System.out.println("Error when Lambda = " + 0.2*i + " is " + error(testingData, weights));
		} **/
		
		//Lasso Regression
			double [][] trainingData = genData(1000);
			double [] weights = lassoReg(trainingData, 3.4);
			for (int i = 0 ; i < weights.length ; i++) {
				System.out.println(weights[i]);
			}
			System.out.println("Error when Lambda = 3.4" + " is " + error(trainingData, weights));
		
		//Modified Ridge Regression (#5)
		/*for (int i = 1 ; i < 20 ; i++) {
			double [][] trainingData = genData(1000);
			double [][] testingData = genData(200);
			double [][] train = new double [trainingData.length][trainingData[0].length - 2];
			double [][] test = new double [testingData.length][testingData[0].length - 2];
			for (int j = 0 ; j < trainingData.length ; j++) {
				train[j][0] = trainingData[j][1];
				for (int k = 1 ; k < train[j].length ; k++) {
					train[j][k] = trainingData[j][k+2];
				}
			}
			for (int j = 0 ; j < testingData.length ; j++) {
				test[j][0] = testingData[j][1];
				for (int k = 1 ; k < test[j].length ; k++) {
					test[j][k] = testingData[j][k+2];
				}
			}
			double [] weights = naiveReg(trainingData);
			System.out.println(error(testingData, weights));
		}
		 */
	}

	public static double [][] genData (int size) {
		double [][] data = new double [size][21];
		for (int k = 0 ; k < size ; k++) {
			for (int i = 0 ; i < 21 ; i++) {
				if ((i >=1 && i <= 10) || (i >= 16)) {
					data[k][i] = new Random().nextGaussian();
				}
				if (i == 11) {
					double x = new Random().nextGaussian() * 0.1;
					data[k][i] = data[k][1] + data[k][2] + x;
				}
				if (i == 12) {
					double x = new Random().nextGaussian() * 0.1;
					data[k][i] = data[k][3] + data[k][4] + x;
				}
				if (i == 13) {
					double x = new Random().nextGaussian() * 0.1;
					data[k][i] = data[k][4] + data[k][5] + x;
				}
				if (i == 14) {
					double x = new Random().nextGaussian() * 0.1;
					data[k][i] = 0.1*data[k][7] + x;
				}
				if (i == 15) {
					double x = new Random().nextGaussian() * 0.1;
					data[k][i] = 2*data[k][2] - 10 + x;
				}
			}
			double y = 10;
			for (int i = 1 ; i <= 10 ; i++) {
				double x = new Random().nextGaussian() * 0.1;
				y = y + Math.pow(0.6, i) * data[k][i] + x;
			}
			data[k][0] = y;		//y value stored in index 0 for each vector
		}
		return data;
	}

	public static double [][] multiply (double [][] a , double [][] b) {
        double [][] product = new double [a.length][b[0].length];
        for(int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    product[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return product;
    }
	
	public static double [][] transpose (double [][] a) {
		double [][] answer = new double [a[0].length][a.length];
		for (int i = 0 ; i < a.length ; i++) {
			for (int j = 0 ; j < a[i].length ; j++) {
				answer[j][i] = a[i][j];
			}
		}
		return answer;
	}
	
	public static double[][] inverse (double a[][]) {
		int n = a.length;
		double x[][] = new double[n][n];
		double b[][] = new double[n][n];
		int index[] = new int[n];
		for (int i = 0; i < n; ++i)
			b[i][i] = 1;

		// Transform the matrix into an upper triangle
		gaussian(a, index);

		// Update the matrix b[i][j] with the ratios stored
		for (int i = 0; i < n - 1; ++i)
			for (int j = i + 1; j < n; ++j)
				for (int k = 0; k < n; ++k)
					b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];

		// Perform backward substitutions
		for (int i = 0; i < n; ++i) {
			x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
			for (int j = n - 2; j >= 0; --j) {
				x[j][i] = b[index[j]][i];
				for (int k = j + 1; k < n; ++k) {
					x[j][i] -= a[index[j]][k] * x[k][i];
				}
				x[j][i] /= a[index[j]][j];
			}
		}
		return x;
	}
	
	public static void gaussian(double a[][], int index[]) {
		int n = index.length;
		double c[] = new double[n];

		// Initialize the index
		for (int i = 0; i < n; ++i)
			index[i] = i;

		// Find the rescaling factors, one from each row
		for (int i = 0; i < n; ++i) {
			double c1 = 0;
			for (int j = 0; j < n; ++j) {
				double c0 = Math.abs(a[i][j]);
				if (c0 > c1)
					c1 = c0;
			}
			c[i] = c1;
		}

		// Search the pivoting element from each column
		int k = 0;
		for (int j = 0; j < n - 1; ++j) {
			double pi1 = 0;
			for (int i = j; i < n; ++i) {
				double pi0 = Math.abs(a[index[i]][j]);
				pi0 /= c[index[i]];
				if (pi0 > pi1) {
					pi1 = pi0;
					k = i;
				}
			}

			// Interchange rows according to the pivoting order
			int itmp = index[j];
			index[j] = index[k];
			index[k] = itmp;
			for (int i = j + 1; i < n; ++i) {
				double pj = a[index[i]][j] / a[index[j]][j];

				// Record pivoting ratios below the diagonal
				a[index[i]][j] = pj;

				// Modify other elements accordingly
				for (int l = j + 1; l < n; ++l)
					a[index[i]][l] -= pj * a[index[j]][l];
			}
		}
	}

	public static double[] naiveReg (double [][] x) {
		double [][] y = new double [x.length][1];
		double [][] newX = new double [x.length][x[0].length-1];
		for (int i = 0 ; i < x.length ; i++) {
			y[i][0] = x[i][0];
			for (int j = 1 ; j < x[i].length ; j++) {
				newX[i][j-1] = x[i][j];
			}
		}
		double [][] tranProduct = inverse(multiply(transpose(newX),newX));
		double [][] finalAns = multiply(multiply(tranProduct, transpose(newX)), y);
		double [] answer = new double [finalAns.length];
		for (int i = 0 ; i < finalAns.length ; i++) {
			answer[i] = finalAns[i][0];
		}
		return answer;
	}
	
	public static double [] ridgeReg (double [][] x, double lam) {
		double [][] y = new double [x.length][1];
		double [][] newX = new double [x.length][x[0].length-1];
		for (int i = 0 ; i < x.length ; i++) {
			y[i][0] = x[i][0];
			for (int j = 1 ; j < x[i].length ; j++) {
				newX[i][j-1] = x[i][j];
			}
		}
		double [][] XtX = multiply(transpose(newX),newX);
		for (int i = 0 ; i < XtX.length ; i++) {
			XtX[i][i] = XtX[i][i] + lam;
		}
		double [][] weights = multiply(multiply(inverse(XtX), transpose(newX)), y);
		double [] answer = new double [weights.length];
		for (int i = 0 ; i < weights.length ; i++) {
			answer[i] = weights[i][0];
		}
		return answer;
	}
	
	public static double [] lassoReg (double [][] x , double lam) {
		double [] y = new double [x.length];
		double [][] newX = new double [x.length][x[0].length];
		double [] weights = naiveReg(x);
		double [][] w = new double [weights.length + 1][1];
		w[0][0] = 4;	//random bias
		for (int i = 1 ; i < w.length ; i++) {
			w[i][0] = weights[i-1];
		}
		for (int i = 0 ; i < x.length ; i++) {
			y[i] = x[i][0];
			x[i][0] = 1;	//bias term
			for (int j = 1 ; j < x[i].length ; j++) {
				newX[i][j] = x[i][j];
			}
		}
			//updating bias
			double sum = 0;
			for (int i = 0 ; i < newX.length ; i++) {
				double dotP = 0;
				for (int k = 0 ; k < newX[i].length ; k++) {
					dotP = dotP + newX[i][k] * w[k][0];
				}
				sum = sum + y[i] - dotP;
			}
			w[0][0] = w[0][0] + sum / newX.length;
			//
			
			//updating non-bias terms
			for (int k = 1 ; k < w.length ; k++) {
				double [] xi = new double [newX.length];
				for (int i = 0 ; i < xi.length ; i++) {
					xi[i] = newX[i][k];
				}
				double neg = 0;
				double pos = 0;
				double [] yMinusXw = new double [y.length];
				double [][] Xw = multiply (newX, w);
				for (int i = 0 ; i < y.length ; i++) {
					yMinusXw[i] = y[i] - Xw[i][0];
				}
				double top = 0;
				double bottom = 0;
				for (int i = 0 ; i < xi.length ; i++) {
					top = top + xi[i] * yMinusXw[i];
					bottom = bottom + xi[i]*xi[i];
				}
				top = top * -1;
				neg = (top + lam/2) / bottom;
				pos = (top - lam/2) / bottom;
				if (neg < w[k][0]) {
					w[k][0] = (top*-1 - lam/2)/bottom;
				}
				else if (w[k][0] < pos) {
					w[k][0] = (top*-1 + lam/2)/bottom;
				}
				else {
					w[k][0] = 0;
				}
		}
		
		double[] finalW = new double [weights.length + 1];
		for (int i = 0 ; i < finalW.length ; i++) {
			finalW[i] = w[i][0];
		}
		return finalW;
	}
	
	public static double error (double [][] data, double [] weights) {
		double error = 0;
		double [] y = new double [data.length];
		double [][] x = new double [data.length][data[0].length-1];
		for (int i = 0 ; i < data.length ; i++) {
			y[i] = data[i][0];
			for (int j = 1 ; j < data[i].length ; j++) {
				x[i][j-1] = data[i][j];
			}
		}
		for (int i = 0 ; i < x.length ; i++) {
			double prediction = 0;
			for (int j = 0 ; j < x[i].length ; j++) {
				prediction = prediction + x[i][j]*weights[j];
			}
			error = error + Math.pow(prediction - y[i], 2);
		}
		error = error / x.length;
		return error;
	}




}

