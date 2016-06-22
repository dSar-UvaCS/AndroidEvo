package simpleASTParser;

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
	   
	   //parseMethodInvoc(charArray,infile);
	   parse(charArray,infile);
	     
	 }
	
	private static void parseMethodInvoc(char[] arr, String infile_name){
		MethodInvocationASTVisitor astv = new MethodInvocationASTVisitor(arr, infile_name);
		astv.parse();
	}
	
	private static void parse(char[] arr, String infile_name){
		SimpleASTVisitor astv = new SimpleASTVisitor(arr, infile_name);
		astv.parse();
	}
	
}