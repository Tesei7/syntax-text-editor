package ru.tesei7.textEditor.app.menuActions;

import java.io.IOException;
import java.io.InputStream;

import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.apache.commons.io.IOUtils;

public class LoadFileService {

	public String loadFileAsText(){
		 FileOpenService fos = null;
         FileContents fileContents = null;

         try {
             fos = (FileOpenService)ServiceManager.
                       lookup("javax.jnlp.FileOpenService"); 
         } catch (UnavailableServiceException exc) { }

         if (fos != null) {
             try {
                 fileContents = fos.openFileDialog(null, null); 
             } catch (Exception e) {
                e.printStackTrace();
             }
         }

         if (fileContents != null) {
             try {
                 return "Opened file: " + fileContents.getName()
                            + ".\n"  + getText(fileContents.getInputStream());
             } catch (IOException exc) {
            	 return "Problem opening file: "
                            + exc.getLocalizedMessage();
             }
         } else {
        	 return "User canceled open request.";
         }
	}

	private String getText(InputStream inputStream) {
		try {
			return IOUtils.toString(inputStream, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return "Problem opening file: " + e.getMessage();
		}
	}
}
