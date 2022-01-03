public class HearthstoneGameCalc {
	static double winPerc;
	static int curRank;
	static int starGain;
	static boolean winStreakB;
	static final int MAX_RANK = 150;
	static final int TESTS = 100000;

	public HearthstoneGameCalc(double winPerc, int curRank, int starGain, boolean winStreakB){
		this.winPerc = winPerc;
		this.curRank = curRank;
		this.starGain = starGain;
		this.winStreakB = winStreakB;
	}

	public static void main(String[] args){
		HearthstoneGameCalc me = new HearthstoneGameCalc(.6, 140, 1, false);
		//System.out.println(starGain);
		double totalGamePlayed = 0;
		boolean impossible = false;
		for (int i = 0; i<TESTS; i++){
			//System.out.println(i);
			int tempRank = curRank;
			int tempStarGain = starGain;
			int winStreak = 3;
			int gameCount = 0;
			int nextFloor = 150;//will have to manually change based on starting rank
			while (tempRank<=MAX_RANK){
				if (gameCount==10000){
					impossible = true;
					break;
				}
				gameCount++;
				double result= Math.random();
				if (result<=winPerc){
					//System.out.println("win");
					winStreak++;
					if (winStreakB && winStreak>=3){
						tempRank+=2*tempStarGain;
					} else {
						tempRank+=tempStarGain;
					}
					while (tempRank>nextFloor){
						if (tempStarGain>1){
							tempStarGain--;
						}
						nextFloor+=15;
					}
					//System.out.println("WIN: "+tempRank+", "+tempStarGain);
					
				} else {
					//System.out.println("loss");
					winStreak = 0;
					if (tempRank-1>=nextFloor-15){
						tempRank--;
					}
					//System.out.println("LOSS: "+tempRank+", "+tempStarGain);
				}
			}
			if (impossible){
				System.out.println("not possible to reach legend");
				break;
			}
			totalGamePlayed+=gameCount;
		}
		if (!impossible){
			System.out.println(totalGamePlayed+", "+totalGamePlayed/TESTS);
		}
	}
}