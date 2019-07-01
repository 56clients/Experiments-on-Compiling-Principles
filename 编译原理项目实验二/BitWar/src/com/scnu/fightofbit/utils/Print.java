package com.scnu.fightofbit.utils;

public class Print {
	public static final int STATEGY = 1;
	public static final int DECLARE = 2;
	public static final int ASSIGN = 3;
	public static final int CHOOSE = 4;
	public static final int IF_EXP = 5;
	public static final int IF_THEN = 6;
	public static final int IF_ELSE = 7;
	
	private static String depth(String m,int d){
		if(d==0)
			return m;
		int tag2 = m.indexOf("\r\n");
		StringAlign sa = new StringAlign(tag2+30*d,StringAlign.JUST_RIGHT);
		StringBuffer sb = new StringBuffer();
		int tag1 = 0;
		while(tag2>=0){
			sb.append(sa.format(m.substring(tag1, tag2).trim())).append("\r\n");
			tag1=tag2;
			tag2++;
			tag2 = m.indexOf("\r\n",tag2);
		}
		return sb.toString();
	}

	/**
	 * 策略和声明 节点
	 * @param node
	 * @param name
	 * @param dep
	 * @return
	 */
	private static String stategy_declareNode(int node,String name,int dep){
		StringBuffer sb = new StringBuffer();
		String nodeType;
		if(node ==1 )
			nodeType = "stategy";
		else if(node == 2)
			nodeType = "declare";
		else nodeType = "";
		StringAlign sa = new StringAlign(18,StringAlign.JUST_CENTER);
		sb.append("--------------------\r\n");
		sb.append("|");
		sb.append(sa.format(nodeType));
		sb.append("|\r\n");		
		sb.append("|");
		sb.append(sa.format("<"+name+">"));
		sb.append("|");
		sb.append("\r\n--------------------\r\n");		
		return depth(sb.toString(),dep);
	}

	/**
	 * 赋值和选择 节点
	 * @param node
	 * @param innerPara
	 * @param childExp
	 * @param childExpPara
	 * @param dep
	 * @return
	 */
	private static String assign_chooseNode(int node,String innerPara,String childExp,String childExpPara,int dep){
		StringBuffer sb = new StringBuffer();
		StringAlign sa = new StringAlign(18,StringAlign.JUST_CENTER);
		String nodeType;
		if(node == 3){
			nodeType = "assign";
			sb.append("--------------------          --------------------          --------------------\r\n");
			sb.append("|");
			sb.append(sa.format(nodeType));
			sb.append("|----------|                  |----------|                  |\r\n");
			sb.append("|");
			sb.append(sa.format("<"+innerPara+">"));
			sb.append("|          |");
			sb.append(sa.format("<"+childExp+">"));
			sb.append("|          |");
			sb.append(sa.format("<"+childExpPara+">"));
			sb.append("|\r\n");
			sb.append("--------------------          --------------------          --------------------\r\n");	
		}else if(node == 4){
			nodeType = "choose";
			if("0".equals(innerPara)||"1".equals(innerPara)){
				sb.append("--------------------\r\n");
				sb.append("|");
				sb.append(sa.format(nodeType));
				sb.append("|\r\n");
				sb.append("|");
				sb.append(sa.format("<"+innerPara+">"));
				sb.append("|\r\n");
				sb.append("--------------------\r\n");
			}else if("MypreK".equals(innerPara)||"EnemypreK".equals(innerPara)){
				sb.append("--------------------          --------------------          --------------------\r\n");
				sb.append("|");
				sb.append(sa.format(nodeType));
				sb.append("|----------|                  |----------|                  |\r\n");
				sb.append("|");
				sb.append(sa.format("<"+innerPara+">"));
				sb.append("|          |");
				sb.append(sa.format("<"+childExp+">"));
				sb.append("|          |");
				sb.append(sa.format("<"+childExpPara+">"));
				sb.append("|\r\n");
				sb.append("--------------------          --------------------          --------------------\r\n");	
			}
		}
		
		return depth(sb.toString(),dep);
	}

	/**
	 * if 节点
	 * @param node
	 * @param childExp
	 * @param childExpPara
	 * @param dep
	 * @return
	 */
	private static String ifNode(int node,String childExp,String childExpPara,int dep){
		StringBuffer sb = new StringBuffer();
		StringAlign sa = new StringAlign(18,StringAlign.JUST_CENTER);
		switch(node){
		case IF_EXP:
			sb.append("--------------------          --------------------    --------------------------------\r\n");
			sb.append("|        if        |----------|                  |----|                              |\r\n");
			sb.append("|                  |          ");
			sb.append("|");
			sb.append(sa.format("<"+childExp+">"));
			sb.append("|    |");
			sa = new StringAlign(30,StringAlign.JUST_CENTER);
			sb.append(sa.format("<"+childExpPara+">"));
			sb.append("|\r\n");
			sb.append("--------------------          --------------------    --------------------------------\r\n");
			return depth(sb.toString(),dep);
		case IF_THEN:
			sb.append("       \\      then\r\n");
			sb.append("         ------------------\\\r\n");
			break;
		case IF_ELSE:
			sb.append("       \\      else\r\n");
			sb.append("         ------------------\\\r\n");
			break;
		default:
			return "";
		}
		return depth(sb.toString(),dep);
	}

