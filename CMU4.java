import java.util.*;
import java.lang.*;

public class CMU4 {
	private ArrayList<Integer> usedGaps;
	private ArrayList<Integer> potentialGaps;
	private ArrayList<Integer> answer = new ArrayList<Integer>(Arrays.asList(1, 2));
	private int max;

	public CMU4 (int max) {
		this.max = max;
		usedGaps = new ArrayList<>();
		potentialGaps = new ArrayList<>();
		computeSet();
	}

	private void computeSet () {
		int curMax = answer.get(answer.size()-1);
		while (curMax <= max) {
			potentialGaps.clear();
			potentialGaps.add(curMax-answer.get(answer.size()-2));
			potentialGaps.add(curMax-answer.get(0));
			/*System.out.println(answer);
			System.out.println(potentialGaps);
			System.out.println(usedGaps);*/
			for (int i = potentialGaps.get(0); i<=potentialGaps.get(1); i++) {
				if (!usedGaps.contains(i)){
					for (int x=0; x<answer.size()-1; x++) {
						usedGaps.add(curMax-answer.get(x));
					}
					curMax+=i;
					answer.add(curMax);
					break;
				}
			}
		}
		System.out.println(answer);
		System.out.println(answer.size());
		System.out.println(Math.pow(max, 0.5)+Math.pow(max, 0.25));
	}

	public static void main(String[] args) {
		CMU4 c = new CMU4(1000000);
	}
}