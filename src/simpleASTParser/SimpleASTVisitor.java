package simpleASTParser;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

public class SimpleASTVisitor extends ASTVisitor{
	
	private final CompilationUnit cu;
	private String filename;

	public SimpleASTVisitor(char[] arr,String file_name) {
		
		this.filename = file_name;
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(arr);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		cu = (CompilationUnit) parser.createAST(null);
		
	}
	
	public boolean visit(SimpleName node) {
		//if (this.names.contains(node.getIdentifier())) {
		
		System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition())
				+ " type: " + node.resolveBinding());
		//}
		return true;
	}
	
	/*
	
	public boolean visit(MethodDeclaration  node) {
		
		
		IMethodBinding binding = node.resolveBinding();
		System.out.println(binding);
		 if (binding != null) {
	            ITypeBinding type = binding.getDeclaringClass();
	            if (type != null) {
	                System.out.println("Decl: " + type.getName());
	            }
	        }
		
		System.out.println(this.filename + " , " + 
					cu.getLineNumber(node.getStartPosition()) + 
					", MethodDeclaration , " + node.getName() );
		return true;
	}
	*/

	/*
	public boolean visit( ClassInstanceCreation  node) {
		
		System.out.println(this.filename + " , " + 
					cu.getLineNumber(node.getStartPosition()) + 
					", ClassDeclaration , " + node.toString());
		return true;
	}
	*/
	
	public void parse() {
		cu.accept(this);
	}
}

