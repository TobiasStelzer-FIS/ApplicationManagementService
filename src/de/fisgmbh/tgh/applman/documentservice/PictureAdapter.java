package de.fisgmbh.tgh.applman.documentservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.sap.hana.cloud.samples.util.IOUtils;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;

import de.fisgmbh.tgh.applman.util.IOUtils;

/**
 * This class accesses functionalities of the document service provided by SAP
 * HANA Cloud Platform. It is used for storing and retrieving document content
 * in a folder-like structure. In the current application, the document service
 * is used for storing profile images.
 *
 * @see <a href=
 *      "https://help.hana.ondemand.com/help/frameset.htm?e60b7e45bb57101487a881c7c5487778.html">SAP
 *      HANA Cloud Document Service</a>
 */
public class PictureAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(PictureAdapter.class);

	private static Session cmisSession = null;
	private static final String UNIQUE_NAME = "de.fisgmbh.tgh.applman";
	private static final String UNIQUE_KEY = "de.fisgmbh.tgh.applman.tzfouhuw8kbqfgs7b1zvjku6fjk54lujsnty58d2p";

	private String name;
	private String containingFolder;

	public PictureAdapter(String fileName, String containingFolder) {
		this.name = fileName;
		this.containingFolder = containingFolder;
	}

	/**
	 * This method uploads a document represented as a byte array into the
	 * document service repository. If a document with the same name exists then
	 * it is replaced with the new one.
	 *
	 * @param documentContent
	 *            - the content of the uploaded document represented as a byte
	 *            array
	 */
	public void upload(byte[] documentContent) {

		Session session = getCmisSession();
		if (session == null) {
			LOGGER.error("ECM not found, Session is null.");
			return;
		}

		Document document = getDocument(session);
		if (document != null) {
			deleteDocument(document);
		}

		Folder root = session.getRootFolder();
		Folder folder = null;

		try {
			folder = (Folder) session.getObjectByPath("/" + containingFolder);
		} catch (CmisObjectNotFoundException e) {
			// Folder does not exist
			Map<String, String> newFolderProps = new HashMap<String, String>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, containingFolder);
			folder = root.createFolder(newFolderProps);
		}

		if (folder instanceof Folder) {
			Map<String, Object> properties = getProperties(name);
			ContentStream contentStream = getContentStream(session, documentContent);
	
			folder.createDocument(properties, contentStream, VersioningState.NONE);
		}
	}

	/**
	 * This method retrieves the content of a document (as a byte array) in the
	 * document service repository. This method returns null if the document
	 * does not exist.
	 *
	 * @return the document content as a byte array
	 * @throws IOException
	 */
	public byte[] getAsByteArray() throws IOException {
		InputStream stream = null;

		Session session = getCmisSession();
		if (session == null) {
			LOGGER.error("ECM not found, Session is null.");
			return null;
		}

		Document document = getDocument(session);
		if (document == null) {
			return null;
		}

		try {
			stream = document.getContentStream().getStream();
			return IOUtils.toByteArray(stream);
		}

		finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private Document getDocument(Session session) {

		Document document = null;

		try {
			document = (Document) session.getObjectByPath("/" + containingFolder + "/" + name);
		} catch (ClassCastException exc) {
			LOGGER.error("The path does not point to a Document.", exc);
		} catch (CmisObjectNotFoundException exc) {
			LOGGER.error("The document " + name + " cannot be found.", exc);
		}

		return document;
	}

	private void deleteDocument(Document document) {
		document.deleteAllVersions();
	}

	/**
	 * These properties contain the object type we are going to create in the
	 * repository. In this case - a document with the name specified.
	 *
	 * @param documentName
	 *            - the name of the document to be created
	 */
	private static Map<String, Object> getProperties(String documentName) {

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, documentName);
		properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, "image/png");
		return properties;
	}

	private ContentStream getContentStream(Session session, byte[] documentContent) {
		InputStream stream = new ByteArrayInputStream(documentContent);

		String documentExtension = name.substring(name.lastIndexOf('.') + 1);
		String mimeType = "image/" + documentExtension;

		ContentStream contentStream = session.getObjectFactory().createContentStream(name, documentContent.length,
				mimeType, stream);

		return contentStream;
	}

	private static Session getCmisSession() {

		if (cmisSession == null) {

			try {
				InitialContext ctx = new InitialContext();
				EcmService ecmSvc = (EcmService) ctx.lookup("java:comp/env/EcmService");
				try {
					// connect to my repository
					cmisSession = ecmSvc.connect(UNIQUE_NAME, UNIQUE_KEY);
				} catch (CmisObjectNotFoundException e) {
					// repository does not exist, so try to create it
					createRepository(ecmSvc);
					// should be created now, so connect to it
					cmisSession = ecmSvc.connect(UNIQUE_NAME, UNIQUE_KEY);
				}
			} catch (NamingException exc) {
				LOGGER.error("Could not find the ECM service.", exc);
			}

		}

		return cmisSession;
	}

	private static void createRepository(EcmService ecmSvc) {
		RepositoryOptions options = new RepositoryOptions();
		options.setUniqueName(UNIQUE_NAME);
		options.setRepositoryKey(UNIQUE_KEY);
		options.setVisibility(com.sap.ecm.api.RepositoryOptions.Visibility.PROTECTED);
		ecmSvc.createRepository(options);
	}
}
