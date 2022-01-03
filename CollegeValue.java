public class CollegeValue {
	private double startingMoney;
	private int yearsCollege;
	private int tuition;
	private double interest;
	private int[] yearsWorking;
	private int expenses;


	public CollegeValue(double startingMoney, int yearsCollege, int tuition, int yearsWorking, int salary, double interest, int expenses) {
		this(startingMoney, yearsCollege, tuition, new int[yearsWorking], interest, expenses);
		int[] temp = new int[yearsWorking];
		for (int i = 0; i<temp.length; i++) {
			temp[i] = salary;
		}
		this.yearsWorking = temp;
	}

	public CollegeValue(double startingMoney, int yearsCollege, int tuition, int[] yearsWorking, double interest, int expenses) {
		this.startingMoney = startingMoney;
		this.yearsCollege = yearsCollege;
		this.tuition = tuition;
		this.yearsWorking = yearsWorking;
		this.interest = interest;
		this.expenses = expenses;
	}

	public double noCollege() {
		return Math.pow(interest, yearsCollege+yearsWorking.length)*startingMoney - ((yearsCollege+yearsWorking.length)*expenses);
	}

	public double college() {
		double curMoney = startingMoney;
		int count = 1;
		for (int i = 0; i<yearsCollege; i++) {
			curMoney-=tuition;
			curMoney = calcInterest(curMoney);
			curMoney-=expenses;
			System.out.println("Year "+count+": $"+curMoney);
			count++;
		}
		
		for (int i = 0; i<yearsWorking.length; i++) {
			double trueSalary = calcTrueSalary(yearsWorking[i]);
			curMoney = calcInterest(curMoney);
			curMoney += trueSalary;
			curMoney-=expenses;
			System.out.println("Year "+count+": $"+curMoney);
			count++;
		}
		return curMoney;
	}

	private double calcTrueSalary(int salary) {
		double answer = 0;
		if (salary>0) {
			answer+=0.9*min(salary, 9950);
			salary-=9950;
		}
		if (salary>0) {
			answer+=0.88*min(salary, 30575);
			salary-=30575;
		}
		if (salary>0) {
			answer+=0.78*min(salary, 45850);
			salary-=45850;
		}
		if (salary>0) {
			answer+=0.76*min(salary, 78550);
			salary-=78550;
		}
		if (salary>0) {
			answer+=0.68*min(salary, 44500);
			salary-=44500;
		}
		if (salary>0) {
			answer+=0.65*min(salary, 314175);
			salary-=314175;
		}
		if (salary>0) {
			answer+=0.63*salary;
		}
		//System.out.println(answer);
		return answer;
	}

	private int min(int a, int b) {
		return a<b?a:b;
	}

	private double calcInterest(double curMoney) {
		return curMoney*interest;
	}

	public static void main(String[] args) {
		int[] workingYears = new int[]{192000, 192000, 192000, 267000, 267000, 267000, 356000, 356000, 356000, 356000, 356000, 356000, 487000, 487000, 487000, 487000, 487000, 487000};
		CollegeValue cv = new CollegeValue(2000000, 4, 35000, workingYears, 1.1, 20000);
		cv.college();
	}
}