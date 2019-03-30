package decisionTree;
import decisionTree.Node;
import java.util.Arrays;

public class Tree {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [][] a = dataGenerator(10,100);
		Node tree = new Node (0);
		constructTree(a,tree);
		for (int k = 1 ; k < 6 ; k++) {
			int [][] b = dataGenerator(10,50*k);
			int [][] data = new int [b.length][b[0].length-1];
			int [] answers = new int [b.length];
			for (int i = 0 ; i < data.length ; i++) {
				for (int j = 0 ; j < data[0].length ; j++) {
					data[i][j] = b[i][j];
				}
				answers[i] = b[i][b[i].length-1];
			}
			System.out.println("Error for " + k*50 + " is " + treeError(data,answers,tree));
		}

	}

	public static int[][] dataGenerator (int k , int m) {
		int [][] answer = new int [m][k+1];
		for (int j = 0 ; j < m ; j++) {
			double r = Math.random();
			if (r < 0.5) {
				answer[j][0] = 0;
			}
			else {
				answer[j][0] = 1;
			}
			for (int i = 1 ; i < k ; i++) {
				r = Math.random();
				if (r < 0.75) {
					answer[j][i] = answer[j][i-1];
				}
				else {
					answer[j][i] = 1 - answer[j][i-1];
				}
			}
			double den = 0;
			for (int i = 2 ; i <= k ; i++) {
				den = den + Math.pow(0.9,i);
			}
			double sum = 0;
			for (int i = 1 ; i < k ; i++) {
				double weight = Math.pow(0.9, i+1) / den;
				sum = sum + weight * answer[j][i];
			}
			if (sum >= 0.5) {
				answer[j][k] = answer[j][0];
			}
			else {
				answer[j][k] = 1 - answer[j][0];
			}
		}
		return answer;
	}
	
	public static double findProb (int [][] data , int x) {
		double answer = 0;
		for (int i = 0 ; i < data.length ; i++) {
			if (data[i][data[i].length - 1] == x) {
				answer++;
			}
		}
		answer = answer / data.length;
		return answer;
	}
	
	public static int[][][] divideData (int [][] data, int index) {
		int count = 0;
		for (int i = 0 ; i < data.length ; i++) {
			if (data[i][index] == 0) {
				count++;
			}
		}
		int [][] i0 = new int [count][data[0].length];
		int [][] i1 = new int [data.length - count][data[0].length];
		int count0 = 0;
		int count1 = 0;
		for (int i = 0 ; i < data.length ; i++) {
			if (data[i][index] == 0) {
				i0[count0] = data[i];
				count0++;
			}
			if (data[i][index] == 1) {
				i1[count1] = data[i];
				count1++;
			}
		}
		
		int[][][] a = {i0,i1};
		return a;
	}

	public static void constructTree (int [][] data, Node ptr) {
		double p0 = findProb(data,0);
		double p1 = findProb(data,1);
		if (p0 == 1) {
			ptr.value = 0;
			System.out.println("Return 0");
		}
		else if (p1 == 1) {
			ptr.value = 1;
			System.out.println("Return 1");
		}
		else {
			double  maxig = -1000;
			int splitOn = 0;
			double maxProb = -1;
			int splitOn1 = 0;
			double infoContent = 0;
			double log0 = 0;
			if (p0 != 0) {
				log0 = Math.log(p0);
			}
			double log1 = 0;
			if (p1 != 0) {
				log1 = Math.log(p1);
			}
			infoContent = p0*log0 + p1*log1;
			infoContent = infoContent * -1;
			for (int i = 0 ; i < data[0].length-1 ; i++) {
				int [][][] sides = divideData(data, i);
				double prob0Given0 = findProb(sides[0], 0);
				double log00 = 0;
				if (prob0Given0 != 0) {
					log00 = Math.log(prob0Given0);
				}
				double prob1Given0 = findProb(sides[0], 1);
				double log10 = 0;
				if (prob1Given0 != 0) {
					log10 = Math.log(prob1Given0);
				}
				double prob0Given1 = findProb(sides[1],0);
				double log01 = 0;
				if (prob0Given1 != 0) {
					log01 = Math.log(prob0Given1);
				}
				double prob1Given1 = findProb(sides[1],1);
				double log11 = 0;
				if (prob1Given1 != 0) {
					log11 = Math.log(prob1Given1);
				}
				double infoGain = p0 * (-1 * (prob0Given0 * log00 + prob1Given0 * log10)) + p1 * (-1 * (prob0Given1 * log01 + prob1Given1 * log11));
				infoGain = infoContent - infoGain;
				if (infoGain > maxig) {
					maxig = infoGain;
					splitOn = i;
				}
				double product = prob1Given0 * prob0Given0 * prob1Given1 * prob0Given1;
				if (product > maxProb) {
					maxProb = product;
					splitOn1 = i;
				}
			}
			int [][][] sides = divideData(data, splitOn1);
			ptr.value = splitOn1;
			ptr.left= new Node(0);
			ptr.right= new Node(0);
			System.out.println("If index " + splitOn1 + " is 0 then...");
			constructTree(sides[0], ptr.left);
			System.out.println("If index " + splitOn1 + " is 1 then...");
			constructTree(sides[1], ptr.right);
		}
		
	}
	
	public static int runTree (int [] a, Node tree) {	//make sure the answers arent in the tree
		while (tree.left != null) {
			int split = tree.value;
			if (a[split] == 0) {
				tree = tree.left;
			}
			else {
				tree = tree.right;
			}
		}
		return tree.value;
	}
	
	public static double treeError (int [][] data, int [] answers, Node tree) {
		double answer = 0;
		for (int i = 0 ; i < data.length ; i++) {
			int prediction = runTree(data[i], tree);
			if (answers[i] != prediction) {
				answer++;
			}
		}
		answer = answer / data.length;
		return answer;
	}
	

}
