package server;

import java.util.Random;

public class MathProtocol {
	
	private static Random rand = new Random();
	
	private static final int WAITING = 0;
	private static final int WANNAPLAY = 1;
	private static final int ANS = 2;
	private static final int GIVEPROB = 3;
	private static final int GIVEREACTION = 4;
	private static final int TERMINATE = 5;
	
	private  int state = WAITING;
	private int n = 0;
	
	private String[] probs = {
	                        "2 + 3 * 4",
	                        "8 - 5 / 2",
	                        "4 * (6 + 2)",
	                        "(15 / 3) + 7",
	                        "10 + 7 * (4 - 2)",
	                        "12 - 9 + 5",
	                        "5 * (8 / 2)",
	                        "18 / (2 + 1)",
	                        "9 + 4 * 2",
	                        "(16 - 6) / 2",
	                        "7 * (3 + 5)",
	                        "(21 / 7) - 1",
	                        "6 + 5 * 2",
	                        "20 - (8 / 2)",
	                        "3 * (9 + 2)",
	                        "(27 / 3) - 2",
	                        "8 + 2 * 3",
	                        "15 - (11 + 3)",
	                        "4 * 4 + 2",
	                        "(16 / 2) + 3"
	                        };
	private String[] GoodReaction = {};
	private String[] BadReaction = {};
	
	public static double StringToMath(final String str) {
	    return new Object() {
	        int pos = -1, ch;
	        
	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }
	        
	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }
	        
	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }
	        
	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)` | number
	        //        | functionName `(` expression `)` | functionName factor
	        //        | factor `^` factor
	        
	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }
	        
	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }
	        
	        double parseFactor() {
	            if (eat('+')) return +parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus
	            
	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                if (!eat(')')) throw new RuntimeException("Missing ')'");
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                if (eat('(')) {
	                    x = parseExpression();
	                    if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
	                } else {
	                    x = parseFactor();
	                }
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }
	            
	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
	            
	            return x;
	        }
	    }.parse();
	}
	
	public String processInput(String Inp) {
		String Oup = "n";
		int cprob = -1;
		if(state == WAITING) {
			Oup = "Do you want to play?";
			n = rand.nextInt(10);
			cprob = rand.nextInt(probs.length-1);
			state = WANNAPLAY;
		}
		else if(state == WANNAPLAY)
		{
			if(Inp.equalsIgnoreCase("Yes")) {
				state = GIVEPROB;
				Oup = probs[cprob];
			}
			else if(Inp.equalsIgnoreCase("No"))
			{
				state = TERMINATE;
			}
		}
		else if(state == GIVEPROB)
		{
			double ans = Integer.parseInt(Oup);
			if(ans == StringToMath(probs[cprob])) {
				Oup = GoodReaction[rand.nextInt(GoodReaction.length-1)];
				state = WAITING;
			}
			else
				Oup = BadReaction[rand.nextInt(BadReaction.length-1)] + ", try again";
			n--;
			state = GIVEREACTION;
		}
		else if(state == GIVEREACTION)
		{
			if(n != 0)
				state = GIVEPROB;
			else{
				state = WAITING;
			}
		}
		else if(state == TERMINATE)
		{
			Oup = "Server terminate";
		}
		
		return Oup;
		
	}
}
