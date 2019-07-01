package analyse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

import java.util.HashSet;
import java.util.List;


/*
 * 一些基本语法:
    exp -> exp {addop term}
    addop -> + \ -
    term -> Negative {mulop Negative}
    mulop -> *|/
    Nagative -> -factor|factor
    factor -> (exp) |number

 */
class StateAnalyze {

    public LinkedList<Toke> tokes;
    public Toke tok;
    public int index;
    String fail;
    public String output="";

    public StateAnalyze(LinkedList<Toke> list)
    {
        this.tokes=list;
    }
    public void Error(String a) throws CodeException
    {
        System.err.print(a);
        throw new CodeException(a);
    }
    public void match(String a) throws CodeException
    {
        if (tok.word.equals(a)||tok.type.equals(a))
        {
            if (index<tokes.size())
            {
                tok=tokes.get(index++);
            }
        }
        else
        {
            Error("wrong word! expected："+a+" actually:"+tok.word+"index:"+(index+1)+";line:"+tok.line);

        }
    }
    public void start_analyse()
    {
        index=0;
        try {
            tok= tokes.get(index++);
            exp();
        }catch (CodeException e)
        {
            fail=e.message;
        }
    }
    public void exp() throws CodeException
    {
        term();
        while(tok.word.equals("+")||tok.word.equals("-"))
        {
            String s=tok.word;
            match(tok.word);
            term();
            output+=s;
            output+="|";
        }

    }
    public void term() throws CodeException
    {
        Negative();
        while (tok.word.equals("*")||tok.word.equals("/"))
        {
            String s=tok.word;
            match(tok.word);
            Negative();
            output+=s;
            output+="|";
        }

    }
    public void Negative() throws CodeException
    {
        if (tok.word.equals("-"))
        {
            String s=tok.word;
            match("-");
            output+=s;
            factor();
        }
        else factor();
    }
    public void factor() throws CodeException
    {
        char c=tok.word.charAt(0);
        if (tok.word.equals("("))
        {
            match("(");
            exp();
            match(")");
        }
        else if (tok.type.equals("number"))
        {
            int temp=Integer.parseInt(tok.word);
            String s=tok.word;
            match("number");
            output+=s;
            output+="|";
        }
        else Error("不是合法的表达式！！！ (列:"+(index)+",行:"+tok.line+")");
    }

    public static void main(String args[])
    {
        Users users=new Users("test.txt");
        System.out.println(users.wordAnalyse.tokes);
        users.stateAnalyze.start_analyse();
        System.out.println(users.stateAnalyze.output);
    }
}

class CodeException extends Exception {
    public String message;
    public CodeException(String s)
    {
        super();
        message=s;
    }
}


/*
 * 一些基本语法:
    exp -> exp {addop term}
    addop -> + \ -
    term -> Negative {mulop Negative}
    mulop -> *|/
    Nagative -> -factor|factor
    factor -> (exp) |number

 */


class Users {

    public WordAnalyse wordAnalyse;
    public StateAnalyze stateAnalyze;
    public String name;
    public Users(String filepath)
    {
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(filepath));
            StringBuffer stringBuffer=new StringBuffer();
            String line=bufferedReader.readLine();
            while(line!=null)
            {
                stringBuffer.append(line+'\n');
                line=bufferedReader.readLine();
            }
            name=filepath;
            wordAnalyse=new WordAnalyse(stringBuffer.toString());
            wordAnalyse.analyse();
            stateAnalyze=new StateAnalyze(wordAnalyse.tokes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Users(){}

}

class Toke {
    //public static int no=0;

    int number;
    String word;
    String type;
    int line;

    public Toke(int n, int l)
    {
        this.number=n;
        this.line=l;
    }

    @Override
    public String toString() {
        return number+"+"+word+"+"+type+'\n';
    }
}
//词法分析器
public class WordAnalyse {

    public LinkedList<Toke> tokes;
    public String source;

    int no;
    int line;

    final static int NUMBER=1;
    final static int CHAR=2;
    final static int START=0;

    public static HashSet<String> reserer=new HashSet<>();


    public WordAnalyse(String s){

        source=s;
        tokes =new LinkedList<>();
        no=0;
        line=1;
    }

    public int getWhat(char c)
    {
        if(c<='9'&&c>='0')
            return NUMBER;
        else return CHAR;
    }

    public List<Toke> analyse()
    {
        int index=0;
        int state=0;
        String temp="";

        while(index<source.length())
        {
            char cur=source.charAt(index);

            if (state==START)
            {
                state=getWhat(cur);
            }
            switch (state)
            {
                case NUMBER:
                    if ((cur>='0'&&cur<='9')||cur=='.')
                    {
                        temp=temp+cur;
                        cur=source.charAt(index++);
                        continue;
                    }
                    else{
                        Toke t=new Toke(no++,line);
                        t.word=temp;
                        t.type="number";
                        tokes.addLast(t);
                        temp="";
                        state=START;
                    }
                    break;
                case CHAR:
                    switch (cur)
                    {
                        case '\n':
                            line++;
                            index++;
                            state=START;
                            break;
                        case '+':
                        case '-':
                        case '*':
                        case '/':
                        case '(':
                        case ')':
                            Toke t=new Toke(no++,line);
                            temp=temp+cur;
                            t.type="character";
                            t.word=temp;
                            tokes.addLast(t);
                            state=START;
                            temp="";
                            index++;
                            break;
                        default:
                            index++;
                            state=START;
                            break;
                    }
                    break;
            }
        }
        return tokes;
    }
    //测试
    public static void main(String args[]) {
        StringBuffer all = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
            String line = reader.readLine();
            while (line != null) {
                all.append(line);
                all.append('\n');
                line = reader.readLine();
            }
            WordAnalyse w = new WordAnalyse("1--2-3"+"\n");
            w.analyse();
            System.out.print(w.source);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(w.tokes);
        } catch (Exception e) {

        }
    }
}
