package com.scnu.fightofbit.utils;

import com.scnu.fightofbit.TokenType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Scan {
	public static enum StateType{START,INASSIGN,INCOMMENT,INNUM,INID,DONE};
	public static StringBuffer tokenString;
	public static String lineBuf;  //读入的当前行数据
	public static int linepos;           //lineBuf中的位置
	public static FileReader fr;
	public static BufferedReader br;
	public static boolean FileEOF; //文件是否读完
	public static int filelineno; //定位代码在源文件第几行
	
	public static HashMap<String, String> reservedWords = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
	        put("if", "IF");  
	        put("then", "THEN");
	        put("else","ELSE");
	        put("end","END");
	        put("stategy","STATEGY");
	        put("choose","CHOOSE");
	        put("var","VAR");
	        put("random","RANDOM");
	        put("mypre","MYPRE");
	        put("enemypre","ENEMYPRE");
	    }
	};

	public static void readFile(String filepath){
		try {
			FileEOF = false;
			filelineno = 0;
			linepos = 0;
			tokenString = new StringBuffer();
			lineBuf = new String();
			fr = new FileReader(filepath);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}



	public static char getNextChar(){
		if(FileEOF) return '$';
		if(linepos<lineBuf.length())
		{
			return lineBuf.charAt(linepos++);
		}else{		
			try {
				if((lineBuf = br.readLine())!=null){
					linepos=0;
					filelineno++;
					return lineBuf.charAt(linepos++);
				}
				else{
					br.close();
					fr.close();
					FileEOF = true;
					return '$';
				}
			} catch (IOException e) {
				e.printStackTrace();
				return '$';
			}//catch		        	        
		}//else
	}
	
	public static void ungetNextChar() {
		linepos--;
	}
	
	public static TokenType reservedLookup(String s){
		if(reservedWords.get(s) != null)
			return TokenType.valueOf(reservedWords.get(s));
		else
			return TokenType.ID;
	}
	
	public static boolean isAlpha(char c){
		int i = (int)c;  
		if((i>=65&&i<=90)||(i>=97&&i<=122))   
			return true;  
		else return false;     
	}
	
	public static TokenType getToken(){
	   TokenType currentToken = null; //当前类型
	   StateType state = StateType.START; //当前DFA中的状态
	   if(tokenString.length()!=0)
		   tokenString.delete(0, tokenString.length());//清空字符串
	   boolean save; //是否需要存储单词
	   
	   while (state != StateType.DONE)
	   { 
		   char c = getNextChar();
		   save = true;
		   switch (state)
		   {
		   case START:
			   if (Character.isDigit(c))
				   state = StateType.INNUM;
			   else if (isAlpha(c))
				   state = StateType.INID;
			   else if (c == ':')
				   state = StateType.INASSIGN;
			   else if ((c == ' ') || (c == '\t') || (c == '\n'))
				   save = false;
			   else if (c == '{'){
				   save = false;
				   state = StateType.INCOMMENT;
			   }else{
				   state = StateType.DONE;
				   switch (c)
				   {
				   	 case '$': //文件结束
				   		 save = false;
				   		 currentToken = TokenType.ENDFILE;
				   		 break;
				   	 case '=':
				   		 currentToken = TokenType.EQ;
				   		 break;
				   	 case '<':
				   		 currentToken = TokenType.LT;
				   		 break;
				   	 case '+':
				   		 currentToken = TokenType.PLUS;
				   		 break;
				   	 case '-':
				   		 currentToken = TokenType.MINUS;
				   		 break;
				   	 case '*':
				   		 currentToken = TokenType.TIMES;
				   		 break;
				   	 case '/':
				   		 currentToken = TokenType.OVER;
				   		 break;
				   	 case '(':
				   		 currentToken = TokenType.LPAREN;
				   		 break;
				   	 case ')':
				   		 currentToken = TokenType.RPAREN;
				   		 break;
				   	 case ';':
				   		 currentToken = TokenType.SEMI;
				   		 break;
				   	 default:
				   		 currentToken = TokenType.ERROR;
				   		 break;
				   }//switch(c)
			   }//else
			   break;
	       case INCOMMENT:
	    	   save = false;
	    	   if (c == '$'){
	    		   state = StateType.DONE;
	    		   currentToken = TokenType.ENDFILE;
	    	   }else if (c == '}') state = StateType.START;
	    	   break;
	       case INASSIGN:
	    	   state = StateType.DONE;
	    	   if (c == '=')
	    		   currentToken = TokenType.ASSIGN;
	    	   else{
	    		   ungetNextChar();
	    		   save = false;
	    		   currentToken = TokenType.ERROR;
	    	   }
	    	   break;
	       case INNUM:
	    	   if (!Character.isDigit(c)){	    		   
		           ungetNextChar();
		           save = false;
		           state = StateType.DONE;
		           currentToken = TokenType.NUM;
	    	   }
	    	   break;
	       case INID:
	    	   if (!(isAlpha(c)||Character.isDigit(c))){
	    		   ungetNextChar();
	    		   save = false;
	    		   state = StateType.DONE;
	    		   currentToken = TokenType.ID;
	    	   }
	    	   break;
	       case DONE:
	       default: 
	     }
		   
	     if (save)
	       tokenString.append(c);
	     
	     if (state == StateType.DONE){
	       if (currentToken == TokenType.ID)
	         currentToken = reservedLookup(tokenString.toString());
	     }//if
	     
	   }//while	  
	   return currentToken;	   
	}//end getToken
	

}
