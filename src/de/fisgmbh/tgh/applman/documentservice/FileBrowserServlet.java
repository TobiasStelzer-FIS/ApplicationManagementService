package de.fisgmbh.tgh.applman.documentservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

public class FileBrowserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String UNIQUE_NAME = "de.fisgmbh.tgh.applman";
	private static final String UNIQUE_KEY = "de.fisgmbh.tgh.applman.tzfouhuw8kbqfgs7b1zvjku6fjk54lujsnty58d2p";

	public FileBrowserServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doEcm(request, response);
	}

	private void doEcm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().println("<html><body>");

		User user = getUser(request);
		String action = request.getParameter("action");
		if (action == null) {
			action = "navigate";
		}
		action = action.toUpperCase();
		String sPath = request.getPathInfo().substring(1);
		String[] path = sPath.split("/");
		if (sPath.equals("")) {
			path = new String[0];
		}
		System.err.println(path);
		String jsonResponse;
		CmisObject obj = null;

		try {
//			if (user != null) {
//				response.getWriter()
//						.println("User name: " + user.getAttribute("firstname") + " " + user.getAttribute("lastname"));
//				response.getWriter().println("Email: " + user.getAttribute("email"));
//			}
			Session repository = openCmisSession();
			// access the root folder of the repository
			Folder root = repository.getRootFolder();

			String id = null;
			switch (action) {
			case "DELETE":

				break;
			case "CREATEFOLDER":
				id = request.getParameter("id");
				if (id == null) {
					// Create by path
					obj = createFolder(root, path);
				} else {
					// Create by id
					String name = request.getParameter("name");
					obj = createFolderById(repository, id, name);
				}
				jsonResponse = getObjectAsJson(obj);
				response.getWriter().write(jsonResponse);
				break;
			case "CREATEFILE":

				break;
			default:	// Navigate
				id = request.getParameter("id");
				if (id == null) {
					// Navigate by path
					obj = navigate(root, path);
				} else {
					// Navigate by id
					obj = navigateById(repository, id);
				}
				jsonResponse = getObjectAsJson(obj);
				response.getWriter().write(jsonResponse);
				break;
			}
		} catch (Exception e) {
			response.getWriter().println(e.getMessage());
			throw new ServletException(e);
		} finally {
//			response.getWriter().println("</body></html>");
		}
		response.setContentType("application/json");
	}

	// Returns parent folder of newly created Folder
	private CmisObject createFolderById(Session repository, String idOfParent, String folderName) throws ServletException {
		if (folderName == null) {
			throw new ServletException("No name was provided for the Folder.");
		}
		try {
			Folder parent = (Folder)repository.getObject(idOfParent);
			Map<String, String> newFolderProps = new HashMap<String, String>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, folderName);
			parent.createFolder(newFolderProps);
			return parent;
		} catch (ClassCastException e) {
			throw new ServletException("Can't create a Folder inside a Document.");
		}
	}
	
	// Returns parent folder of newly created Folder
	private CmisObject createFolder(Folder root, String[] path) throws ServletException {
		if (path.length < 1) {
			throw new ServletException("Error: You have to specify a path.");
		}

		String jsonResponse = null;
		String foldername = path[path.length-1];
		String[] parentPath = new String[path.length - 1];
		for (int i = 0; i < parentPath.length; i++) {
			parentPath[i] = path[i];
		}
		CmisObject obj = navigate(root, parentPath);
		if (obj instanceof Folder) {
			Map<String, String> newFolderProps = new HashMap<String, String>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, path[path.length - 1]);

			try {
				((Folder) obj).createFolder(newFolderProps);
			} catch (CmisNameConstraintViolationException e) {
				// Folder exists already, nothing to do
				throw new ServletException("NO_ERROR: Folder already exists.");
			}
		} else {
			throw new ServletException(
					"Error: Can't create an Object inside a File. Give the path to a Folder!");
		}
		return obj;
	}
	
	private String getObjectAsJson(CmisObject object) {
		String jsonResponse;
		if (object instanceof Folder) {
			Folder f = (Folder)object;
			jsonResponse = getObjectsAsJson(f.getChildren());
		} else {
			Document d = (Document)object;
			// TODO: do something here
			jsonResponse = "";
		}
		return jsonResponse;
	}
	
	private String getObjectsAsJson(ItemIterable<CmisObject> objects) {
		StringBuilder strbld = new StringBuilder();
		strbld.append("{\"Objects\":[");
		Iterator<CmisObject> it = objects.iterator();
		while (it.hasNext()) {
			CmisObject obj = it.next();
			strbld.append("{");
			strbld.append("\"Name\":\""+obj.getName()+"\",");
			strbld.append("\"Id\":\""+obj.getId()+"\",");
			if (obj instanceof Folder) {
				Folder f = (Folder)obj;
				strbld.append("\"Type\":\"Folder\",");
				ItemIterable<CmisObject> children = f.getChildren();
				strbld.append("\"Items\":\""+children.getTotalNumItems()+"\"");
			} else {
				Document d = (Document)obj;
				strbld.append("\"Type\":\"Document\",");
				strbld.append("\"Size\":\""+d.getContentStreamLength()+"\",");
				strbld.append("\"Filetype\":\""+d.getContentStreamMimeType()+"\"");
			}
			strbld.append("}");
			if (it.hasNext()) {
				strbld.append(",");
			}
		}
		strbld.append("]}");
		return strbld.toString();
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

	private CmisObject navigateById(Session repository, String id) throws ServletException {
		CmisObject obj = repository.getObject(id);
		return obj;
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

	private Session openCmisSession() throws NamingException {
		// Use a unique name with package semantics
		String uniqueName = UNIQUE_NAME;
		// Use a secret key only known to your application (min. 10 chars)
		String secretKey = UNIQUE_KEY;
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
		return openCmisSession;
	}
}
