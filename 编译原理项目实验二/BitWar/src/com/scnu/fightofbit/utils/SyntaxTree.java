package com.scnu.fightofbit.utils;

import com.scnu.fightofbit.TokenType;

import java.util.HashMap;
import java.util.Random;
 
public class SyntaxTree {
    public static enum NodeKind {StmtK, ExpK}

    public static enum StmtKind {IfK, ChooseK, AssignK, StategyK, DeclareK}


    public static enum ExpKind {OpK, ConstK, IdK, RandomK, MypreK, EnemypreK}

    //	public static enum ExpType {Void,Integer,Boolean};
    public static int MinimunInt = -2147483648;


    private TreeNode root; //根节点

    private TokenType token;

    private HashMap<String, Integer> variable; //变量保存表

    private HashMap<Integer, Integer> MyPrevious, EnemyPrevious;//敌我双方的历史选择
    private int CurrentRound; //当前回合数
    private HashMap<String, Integer> ErrorMsg; //错误信息

    private String filePath;

    public String Print(){
        return Print.Graphics(root);
    }

    public SyntaxTree(String filepath) {
        this.filePath = filepath;
        Scan.readFile(filepath);
        variable = new HashMap<String, Integer>();
        MyPrevious = new HashMap<Integer, Integer>();
        EnemyPrevious = new HashMap<Integer, Integer>();
        ErrorMsg = new HashMap<String, Integer>();
        token = Scan.getToken();
        CurrentRound = 1;
    }

    public String getFileName() {
        return filePath.substring(filePath.lastIndexOf("\\")+1);
    }

    public boolean Parse() {
        root = stmt_sequence();
        if (root.nodekind != SyntaxTree.NodeKind.StmtK || root.stmtkind != SyntaxTree.StmtKind.StategyK)
            ErrorMsg.put("必须首先命名策略。", 1);
        if (ErrorMsg.isEmpty())
            return true;
        else return false;
    }


    public String getStategyName() {
        return root.name;
    }

    public String getErrorMsg() {
        StringBuffer t = new StringBuffer();
        for (String s : ErrorMsg.keySet()) {
            t.append("第" + ErrorMsg.get(s) + "行，" + s + "\n");
        }
        return t.toString();
    }

    public int MakeChoice() {
        int res = InnerMakeChoice(root);
        if (res != 1 && res != 0) {
            //error
            return -1;
        } else {
            MyPrevious.put(CurrentRound, res);
            CurrentRound++;
            return res;
        }
    }

    public void SaveEnemyChoice(int c) {
        EnemyPrevious.put(CurrentRound - 1, c);
    }


    private int InnerMakeChoice(TreeNode t) {
        if (t == null) return -1;//策略为空
        int res = -1;
        switch (t.nodekind) {
            case StmtK:
                switch (t.stmtkind) {
                    case StategyK:
                        res = InnerMakeChoice(t.sibling);
                        break;
                    case ChooseK:
                        if (t.child1 == null)
                            res = t.val;
                        else if (t.child1.expkind == ExpKind.MypreK) {
                            int round = ExpResult(t.child1.child1);
                            res = MyPrevious.get(round);
                        } else if (t.child1.expkind == ExpKind.EnemypreK) {
                            int round = ExpResult(t.child1.child1);
                            res = EnemyPrevious.get(round);
                        }
                        break;
                    case AssignK:
                        res = InnerMakeChoice(t.sibling);
                        break;
                    case IfK:
                        if (t.child1.op == TokenType.LT || t.child1.op == TokenType.EQ) //if后面是比较运算
                        {
                            int x = ExpResult(t.child1);
                            if (x == 1)
                                res = InnerMakeChoice(t.child2);
                            else if (x == 0)
                                res = InnerMakeChoice(t.child3);
                            else {
                                //x==MinimunInt 比较出错
                            }
                        } else if (t.child1.op == TokenType.ID) //if后面是一个变量
                        {
                            if (IdValueOf(t.child1.name) != 0)
                                res = InnerMakeChoice(t.child2);
                            else
                                res = InnerMakeChoice(t.child3);
                        } else if (t.child1.op == TokenType.NUM) //if后面是一个常量
                        {
                            if (t.child1.val != 0)
                                res = InnerMakeChoice(t.child2);
                            else
                                res = InnerMakeChoice(t.child3);
                        }
                        break;
                }
            case ExpK:
                ErrorMsg.put("该算术表达式意义。", t.lineno);
                break;
        }
        while (res != 0 && res != 1) {
            if (t.sibling != null)
                res = InnerMakeChoice(t.sibling);
            else break;
        }
        return res;//
    }

//	private int CompareResult(TreeNode t){
//		int a,b;
//		int res = -1 ; //-1为错误或者初始状态，1代表逻辑表达式成立，0代表逻辑表达式不成立
//		
//		if(t.child1.expkind == ExpKind.ConstK)
//			a = t.child1.val;
//		else if(t.child1.expkind == ExpKind.IdK)
//			a = IdValueOf(t.child1.name);
//		else a = ComputeResult(t.child1);
//		
//		if(t.child2.expkind == ExpKind.ConstK)
//			b = t.child2.val;
//		else if(t.child2.expkind == ExpKind.IdK)
//			b = IdValueOf(t.child2.name);
//		else b = ComputeResult(t.child2);
//
//		switch(t.op)
//		{
//		case LT://小于比较			
//			if(a<b) res = 1;
//			else res = 0;
//			break;
//		case EQ://等于比较
//			if(a == b)
//				res = 1;
//			else res = 0;
//			break;
//		}
//		
//		return res;
//	}

