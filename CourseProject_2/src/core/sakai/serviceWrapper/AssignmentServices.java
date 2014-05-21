package core.sakai.serviceWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import taper.util.MyHTTPUtil;
import core.sakai.objects.Constants;
import core.sakai.objects.SakaiAssignment;

public class AssignmentServices {

	public static void main(String[] args) throws IOException, JAXBException {
		SakaiAssignment[] assignments = getAssignmentsForSite("mercury",
				"fd1b44ea-6ee4-4857-809b-dfe5bdb8f0f0",
				"http://localhost:8080/");
		for (SakaiAssignment a : assignments) {
			System.out.println(a);
		}
	}

	/**
	 * Get all assignment for a site
	 * @param siteId e.g. mercury
	 * @param sessionStr The session that you got.
	 * @param serverURL URL points to the server
	 * @return An array of Assignments.
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static SakaiAssignment[] getAssignmentsForSite(String siteId,
			String sessionStr, String serverURL) throws IOException,
			JAXBException {

		@XmlRootElement(name = "assignment_collection")
		class SakaiAssignmentCollection {
			private List<SakaiAssignment> assignment;

			public List<SakaiAssignment> getAssignment() {
				return assignment;
			}

			@SuppressWarnings("unused")
			// Do not delete this method. It's important for a unmarshaller to work.
			public void setAssignment(List<SakaiAssignment> assignment) {
				this.assignment = assignment;
			}

		}

		if (siteId.startsWith("/")) {
			siteId = siteId.substring(1, siteId.length());
		}
		if (siteId.endsWith("/")) {
			siteId = siteId.substring(0, siteId.length() - 1);
		}
		if (serverURL.endsWith("/")) {
			serverURL = serverURL.substring(0, serverURL.length() - 1);
		}

		URL url = new URL(serverURL + "/direct/assignment/site/" + siteId);
		String acceptType = "application/xml";
		String cookie = Constants.PRECEDE_TO_SESSION_ID + sessionStr
				+ Constants.APPEND_TO_SESSION_ID;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				MyHTTPUtil.getRemoteInputStream(conn, acceptType, cookie), "UTF-8"));

		JAXBContext jc = JAXBContext
				.newInstance(SakaiAssignmentCollection.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		SakaiAssignmentCollection collection = (SakaiAssignmentCollection) unmarshaller
				.unmarshal(br);
		conn.disconnect();
		return collection.getAssignment().toArray(new SakaiAssignment[0]);

	}
}
