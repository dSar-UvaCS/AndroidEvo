import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class ClassLevelChangeDetails {

	public static void main(String[] args) throws IOException {
		/**
		 * Begin: Setup
		 */
		
		/* Create string and file to write output to */
		String toWrite = "";
		File file = new File("output/ClassesChanges.csv");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		String header = "API_version,element_type,element_name,element_type_change_type,changed_element_type,changed_element_name,changed_element_modification_type\n";
		bw.write(header);

		/* Iterate through all available version data online */
		for (int version = 25; version >= 19; version--) {
			int versionNum = version;

			int totalChangedClasses = 0;
			int totalAddedClasses = 0;
			int totalRemovedClasses = 0;
			
			/**
			 * "CHANGES" Classes
			 * Exists cases where the documentation page does not contain a table of specific changes that were made to the class
			 * In which case, there will be a one line summary
			 */

			String changedClassesUrl = "https://developer.android.com/sdk/api_diff/" + versionNum
					+ "/changes/classes_index_changes.html";

			/* Loads HTML and grabs table entries from list */
			Document changedClassesDoc = Jsoup.connect(changedClassesUrl).get();
			Elements changedClassesList = changedClassesDoc.getElementsByClass("hiddenlink");
						
			for (Element changedClass : changedClassesList) {
				
				totalChangedClasses += 1;
				
				String changedClassName = changedClass.text();
				String changedClassLink = changedClass.attr("href");
				
				String primaryToWrite = versionNum  + ",Class,"+ changedClassName + ",Changes"; // alters the output string

				String changedClassUrl = "https://developer.android.com/sdk/api_diff/" + versionNum + "/changes/" + changedClassLink;
				Document changedClassContent = Jsoup.connect(changedClassUrl).get();
				Element body = changedClassContent.getElementById("body-content"); // grabs only body, where all tables are

				int count = body.select("table").size();

				// There is no table
				if (count == 0) {
					Elements summary = body.getElementById("mainBodyFluid").getElementsByTag("p");
					String text = summary.text();
					if (text.endsWith("."))
						text = text.substring(0, text.length()-1);
					
					// Case 1: Class is Deprecated
					if (text.contains("deprecated")) {
						toWrite = versionNum  + ",Class,"+ changedClassName + ",Deprecated\n";
						bw.write(toWrite);
					}
					// Case 2: Removed Interfaces Only
					else if (text.substring(0, text.indexOf(" ")).contains("Removed") && !text.contains("Added")) {
						String changedElementModificationType = text.substring(0, text.indexOf(" "));
						text = text.substring(text.indexOf(" ")).trim();
						String changedElementType = "Interfaces";
						text = text.substring(text.indexOf(" ")).trim();
						String[] changedElements = text.split(",");
						
						for (int i = 0; i < changedElements.length; i++) {
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
					}
					// Case 3: Added Interfaces Only
					else if (text.substring(0, text.indexOf(" ")).contains("Added") && !text.contains("Removed")) {
						String changedElementModificationType = text.substring(0, text.indexOf(" "));
						text = text.substring(text.indexOf(" ")).trim();
						String changedElementType = "Interfaces";
						text = text.substring(text.indexOf(" ")).trim();
						String[] changedElements = text.split(",");
						
						for (int i = 0; i < changedElements.length; i++) {
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
					}
					// Case 4: Removed AND Added Interfaces Only
					else if (text.substring(0, text.indexOf(" ")).contains("Removed") && text.contains("Added")) {
						String removedInterfaceString = text.substring(0, text.indexOf("Added")).trim();
						String addedInterfaceString = text.substring(text.indexOf("Added")).trim();
						String changedElementType = "Interfaces";
						
						if (removedInterfaceString.endsWith("."))
							removedInterfaceString = removedInterfaceString.substring(0, removedInterfaceString.length()-1);
						if (addedInterfaceString.endsWith("."))
							addedInterfaceString = addedInterfaceString.substring(0, addedInterfaceString.length()-1);
						
						String changedElementModificationType = removedInterfaceString.substring(0, removedInterfaceString.indexOf(" "));
						removedInterfaceString = removedInterfaceString.substring(removedInterfaceString.indexOf(" ")).trim();
						removedInterfaceString = removedInterfaceString.substring(removedInterfaceString.indexOf(" ")).trim();
						String[] changedElements = removedInterfaceString.split(",");
						
						for (int i = 0; i < changedElements.length; i++) {
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
						
						changedElementModificationType = addedInterfaceString.substring(0, addedInterfaceString.indexOf(" "));
						addedInterfaceString = addedInterfaceString.substring(addedInterfaceString.indexOf(" ")).trim();
						addedInterfaceString = addedInterfaceString.substring(addedInterfaceString.indexOf(" ")).trim();
						changedElements = addedInterfaceString.split(",");
						
						for (int i = 0; i < changedElements.length; i++) {
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
					}
					// Case 5: Added AND Removed Interfaces Only
					else if (text.substring(0, text.indexOf(" ")).contains("Added") && text.contains("Removed")) {
						String addedInterfaceString = text.substring(0, text.indexOf("Removed")).trim();
						String removedInterfaceString = text.substring(text.indexOf("Removed")).trim();
						String changedElementType = "Interfaces";

						if (addedInterfaceString.endsWith("."))
							addedInterfaceString = addedInterfaceString.substring(0, addedInterfaceString.length()-1);
						if (removedInterfaceString.endsWith("."))
							removedInterfaceString = removedInterfaceString.substring(0, removedInterfaceString.length()-1);
						
						String changedElementModificationType = addedInterfaceString.substring(0, addedInterfaceString.indexOf(" "));
						addedInterfaceString = addedInterfaceString.substring(addedInterfaceString.indexOf(" ")).trim();
						addedInterfaceString = addedInterfaceString.substring(addedInterfaceString.indexOf(" ")).trim();
						String[] changedElements = addedInterfaceString.split(",");
						
						for (int i = 0; i < changedElements.length; i++) {
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
						
						changedElementModificationType = removedInterfaceString.substring(0, removedInterfaceString.indexOf(" "));
						removedInterfaceString = removedInterfaceString.substring(removedInterfaceString.indexOf(" ")).trim();
						removedInterfaceString = removedInterfaceString.substring(removedInterfaceString.indexOf(" ")).trim();
						changedElements = removedInterfaceString.split(",");
						
						for (int i = 0; i < changedElements.length; i++) {
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
					}
					// Case 6: Superclass changed and other changes possibly
					else if (text.contains("superclass")){
						// Only change is Superclass changed
						if (!text.contains("Removed") && !text.contains("Added")) {
							text = text.substring(text.indexOf(" ")).trim();
							
							String changedElementType = "Superclass";
							text = text.substring(text.indexOf(" ")).trim();
							String changedElementModificationType = "Changed";
							text = text.substring(text.indexOf(" ")).trim();
							
							String changedElement = text.substring(0,1).toUpperCase() + text.substring(1);
							
							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElement.trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
						}
						// Superclass changed and Removed Interfaces
						else if (text.contains("Removed") && !text.contains("Added")) {
							String superclassString = text.substring(text.indexOf(" "), text.indexOf("Removed")).trim();
							String removedInterfaceString = text.substring(text.indexOf("Removed")).trim();
														
							if (superclassString.endsWith("."))
								superclassString = superclassString.substring(0, superclassString.length()-1);
							if (removedInterfaceString.endsWith("."))
								removedInterfaceString = removedInterfaceString.substring(0, removedInterfaceString.length()-1);
							
							String changedElementType = "Superclass";
							superclassString = superclassString.substring(superclassString.indexOf(" ")).trim();
							String changedElementModificationType = "Changed";
							superclassString = superclassString.substring(superclassString.indexOf(" ")).trim();
							String changedElement = superclassString.substring(0,1).toUpperCase() + superclassString.substring(1);

							toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElement.trim()) + "," + changedElementModificationType + "\n";
							bw.write(toWrite);
							
							changedElementType = "Interface";
							changedElementModificationType = removedInterfaceString.substring(0, removedInterfaceString.indexOf(" ")).trim();
							removedInterfaceString = removedInterfaceString.substring(removedInterfaceString.indexOf(" ")).trim();
							removedInterfaceString = removedInterfaceString.substring(removedInterfaceString.indexOf(" ")).trim();
							String[] changedElements = removedInterfaceString.split(",");
							
							for (int i = 0; i < changedElements.length; i++) {
								toWrite = versionNum  + ",Class,"+ changedClassName + ",Changes," + changedElementType + "," + whitespaceReplacer(changedElements[i].trim()) + "," + changedElementModificationType + "\n";
								bw.write(toWrite);
							}
						}
						// Unexpected input
						else
							System.out.println("\tCase 6 Superclass Changed Unexpected Input:\t" + changedClassName + "\t" + text);
						
					}
					// Case 7: Unexpected input
					else {
						System.out.println("\tCase 7 Unexpected Input:\t" + changedClassName + "\t" + text);
					}
				}
				
				// There exists a table that describes all changes made to the Class
				for (int index = 0; index < count; index++) {
					Element table = body.select("table").get(index);
					String changedElementModificationInfo = table.getElementsByTag("tr").get(0).text().trim();
					String changedElementModificationType = changedElementModificationInfo.substring(0, changedElementModificationInfo.indexOf(" ")).trim();
					String changedElementType = changedElementModificationInfo.substring(changedElementModificationInfo.indexOf(" ")).trim();
					
					// how many entries there are in each table
					int numEntries = table.select("tr").size();

					// Writes each entry into table
					for (int x = 1; x < numEntries; x++) {
						String entry = table.getElementsByTag("tr").get(x).text();
				
						entry = whitespaceFixer(entry);
				
						toWrite = primaryToWrite + "," + changedElementType + ",\"" + whitespaceReplacer(entry) + "\"," + changedElementModificationType + "\n";
						bw.write(toWrite);
					}
						
				}
				
				// routinely clear buffer
				bw.flush();
			}

			/**
			 * "ADDITIONS" Classes
			 */
			
			String addedClassesUrl = "https://developer.android.com/sdk/api_diff/" + versionNum
					+ "/changes/classes_index_additions.html";

			/* Loads HTML and grabs table entries from list */
			Document addedClassesDoc = Jsoup.connect(addedClassesUrl).get();

			Elements addedClassesList = addedClassesDoc.getElementsByClass("hiddenlink");
			
			for (Element addedClass : addedClassesList) {
				
				totalAddedClasses += 1;
				String addedClassName = addedClass.text();
				String addedClassLink = addedClass.attr("href");
				String primaryToWrite = versionNum  + ",Class,"+ addedClassName + ",Additions\n"; // alters the output string
				
				bw.write(primaryToWrite);
			}
			
			// routinely clear writer
			bw.flush();
			
			/**
			 * "REMOVALS" Classes
			 */
			
			String removedClassesUrl = "https://developer.android.com/sdk/api_diff/" + versionNum
					+ "/changes/classes_index_removals.html";

			/* Loads HTML and grabs table entries from list */
			Document removedClassesDoc = Jsoup.connect(removedClassesUrl).get();

			Elements removedClassesList = removedClassesDoc.getElementsByClass("hiddenlink");
			
			for (Element removedClass : removedClassesList) {
				
				totalRemovedClasses += 1;
				String removedClassName = removedClass.text();
				String removedClassLink = removedClass.attr("href");
				String primaryToWrite = versionNum  + ",Class,"+ removedClassName + ",Removals\n"; // alters the output string
				
				bw.write(primaryToWrite);
			}
			
			// routinely clear writer
			bw.flush();
			
			/**
			 * Summary Statistic
			 */
			System.out.println("v" + versionNum + ":");
			System.out.println("# Added Classes: " + totalAddedClasses);
			System.out.println("# Changed Classes: " + totalChangedClasses);
			System.out.println("# Removed Classes: " + totalRemovedClasses);
		}

		bw.flush();
		bw.close();
		System.out.println("TASK COMPLETED!");
	}
	
	// Fixes any trailing whitespaces resulting from a character in the extended ASCII table
	public static String whitespaceFixer(String input) {
		String fixed = input.trim();
		char lastChar = fixed.charAt(fixed.length()-1);
		
		while ((int)lastChar > 127) {
			fixed = fixed.substring(0, fixed.length()-1);
			lastChar = fixed.charAt(fixed.length()-1);
		}
		
		fixed = fixed.trim();
		
		return fixed;
	}

	// Fixes any inside whitespaces resulting from a character in the extended ASCII table
		public static String whitespaceReplacer(String input) {
			String fixed = input.trim();
			
			for (int i = 0; i < fixed.length(); i++) {
				if ((int)fixed.charAt(i) > 127) {
					String temp = fixed.substring(0, i) + " " + fixed.substring(i + 1);
					fixed = temp;
				}
			}
			
			return fixed;
		}
}