    private int ExpResult(TreeNode t) {
        if (t == null)
            return SyntaxTree.MinimunInt;

        if (t.nodekind == NodeKind.ExpK && t.expkind == ExpKind.ConstK)
            return t.val;
        if (t.nodekind == NodeKind.ExpK && t.expkind == ExpKind.RandomK) {
            Random r = new Random();
            int x;
            while ((x = r.nextInt() % (t.val + 1)) < 0) ;
            return x;
        }
        if (t.nodekind == NodeKind.ExpK && t.expkind == ExpKind.MypreK) {
            int round = ExpResult(t.child1);
            if (round < CurrentRound)
                return MyPrevious.get(round);
            else {
                //出错，回合数不合理
            }

        }
        if (t.nodekind == NodeKind.ExpK && t.expkind == ExpKind.EnemypreK) {
            int round = ExpResult(t.child1);
            if (round < CurrentRound)
                return EnemyPrevious.get(round);
            else {
                //出错，回合数不合理
            }

        }

        int a, b, res;
        res = SyntaxTree.MinimunInt;

        if (t.child1.expkind == ExpKind.ConstK)
            a = t.child1.val;
        else if (t.child1.expkind == ExpKind.IdK)
            a = IdValueOf(t.child1.name);
        else if (t.child1.expkind == ExpKind.MypreK) {
            int round = ExpResult(t.child1.child1);
            if (round < CurrentRound)
                a = MyPrevious.get(round);
            else {
                a = SyntaxTree.MinimunInt;
                //出错，回合数不合理
            }

        } else if (t.child1.expkind == ExpKind.EnemypreK) {
            int round = ExpResult(t.child1.child1);
            if (round < CurrentRound)
                a = EnemyPrevious.get(round);
            else {
                a = SyntaxTree.MinimunInt;
                //出错，回合数不合理
            }

        } else
            a = ExpResult(t.child1);


        if (t.child2.expkind == ExpKind.ConstK)
            b = t.child2.val;
        else if (t.child2.expkind == ExpKind.IdK)
            b = IdValueOf(t.child2.name);
        else if (t.child2.expkind == ExpKind.MypreK) {
            int round = ExpResult(t.child2.child1);
            if (round < CurrentRound)
                b = MyPrevious.get(round);
            else {
                b = SyntaxTree.MinimunInt;
                //出错，回合数不合理
            }

        } else if (t.child2.expkind == ExpKind.EnemypreK) {
            int round = ExpResult(t.child2.child1);
            if (round < CurrentRound)
                b = EnemyPrevious.get(round);
            else {
                b = SyntaxTree.MinimunInt;
                //出错，回合数不合理
            }

        } else
            b = ExpResult(t.child2);

        switch (t.op) {
            case PLUS:// 加+
                res = a + b;
                break;
            case MINUS:// 减-
                res = a - b;
                break;
            case TIMES:// 乘*
                res = a * b;
                break;
            case OVER:// 除/
                res = a / b;
                break;
            case LT://小于比较
                if (a < b) res = 1;
                else res = 0;
                break;
            case EQ://等于比较
                if (a == b)
                    res = 1;
                else res = 0;
                break;
        }

        return res;
    }


    private int IdValueOf(String name) {
        if (variable.get(name + "*Random") != null) //此变量是随机变量
        {
            Random r = new Random();
            int x;
            while ((x = r.nextInt() % (variable.get(name + "*Random") + 1)) < 0) ;
            return x;
        }

        if (name.equals("cur"))  //当前回合数
            return CurrentRound;

        if (variable.get(name) != null)
            return variable.get(name).intValue();

        else return SyntaxTree.MinimunInt;//表示数据表中没有对应变量的值
    }

