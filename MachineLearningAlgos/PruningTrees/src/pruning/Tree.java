package pruning;

public class Tree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			int [][] training = dataGenerator(800);
			Node tree = new Node (0);
			constructTree(training,tree);
				int [][] b = dataGenerator(100);
				int [][] testData = new int [b.length][b[0].length-1];
				int [] testAnswers = new int [b.length];
				for (int i = 0 ; i < testData.length ; i++) {
					for (int j = 0 ; j < testData[0].length ; j++) {
						testData[i][j] = b[i][j];
					}
					testAnswers[i] = b[i][b[i].length-1];
				}
				System.out.println("Testing Error is " + treeError(b, testAnswers, tree));


	}
	
	public static int[][] dataGenerator (int m) {		//creates a data set of m data points with 20 X variables each and one y output value
		boolean firstNum0 = false;
		int [][] answer = new int [m][22];
		for (int j = 0 ; j < m ; j++) {
			double r = Math.random();
			if (r < 0.5) {
				answer[j][0] = 0;
			}
			else {
				answer[j][0] = 1;
				firstNum0 = true;
			}
			for (int i = 1 ; i < 21 ; i++) {
				r = Math.random();
				if (i < 15) {
					if (r < 0.75) {
						answer[j][i] = answer[j][i-1];
					}
					else {
						answer[j][i] = 1 - answer[j][i-1];
					}
				}
				else {
					if (r < 0.5) {
						answer[j][i] = 0;
					}
					else {
						answer[j][i] = 1;
					}
				}
			}
			if (firstNum0) {
				int counter = 0;
				for (int i = 1 ; i <= 7 ; i++) {
					if (answer[j][i] == 0) {
						counter++;
					}
					if (counter > 3) {
						answer[j][21] = 0;
					}
					else {
						answer[j][21] = 1;
					}
				}
			}
			else {
				int counter = 0;
				for (int i = 8 ; i <= 14 ; i++) {
					if (answer[j][i] == 0) {
						counter++;
					}
					if (counter > 3) {
						answer[j][21] = 0;
					}
					else {
						answer[j][21] = 1;
					}
				}
			}
		}
		return answer;
	}
	
	public static double findProb (int [][] data , int y) {		//given a set of data points, calculate the probability that a random point has an output value of y
		double answer = 0;
		for (int i = 0 ; i < data.length ; i++) {
			if (data[i][data[i].length - 1] == y) {
				answer++;
			}
		}
		answer = answer / data.length;
		return answer;
	}
	
	public static int[][][] divideData (int [][] data, int index) {		//divide data set across X variable in the ith index
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

	public static void constructTree (int [][] data, Node ptr) {	//construct classic tree classifier
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
			}
			int [][][] sides = divideData(data, splitOn);
			ptr.value = splitOn;
			ptr.left= new Node(0);
			ptr.right= new Node(0);
			System.out.println("If index " + splitOn + " is 0 then...");
			constructTree(sides[0], ptr.left);
			System.out.println("If index " + splitOn + " is 1 then...");
			constructTree(sides[1], ptr.right);
		}
		
	}
	
	public static void constructDepthTree (int [][] data, Node ptr, int height) {	//depth tree
		
		if (height > 0) {	//middle node
			double p0 = findProb(data,0);
			double p1 = findProb(data,1);
			if (p0 == 1) {
				ptr.value = 0;
				//System.out.println("Return 0");
			}
			else if (p1 == 1) {
				ptr.value = 1;
				//System.out.println("Return 1");
			}
			else {
				double  maxig = -1000;
				int splitOn = 0;
				double maxProb = -1;
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
				}
				int [][][] sides = divideData(data, splitOn);
				ptr.value = splitOn;
				ptr.left= new Node(0);
				ptr.right= new Node(0);
				//System.out.println("If index " + splitOn + " is 0 then...");
				constructDepthTree(sides[0], ptr.left, height - 1);
				//System.out.println("If index " + splitOn + " is 1 then...");
				constructDepthTree(sides[1], ptr.right, height - 1);
			}
		}
		else {	//leaf node
			double prob0 = findProb(data, 0);
			if (prob0 == 0.5) {
				double r = Math.random();
				if (r > 0.5) {
					ptr.value = 0;
				}
				else {
					ptr.value = 1;
				}
			}
			else if (prob0 > 0.5) {
				ptr.value = 0;
			}
			else {
				ptr.value = 1;
			}
		}
		
	}
	
