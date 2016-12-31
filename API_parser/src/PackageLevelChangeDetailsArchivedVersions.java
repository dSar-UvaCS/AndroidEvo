import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PackageLevelChangeDetailsArchivedVersions {

	public static void main(String[] args) throws IOException {

		/**
		 * Begin: Setup
		 */

		/* Create string and file to write output to */
		String toWrite = "";
		File file = new File("output/PackagesChangesArchivedVersions.csv");
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

			/**
			 * "CHANGES" Packages
			 */
			
			/* Loads HTML and grabs table entries from list */
			
			File changedPackagesFile = new File("./input/" + versionNum + "/changes/packages_index_changes.html");
			Document changedPackagesDoc = Jsoup.parse(changedPackagesFile, "UTF-8", "");
			
			Element changedPackagesList = changedPackagesDoc.getElementById("indexTableEntries");
			String[] changedPackages = changedPackagesList.text().split(" "); // List
																				// of
																				// changed
																				// packages

			/* Iterate through all changed Packages */
			if (!changedPackagesList.text().isEmpty()) {
				for (int i = 0; i < changedPackages.length; i++) {
					String primaryToWrite = versionNum + ",Package," + changedPackages[i] + ",Changes"; // alters
																										// the
																										// output
																										// string
					
					File changedPackageFile = new File("./input/" + versionNum + "/changes/pkg_" + changedPackages[i] + ".html");
					Document changedPackage = Jsoup.parse(changedPackageFile, "UTF-8", "");
					
					Element body = changedPackage.getElementById("body-content"); // grabs
																					// only
																					// body,
																					// where
																					// all
																					// tables
																					// are

					/*
					 * Iterate through all possible tables (Changed Classes /
					 * Added Classes and Interfaces / etc.)
					 */
					// num tables there are
					int count = body.select("table").size();

					for (int index = 0; index < count; index++) {
						Element table = body.select("table").get(index);
						String elementModificationInfo = table.getElementsByTag("tr").get(0).text().trim();
						String elementModificationType = elementModificationInfo
								.substring(0, elementModificationInfo.indexOf(" ")).trim();
						String changedElementType = elementModificationInfo
								.substring(elementModificationInfo.indexOf(" ")).trim();

						// how many entries there are in each table
						int numEntries = table.select("tr").size();

						// Writes each entry into table
						for (int x = 1; x < numEntries; x++) {

							String entry = table.getElementsByTag("tr").get(x).text();

							entry = whitespaceFixer(entry);

							String changedElementTypeToWrite = changedElementType;

							// Determines if is class or interface, taking into
							// account webpage no longer exists
							// If webpage no longer exists or webpage cannot be inspected, keep changedElementType unchanged
							if (changedElementType.equals("Classes and Interfaces")) {
								String elementURL = "https://developer.android.com/reference/"
										+ changedPackages[i].replace('.', '/') + "/" + entry.replaceAll("\\s", "")
										+ ".html";
								
								HttpURLConnection huc = (HttpURLConnection) (new URL(elementURL)).openConnection();
								huc.setRequestMethod("HEAD");
								int responseCode = huc.getResponseCode();

								if (responseCode == 200) {
									Document referencePage = Jsoup.connect(elementURL).get();

									if (!referencePage.getElementsByClass("api-signature").isEmpty()) {
										String apiSignature = referencePage.getElementsByClass("api-signature").first()
												.text();

										if (apiSignature.contains("class"))
											changedElementTypeToWrite = "Classes";
										else
											changedElementTypeToWrite = "Interfaces";
									}
								}
							}

							toWrite = primaryToWrite + "," + changedElementTypeToWrite + "," + entry + ","
									+ elementModificationType + "\n";
							bw.write(toWrite);
						}
					}

					// routinely clear buffer
					bw.flush();

				}
			}

			/**
			 * "ADDITIONS" Packages
			 */
			/* Loads HTML and grabs table entries from list */
			File addedPackagesFile = new File("./input/" + versionNum + "/changes/packages_index_additions.html");
			Document addedPackagesDoc = Jsoup.parse(addedPackagesFile, "UTF-8", "");
			
			Element addedPackagesList = addedPackagesDoc.getElementById("indexTableEntries");
			// List of added packages
			String[] addedPackages = addedPackagesList.text().split(" ");

			if (!addedPackagesList.text().isEmpty()) {
				for (int i = 0; i < addedPackages.length; i++) {
					toWrite = versionNum + ",Package," + addedPackages[i] + ",Additions\n";
					bw.write(toWrite);
					toWrite = "";
				}
			}

			// routinely clear writer
			bw.flush();

			/**
			 * "REMOVALS" Packages
			 */
			/* Loads HTML and grabs table entries from list */
			File removedPackagesFile = new File("./input/" + versionNum	+ "/changes/packages_index_removals.html");
			Document removedPackagesDoc = Jsoup.parse(removedPackagesFile, "UTF-8", "");
			
			Element removedPackagesList = removedPackagesDoc.getElementById("indexTableEntries");
			// List of removed packages
			String[] removedPackages = removedPackagesList.text().split(" ");

			if (!removedPackagesList.text().isEmpty()) {
				for (int i = 0; i < removedPackages.length; i++) {
					toWrite = versionNum + ",Package," + removedPackages[i] + ",Removals\n";
					bw.write(toWrite);
					toWrite = "";
				}
			}

			// routinely clear writer
			bw.flush();

			/**
			 * Summary Statistic
			 */
			System.out.println("v" + versionNum + " Summary");
			
			if(addedPackages[0] != null && addedPackages[0].length() > 0)
				System.out.println("# Added packages: " + addedPackages.length);
			else
				System.out.println("# Added packages: 0");
			
			if(changedPackages[0] != null && changedPackages[0].length() > 0)
				System.out.println("# Changed packages: " + changedPackages.length);
			else
				System.out.println("# Changed packages: 0");
			
			if(removedPackages[0] != null && removedPackages[0].length() > 0)
				System.out.println("# Removed packages: " + removedPackages.length);
			else
				System.out.println("# Removed packages: 0");
		}

		bw.close();
		System.out.println("TASK COMPLETED!");
	}

	// Fixes any trailing whitespaces resulting from a character in the extended
	// ASCII table
	public static String whitespaceFixer(String input) {
		String fixed = input.trim();
		char lastChar = fixed.charAt(fixed.length() - 1);

		while ((int) lastChar > 127) {
			fixed = fixed.substring(0, fixed.length() - 1);
			lastChar = fixed.charAt(fixed.length() - 1);
		}

		fixed = fixed.trim();

		return fixed;
	}

}