    private boolean IdDeclare(String name) {
        if (name.equals("cur"))  //不能声明系统变量
            return false;
        if (variable.get(name) == null) {
            variable.put(name, SyntaxTree.MinimunInt);
            return true;
        } else return false;//已存在同名变量，不能重复声明
    }

    private void FillIdValue(String name, int val) {
        if (variable.get(name) != null) {    //变量已赋值，需覆盖
            variable.remove(name);
        }
        variable.put(name, val);
    }


    private boolean match(TokenType expectedtoken) {
        if (token == expectedtoken) {
            token = Scan.getToken();
            return true;
        } else {
//			token = Scan.getToken();
            return false;
            //match出错处理
        }
    }

    private TreeNode stmt_sequence() {
        TreeNode t = statement();
        TreeNode p = t;
        while ((token != TokenType.ENDFILE) && (token != TokenType.END) && (token != TokenType.ELSE)) {
            TreeNode q;
            if (!match(TokenType.SEMI)) {
                ErrorMsg.put("缺少分号‘;’。", Scan.filelineno);
            }
            if (token == TokenType.ENDFILE)
                break;
            q = statement();
            if (q != null) {
                if (t == null) t = p = q;
                else {
                    p.sibling = q;
                    p = q;
                }//else
            }//if
        }//while
        return t;
    }

    private TreeNode statement() {
        TreeNode t = null;
        switch (token) {
            case IF:
                t = if_stmt();
                break;
            case CHOOSE:
                t = choose_stmt();
                break;
            case ID:
                t = assign_stmt();
                break;
            case STATEGY:
                t = stategy_stmt();
                break;
            case VAR:
                t = declare_stmt();
                break;
            case SEMI:
                ErrorMsg.put("分号‘;’错误。", Scan.filelineno);
                break;
            default:
                //提示错误
                ErrorMsg.put("语句错误，无法识别。", Scan.filelineno);
                token = Scan.getToken();
                break;
        } //switch
        return t;
    }

    private TreeNode if_stmt() {
        TreeNode t = newStmtNode(StmtKind.IfK);

        match(TokenType.IF);
        if (t != null)
            t.child1 = exp();

        if (!match(TokenType.THEN)) {
            ErrorMsg.put("缺少关键词then。", Scan.filelineno);
        }
        if (t != null)
            t.child2 = stmt_sequence();

        if (token == TokenType.ELSE) {
            match(TokenType.ELSE);
            if (t != null)
                t.child3 = stmt_sequence();
        }
        if (!match(TokenType.END)) {
            ErrorMsg.put("缺少关键词end。", Scan.filelineno);
        }

        return t;
    }

    private TreeNode choose_stmt() {
        TreeNode t = newStmtNode(StmtKind.ChooseK);
        match(TokenType.CHOOSE);
        switch (token) {
            case NUM:
                t.val = Integer.valueOf(Scan.tokenString.toString());
                match(TokenType.NUM);
                break;
            case MYPRE:
                t.child1 = newExpNode(ExpKind.MypreK);
                match(TokenType.MYPRE);
                if (!match(TokenType.LPAREN)) {
                    ErrorMsg.put("左括号丢失。", Scan.filelineno);
                }
                t.child1.child1 = exp();
                if (!match(TokenType.RPAREN)) {
                    ErrorMsg.put("右括号丢失。", Scan.filelineno);
                }
                break;
            case ENEMYPRE:
                t.child1 = newExpNode(ExpKind.EnemypreK);
                match(TokenType.ENEMYPRE);
                if (!match(TokenType.LPAREN)) {
                    ErrorMsg.put("左括号丢失。", Scan.filelineno);
                }
                t.child1.child1 = exp();
                if (!match(TokenType.RPAREN)) {
                    ErrorMsg.put("右括号丢失。", Scan.filelineno);
                }
                break;
        }
        return t;
    }

    private TreeNode assign_stmt() {
        TreeNode t = newStmtNode(StmtKind.AssignK);
        if ((t != null) && (token == TokenType.ID))
            t.name = Scan.tokenString.toString();
        match(TokenType.ID);
        if (!match(TokenType.ASSIGN)) {
            ErrorMsg.put("赋值符号缺失或错误。", Scan.filelineno);
        }
        if (t != null)
            t.child1 = exp();
        if (t.child1.expkind == ExpKind.RandomK)
            FillIdValue(t.name + "*Random", t.child1.child1.val);
        else {
            int val = ExpResult(t.child1);
            FillIdValue(t.name, val);
        }
        return t;
    }

