import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FieldLevelChangeDetails {

	public static void main(String[] args) throws IOException {
		
		/**
		 * Begin: Setup
		 */

		/* Create string and file to write output to */
		String toWrite = "";
		File file = new File("output/FieldsChanges.csv");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		String header = "API_version,element_type,element_name,element_type_change_type,changed_element_type,changed_element_name,changed_element_modification_type\n";
		bw.write(header);

		/* Iterate through all available version data online */
		for (int version = 24; version >= 19; version--) {
			int versionNum = version;

			int totalChangedFields = 0;
			int totalAddedFields = 0;
			int totalRemovedFields = 0;
			
			/**
			 * "CHANGES" Fields
			 */

			String changedFieldsUrl = "https://developer.android.com/sdk/api_diff/" + versionNum
					+ "/changes/fields_index_changes.html";

			/* Loads HTML and grabs table entries from list */
			Document changedFieldsDoc = Jsoup.connect(changedFieldsUrl).get();
			Elements changedFieldsList = changedFieldsDoc.getElementsByClass("hiddenlink");
						
			for (Element changedField : changedFieldsList) {
				
				String changedFieldName = changedField.text();
				String changedFieldLink = changedField.attr("href");
				String primaryToWrite = versionNum  + ",Field,"+ changedFieldName.replace(",", " ") + ",Changes\n"; // alters the output string
				
				bw.write(primaryToWrite);
				
				if (!primaryToWrite.isEmpty())
					totalChangedFields += 1;
			}
			
			// routinely clear writer
			bw.flush();

			/**
			 * "ADDITIONS" Fields
			 */
			
			String addedFieldsUrl = "https://developer.android.com/sdk/api_diff/" + versionNum
					+ "/changes/fields_index_additions.html";

			/* Loads HTML and grabs table entries from list */
			Document addedFieldsDoc = Jsoup.connect(addedFieldsUrl).get();

			Elements addedFieldsList = addedFieldsDoc.getElementsByClass("hiddenlink");
			
			for (Element addedField : addedFieldsList) {
				
				String addedFieldName = addedField.text();
				String addedFieldLink = addedField.attr("href");
				String primaryToWrite = versionNum  + ",Field,"+ addedFieldName.replace(",", " ") + ",Additions\n"; // alters the output string
				
				bw.write(primaryToWrite);
				
				if (!primaryToWrite.isEmpty())
					totalAddedFields += 1;
			}
			
			// routinely clear writer
			bw.flush();
			
			/**
			 * "REMOVALS" Fields
			 */
			
			String removedFieldsUrl = "https://developer.android.com/sdk/api_diff/" + versionNum
					+ "/changes/fields_index_removals.html";

			/* Loads HTML and grabs table entries from list */
			Document removedFieldsDoc = Jsoup.connect(removedFieldsUrl).get();

			Elements removedFieldsList = removedFieldsDoc.getElementsByClass("hiddenlink");
			
			for (Element removedField : removedFieldsList) {
				
				String removedFieldName = removedField.text();
				String removedFieldLink = removedField.attr("href");
				String primaryToWrite = versionNum  + ",Field,"+ removedFieldName.replace(",", " ") + ",Removals\n"; // alters the output string
				
				bw.write(primaryToWrite);
				
				if (!primaryToWrite.isEmpty())
					totalRemovedFields += 1;
			}
			
			// routinely clear writer
			bw.flush();
			
			/**
			 * Summary Statistic
			 */
			System.out.println("v" + versionNum + ":");
			System.out.println("# Added Fields: " + totalAddedFields);
			System.out.println("# Changed Fields: " + totalChangedFields);
			System.out.println("# Removed Fields: " + totalRemovedFields);
		}

		bw.flush();
		bw.close();
		System.out.println("TASK COMPLETED!");
	}

}