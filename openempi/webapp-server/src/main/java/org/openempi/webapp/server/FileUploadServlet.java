/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openempi.webapp.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openempi.webapp.client.model.DatasetWeb;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Dataset;
import org.openhie.openempi.service.PersonManagerService;
import org.springframework.context.ApplicationContext;

public class FileUploadServlet extends HttpServlet
{
	private static final long serialVersionUID = -3586524539799307701L;

	private static File uploadDirectory;
	
	private PersonManagerService personManagerService; 
	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;
	
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

//		if (Context.getConfiguration() == null)
//			Context.startup();	// Csaba -> Odysseas: this was the one I talked in the Issue, I still have to investigate
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
		personManagerService = Context.getPersonManagerService();
		String uploadDirectoryName = Context.getConfiguration().getAdminConfiguration().getFileRepositoryDirectory();
		uploadDirectory = new File(uploadDirectoryName);
        if (!uploadDirectory.exists()) {
        	uploadDirectory.mkdir();
        }
		log.info("Set the upload directory to: " + uploadDirectory.getAbsolutePath());
	}

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String message = "success";
        // process only multipart requests
        if (ServletFileUpload.isMultipartContent(req)) {

            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try {
                List<FileItem> items = upload.parseRequest(req);
                String name = null;
                for (FileItem item : items) {
                	// Store field names
                	if (item.isFormField())  {
                    	if (item.getFieldName().equals(DatasetWeb.TABLE_NAME)) {
                    		name = new String(item.get());
                    	}
                    	continue;
                    }
                    
                    String fileName = item.getName();
                    // get only the file name not whole path
                    if (fileName != null) {
                        fileName = FilenameUtils.getName(fileName);
                    }
                    
                    File uploadedFile = new File(uploadDirectory, fileName);
                    if (uploadedFile.createNewFile()) {
                    	log.debug("Wrote out " + message.length() + " bytes.");
                        item.write(uploadedFile);
                        saveUploadFileEntry(name, fileName, uploadedFile.getAbsolutePath());
                    } else {
                        log.warn("The file already exists in repository: " + uploadedFile.getAbsolutePath());
                        message = "This file already exists in the repository.";
                    }
                }

            } catch (Exception e) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                        "An error occurred while creating the file : " + e.getMessage());
            	log.error("Failed while attempting to upload file: " + e.getMessage(), e);
            	message = "Failed to upload file. Error is: " + e.getMessage();
            }

        } else {
//            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
//                            "Request contents type is not supported by the servlet.");
        	log.warn("Request contents type is not supported by the service.");
        	message = "Upload request contents type is not supported.";
        }
    	// Set to expire far in the past.
    	resp.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
    	// Set standard HTTP/1.1 no-cache headers.
    	resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    	// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
    	resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
    	// Set standard HTTP/1.0 no-cache header.
    	resp.setHeader("Pragma", "no-cache");
    	
    	resp.setContentType("text/html");
    	resp.setContentLength(message.length());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().printf(message);
    }

	private void saveUploadFileEntry(String name, String filename, String absolutePath) throws IOException {
		
		if (name == null || name.length() == 0) {
			log.error("Attempted to upload a file with a blank name of " + name + " and filename of " + filename);
			throw new IOException("File name is not valid.");
		}
		
		// TODO: Once user management is plugged in this won't be needed here
		checkLoggedInUser();
		
		Dataset dataset = new Dataset(name, absolutePath);
		dataset = personManagerService.saveDataset(dataset);
		log.debug("Saved dataset entry: " + dataset);
	}

	private void checkLoggedInUser() {
		String sessionKey = Context.getUserContext().getSessionKey();
		if (sessionKey == null) {
			Context.authenticate(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD);
		}
		log.debug("Currently logged in user is " + Context.getUserContext().getUser());
	}
}
