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
public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileDownloadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user = request.getRemoteUser();
		if (user != null) {
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

		String path = request.getPathInfo();
		String[] elements = path.split("/");
		String applicationId = elements[1];
		String filename = elements[2];

		try {
			FileAdapter fa = new FileAdapter(filename, applicationId);
			byte[] fileBytes = fa.getAsByteArray();
//			response.setHeader("Content-Type", "image/png");
//			response.setContentType("image/png");// or png or gif, etc
			response.setContentLength(fileBytes.length);
			response.getOutputStream().write(fileBytes);
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
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
