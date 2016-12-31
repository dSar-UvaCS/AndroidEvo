import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ConstructorLevelChangeDetailsArchivedVersions {

	public static void main(String[] args) throws IOException {
		
		/**
		 * Begin: Setup
		 */

		/* Create string and file to write output to */
		String toWrite = "";
		File file = new File("output/ConstructorsChangesArchivedVersions.csv");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		String header = "API_version,element_type,element_name,element_type_change_type,changed_element_type,changed_element_name,changed_element_modification_type\n";
		bw.write(header);

		/* Iterate through all available version data online */
		for (int version = 18; version >= 3; version--) {
			int versionNum = version;

			int totalChangedConstructors = 0;
			int totalAddedConstructors = 0;
			int totalRemovedConstructors = 0;
			
			/**
			 * "CHANGES" Constructors
			 */

			/* Loads HTML and grabs table entries from list */
			File changedConstructorsFile = new File("./input/" + versionNum + "/changes/constructors_index_changes.html");
			Document changedConstructorsDoc = Jsoup.parse(changedConstructorsFile, "UTF-8", "");
			
			Elements changedConstructorsList = changedConstructorsDoc.getElementsByClass("hiddenlink");
						
			for (Element changedConstructor : changedConstructorsList) {
				// Find table holding changed constructor change details
				String changedConstructorName = changedConstructor.text();
				String changedConstructorLink = changedConstructor.attr("href");
				
				String primaryToWrite = "";
				
				changedConstructorLink = changedConstructorLink.substring(0, changedConstructorLink.indexOf("#"));
				
				File changedConstructorFile = new File("./input/" + versionNum + "/changes/" + changedConstructorLink);
				Document changedConstructorContent = Jsoup.parse(changedConstructorFile, "UTF-8", "");
				
				
				Element body = changedConstructorContent.getElementById("body-content"); // grabs only body, where all tables are
				Elements tables = body.select("table");
				
				// Iterate through all tables to find the "Changed Constructors" table
				for (int i = 0; i < tables.size(); i++) {
					Element table = tables.get(i);
					String checkTableName = table.select("tr").first().text();
					
					// Process the Changed Constructors table
					if (checkTableName.equals("Changed Constructors")) {
						String changeDetail = table.select("td").get(1).text();
						
						// Case 1: Deprecated
						if (changeDetail.contains("deprecated")) {
							primaryToWrite = versionNum  + ",Constructor,\""+ changedConstructorName + "\",Deprecated\n";
							bw.write(primaryToWrite);
						}
						// Case 2: Change in Type
						else if (changeDetail.contains("type")) {
							String changedElementModificationType = changeDetail.substring(0, changeDetail.indexOf(" ")).trim();
							changeDetail = changeDetail.substring(changeDetail.indexOf(" ")).trim();
							changeDetail = changeDetail.substring(changeDetail.indexOf(" ")).trim();
							String changedElementType = changeDetail.substring(0, changeDetail.indexOf(" ")).trim();
							changedElementType = changedElementType.substring(0,1).toUpperCase() + changedElementType.substring(1);
							changeDetail = changeDetail.trim().substring(changeDetail.indexOf(" ")).trim();
							String changedElementName = changeDetail.substring(0,1).toUpperCase() + changeDetail.substring(1);
							
							primaryToWrite = versionNum  + ",Constructor,\""+ changedConstructorName + "\",Changes," + changedElementType + ",\"" + changedElementName + "\"," + changedElementModificationType + "\n";
							bw.write(primaryToWrite);
						}
						// Case 3: Keyword related change (Visibility)
						else if (changeDetail.contains("visibility")) {
							String changedElementModificationType = changeDetail.substring(0, changeDetail.indexOf(" ")).trim();
							changeDetail = changeDetail.substring(changeDetail.indexOf(" ")).trim();
							changeDetail = changeDetail.substring(changeDetail.indexOf(" ")).trim();
							String changedElementType = changeDetail.substring(0, changeDetail.indexOf(" ")).trim();
							changedElementType = changedElementType.substring(0,1).toUpperCase() + changedElementType.substring(1);
							changeDetail = changeDetail.trim().substring(changeDetail.indexOf(" ")).trim();
							String changedElementName = changeDetail.substring(0,1).toUpperCase() + changeDetail.substring(1);
							
							primaryToWrite = versionNum  + ",Constructor,\""+ changedConstructorName + "\",Changes," + changedElementType + ",\"" + changedElementName + "\"," + changedElementModificationType + "\n";
							bw.write(primaryToWrite);
						}
						// Case : Unexpected Input
						else {
							System.out.println("Unexpected Input:\t" + changedConstructorName);
						}
						
					}
				}
				
				if (!primaryToWrite.isEmpty())
					totalChangedConstructors += 1;
			}
			
			// routinely clear writer
			bw.flush();

			/**
			 * "ADDITIONS" Constructors
			 */
			
			/* Loads HTML and grabs table entries from list */
			File addedConstructorsFile = new File("./input/" + versionNum + "/changes/constructors_index_additions.html");
			Document addedConstructorsDoc = Jsoup.parse(addedConstructorsFile, "UTF-8", "");
			
			Elements addedConstructorsList = addedConstructorsDoc.getElementsByClass("hiddenlink");
			
			for (Element addedConstructor : addedConstructorsList) {
				
				String addedConstructorName = addedConstructor.text();
				String addedConstructorLink = addedConstructor.attr("href");
				String primaryToWrite = versionNum  + ",Constructor,\""+ addedConstructorName + "\",Additions\n"; // alters the output string
				
				bw.write(primaryToWrite);
				
				if (!primaryToWrite.isEmpty())
					totalAddedConstructors += 1;
			}
			
			// routinely clear writer
			bw.flush();
			
			/**
			 * "REMOVALS" Constructors
			 */
			
			/* Loads HTML and grabs table entries from list */
			File removedConstructorsFile = new File("./input/" + versionNum + "/changes/constructors_index_removals.html");
			Document removedConstructorsDoc = Jsoup.parse(removedConstructorsFile, "UTF-8", "");
			
			Elements removedConstructorsList = removedConstructorsDoc.getElementsByClass("hiddenlink");
			
			for (Element removedConstructor : removedConstructorsList) {
				
				String removedConstructorName = removedConstructor.text();
				String removedConstructorLink = removedConstructor.attr("href");
				String primaryToWrite = versionNum  + ",Constructor,\""+ removedConstructorName + "\",Removals\n"; // alters the output string
				
				bw.write(primaryToWrite);

				if (!primaryToWrite.isEmpty())
					totalRemovedConstructors += 1;
			}
			
			// routinely clear writer
			bw.flush();
			
			/**
			 * Summary Statistic
			 */
			System.out.println("v" + versionNum + ":");
			System.out.println("# Added Constructors: " + totalAddedConstructors);
			System.out.println("# Changed Constructors: " + totalChangedConstructors);
			System.out.println("# Removed Constructors: " + totalRemovedConstructors);
		}

		bw.flush();
		bw.close();
		System.out.println("TASK COMPLETED!");
	}

}
