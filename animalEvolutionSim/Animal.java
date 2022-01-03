import java.util.*;
import java.awt.*;

public class Animal{
	
	private String species;
	private boolean predator;
	private int maxSpeed;
	private int wanderSpeed;
	private int maxStamina;
	private int curStamina;
	private int acceleration;//not used
	private int strength;//not used
	private int awareness;
	private int stealth;//not used
	private int size;
	private int lifespan;
	private int intelligence;//not used
	private double agility;
	private int curAge;
	private ArrayList<Double> curVelocity;
	private ArrayList<Double> curPos;
	private Color color;
	private int uniqueId;

	private int food = 500;
	public HashMap<Animal, Integer> reproducedWith = new HashMap<Animal, Integer>();

	private static final double WANDER_DEGREE = 1.75;
	private static int idCount = 0;

	public Animal() {
		this("default", false, 6, 3, 150, 1, 1, 400, 1, 4, 600, 3, 10.0, new ArrayList<Double>(Arrays.asList(0.0, 0.0)), 
			new ArrayList<Double>(Arrays.asList(Math.random()*1300, Math.random()*1300)), Color.BLUE);
	}

	public Animal(Animal a) {
		this(a.getSpecies(), a.getPredator(), a.getMaxSpeed(), a.getWanderSpeed(), a.getStamina(), a.getAcceleration(), a.getStrength(), a.getAwareness(), 
			a.getStealth(), a.getSize(), a.getLifespan(), a.getIntelligence(), a.getAgility(), a.getCurVelocity(), a.getCurPosition(), a.getColor());
	}

	public Animal(String species, boolean predator, int maxSpeed, int wanderSpeed, int stamina, int acceleration, int strength, int awareness, 
			int stealth, int size, int lifespan, int intelligence, double agility, ArrayList<Double> inVelocity, ArrayList<Double> inPos, Color color) {
		this.species = species;
		this.predator = predator;
		this.maxSpeed = maxSpeed;
		this.wanderSpeed = wanderSpeed;
		if (this.wanderSpeed > this.maxSpeed) {
			this.wanderSpeed = this.maxSpeed;
		}
		this.maxStamina = stamina;
		this.acceleration = acceleration;
		this.strength = strength;
		this.awareness = awareness;
		this.stealth = stealth;
		this.size = size;
		this.lifespan = lifespan;
		this.intelligence = intelligence;
		this.agility = agility - size;
		if (this.agility < 1) {
			this.agility = 1;
		}
		this.curPos = inPos;
		this.curVelocity = inVelocity;
		this.color = color;
		curAge = 0;
		uniqueId = idCount;
		idCount++;
	}

	public ArrayList<Double> wander() {
		double xDisp = Math.random() * WANDER_DEGREE - (WANDER_DEGREE/2);
		double yDisp = Math.random() * WANDER_DEGREE - (WANDER_DEGREE/2);
		ArrayList<Double> displacementVector = new ArrayList<Double>(Arrays.asList(xDisp, yDisp));
		curVelocity = changeMagnitude(addVectors(curVelocity, displacementVector), wanderSpeed);
		curPos = addVectors(curPos, curVelocity);
		incrementStamina();
		return curPos;
	}

	public ArrayList<Double> seek(ArrayList<Animal> targets) {
		//might change to more intelligent pursuit and evade in the future

		if (curStamina > maxStamina/2) {
			ArrayList<Double> negativePosition = flipVector(curPos);
			ArrayList<Double> negativeVelocity = flipVector(curVelocity);
			Animal curTarget = isWrapped(pickTarget(targets, negativePosition), curPos, 1300.0);
			if (curTarget != null) {
				ArrayList<Double> desiredVelocity = friendOrFoe(curTarget, addVectors(curTarget.getCurPosition(), negativePosition));
				ArrayList<Double> steering = addVectors(desiredVelocity, negativeVelocity);
				steering = changeMagnitude(steering, truncate(calcMagnitude(steering), (double)agility));
				curVelocity = changeMagnitude(addVectors(curVelocity, steering), maxSpeed);
				curPos = addVectors(curPos, curVelocity);
				decrementStamina();
				return curPos;
			}
			return wander();
		} else {
			return wander();
		}
	}

	private Animal isWrapped(Animal target, ArrayList<Double> me, double sz) {
    	if (target != null) {
    		double x1 = target.getCurPosition().get(0);
	    	double y1 = target.getCurPosition().get(1);
	    	double x2 = me.get(0);
	    	double y2 = me.get(1);
	    	if(Math.abs(x1-x2) > sz/2) {
	    		x1-=sz;
	    	}
	    	if(Math.abs(y1-y2) > sz/2) {
	    		y1-=sz;
	    	}
	    	target.setCurPosition(new ArrayList<Double>(Arrays.asList(x1, y1)));
	    	return target;
    	}
    	return null;
    }

