package sampleASTParser;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.Expression;


public class Test {
	
	


	public static void main(String args[]) throws IOException {
		
		String infile  = "tests/input.java";
		if(args.length > 0) 
           infile = args[0];
		
	   System.out.println("Parsing file " + infile);
	
	   File file = new File(infile);
	   Scanner scanner = new Scanner(file);

	   String fileString = scanner.nextLine();
	   while (scanner.hasNextLine()) {
		   fileString = fileString + "\n" + scanner.nextLine();
	   }

	   char[] charArray = fileString.toCharArray();
	   
	   //System.out.println(charArray);
	   parse1(charArray,infile);
	     
	 }
	
	private static void parse1(char[] arr, String infile_name){
		SimpleASTVisitor astv = new SimpleASTVisitor(arr, infile_name);
		astv.parse();
	}
	
	private static void parse(char[] arr){
		
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		//parser.setSource("public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }".toCharArray());
		parser.setSource(arr);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		//ASTNode node = parser.createAST(null);
 
 
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 
		cu.accept(new ASTVisitor() {
 
			Set names = new HashSet();
			/*
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue to avoid usage info
			}
 
			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
				System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
			*/
			public boolean visit(MethodInvocation  node) {
				//if (this.names.contains(node.getIdentifier())) {
				System.out.println("MethodInvocation '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
				List<Expression> args = node.arguments();
				/*for(Expression l: args){
					System.out.println(l.toString());
				}*/
				
				//}
				return true;
			}
 
		});
	}
}