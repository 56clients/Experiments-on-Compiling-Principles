package com.scnu.fightofbit.utils;

import com.scnu.fightofbit.TokenType;
 
public class TreeNode {
		public int lineno;//位置，定位源代码行数
		
		public SyntaxTree.NodeKind nodekind;
		
		public SyntaxTree.StmtKind stmtkind;
		public SyntaxTree.ExpKind expkind;
		
		public TokenType op;
		public int val ;
		public String name; //在赋值节点中用于保存变量名字，在策略命名节点中用于保存策略名字
		
//		private ExpType exptype;
		
		public TreeNode child1,child2,child3;
		public TreeNode sibling;
		
		public TreeNode(){
			lineno = 0;
			val = 0;
			name = null;
			op = null;
			child1 = null;
			child2 = null;
			child3 = null;
			sibling = null;
			nodekind = null;
			stmtkind = null;
			expkind = null;
//			exptype = null;
		}
}
