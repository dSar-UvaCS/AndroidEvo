package simpleASTParser;

import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class SimpleASTVisitor extends ASTVisitor{
	
	private final CompilationUnit cu;
	private String filename;

	public SimpleASTVisitor(char[] arr,String file_name) {
		
		this.filename = file_name;
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(arr);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		cu = (CompilationUnit) parser.createAST(null);
		
	}
	
	
	//Set names = new HashSet();
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
		
		if(node.getExpression()!=null) {
			System.out.println(this.filename + " , " + 
					cu.getLineNumber(node.getStartPosition()) + 
					", MethodInvocation , " + node.getExpression() + 
					"." + node.getName());
		}
		else {
			System.out.println(this.filename + " , " + 
					cu.getLineNumber(node.getStartPosition()) + 
					", MethodInvocation , " + node.getName());
			
		}
		//List<Expression> args = node.arguments();
		/*for(Expression l: args){
			System.out.println(l.toString());
		}*/
		
		//}
		return true;
	}

	public void parse() {
		cu.accept(this);
	}
}

