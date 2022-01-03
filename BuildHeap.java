public class BuildHeap {
	private int[] data;
	private int nonLeaf;

	public BuildHeap(int[] input) {
		data = new int[input.length+1];
		data[0] = -1;
		for (int i = 1; i<input.length+1; i++) {
			data[i] = input[i-1];
		}
		nonLeaf = (data.length/2)-1;
		buildMax(data);
	}

	private void buildMax(int[] input) {
		for (int i = nonLeaf; i>=1; i--) {
			heapify(i);
		}
	}

	private void heapify(int i) {
		//has one child
		if (2*i+1>=data.length) {
			if (data[i]<data[2*i]) {
				swap(i, 2*i);
				if (2*i<=nonLeaf) {
					heapify(2*i);
				}
			}
		} else {//has two children
			if (data[2*i] < data[2*i+1]) {
				if (data[i]<data[2*i+1]) {
					swap(i, 2*i+1);
					if (2*i+1<=nonLeaf) {
						heapify(2*i+1);
					}
				}
			} else {
				if (data[i]<data[2*i]) {
					swap(i, 2*i);
					if (2*i<=nonLeaf) {
						heapify(2*i);
					}
				}
			}
		}
	}

	private void swap(int i, int x) {
		int temp = data[i];
		data[i] = data[x];
		data[x] = temp;
	}

	public void print() {
		for(int i = 1; i<data.length; i++) {
			System.out.print(data[i]+", ");
		}
	}

	public static void main(String[] args) {
		int[] input = new int[]{1,2,3,4,5,6,7,8,9};
		BuildHeap bh = new BuildHeap(input);
		bh.print();
	}


}