public static void constructSampleTree (int [][] data, Node ptr, int s) {	//construct sample tree
		if (data.length > s) {	//middle node
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
				}
				int [][][] sides = divideData(data, splitOn);
				ptr.value = splitOn;
				ptr.left= new Node(0);
				ptr.right= new Node(0);
				System.out.println("If index " + splitOn + " is 0 then...");
				constructSampleTree(sides[0], ptr.left, s);
				System.out.println("If index " + splitOn + " is 1 then...");
				constructSampleTree(sides[1], ptr.right, s);
			}
		}
		else {	//leaf node
			double prob0 = findProb(data, 0);
			if (prob0 == 0.5) {
				double r = Math.random();
				if (r > 0.5) {
					ptr.value = 0;
				}
				else {
					ptr.value = 1;
				}
			}
			else if (prob0 > 0.5) {
				ptr.value = 0;
			}
			else {
				ptr.value = 1;
			}
		}
		
	}

public static int countIrrelevantSplits (Node ptr) {		//counting the number of irrelevant splits in a sample tree
	if (ptr.left == null && ptr.right == null) {
		return 0;
	}
	else {
		if (ptr.value > 14) {
			return (1 + countIrrelevantSplits(ptr.left) + countIrrelevantSplits(ptr.right));
		}
		else {
			return (countIrrelevantSplits(ptr.left) + countIrrelevantSplits(ptr.right));
		}
	}
}


public static void constructSignificanceTree (int [][] data, Node ptr, double t0) {	//construct a significance tree
	double p0 = findProb(data,0);
	double p1 = findProb(data,1);
	if (p0 == 1) {
		ptr.value = 0;
		//System.out.println("Return 0");
	}
	else if (p1 == 1) {
		ptr.value = 1;
		//System.out.println("Return 1");
	}
	else {
		double  maxig = -1000;
		int splitOn = 0;
		double maxProb = -1;
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
		}
		if (chiSquared(data, splitOn, t0)) {
			int [][][] sides = divideData(data, splitOn);
			ptr.value = splitOn;
			ptr.left= new Node(0);
			ptr.right= new Node(0);
			//System.out.println("If index " + splitOn + " is 0 then...");
			constructSignificanceTree(sides[0], ptr.left, t0);
			//System.out.println("If index " + splitOn + " is 1 then...");
			constructSignificanceTree(sides[1], ptr.right, t0);
		}
			else {	//leaf node
				double prob0 = findProb(data, 0);
				if (prob0 == 0.5) {
					double r = Math.random();
					if (r > 0.5) {
						ptr.value = 0;
					}
					else {
						ptr.value = 1;
					}
				}
				else if (prob0 > 0.5) {
					ptr.value = 0;
				}
				else {
					ptr.value = 1;
				}
			}

	}
	
}


	public static boolean chiSquared (int [][] data, int x, double t0) {		//helper method
		int x0y0 = 0;
		int x0y1 = 0;
		int x1y0 = 0;
		int x1y1 = 0;
		for (int i = 0 ; i < data.length ; i++) {
			if (data[i][x] == 0) {
				if (data[i][data[i].length-1] == 0) {
					x0y0++;
				}
				else {
					x0y1++;
				}
			}
			else {
				if (data[i][data[i].length-1] == 0) {
					x1y0++;
				}
				else {
					x1y1++;
				}
			}
		}
		double e00 = (x0y0+x0y1)*(x0y0+x1y0)/data.length;
		double e01 = (x0y0+x0y1)*(x0y1+x1y1)/data.length;
		double e10 = (x1y0+x1y1)*(x0y0+x1y0)/data.length;
		double e11 = (x1y0+x1y1)*(x0y1+x1y1)/data.length;
		double t = (Math.pow(x0y0 - e00, 2) / e00) + (Math.pow(x0y1 - e01, 2) / e01) + (Math.pow(x1y0 - e10, 2) / e10) + (Math.pow(x1y1 - e11, 2) / e11);
		if (t > t0) {
			return true;
		}
		else {return false;}
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
	
	public static double treeError (int [][] data, int [] answers, Node tree) {		//error
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
