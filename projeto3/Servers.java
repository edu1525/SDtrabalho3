import java.util.ArrayList;
import java.io.IOException;
import org.apache.thrift.TException;

public class Servers {
	private static ArrayList<HTTPServer> servers;

	public static void main(String[] args) throws IOException {
		servers = new ArrayList<HTTPServer>();

		HTTPServer s1 = new HTTPServer(7000, 0);
		HTTPServer s2 = new HTTPServer(8000, 1);
		HTTPServer s3 = new HTTPServer(9000, 2);

		servers.add(s1);
		servers.add(s2);
		servers.add(s3);

		try {
			ADD("/user", "eduardo");
			ADD("/user/age", "21 anos");
			System.out.println(GET("/user") + " - \n" +GET("/user/age"));
		} catch (TException e) {
			e.printStackTrace();
		}
	}

	public static void setServer(int port, int serverName) {
		try {
			HTTPServer server = new HTTPServer(port, serverName);
			servers.add(server);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HTTPServer getServer(int serverName) {
		return servers.get(serverName);
	}

	public static String GET(String path)
	throws org.apache.thrift.TException {
		int hash = path.hashCode() % servers.size();

		for (HTTPServer server : servers) {
			String r = server.GET(path);

			if (r != null) {
				System.out.println("Dado encontrado no servidor " +
					server.getServerName()
				);
				return r;
			}
		}
		return null;
	}

    public static String LIST(String path) throws org.apache.thrift.TException {
    	for (HTTPServer server : servers) {
    		String r = server.LIST(path);
			if (r != null)
				return r;
		}
		return null;
    }

    public static boolean ADD(String path, String data)
	throws org.apache.thrift.TException {
    	int hash = (path.hashCode() % servers.size());
    	HTTPServer server = getServer(hash);

    	System.out.println("Dado inserido no servidor " + server.getServerName());

    	return server.ADD(path, data);
    }

    public static boolean UPDATE(String path, String data)
	throws org.apache.thrift.TException {
    	for (HTTPServer server : servers) {
    		boolean r = server.UPDATE(path, data);
			if (r) return true;
		}
		return false;
    }

    public static boolean DELETE(String path) throws org.apache.thrift.TException {
    	for (HTTPServer server : servers){
			boolean r = server.DELETE(path);
			if (r) return true;
		}
		return false;
    }

    public static boolean UPDATE_VERSION(String path, String data, int version) throws org.apache.thrift.TException{
    	for (HTTPServer server : servers){
			boolean r = server.UPDATE_VERSION(path, data, version);
			if (r) return true;
		}
		return false;
    }

    public static boolean DELETE_VERSION(String path, int version) throws
	org.apache.thrift.TException {
    	for (HTTPServer server : servers) {
			boolean r = server.DELETE_VERSION(path, version);
			if (r) return true;
		}
		return false;
    }

}
