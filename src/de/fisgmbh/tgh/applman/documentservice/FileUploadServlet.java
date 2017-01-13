package de.fisgmbh.tgh.applman.documentservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;
import com.sap.security.auth.login.LoginContextFactory;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

import de.fisgmbh.tgh.applman.util.IOUtils;


/**
 * Servlet implementation class HelloWorldServlet
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static int DEFAULT_MAX_SIZE = 50 * 1024 * 1024;	// 50 MB
	private static final String HEADER_OF_BMP_FILE = "424d";
	private static final String HEADER_OF_JPEG_FILE = "ffffffffffffffd8ffffffffffffffe0";
	private static final String HEADER_OF_GIF_FILE = "47494638";
	private static final String HEADER_OF_PNG_FILE = "ffffff89504e47";
	private static final Set<String> ALLOWED_IMAGE_HEADERS = new HashSet<String>();
	static {
		Collections.addAll(ALLOWED_IMAGE_HEADERS, HEADER_OF_GIF_FILE, HEADER_OF_JPEG_FILE, HEADER_OF_PNG_FILE);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.getWriter().println("<html><body>");
		// response.getWriter().println("<p>Remote User: " +
		// request.getRemoteUser() + "</p>");
		// response.getWriter().println("<p>Method: " + request.getMethod() +
		// "</p>");
		// response.getWriter().println("<p>Protocol: " + request.getProtocol()
		// + "</p>");
		// response.getWriter().println("<p>Auth Type: " + request.getAuthType()
		// + "</p>");
		// response.getWriter().println("<p>Request URI: " +
		// request.getRequestURI() + "</p>");
		// response.getWriter().println("<p>Path Info: " + request.getPathInfo()
		// + "</p>");
		// response.getWriter().println("<p>Context Path: " +
		// request.getContextPath() + "</p>");
		// response.getWriter().println("<p>Query: " + request.getQueryString()
		// + "</p>");
		//
		// response.getWriter().println("<br>");
		// Enumeration<String> headerNames = request.getHeaderNames();
		// while (headerNames.hasMoreElements()) {
		// String headerNameKey = headerNames.nextElement();
		// String headerNameValue = request.getHeader(headerNameKey);
		// response.getWriter().println("<p>");
		// response.getWriter().println("" + headerNameKey + ": " +
		// headerNameValue);
		// response.getWriter().println("</p>");
		// }

		String user = request.getRemoteUser();
		if (user != null) {
//			doEcm(request, response);
			handleRequest(request, response);
		} else {
			LoginContext loginContext;
			try {
				loginContext = LoginContextFactory.createLoginContext("FORM");
				loginContext.login();
			} catch (LoginException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String type;
		Part part = request.getPart("pictureUploader-data");
		if (part != null) {
			type = "picture";
		} else {
			part = request.getPart("documentUploader-data");
			if (part != null) {
				type = "document";
			} else {
				String error = "Could not retrieve the ApplicationId! Part is null.";
				throw new RuntimeException(error);
			}
		}
		
		String applicationId = org.apache.cxf.helpers.IOUtils.toString(part.getInputStream());
		if (applicationId == null) {
			throw new ServletException("You have to send the ApplicationId via additionalData");
		}
		
		InputStream partInputStream = null;
		byte[] fileContent = null;

		if (type.equals("picture")) {
			part = request.getPart("pictureUploader");
		} else if (type.equals("document")) {
			part = request.getPart("documentUploader");
		}
		
		if (part == null) {
			String error = "Could not retrieve the uploaded content! Part is null.";
			throw new RuntimeException(error);
		}
		try {
			partInputStream = part.getInputStream();
			fileContent = IOUtils.toByteArray(partInputStream);
		} finally {
			if (partInputStream != null) {
				partInputStream.close();
			}
		}

		String fileName = getFileName(part);

		if (fileName == null) {
			String error = "Could not retrieve the name of the uploaded file! File name is null.";
			throw new RuntimeException(error);
		}

		if (fileContent.length > DEFAULT_MAX_SIZE) {
			throw new ServletException("Uploaded file is too large! Limit is 1 MB.");
		}

		if (type.equals("picture")) {
			if (!isImage(fileContent)) {
				throw new ServletException(
						"Uploaded file is not an image from the allowed range! (allowed file types: gif, png, jpg, bmp)");
			}
		}

		String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
		String currentUserId = request.getRemoteUser();

		// Provide ApplicationId
		// Return Folder for Application (Create it if necessary)
		// Create Document with Picture data
		// Save it in the folder
		FileAdapter fa = new FileAdapter(fileName, applicationId);
		try {
			fa.upload(fileContent);
		} catch (Exception e) {
			response.setStatus(500);
			throw new ServletException(e.getMessage());
		}
		response.setStatus(200);
		response.getWriter().write("Success");
		response.getWriter().flush();
	}

	private String getFileName(Part part) {
		String[] components = part.getHeader("content-disposition").split(";");
		for (String singleComponent : components) {
			if (!singleComponent.contains("filename")) {
				continue;
			}

			return singleComponent.substring(singleComponent.indexOf("\"") + 1, singleComponent.lastIndexOf("\""));
		}
		return null;
	}

	private static boolean isImage(byte[] fileContent) {

		boolean result = false;

		int neededHeaderBytes = 4;

		if (fileContent.length < neededHeaderBytes) {
			return false;
		}

		StringBuilder header = new StringBuilder();
		for (int i = 0; i < neededHeaderBytes; i++) {
			header.append(Integer.toHexString(fileContent[i]));
		}

		if (ALLOWED_IMAGE_HEADERS.contains(header.toString()) || header.toString().startsWith(HEADER_OF_BMP_FILE)) {
			result = true;
		}

		return result;
	}

	private void doEcm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("<html><body>");

		User user = getUser(request);
		String query = request.getQueryString();
		if (query == null) {
			query = "navigate";
		}
		query = query.toUpperCase();
		String sPath = request.getPathInfo().substring(1);
		String[] path = sPath.split("/");
		CmisObject obj = null;

		try {
			response.getWriter()
					.println("User name: " + user.getAttribute("firstname") + " " + user.getAttribute("lastname"));
			response.getWriter().println("Email: " + user.getAttribute("email"));

			Session repository = openCmisSession();
			// access the root folder of the repository
			Folder root = repository.getRootFolder();

			switch (query) {
			case "DELETE":

				break;
			case "CREATEFOLDER":
				if (path.length < 1) {
					throw new ServletException("Error: You have to specify a path.");
				}

				String[] shortPath = new String[path.length - 1];
				for (int i = 0; i < shortPath.length; i++) {
					shortPath[i] = path[i];
				}
				obj = navigate(root, shortPath);
				if (obj instanceof Folder) {
					Map<String, String> newFolderProps = new HashMap<String, String>();
					newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
					newFolderProps.put(PropertyIds.NAME, path[path.length - 1]);

					try {
						((Folder) obj).createFolder(newFolderProps);
						response.getWriter().println("Folder " + path[path.length - 1] + " successfully created!");
					} catch (CmisNameConstraintViolationException e) {
						// Folder exists already, nothing to do
						throw new ServletException("NO_ERROR: Folder already exists.");
					}
				} else {
					throw new ServletException(
							"Error: Can't create an Object inside a File. Give the path to a Folder!");
				}
				break;
			case "CREATEFILE":

				break;
			default:
				obj = navigate(root, path);
				if (obj instanceof Folder) {
					response.getWriter().println("Objects in " + obj.getName() + ":");
					renderObjects(((Folder) obj).getChildren(), response);
				}
				break;
			}

			// // create a new file in the root folder
			// Map<String, Object> properties = new HashMap<String, Object>();
			// properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			// properties.put(PropertyIds.NAME, "HelloWorld.txt");
			// byte[] helloContent = "Hello World!".getBytes("UTF-8");
			// InputStream stream = new ByteArrayInputStream(helloContent);
			// ContentStream contentStream =
			// repository.getObjectFactory().createContentStream("HelloWorld.txt",
			// helloContent.length, "text/plain; charset=UTF-8", stream);
			// try {
			// root.createDocument(properties, contentStream,
			// VersioningState.NONE);
			// } catch (CmisNameConstraintViolationException e) {
			// // Document exists already, nothing to do
			// }
			//
			// // Display the root folder's children objects
			// ItemIterable<CmisObject> children = root.getChildren();
			// response.getWriter().println("The root folder of the repository
			// with id " + root.getId()
			// + " contains the following objects:<ul>");
			// for (CmisObject o : children) {
			// response.getWriter().print("<li>" + o.getName());
			// if (o instanceof Folder) {
			// response.getWriter().println(" createdBy: " + o.getCreatedBy() +
			// "</li>");
			// } else {
			// Document doc = (Document) o;
			// response.getWriter().println(" createdBy: " + o.getCreatedBy() +
			// " filesize: "
			// + doc.getContentStreamLength() + " bytes" + "</li>");
			// }
			// }
			// response.getWriter().println("</ul>");
		} catch (Exception e) {
			response.getWriter().println(e.getMessage());
			throw new ServletException(e);
		} finally {
			response.getWriter().println("</body></html>");
		}
	}

	private User getUser(HttpServletRequest request) {
		User user = null;

		// Check for a logged in user
		if (request.getUserPrincipal() != null) {
			try {
				// UserProvider provides access to the user storage
				UserProvider users = UserManagementAccessor.getUserProvider();

				// Read the currently logged in user from the user storage
				user = users.getUser(request.getUserPrincipal().getName());
			} catch (Exception e) {
				// Handle errors
			}
		}

		return user;
	}

	private void renderObjects(ItemIterable<CmisObject> objects, HttpServletResponse response) throws IOException {
		response.getWriter().println("<ul>");
		for (CmisObject o : objects) {
			response.getWriter().print("<li>" + o.getName());
			if (o instanceof Folder) {
				response.getWriter().println(" createdBy: " + o.getCreatedBy() + "</li>");
			} else {
				Document doc = (Document) o;
				response.getWriter().println(" createdBy: " + o.getCreatedBy() + " filesize: "
						+ doc.getContentStreamLength() + " bytes" + "</li>");
			}
		}
		response.getWriter().println("</ul>");
	}

	private CmisObject navigate(Folder root, String[] path) throws ServletException {
		CmisObject obj = root;
		Folder current = root;

		for (int i = 0; i < path.length; i++) {
			String name = path[i];

			boolean itemFound = false;
			ItemIterable<CmisObject> children = current.getChildren();
			for (CmisObject o : children) {
				// Iterate through all children of current Folder
				if (o.getName().equals(name)) {
					// Child matches next path element
					if (o instanceof Folder) {
						// Found child is a Folder
						current = (Folder) o;
						obj = o;
						itemFound = true;
					} else {
						// Found child is a Document
						if (i == path.length - 1) {
							// Only the last element of path can be a Document
							obj = o;
							itemFound = true;
						}
					}
				}
			}
			if (!itemFound) {
				throw new ServletException("CmisObject not found!");
			}
		}

		return obj;
	}

	private Session openCmisSession() throws NamingException {
		// Use a unique name with package semantics
		String uniqueName = "de.fisgmbh.tg.applman.documents";
		// Use a secret key only known to your application (min. 10 chars)
		String secretKey = "_)d2lf=c$x56q1^ax%my2hd#kra=-sq3nb9!)^ptwdnc*##9ms";
		Session openCmisSession = null;
		InitialContext ctx = new InitialContext();
		String lookupName = "java:comp/env/" + "EcmService";
		EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
		try {
			// connect to my repository
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		} catch (CmisObjectNotFoundException e) {
			// repository does not exist, so try to create it
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(uniqueName);
			options.setRepositoryKey(secretKey);
			options.setVisibility(Visibility.PROTECTED);
			ecmSvc.createRepository(options);
			// should be created now, so connect to it
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		}
		// response.getWriter().println("<h3>You are now connected to the
		// Repository with Id "
		// + openCmisSession.getRepositoryInfo().getId() + "</h3>");
		return openCmisSession;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//doGet(request, response);
		handleRequest(request, response);
	}

}
