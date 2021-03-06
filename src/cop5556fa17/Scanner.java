/* *
 * Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2017.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2017 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2017
  */

package cop5556fa17;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.Character;
import java.lang.Integer;

public class Scanner {
	
	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {
		
		int pos;

		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}
		
		public int getPos() { return pos; }

	}

	public static enum Kind {
		IDENTIFIER, INTEGER_LITERAL, BOOLEAN_LITERAL, STRING_LITERAL, 
		KW_x/* x */, KW_X/* X */, KW_y/* y */, KW_Y/* Y */, KW_r/* r */, KW_R/* R */, KW_a/* a */, 
		KW_A/* A */, KW_Z/* Z */, KW_DEF_X/* DEF_X */, KW_DEF_Y/* DEF_Y */, KW_SCREEN/* SCREEN */, 
		KW_cart_x/* cart_x */, KW_cart_y/* cart_y */, KW_polar_a/* polar_a */, KW_polar_r/* polar_r */, 
		KW_abs/* abs */, KW_sin/* sin */, KW_cos/* cos */, KW_atan/* atan */, KW_log/* log */, 
		KW_image/* image */,  KW_int/* int */, 
		KW_boolean/* boolean */, KW_url/* url */, KW_file/* file */, OP_ASSIGN/* = */, OP_GT/* > */, OP_LT/* < */, 
		OP_EXCL/* ! */, OP_Q/* ? */, OP_COLON/* : */, OP_EQ/* == */, OP_NEQ/* != */, OP_GE/* >= */, OP_LE/* <= */, 
		OP_AND/* & */, OP_OR/* | */, OP_PLUS/* + */, OP_MINUS/* - */, OP_TIMES/* * */, OP_DIV/* / */, OP_MOD/* % */, 
		OP_POWER/* ** */, OP_AT/* @ */, OP_RARROW/* -> */, OP_LARROW/* <- */, LPAREN/* ( */, RPAREN/* ) */, 
		LSQUARE/* [ */, RSQUARE/* ] */, SEMI/* ; */, COMMA/* , */, EOF;
	}

	/** Class to represent Tokens. 
	 * 
	 * This is defined as a (non-static) inner class
	 * which means that each Token instance is associated with a specific 
	 * Scanner instance.  We use this when some token methods access the
	 * chars array in the associated Scanner.
	 * 
	 * 
	 *
	 *
	 */
	public class Token {
		public final Kind kind;
		public final int pos;
		public final int length;
		public final int line;
		public final int pos_in_line;

		public Token(Kind kind, int pos, int length, int line, int pos_in_line) {
			super();
			this.kind = kind;
			this.pos = pos;
			this.length = length;
			this.line = line;
			this.pos_in_line = pos_in_line;
		}

		public String getText() {
			if (kind == Kind.STRING_LITERAL) {
				return chars2String(chars, pos, length);
			}
			else return String.copyValueOf(chars, pos, length);
		}

		/**
		 * To get the text of a StringLiteral, we need to remove the
		 * enclosing " characters and convert escaped characters to
		 * the represented character.  For example the two characters \ t
		 * in the char array should be converted to a single tab character in
		 * the returned String
		 * 
		 * @param chars
		 * @param pos
		 * @param length
		 * @return
		 */
		private String chars2String(char[] chars, int pos, int length) {
			StringBuilder sb = new StringBuilder();
			for (int i = pos + 1; i < pos + length - 1; ++i) {// omit initial and final "
				char ch = chars[i];
				if (ch == '\\') { // handle escape
					i++;
					ch = chars[i];
					switch (ch) {
					case 'b':
						sb.append('\b');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r'); //for completeness, line termination chars not allowed in String literals
						break;
					case 'n':
						sb.append('\n'); //for completeness, line termination chars not allowed in String literals
						break;
					case '\"':
						sb.append('\"');
						break;
					case '\'':
						sb.append('\'');
						break;
					case '\\':
						sb.append('\\');
						break;
					default:
						assert false;
						break;
					}
				} else {
					sb.append(ch);
				}
			}
			return sb.toString();
		}

		/**
		 * precondition:  This Token is an INTEGER_LITERAL
		 * 
		 * @returns the integer value represented by the token
		 */
		public int intVal() {
			assert kind == Kind.INTEGER_LITERAL;
			return Integer.valueOf(String.copyValueOf(chars, pos, length));
		}

		public String toString() {
			return "[" + kind + "," + String.copyValueOf(chars, pos, length)  + "," + pos + "," + length + "," + line + ","
					+ pos_in_line + "]";
		}

		/** 
		 * Since we overrode equals, we need to override hashCode.
		 * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
		 * 
		 * Both the equals and hashCode method were generated by eclipse
		 * 
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((kind == null) ? 0 : kind.hashCode());
			result = prime * result + length;
			result = prime * result + line;
			result = prime * result + pos;
			result = prime * result + pos_in_line;
			return result;
		}

		/**
		 * Override equals method to return true if other object
		 * is the same class and all fields are equal.
		 * 
		 * Overriding this creates an obligation to override hashCode.
		 * 
		 * Both hashCode and equals were generated by eclipse.
		 * 
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Token other = (Token) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (kind != other.kind)
				return false;
			if (length != other.length)
				return false;
			if (line != other.line)
				return false;
			if (pos != other.pos)
				return false;
			if (pos_in_line != other.pos_in_line)
				return false;
			return true;
		}

		/**
		 * used in equals to get the Scanner object this Token is 
		 * associated with.
		 * @return
		 */
		private Scanner getOuterType() {
			return Scanner.this;
		}

	}

	/** 
	 * Extra character added to the end of the input characters to simplify the
	 * Scanner.  
	 */
	static final char EOFchar = 0;
	
	/**
	 * The list of tokens created by the scan method.
	 */
	final ArrayList<Token> tokens;
	
	/**
	 * An array of characters representing the input.  These are the characters
	 * from the input string plus and additional EOFchar at the end.
	 */
	final char[] chars;  
	
	/*
	 * Hash table for keywords
	 */
	HashMap<String,Kind> hmKeywords; 

	/**
	 * position of the next token to be returned by a call to nextToken
	 */
	private int nextTokenPos = 0;

	Scanner(String inputString) {
		int numChars = inputString.length();
		//System.out.println(numChars);
		this.chars = Arrays.copyOf(inputString.toCharArray(), numChars + 1); // input string terminated with null char
		chars[numChars] = EOFchar;
		tokens = new ArrayList<Token>();
		hmKeywords = new HashMap<>();
		
		/*
		 * Populating Hash Table with keywords
		 * */
		for (Kind k : Kind.values()) {
			String s = k.toString();
			if (s.charAt(0) == 'K' && s.charAt(1) == 'W' && s.charAt(2) == '_') {
				hmKeywords.put(k.toString().substring(3), k);
			}
		}
	}
	
	/*Add this later to reduce lines of code
	 * public void addSingleLengthTokens() {
		
	}*/


	/**
	 * Method to scan the input and create a list of Tokens.
	 * 
	 * If an error is encountered during scanning, throw a LexicalException.
	 * 
	 * @return
	 * @throws LexicalException
	 */
	public Scanner scan() throws LexicalException {
		/* TODO  Replace this with a correct and complete implementation!!! */
		int pos = 0;
		int line = 1;
		int posInLine = 1;
		
		/*if (chars.length == 1) {	//Empty String
			tokens.add(new Token(Kind.EOF, pos, 0, line, posInLine)); //Adding EOF token
			return this;
		}*/
		
		//Input char array
		for (int i =0; i< chars.length - 1; i++) { //-1 for EOFchar
			char c = chars[i];
			switch (c) {
				/*
				 * Single Length Separators
				 * */
				case ';':
					tokens.add(new Token(Kind.SEMI, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case ',':
					tokens.add(new Token(Kind.COMMA, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '(':
					tokens.add(new Token(Kind.LPAREN, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case ')':
					tokens.add(new Token(Kind.RPAREN, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '[':
					tokens.add(new Token(Kind.LSQUARE, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case ']':
					tokens.add(new Token(Kind.RSQUARE, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				/*
				 * Single Length Operators
				 * */
				case '?':
					tokens.add(new Token(Kind.OP_Q, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case ':':
					tokens.add(new Token(Kind.OP_COLON, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '@':
					tokens.add(new Token(Kind.OP_AT, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '&':
					tokens.add(new Token(Kind.OP_AND, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '|':
					tokens.add(new Token(Kind.OP_OR, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '+':
					tokens.add(new Token(Kind.OP_PLUS, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
				case '%':
					tokens.add(new Token(Kind.OP_MOD, pos, 1, line, posInLine));
					pos++;
					posInLine++;
					break;
					
				/*
				 * Double Length Operators	
				 * */					
				//Do we increment pos and posInLine by length of token?
				//Check the if cases
				case '*':		
					//Power case has '**'
					if (i+1 < chars.length-1 && chars[i+1] == '*') {
						tokens.add(new Token(Kind.OP_POWER, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else {
						tokens.add(new Token(Kind.OP_TIMES, pos, 1, line, posInLine));
					}
					pos++;
					posInLine++;
					break;
					
				case '=':		
					if (i+1 < chars.length-1 && chars[i+1] == '=') {
						tokens.add(new Token(Kind.OP_EQ, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else {
						tokens.add(new Token(Kind.OP_ASSIGN, pos, 1, line, posInLine));
					}
					pos++;
					posInLine++;
					break;
				
				case '!':		
					if (i+1 < chars.length-1 && chars[i+1] == '=') {
						tokens.add(new Token(Kind.OP_NEQ, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else {
						tokens.add(new Token(Kind.OP_EXCL, pos, 1, line, posInLine));
					}
					pos++;
					posInLine++;
					break;
				
				case '<':		
					if (i+1 < chars.length-1 && chars[i+1] == '=') {
						tokens.add(new Token(Kind.OP_LE, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else if (i+1 < chars.length-1 && chars[i+1] == '-') {
						tokens.add(new Token(Kind.OP_LARROW, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else {
						tokens.add(new Token(Kind.OP_LT, pos, 1, line, posInLine));
					}
					pos++;
					posInLine++;
					break;
				
				case '>':		
					if (i+1 < chars.length-1 && chars[i+1] == '=') {
						tokens.add(new Token(Kind.OP_GE, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else {
						tokens.add(new Token(Kind.OP_GT, pos, 1, line, posInLine));
					}
					pos++;
					posInLine++;
					break;
					
				case '-':
					if (i+1 < chars.length-1 && chars[i+1] == '>') {
						tokens.add(new Token(Kind.OP_RARROW, pos, 2, line, posInLine));
						i++;
						pos++;
						posInLine++;
					}
					else {
						tokens.add(new Token(Kind.OP_MINUS, pos, 1, line, posInLine));
					}
					pos++;
					posInLine++;
					break;						
				
				/*
				 * Integer Literal 
				 * Only 0
				 * For these check java int overflow
				 * */
					
				case '0':
					if (Character.isDigit(c)) {	//Is this needed??
						tokens.add(new Token(Kind.INTEGER_LITERAL, pos, 1, line, posInLine));
						pos++;
						posInLine++;
					}
					break;
					
				/*
				 * Single Line Comments
				 * Skip these but keep incrementing pointers
				 * */	
				// Add code for \r and \r\n
				case '/':
					if (i+1 < chars.length-1 && chars[i+1] == '/') {
						i=i+2;
						pos=pos+2;
						//posInLine += 2; Not sure about this
						while(i < chars.length-1 && chars[i] != '\n' && chars[i] != '\r') {
							pos++;
							i++;
						}
						if (i < chars.length-1 && (chars[i] == '\n' || chars[i] == '\r')) {
							pos++;
							line++;
							posInLine = 1;
							//Should I increment pos by 1 or 2?
							if (chars[i] == '\r' && i+1 < chars.length-1 && chars[i+1] == '\n') {
								i++;
								pos++;
							}
						}
					}
					else {
						tokens.add(new Token(Kind.OP_DIV, pos, 1, line, posInLine));
						pos++;
						posInLine++;
					}
					break;	
					
				/*
				 * Whitespace - Increment pos, posInLine and line if newline but don't to tokens list
				 * */
				case ' ': case '\t': case '\f':
					pos++;
					posInLine++; 
					break;
				case '\n': 
					pos++;
					line++;
					posInLine=1;
					break;
				case '\r':
					//Should I increment pos by 1 or 2?
					if (i+1 < chars.length-1 && chars[i+1] == '\n') {
						i++;
						pos++;
					}
					pos++;
					line++;
					posInLine=1;
					break;
					
				default:
					
					/* 
					 * Identifiers, Keywords and Boolean Literals
					 * For getting keywords use hash table
					 * Check for boolean literals 
					 */
					//if (Character.isJavaIdentifierStart(c)) {
					if (Character.isLetter(c) || c == '_' || c == '$') {
						int startPos = pos;
						int len = 1;
						int startposInLine = posInLine;
						i++;
						pos++;
						posInLine++;
						//while(i < chars.length-1 && Character.isJavaIdentifierPart(chars[i])) {
						while(i < chars.length-1 && (Character.isLetterOrDigit(chars[i]) 
								|| chars[i] == '_' || chars[i] == '$')) {
							i++;
							len++;
							pos++;
							posInLine++;
						}
						
						//if (i < chars.length-1 && !Character.isJavaIdentifierPart(chars[i])) {
						if (i < chars.length-1 && !(Character.isLetterOrDigit(chars[i])
								|| chars[i] == '_' || chars[i] == '$')) {
							i--;
						}
						
						String s = String.copyValueOf(chars, startPos, len);
						//Boolean Literals
						if (s.equals("true") || s.equals("false")) {
							tokens.add(new Token(Kind.BOOLEAN_LITERAL, startPos, len ,line, startposInLine));
						}
						//Keywords
						else if (hmKeywords.containsKey(s)) {
							tokens.add(new Token(hmKeywords.get(s), startPos, len ,line, startposInLine));
						}
						//Identifiers
						else {
							tokens.add(new Token(Kind.IDENTIFIER, startPos, len ,line, startposInLine));	
						}
					}
				
					/*
					 * String Literals
					 * newline and return not allowed
					 */
					
					else if (c == '"') {
						int startPos = pos;
						int len = 1;
						int startposInLine = posInLine;	
						i++;
						pos++;
						posInLine++;
						while (i < chars.length-1 && chars[i] != '"') {
							if (chars[i] == '\n' || chars[i] == '\r') {
								throw new LexicalException("Newline character in string literal", pos);
							}
							
							char ch = chars[i];
							if (ch == '\\') { // handle escape
								if (i+1 < chars.length-1) {
									i++;
									ch = chars[i];
									switch (ch) {
									case 'b': case 't': case 'f': case '\"': case '\'': case '\\': case 'r': case 'n':
										len++;
										pos++;
										posInLine++;
										break;
									default:
										pos++;
										posInLine++;
										throw new LexicalException("Invalid escape character in string literal", pos);
									}
								}
							}
							i++;
							len++;
							pos++;
							posInLine++;
						}
						if (i < chars.length-1 && chars[i] == '"') {
							len++;
							pos++;
							posInLine++;
							tokens.add(new Token(Kind.STRING_LITERAL, startPos, len ,line, startposInLine));
						}
						else if (i >= chars.length-1) {	//Eof
							throw new LexicalException("Missing closing \" in the string literal!!", pos);
						}
					}	
				
					/*
					 * Integer Literals 
					 * Convert to int
					 * For these check java int overflow and throw Lexical Exception
					 * */
					
					else if (c != '0' && Character.isDigit(c)) {
						int startPos = pos;
						int len = 1;
						int startposInLine = posInLine;	
						i++;
						pos++;
						posInLine++;
						while (i < chars.length-1 && Character.isDigit(chars[i])) {
							i++;
							len++;
							pos++;
							posInLine++;
						}
						if (i < chars.length-1 && !Character.isDigit(chars[i])) {
							i--;
						}
						try {
							Integer.parseInt(String.copyValueOf(chars, startPos, len));
						}
						catch(NumberFormatException e){
							//Add line number
							throw new LexicalException("Error!! Integer Overflow at: " + startPos, startPos);
						}
						tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, len ,line, startposInLine));
					}	
						
					/*
					 * Illegal Characters (not included in grammar)
					 * Show pos as well
					 * */
					else {
						throw new LexicalException("Illegal character found!!", pos);
					}		
					break;
			}
		}
		tokens.add(new Token(Kind.EOF, pos, 0, line, posInLine)); //Adding EOF token
		return this;

	}


	/**
	 * Returns true if the internal iterator has more Tokens
	 * 
	 * @return
	 */
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}

	/**
	 * Returns the next Token and updates the internal iterator so that
	 * the next call to nextToken will return the next token in the list.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * @return
	 */
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}
	
	/**
	 * Returns the next Token, but does not update the internal iterator.
	 * This means that the next call to nextToken or peek will return the
	 * same Token as returned by this methods.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * 
	 * @return next Token.
	 */
	public Token peek() {
		return tokens.get(nextTokenPos);
	}
	
	
	/**
	 * Resets the internal iterator so that the next call to peek or nextToken
	 * will return the first Token.
	 */
	public void reset() {
		nextTokenPos = 0;
	}

	/**
	 * Returns a String representation of the list of Tokens 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tokens:\n");
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append('\n');
		}
		return sb.toString();
	}

}