    private ArrayList<Double> friendOrFoe(Animal otherAnimal, ArrayList<Double> runTowards) {
    	if (this.species.equals(otherAnimal.getSpecies())){return runTowards;}
    	if (this.predator) {
    		if (otherAnimal.getPredator()) {
    			if (this.size > otherAnimal.getSize()) {
    				return runTowards;
    			} else {
    				return flipVector(runTowards);
    			}
    		} else {
    			return runTowards;
    		}
    	} else {
    		if (otherAnimal.getPredator()) {
    			return flipVector(runTowards);
    		}
    	}
    	return runTowards;
    }

	private Animal pickTarget(ArrayList<Animal> targets, ArrayList<Double> negativePosition) {
		Animal bestTarget = null;
		double bestTargetDist = 100000000;
		for (Animal target : targets) {
			if (suitableTarget(target)) {
				double curTargetDist = calcMagnitude(addVectors(negativePosition, target.getCurPosition()));
				if (curTargetDist < bestTargetDist) {
					bestTargetDist = curTargetDist;
					bestTarget = target;
				}
			}
		}
		return bestTarget;
	}

	private boolean suitableTarget(Animal target) {
		if (this.species.equals(target.getSpecies()) && !reproducedWith.containsKey(target) && 
			this.food>200 && target.getFood()>200 && this.curAge > 200 && target.getCurAge()>200){return true;}
		if (this.predator) {
    		if (!target.getPredator()) {
    			if (1.5 * this.size <= target.getSize()) {
    				return false;
    			}
    		} else {
    			if (this.size == target.getSize()) {
    				return false;
    			}
    		}
    	} else {
			if (!target.getPredator()) {
				return false;
			} else if (this.size > target.getSize() * 1.5) {
				return false;
			}
    	}
    	return true;
	}

	private double truncate(double cur, double max) {
		if (cur > max) {
			return max;
		}
		return cur;
	}

	public int survey() {
		return awareness;
	}

	private ArrayList<Double> changeMagnitude(ArrayList<Double> vector, double max) {
		ArrayList<Double> newVector = new ArrayList<Double>();
		double curMagnitude = calcMagnitude(vector);
		for (double d : vector) {
			newVector.add((d*max)/curMagnitude);
		}
		return newVector;
	}

	private double calcMagnitude(ArrayList<Double> vector) {
		return Math.pow(Math.pow(vector.get(0), 2) + Math.pow(vector.get(1), 2), 0.5);
	}

	private ArrayList<Double> addVectors(ArrayList<Double> vector1, ArrayList<Double> vector2) {
		ArrayList<Double> newVector = new ArrayList<>();
		for (int i = 0; i<vector1.size(); i++) {
			newVector.add(vector1.get(i) + vector2.get(i));
		}
		return newVector;
	}

	private ArrayList<Double> flipVector(ArrayList<Double> vector) {
		ArrayList<Double> newVector = new ArrayList<Double>();
		for (Double d : vector) {
			newVector.add(-1*d);
		}
		return newVector;
	}

	public void addFood(int size) {
		food+=size*5;
		System.out.println("eat");
	}

	public void decrementFood() {
		food-=1;
		if (food < 0) {
			food = 0;
			incrementAge();
		}
	}

	public boolean incrementAge() {
		curAge++;
		if (curAge >= lifespan) {
			return true;
		}
		if (this.predator) {
			decrementFood();
		}
		return false;
	}

	public void decrementStamina() {
		curStamina--;
		if (curStamina <= 0) {
			curStamina = 0;
		}
	}

	public void incrementStamina() {
		curStamina+=5;//should create stamina regen rate
		if (curStamina > maxStamina) {
			curStamina = maxStamina;
		}
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(double)curPos.get(0), (int)(double)curPos.get(1) - size, 2*size, 2*size);
	}

	protected void paint(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int)(double)curPos.get(0), (int)(double)curPos.get(1) - size, 2*size, 2*size);
	}

	public void setCurPosition(ArrayList<Double> newPos) {
		curPos = newPos;
	}

	public void setCurAge(int age) {
		curAge = age;
	}

	public String getSpecies() {
		return species;
	}

	public boolean getPredator() {
		return predator;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getWanderSpeed() {
		return wanderSpeed;
	}

	public int getStamina() {
		return maxStamina;
	}

	public int getCurStamina() {
		return curStamina;
	}

	public int getAcceleration() {
		return acceleration;
	}

	public int getStrength() {
		return strength;
	}

	public int getAwareness() {
		return awareness;
	}

	public int getStealth() {
		return stealth;
	}

	public int getSize() {
		return size;
	}

	public int getLifespan() {
		return lifespan;
	}

	public int getCurAge() {
		return curAge;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public double getAgility() {
		return agility;
	}

	public Color getColor() {
		return color;
	}

	public int getFood() {
		return food;
	}

	public ArrayList<Double> getCurPosition() {
		return curPos;
	}

	public ArrayList<Double> getCurVelocity() {
		return curVelocity;
	}

	public int getUniqueId(){
		return uniqueId;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Animal)) {
			return false;
		}
		Animal a = (Animal) o;
		return this.uniqueId == a.getUniqueId();
	}
}