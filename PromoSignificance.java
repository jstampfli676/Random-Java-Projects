import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class PromoSignificance {
	public static Map<Integer, Integer> timesPlayed;
	public static Map<Integer, Integer> timesWonAndWon;
	public static Map<Integer, Integer> timesLostAndWon;
	public static Map<Integer, Integer> timesWon;
	public static int totalWins;
	public static int trials;
	private static double winProb;

	public PromoSignificance(int trials, double winProb) {
		this.trials = trials;
		this.winProb = winProb;
		timesPlayed = new HashMap<Integer, Integer>();
		timesWonAndWon = new HashMap<Integer, Integer>();
		timesLostAndWon = new HashMap<Integer, Integer>();
		timesWon = new HashMap<Integer, Integer>();
		for (int i = 1; i<=5; i++) {
			timesPlayed.put(i, 0);
			timesWonAndWon.put(i, 0);
			timesLostAndWon.put(i, 0);
			timesWon.put(i, 0);
		}
		runTrials();
	}

	private void runTrials() {
		for (int i = 0; i<trials; i++) {
			int curLosses = 0;
			int curWins = 0;
			int curGame = 1;
			ArrayList<Integer> wonGame = new ArrayList<>();
			while (curLosses<3 && curWins<3) {
				timesPlayed.put(curGame, timesPlayed.get(curGame)+1);
				double gameResult = Math.random();
				if (gameResult < winProb) {
					curWins++;
					wonGame.add(curGame);
					timesWon.put(curGame, timesWon.get(curGame)+1);
				} else {
					curLosses++;
				}
				curGame++;
			}
			if (curWins>=3) {
				totalWins++;
				for (int x = 1; x<curGame; x++) {
					if (wonGame.contains(x)) {
						timesWonAndWon.put(x, timesWonAndWon.get(x)+1);
					} else {
						timesLostAndWon.put(x, timesLostAndWon.get(x)+1);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		PromoSignificance ps = new PromoSignificance(1000000, .5);
		System.out.println(ps.timesPlayed);
		System.out.println(ps.timesWonAndWon);
		System.out.println(ps.timesLostAndWon);
		System.out.println(ps.timesWon);
		System.out.println(totalWins);
		for (int i = 1; i<=5; i++) {
			double sAndGW = ps.timesWonAndWon.get(i);
			double gW = ps.timesWon.get(i);
			double sAndGL = ps.timesLostAndWon.get(i);
			double gL = ps.timesPlayed.get(i) - gW;
			System.out.println(((sAndGW/gW)-(sAndGL/gL))/2);
		}
	}
}