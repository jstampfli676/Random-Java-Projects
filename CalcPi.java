import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcPi {
	public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}

	public static void main (String[] args) {
		long total = 100000000000L;
		double inCount = 0;
		double progress = 0.01;
		double increment = 0.01;
		for (double i = 0; i<total; i++) {
			double xCoord = Math.random();
			double yCoord = Math.random();
			if (Math.pow(xCoord, 2) + Math.pow(yCoord, 2) <= 1) {
				inCount++;
			}
			if (Math.abs(i/total - 0.001) <= progress && Math.abs(i/total + 0.001) >= progress) {//something going wrong
				System.out.println(progress);
				progress += 0.01;
				progress = CalcPi.round(progress, 2);
			}
		}
		System.out.println(4*inCount/total);
	}
}