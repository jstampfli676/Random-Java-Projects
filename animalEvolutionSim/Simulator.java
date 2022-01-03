/*import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java*/
import java.awt.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import java.util.*;

public class Simulator{
	private ArrayList<Animal> allAnimals = new ArrayList<Animal>();
	private ArrayList<Animal> dead = new ArrayList<Animal>();
    private ArrayList<Animal> born = new ArrayList<Animal>();
    private int dimensions = 1300;


	public static void main(String[] args) {
		new Simulator();
	}

	public Simulator() {
		//create lots of animals to test for lag capacity
		for (int i = 0; i<4; i++) {
			allAnimals.add(new Animal());
		}
		for (int i = 0; i<4; i++) {
			allAnimals.add(new Animal("killer", true, 4, 2, 75, 3, 1, 600, 1, 5, 600, 1, 6.0, new ArrayList<Double>(Arrays.asList(0.0, 0.0)), 
			new ArrayList<Double>(Arrays.asList(Math.random()*1300, Math.random()*1300)), Color.RED));
		}
		for (int i = 0; i<3; i++) {
			allAnimals.add(new Animal("killer1", true, 5, 3, 75, 3, 1, 600, 1, 6, 600, 1, 6.0, new ArrayList<Double>(Arrays.asList(0.0, 0.0)), 
			new ArrayList<Double>(Arrays.asList(Math.random()*1300, Math.random()*1300)), Color.GREEN));
		}


		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                SimulatorEngine se = new SimulatorEngine(dimensions);
                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(se);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                new Thread(se).start();
            }
        });
	}

	public class SimulatorEngine extends JPanel implements Runnable{
		
		private ArrayList<ArrayList<Double>> curPositions = new ArrayList<>();
		private int dimensions;

		public SimulatorEngine(int dimensions) {
			this.dimensions = dimensions;
		}

		@Override
		public void run() {
			ArrayList<String> speciesCount = new ArrayList<>();
			for (Animal a : allAnimals) {
				if (!speciesCount.contains(a.getSpecies())) {
					speciesCount.add(a.getSpecies());
				}
			}
			System.out.println(speciesCount + " " + speciesCount.size());
			while (speciesCount.size()>1) {
				speciesCount.clear();
				for (int i = 0; i < allAnimals.size(); i++) {
	            	//System.out.println(allAnimals.get(i).getCurPosition());
	            	if (!speciesCount.contains(allAnimals.get(i).getSpecies())) {
						speciesCount.add(allAnimals.get(i).getSpecies());
					}

	            	Animal curAnimal = allAnimals.get(i);
	            	int range = curAnimal.survey();
	            	ArrayList<Animal> foundAnimals = new ArrayList<>();
	            	ArrayList<Animal> satisfyTime = new ArrayList<>();
	            	for (Animal b : curAnimal.reproducedWith.keySet()) {
	            		if (curAnimal.getCurAge()-200 > curAnimal.reproducedWith.get(b)) {
	            			satisfyTime.add(b);
	            		}
	            	}
	            	for (Animal b : satisfyTime) {
	            		curAnimal.reproducedWith.remove(b);
	            	}
	            	for (Animal b : allAnimals) {
	            		if (!b.equals(curAnimal)){
	            			ArrayList<Boolean> distWrap = withinRange(range, curAnimal.getCurPosition(), b.getCurPosition(), curAnimal.getSize(), b.getSize());
	            			if (distWrap.get(1)) {
	            				//System.out.println("collision");
	            				animalCollision(curAnimal, b);
	            			} else {
	            				if (distWrap.get(0)) {
	                				foundAnimals.add(b);
	                			}
	            			}
	            		}

	            	}
	            	if (foundAnimals.size()>0) {
	            		curAnimal.setCurPosition(checkBounds(curAnimal.seek(foundAnimals)));
	            	} else {
	            		curAnimal.setCurPosition(checkBounds(curAnimal.wander()));
	            	}
	            	if (curAnimal.incrementAge()) {
	            		dead.add(curAnimal);
	            	}
	            }
	            for (Animal a : dead) {
	            	allAnimals.remove(a);
	            } 
	            dead.clear();
	            for (Animal a : born) {
	            	allAnimals.add(a);
	            }
	            born.clear();
	            try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            repaint();
                        }
                    });
                } catch (InterruptedException exp) {
                    exp.printStackTrace();
                } catch (InvocationTargetException exp) {
                    exp.printStackTrace();
                }//not working still buffering and blinking
	            
	            /*SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    repaint();
	                }
	            });*/
	            /*while (isVisible()) {
	                System.out.println("a");
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    System.out.println("interrupted");
	                }

	                try {
	                    SwingUtilities.invokeAndWait(new Runnable() {
	                        @Override
	                        public void run() {
	                            repaint();
	                        }
	                    });
	                } catch (InterruptedException exp) {
	                    exp.printStackTrace();
	                } catch (InvocationTargetException exp) {
	                    exp.printStackTrace();
	                }
	            }*/

	            try {
	                Thread.sleep(30);
	            } catch (InterruptedException ex) {}
			}
        }

        private void animalCollision(Animal a, Animal b) {
        	if (a.getSpecies().equals(b.getSpecies())) {
        		if (!a.reproducedWith.containsKey(b) && a.getFood() >= 75 && b.getFood()>=75 && a.getCurAge() > 200 && b.getCurAge() > 200) {
        			Animal child = new Animal(a);
        			ArrayList<Double> birthplace = new ArrayList<Double>(Arrays.asList(Math.random()*1300, Math.random()*1300));
        			child.setCurPosition(birthplace);
        			child.setCurAge(0);
        			child.reproducedWith.put(a, 0);
        			child.reproducedWith.put(b, 0);
        			born.add(child);
        			a.reproducedWith.put(b, a.getCurAge());
        			a.reproducedWith.put(child, a.getCurAge());
        			b.reproducedWith.put(a, b.getCurAge());
        			b.reproducedWith.put(child, b.getCurAge());
        		}
        	} else {
        		if (a.getPredator() || b.getPredator()) {
        			if (a.getPredator() && b.getPredator()) {
        				if (a.getSize() > b.getSize()) {
        					dead.add(b);
        					a.addFood(b.getSize());
        				} else if (b.getSize() > a.getSize()){
        					dead.add(a);
        					b.addFood(a.getSize());
        				}
        			} else if (a.getPredator() && b.getSize() < 1.5 * a.getSize()) {
        				dead.add(b);
        				a.addFood(b.getSize());
        			} else if (b.getPredator() && a.getSize() < 1.5 * b.getSize()) {
        				dead.add(a);
        				b.addFood(a.getSize());
        			}
        		}
        	}
        }

        private double getDistance(double x1, double y1, double x2, double y2, double sz) {
        	if(x1-x2 > sz/2) {
        		x1 -= sz;
        	} else if(x2-x1 > sz/2) {
        		x2 -= sz;
        	}
        	if(y1-y2 > sz/2) {
        		y1 -= sz;
        	} else if(y2-y1 > sz/2) {
        		y2 -= sz;
        	}
        	return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
        }

        private ArrayList<Boolean> withinRange(int range, ArrayList<Double> curPos, ArrayList<Double> otherPos, int size1, int size2) {
        	double cX = curPos.get(0);
        	double cY = curPos.get(1);
        	double bX = otherPos.get(0);
        	double bY = otherPos.get(1);
        	double distance = getDistance(cX, cY, bX, bY, dimensions);
        	ArrayList<Boolean> answer = new ArrayList<>();
        	if (distance <= range) {
        		answer.add(true);
        	} else {
        		answer.add(false);
        	}
        	if (distance <= size1+size2) {
        		answer.add(true);
        	} else {
        		answer.add(false);
        	}
        	return answer;
        }

        private ArrayList<Double> checkBounds(ArrayList<Double> curPos) {
        	double x = curPos.get(0);
        	double y = curPos.get(1);
        	if (x > dimensions) {
            	curPos.set(0, x-dimensions);
            } else if (x < 0) {
            	curPos.set(0, x+dimensions);
            }
            if (y > dimensions) {
            	curPos.set(1, y-dimensions);
            } else if (y < 0) {
            	curPos.set(1, y+dimensions);
            }
            return curPos;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(dimensions, dimensions);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Animal a : allAnimals) {
            	a.paint(g2d);
            }
            g2d.dispose();
        }
	}

	/*public class TestPane extends JPanel {

        

        
        //private int radius = a.getSize()*10;

        public TestPane() {
            Timer timer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {}
                    
            });
            timer.start();
        }

        

        
    }*/
}