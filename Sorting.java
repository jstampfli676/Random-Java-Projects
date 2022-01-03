import java.util.*;

public class Sorting {
	
	public ArrayList<Integer> mergeSort(ArrayList<Integer> input) {
		if (input.size()<=1) {
			return input;
		}

		ArrayList<Integer> left = new ArrayList<Integer>();
		ArrayList<Integer> right = new ArrayList<Integer>();
		for (int i = 0; i<input.size(); i++) {
			if (i<input.size()/2) {
				left.add(input.get(i));
			} else {
				right.add(input.get(i));
			} 
		}
		left = mergeSort(left);
		right = mergeSort(right);
		return merge(left, right);
	}

	private ArrayList<Integer> merge(ArrayList<Integer> left, ArrayList<Integer> right) {
		ArrayList<Integer> answer = new ArrayList<>();
		int i = 0; 
		int x = 0;
		while (i<left.size() && x<right.size()) {
			if (left.get(i)<right.get(x)) {
				answer.add(left.get(i));
				i++;
			} else {
				answer.add(right.get(x));
				x++;
			}
		}

		while (i<left.size()) {
			answer.add(left.get(i));
			i++;
		}

		while (x<right.size()) {
			answer.add(right.get(x));
			x++;
		}

		return answer;
	}

	public void quickSort(ArrayList<Integer> input, int low, int high) {
		if (low<high) {
			int pi = partition(input, low, high);
			quickSort(input, low, pi-1);
			quickSort(input, pi+1, high);
		}
	}

	private int partition(ArrayList<Integer> input, int low, int high) {
		int pivotInd = (int) (Math.random()*(high-low+1)+low);
		int pivot = input.get(pivotInd);
		if (pivotInd!=low) {
			int temp = input.get(low);
			input.set(low, pivot);
			input.set(pivotInd, temp);
		}

		int x = low; 
		for (int i = low+1; i<=high; i++) {
			if (input.get(i) < pivot) {
				x++;
				int temp = input.get(i);
				input.set(i, input.get(x));
				input.set(x, temp);
			}
		}

		input.set(low, input.get(x));
		input.set(x, pivot);
		return x;
	}

	public static void main(String[] args) {
		long n = 10000000;
		Sorting s = new Sorting();
		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(9, 8, 7, 7, 3, 4, 3, 2, 1));
		for (int i = 0; i<n; i++) {
			test.add((int)(Math.random()*n));
		}
		long start = System.currentTimeMillis();
		s.quickSort(test, 0, test.size()-1);
		long mid = System.currentTimeMillis();
		System.out.println("Quick Sort Time: "+String.valueOf(mid-start));
		//System.out.println(test);
		s.mergeSort(test);
		System.out.println("Merge Sort Time: "+String.valueOf(System.currentTimeMillis()-mid));
		//System.out.println(s.mergeSort(test));
	}
}