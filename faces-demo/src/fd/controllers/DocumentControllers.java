package fd.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.event.FileUploadEvent;

import fd.models.Document;
import fd.models.User;
import fd.services.DocumentServices;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.FileInputStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ApplicationScoped
public class DocumentControllers {

    @ManagedProperty(value = "#{document}")
    Document document;
    @ManagedProperty(value = "#{user}")
    User user;
    static Dotenv dotenv = Dotenv.configure().directory("E:\\Git\\demo-primefaces\\faces-demo").ignoreIfMalformed()
            .ignoreIfMissing().load();
    static DocumentServices documentServices = new DocumentServices();
    static String fileName;
    static String altName;
    static InputStream fileStream;
    private String uploadedMessage = null;
    private String documentType;
    private Map<String, String> documentTypes = new HashMap<>();
    private StreamedContent originFile;

    @PostConstruct
    public void init() {
        documentTypes = new HashMap<>();
        List<String> allTypes = new ArrayList<>();
        try {
            allTypes = documentServices.getAllDocumentTypes();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        for (String type : allTypes) {
            documentTypes.put(type, type);
        }
    }

    public String getUploadedMessage() {
        return uploadedMessage;
    }

    public void setUploadedMessage(String uploadedMessage) {
        this.uploadedMessage = uploadedMessage;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Map<String, String> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(Map<String, String> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StreamedContent getOriginFile() {
        return originFile;
    }

    public void setOriginFile(StreamedContent originFile) {
        this.originFile = originFile;
    }

    public void resetValue() {
        document = new Document();
        document.setName(null);
        document.setType(null);
        document.setFileName(null);
        uploadedMessage = null;
    }

    public void upload(FileUploadEvent event) throws IOException {
        document = new Document();
        document.setType(documentType);
        document.setFileName(event.getFile().getFileName());
        fileName = event.getFile().getFileName();
        fileStream = event.getFile().getInputstream();
        altName = fileName;
        document.setName(altName);
        uploadedMessage = fileName + " is uploaded";
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public void copyFile(String fileName, InputStream in) throws Exception {
        String destination = dotenv.get("UPLOAD_DIR");
        int exFiles = documentServices.existedFiles(fileName.substring(0, fileName.lastIndexOf(".")));
        if (exFiles > 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_v" + exFiles + ".pdf";
            altName = fileName;
        }
        try {
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            @SuppressWarnings("UnusedAssignment")
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public void submitUpload() throws Exception {
        try {
            copyFile(fileName, fileStream);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        if (document.getName().isEmpty()) {
            documentServices.uploadNewDocument(document.getFileName(), documentType, user.getId(), altName);
        } else {
            documentServices.uploadNewDocument(document.getName(), documentType, user.getId(), altName);
        }
        if (documentTypes.get(documentType) == null) {
            documentTypes.put(documentType, documentType);
        }
        document.setName(null);
        document.setFileName(null);
        document.setType(null);
        uploadedMessage = null;
    }

    public void editDocument(int id) throws Exception {
        Document refDoc = documentServices.getDocumentByID(id);
        document = new Document();
        document.setId(refDoc.getId());
        document.setName(refDoc.getName());
        document.setFileName(refDoc.getFileName());
        document.setType(refDoc.getType());
    }

    public void submitEdit() throws Exception {
        documentServices.updateDocumentByID(document.getId(), document.getName(), documentType);
        document.setId(0);
        document.setName(null);
        document.setType(null);
    }

    public void viewDocument(int id) throws Exception {

        Document refDoc = documentServices.getDocumentByID(id);
        String originFileName = refDoc.getFileName();
        @SuppressWarnings("null")
        String base = dotenv.get("ASSETS_DIR").trim();
        File file = new File(base + originFileName);
        DefaultStreamedContent sc = new DefaultStreamedContent(new FileInputStream(file), "application/pdf",
                originFileName.substring(0, originFileName.lastIndexOf(".")));
        originFile = sc;
        document = new Document();
        document.setName(refDoc.getName());
    }
}
