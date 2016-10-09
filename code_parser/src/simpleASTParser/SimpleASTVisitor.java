package simpleASTParser;

import java.awt.Window.Type;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SimpleASTVisitor extends ASTVisitor {

	private final CompilationUnit cu;
	private String filename;
	private XSSFWorkbook wb = new XSSFWorkbook();
	private XSSFSheet sheet = wb.createSheet("methodname");
	private int rowNum = 0;
	// private POI excelFile;

	public SimpleASTVisitor(char[] arr, String file_name) {

		this.filename = file_name;
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(arr);
		parser.setResolveBindings(true);
		cu = (CompilationUnit) parser.createAST(null);

		// initiate excelFile here
	}

	/*
	 * public boolean visit(SimpleName node) { //if
	 * (this.names.contains(node.getIdentifier())) {
	 * 
	 * System.out.println("Usage of '" + node + "' at line " +
	 * cu.getLineNumber(node.getStartPosition()) + " type: " +
	 * node.resolveBinding()); //} return true; }
	 */

	public boolean visit(TypeDeclaration node) {

		System.out.println(this.filename + " , " + cu.getLineNumber(node.getStartPosition()) + ", TypeDeclaration , "
				+ node.getName());
		return true;

	}

	public boolean visit(ImportDeclaration node) {

		System.out.println(this.filename + " , " + cu.getLineNumber(node.getStartPosition()) + ", ImportDeclaration , "
				+ node.getName());
		return true;

	}

	public boolean visit(MethodDeclaration node) {

		System.out.println(this.filename + " , Starting Line: " + cu.getLineNumber(node.getStartPosition())
				+ ", MethodDeclaration: " + node.getName() + ", ReturnType: " + node.getReturnType2() + ", JavaDoc: "
				+ node.getJavadoc() + ", Body: " + node.getBody() + ", Parameter Names: " + node.parameters());

		PrintWriter writer;
		try {
			// writer = new PrintWriter("info.txt", "UTF-8");
			writer = new PrintWriter(new FileOutputStream(new File("info.txt"), true));
			writer.println(this.filename + " , Starting Line: " + cu.getLineNumber(node.getStartPosition())
					+ ", MethodDeclaration: " + node.getName() + ", ReturnType: " + node.getReturnType2()
					+ ", JavaDoc: " + node.getJavadoc() +

					", Body: " + node.getBody() + ", Parameter Names: " + node.parameters());

			String sheetName = "MethodInfo";// name of sheet

			ArrayList<Object> data = new ArrayList<Object>();
			data.add(node.getName().toString());
			data.add(node.getReturnType2().toString());
			data.add(node.parameters().toString());
			this.writeARow(data);

			// Write the workbook in file system

			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");

		} catch (Exception e) {

		}
		return true;
	}

	private void writeARow(ArrayList<Object> data) {
		// Iterate over data and write to sheet
		// Set<String> keyset = data.keySet();
		// for (String key : keyset) {
		Row row = sheet.createRow(rowNum++);
		// Object[] objArr = data.get(key);
		int cellnum = 0;
		for (Object obj : data) {
			Cell cell = row.createCell(cellnum++);
			if (obj instanceof String)
				cell.setCellValue((String) obj);
			else if (obj instanceof Integer)
				cell.setCellValue((Integer) obj);
		}
		// }
	}

	/*
	 * public boolean visit( ClassInstanceCreation node) {
	 * 
	 * System.out.println(this.filename + " , " +
	 * cu.getLineNumber(node.getStartPosition()) + ", ClassDeclaration , " +
	 * node.toString()); return true; }
	 */

	public void parse() {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add("Method Name");
		data.add("Return Type");
		data.add("Parameter Names");
		this.writeARow(data);

		cu.accept(this);
		FileOutputStream out;
		try {
			out = new FileOutputStream(new File("howtodoinjava_demo.xlsx"));
			wb.write(out);
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
