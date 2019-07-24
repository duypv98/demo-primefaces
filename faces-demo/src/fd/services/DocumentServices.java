package fd.services;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fd.db_utils.MySQLConnector;
import fd.models.Document;

public class DocumentServices {
	public DocumentServices() {

	}

	public List<Document> getAllDocuments() throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<Document> documents = new ArrayList<Document>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL;";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Document doc = new Document();
			doc.setId(rs.getInt("id"));
			doc.setName(rs.getString("name"));
			doc.setType(rs.getString("type"));
			doc.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
			doc.setUserID(rs.getInt("user_id"));
			doc.setFileName(rs.getString("file_name"));
			documents.add(doc);
		}
		connectionManager.close();
		return documents;
	}

	public List<String> getAllDocumentTypes() throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<String> documentTypes = new ArrayList<String>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT DISTINCT type FROM documents WHERE deleted_at IS NULL;";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			documentTypes.add(rs.getString("type"));
		}
		return documentTypes;
	}

	public Document getDocumentByID(int id) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		Document document = new Document();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE id = " + id + ";";
		ResultSet rs = st.executeQuery(sql);
		rs.first();
		document.setId(id);
		document.setName(rs.getString("name"));
		document.setType(rs.getString("type"));
		document.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
		document.setUserID(rs.getInt("user_id"));
		document.setFileName(rs.getString("file_name"));
		return document;
	}

	public boolean deleteDocumentByID(int id) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		String sql = "UPDATE documents SET deleted_at = NOW() WHERE id = " + id + ";";
		int rowEffected = st.executeUpdate(sql);
		connectionManager.close();
		return (rowEffected != 0);
	}

	public boolean uploadNewDocument(String name, String type, int userId, String fileName) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		String sql = "INSERT INTO documents (name, type, user_id, file_name) VALUES ('" + name + "','" + type + "',"
				+ userId + ",'" + fileName + "');";
		int rowEffected = st.executeUpdate(sql);
		connectionManager.close();
		return (rowEffected != 0);
	}

	public int existedFiles(String name) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		int count = 0;
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL AND file_name LIKE BINARY '" + name + "%';";
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			count++;
		}
		connectionManager.close();
		return count;
	}

	public List<Document> getDocumentsByName(String q) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<Document> documents = new ArrayList<Document>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL AND name LIKE '%" + q + "%';";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Document doc = new Document();
			doc.setId(rs.getInt("id"));
			doc.setName(rs.getString("name"));
			doc.setType(rs.getString("type"));
			doc.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
			doc.setUserID(rs.getInt("user_id"));
			doc.setFileName(rs.getString("file_name"));
			documents.add(doc);
		}
		connectionManager.close();
		return documents;
	}

	public List<Document> getDocumentsByType(String q) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<Document> documents = new ArrayList<Document>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL AND type LIKE '%" + q + "%';";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Document doc = new Document();
			doc.setId(rs.getInt("id"));
			doc.setName(rs.getString("name"));
			doc.setType(rs.getString("type"));
			doc.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
			doc.setUserID(rs.getInt("user_id"));
			doc.setFileName(rs.getString("file_name"));
			documents.add(doc);
		}
		connectionManager.close();
		return documents;
	}

	public List<Document> getDocumentByDateRange(String dFrom, String dTo) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<Document> documents = new ArrayList<Document>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL AND created_at >= '" + dFrom
				+ "' AND created_at <= '" + dTo + "';";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Document doc = new Document();
			doc.setId(rs.getInt("id"));
			doc.setName(rs.getString("name"));
			doc.setType(rs.getString("type"));
			doc.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
			doc.setUserID(rs.getInt("user_id"));
			doc.setFileName(rs.getString("file_name"));
			documents.add(doc);
		}
		connectionManager.close();
		return documents;
	}
	public List<Document> getDocumentByDateTo(String dTo) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<Document> documents = new ArrayList<Document>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL AND created_at <= '" + dTo + "';";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Document doc = new Document();
			doc.setId(rs.getInt("id"));
			doc.setName(rs.getString("name"));
			doc.setType(rs.getString("type"));
			doc.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
			doc.setUserID(rs.getInt("user_id"));
			doc.setFileName(rs.getString("file_name"));
			documents.add(doc);
		}
		connectionManager.close();
		return documents;
	}
	public List<Document> getDocumentByDateFrom(String dFrom) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		List<Document> documents = new ArrayList<Document>();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM documents WHERE deleted_at IS NULL AND created_at >= '" + dFrom + "';";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Document doc = new Document();
			doc.setId(rs.getInt("id"));
			doc.setName(rs.getString("name"));
			doc.setType(rs.getString("type"));
			doc.setDateCreated(new Date(rs.getTimestamp("created_at").getTime()));
			doc.setUserID(rs.getInt("user_id"));
			doc.setFileName(rs.getString("file_name"));
			documents.add(doc);
		}
		connectionManager.close();
		return documents;
	}
}
