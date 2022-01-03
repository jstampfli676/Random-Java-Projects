import java.util.*;

public class DrawCardHearthstone {
	private final static int DECK_SIZE = 30;
	

	public static ArrayList<Float> expectedTurn (int numCards, boolean mulligan, int trials) {
		ArrayList<Float> answer = new ArrayList<Float>();
		ArrayList<Integer> records = new ArrayList<Integer>();
		float sum = 0;
		int variance = 0;
		int i = 0;
		while (i < trials) {
			int alreadyDrawn = 0;
			while (true) {
				int drawnCard = (int)(Math.random()*(DECK_SIZE-alreadyDrawn));
				if (drawnCard<numCards) {
					sum+=getMax(alreadyDrawn+1-3, 0);
					records.add(getMax(alreadyDrawn+1-3, 0));
					break;
				}
				alreadyDrawn++;
			}
			i++;
		}
		float average = sum/trials;
		answer.add(average);
		for (i = 0; i<records.size(); i++) {
			variance+=Math.pow(records.get(i)-average, 2);
		}
		variance = variance/(trials-1);
		answer.add((float)Math.sqrt(variance));
		return answer;
	}

	private static int getMax(int a, int b) {
		return a>b?a:b;
	}

	public static void main(String[] args) {
		System.out.println(DrawCardHearthstone.expectedTurn(4, false, 100000));
	}
}