	public static String Graphics(TreeNode node){
    	if(node == null)
    		return "";
		return Graphics(node,0);
	}
	
    private static String Graphics(TreeNode node, int dep){
    	TreeNode temp = node;
    	String t1,t2,t3;
    	StringBuffer sb = new StringBuffer();
    	while(temp!=null){
			switch(temp.stmtkind){
			case AssignK:
				if(temp.child1.expkind == SyntaxTree.ExpKind.ConstK)
					sb.append(assign_chooseNode(Print.ASSIGN,temp.name," ",String.valueOf(temp.child1.val),dep));
				else if(temp.child1.expkind == SyntaxTree.ExpKind.OpK){
//					if(temp.child1.child1.expkind == ExpKind.ConstK)
//						t3 = temp.child1.child1.val + ",";
//					else t3 = temp.child1.child1.name + ",";
//					if(temp.child1.child2.expkind == ExpKind.ConstK)
//						t3 = t3+temp.child1.child2.val + "";
//					else t3 = t3+temp.child1.child2.name + "";
					t3 = getIfExpPara(temp.child1.child1)+","+getIfExpPara(temp.child1.child2);
					sb.append(assign_chooseNode(Print.ASSIGN,temp.name,temp.child1.op.toString(),t3,dep));
				}else if(temp.child1.expkind == SyntaxTree.ExpKind.RandomK){
					sb.append(assign_chooseNode(Print.ASSIGN,temp.name,"random",String.valueOf(temp.child1.child1.val),dep));
				}
				break;
			case ChooseK:
				if(temp.child1==null){
					sb.append(assign_chooseNode(Print.CHOOSE,String.valueOf(temp.val),null,null,dep));
				}else {
					t1 = temp.child1.expkind.toString();
					t2 = temp.child1.child1.op.toString();
//					if(temp.child1.child1.child1.expkind == ExpKind.ConstK)
//						t3 = temp.child1.child1.child1.val + ",";
//					else t3 = temp.child1.child1.child1.name + ",";
//					if(temp.child1.child1.child2.expkind == ExpKind.ConstK)
//						t3 = t3+temp.child1.child1.child2.val + "";
//					else t3 = t3+temp.child1.child1.child2.name + "";
					t3 = getIfExpPara(temp.child1.child1.child1)+","+getIfExpPara(temp.child1.child1.child2);
					sb.append(assign_chooseNode(Print.CHOOSE,t1,t2,t3,dep));
				}
				break;
			case DeclareK:
				sb.append(stategy_declareNode(Print.DECLARE,temp.name,dep));
				break;
			case IfK:
//				if(temp.child1.child1.expkind == ExpKind.ConstK)
//					t3 = temp.child1.child1.val + ",";
//				else t3 = temp.child1.child1.name + ",";
//				if(temp.child1.child2.expkind == ExpKind.ConstK)
//					t3 = t3+temp.child1.child2.val + "";
//				else t3 = t3+temp.child1.child2.name + "";
				t3 = getIfExpPara(temp.child1.child1)+","+getIfExpPara(temp.child1.child2);
				sb.append(ifNode(Print.IF_EXP,temp.child1.op.toString(),t3,dep));
				sb.append(ifNode(Print.IF_THEN,null,null,dep));
				sb.append(Graphics(temp.child2,dep+1));
				if(temp.child3!=null)
					sb.append(ifNode(Print.IF_ELSE,null,null,dep));
					sb.append(Graphics(temp.child3,dep+1));
				break;
			case StategyK:
				sb.append(stategy_declareNode(Print.STATEGY,temp.name,dep));
				break;
			default:
				break;
			}
			if(temp.sibling!=null){
				sb.append("        ↓\r\n        ↓\r\n");
			}
			temp = temp.sibling;
		}
    	return sb.toString();
    }

    private static String getIfExpPara(TreeNode child){
    	switch(child.expkind){
		case ConstK:
			return String.valueOf(child.val);
		case EnemypreK:
			return "enemypre<"+getIfExpPara(child.child1)+">";
		case IdK:
			return child.name;
		case MypreK:
			return "mypre<"+getIfExpPara(child.child1)+">";
		case OpK:
			return "<<"+child.op.toString() +">"+ getIfExpPara(child.child1)+","+ getIfExpPara(child.child2)+">";
		case RandomK:
			return "random<"+getIfExpPara(child.child1)+">";
		default:
			return "error";
    	}
    }
}