    private TreeNode stategy_stmt() {
        TreeNode t = newStmtNode(StmtKind.StategyK);
        match(TokenType.STATEGY);
        if ((t != null) && (token == TokenType.ID))
            t.name = Scan.tokenString.toString();
        match(TokenType.ID);
        return t;
    }

    private TreeNode declare_stmt() {
        TreeNode t = newStmtNode(StmtKind.DeclareK);
        match(TokenType.VAR);
        if ((t != null) && (token == TokenType.ID))
            t.name = Scan.tokenString.toString();
        IdDeclare(t.name);
        match(TokenType.ID);
        return t;
    }


    private TreeNode exp() {
        TreeNode t = simple_exp();
        if ((token == TokenType.LT) || (token == TokenType.EQ)) {
            TreeNode p = newExpNode(ExpKind.OpK);
            if (p != null) {
                p.child1 = t;
                p.op = token;
                t = p;
            }
            match(token);
            if (t != null)
                t.child2 = simple_exp();
        }
        return t;
    }

    private TreeNode simple_exp() {
        TreeNode t = term();
        while ((token == TokenType.PLUS) || (token == TokenType.MINUS)) {
            TreeNode p = newExpNode(ExpKind.OpK);
            if (p != null) {
                p.child1 = t;
                p.op = token;
                t = p;
                match(token);
                t.child2 = term();
            }//if
        }//while
        return t;
    }

    private TreeNode term() {
        TreeNode t = factor();
        while ((token == TokenType.TIMES) || (token == TokenType.OVER)) {
            TreeNode p = newExpNode(ExpKind.OpK);
            if (p != null) {
                p.child1 = t;
                p.op = token;
                t = p;
                match(token);
                p.child2 = factor();
            }//if
        }//while
        return t;
    }

    private TreeNode factor() {
        TreeNode t = null;
        switch (token) {
            case NUM:
                t = newExpNode(ExpKind.ConstK);
                if ((t != null) && (token == TokenType.NUM))
                    t.val = Integer.valueOf(Scan.tokenString.toString());
                match(TokenType.NUM);
                break;
            case ID:
                t = newExpNode(ExpKind.IdK);
                if ((t != null) && (token == TokenType.ID))
                    t.name = Scan.tokenString.toString();
                match(TokenType.ID);
                break;
            case LPAREN:
                match(TokenType.LPAREN);
                t = exp();
                if (!match(TokenType.RPAREN)) {
                    ErrorMsg.put("右括号丢失。", Scan.filelineno);
                }
                break;
            case RANDOM:
                t = newExpNode(ExpKind.RandomK);
                match(TokenType.RANDOM);
                if (!match(TokenType.LPAREN)) {
                    ErrorMsg.put("左括号丢失。", Scan.filelineno);
                }
                t.child1 = exp();
                if (!match(TokenType.RPAREN)) {
                    ErrorMsg.put("右括号丢失。", Scan.filelineno);
                }
                break;
            case MYPRE:
                t = newExpNode(ExpKind.MypreK);
                match(TokenType.MYPRE);
                if (!match(TokenType.LPAREN)) {
                    ErrorMsg.put("左括号丢失。", Scan.filelineno);
                }
                t.child1 = exp();
                if (!match(TokenType.RPAREN)) {
                    ErrorMsg.put("右括号丢失。", Scan.filelineno);
                }
                break;
            case ENEMYPRE:
                t = newExpNode(ExpKind.EnemypreK);
                match(TokenType.ENEMYPRE);
                if (!match(TokenType.LPAREN)) {
                    ErrorMsg.put("左括号丢失。", Scan.filelineno);
                }
                t.child1 = exp();
                if (!match(TokenType.RPAREN)) {
                    ErrorMsg.put("右括号丢失。", Scan.filelineno);
                }
                break;
            default:
                ErrorMsg.put("语句错误，无法识别。", Scan.filelineno);
                token = Scan.getToken();
                break;
        }//switch
        return t;
    }


    private TreeNode newStmtNode(StmtKind kind) {
        TreeNode t = new TreeNode();
        t.nodekind = NodeKind.StmtK;
        t.stmtkind = kind;
        t.lineno = Scan.filelineno;
        return t;
    }

    private TreeNode newExpNode(ExpKind kind) {
        TreeNode t = new TreeNode();
        t.nodekind = NodeKind.ExpK;
        t.expkind = kind;
        t.lineno = Scan.filelineno;
//	    t.exptype = ExpType.Void;
        return t;
    }


}


