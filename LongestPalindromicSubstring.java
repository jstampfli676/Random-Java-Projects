import java.util.ArrayList;

public class LongestPalindromicSubstring {
	
	public String findLPSeq(String input, String answer) {
		if (input.length()==0) {
			for (int i = answer.length()-1; i>=0; i--) {
				answer+=answer.charAt(i);
			}
			return answer;
		} else if (input.length()==1) {
			answer+=input.charAt(0);
			for (int i = answer.length()-2; i>=0; i--) {
				answer+=answer.charAt(i);
			}
			return answer;
		}

		if (input.charAt(0)==input.charAt(input.length()-1)) {
			answer += input.charAt(0);
			return findLPSeq(input.substring(1, input.length()-1), answer);
		} else {
			return biggerOf(findLPSeq(input.substring(0, input.length()-1), answer), findLPSeq(input.substring(1, input.length()), answer));
		}
	}

	public String findLPStr(String input) {
		ArrayList<String> answers = new ArrayList<String>();
		for (int i = 0; i<input.length(); i++) {
			int x = i-1;
			int j = i+1;
			String answer = String.valueOf(input.charAt(i));
			while (x>=0 && j<input.length()) {
				if (input.charAt(x)==input.charAt(j)) {
					answer = input.charAt(x) + answer;
					answer += input.charAt(x);
				}
				x--;
				j++;
			}
			answers.add(answer);
		}
		
		int maxLen = answers.get(0).length();
		int maxInd = 0;
		for (int i = 1; i<answers.size(); i++) {
			if (answers.get(i).length()>maxLen) {
				maxLen = answers.get(i).length();
				maxInd = i;
			}
		}

		return answers.get(maxInd);
	}

	private String biggerOf(String a, String b) {
		return a.length()>b.length()?a:b;
	}

	public static void main(String[] args) {
		LongestPalindromicSubstring lps = new LongestPalindromicSubstring();
		String input = "abbcdefgfhijklmnopqrstub";
		System.out.println(lps.findLPSeq(input, ""));
	}
}