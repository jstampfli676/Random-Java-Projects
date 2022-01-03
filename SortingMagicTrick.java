import java.util.*;

public class SortingMagicTrick {
    public static ArrayList<Integer> check(int[] order) {
        ArrayList<Integer> group = new ArrayList<>();
        ArrayList<Integer> init = new ArrayList<>(Arrays.asList(1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4));
        order = removeZeros(order);
        if (order.length==0) {
            return null;
        }
        int size = init.size();
        int index = 0;
        while (init.size()>0) {
            for (int i = 0; i<size; i++) {
                index+=order[i%order.length]-1;
                index = index%init.size();
                int temp = init.get(index);
                group.add(temp);
                init.remove(index);
            }
        }
        //System.out.println(group);
        return group;
    }
    
    private static int[] removeZeros(int[] input) {
        int zerosCount = 0;
        for (int i = 0; i<input.length; i++) {
            if (input[i]==0) {
                zerosCount++;
            }
        }
        int[] answer = new int[input.length-zerosCount];
        int skipped = 0;
        for (int i = 0; i<input.length; i++) {
            if (input[i]!=0) {
                answer[i-skipped]=input[i];
            } else {
                skipped++;
            }
        }
        return answer;
    }
    
    private static int max(int a, int b) {
        return a>b?a:b;
    }
    
    private static boolean checkGroup(ArrayList<Integer> input, int max, int cards) {
        if (input==null) {
            return false;
        }
        
        int x = 0;
        while (x<max-1) {
            for (int i = 1; i<cards; i++) {
                if (input.get(0+x)!=input.get(0+x+max*(i))) {
                    return false;
                }
            }
            x++;
        }
        
        return true;
    }
    
    public static void print(int[] input) {
        for (int i = 0; i<input.length; i++) {
            System.out.print(input[i]+" ");
        }
    }

    /*for (int i = 0; i<answer.length; i++) { generate random numbers within range
                answer[i] = (int)(Math.random()*max);
                while (answer[i]==1) {
                    answer[i] = (int)(Math.random()*max);
                }
            }*/
    
    public static void main(String args[]) {
        boolean done = false;
        int size = 8;
        int[] answer = new int[size];
        for (int i = 0; i<size; i++) {
            answer[i]=0;
        }
        int max = 20;
        while (!done) {
            
            answer[size-1]+=1;
            for (int i = size-1; i >= 0; i--) {
                if (answer[i]>max) {
                    if (i==0) {
                        System.out.println("failed");
                        return;
                    } else if (i==1 || i==2) {
                        int curPlace = i-1;
                        System.out.println("the current value of "+curPlace+" is "+answer[curPlace]);
                    }
                    answer[i-1]+=1;
                    answer[i]=0;
                } else if (answer[i]==1) {
                    answer[i]+=1;
                }
            }
            done = checkGroup(check(answer), 4, 6);
        }
        print(answer);
        //System.out.println(check(new int[]{6,17,2,19,10,9,13}));
    }
}