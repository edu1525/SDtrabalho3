

import java.util.ArrayList;
import java.io.IOException;


public class Servers{
	private static ArrayList<HTTPServer> servers = new ArrayList<HTTPServer>();


	public static void main(String[] args) throws IOException {

		HTTPServer s1 = new HTTPServer(8080, 1);
		HTTPServer s2 = new HTTPServer(8080, 2);
		HTTPServer s3 = new HTTPServer(8080, 3);

		servers.add(s1);
		servers.add(s2);
		servers.add(s3);





	}


	public void setServer(int port, int serverName){

		try{
			HTTPServer server = new HTTPServer(port, serverName);
			servers.add(server);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public HTTPServer getServer(int serverName){
		HTTPServer aux = null;
		try{
			aux = new HTTPServer(0, serverName);
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		return servers.get(servers.indexOf(aux));

	}

	public String GET(String path) throws org.apache.thrift.TException{
		int hash = path.hashCode() % servers.size();

		for(HTTPServer server : servers){

			String r = server.GET(path);

			if(r != null) return r; 


		}
		return null;

	}

    public String LIST(String path) throws org.apache.thrift.TException{
    	for(HTTPServer server : servers){

    		String r = server.LIST(path);
			if( r != null) return r;


		}
		return null;



    }

    public boolean ADD(String path, String data) throws org.apache.thrift.TException{
    	int hash = (path.hashCode() % servers.size());

    	HTTPServer server = getServer(hash);
    	return server.ADD(path, data);



    }

    public boolean UPDATE(String path, String data) throws org.apache.thrift.TException{
    	for(HTTPServer server : servers){

    		boolean r = server.UPDATE(path, data);
			if(r) return true;


		}

		return false;

    }

    public boolean DELETE(String path) throws org.apache.thrift.TException{
    	for(HTTPServer server : servers){

			boolean r = server.DELETE(path);
			if(r) return true;


		}

		return false;


    }

    public boolean UPDATE_VERSION(String path, String data, int version) throws org.apache.thrift.TException{
    	for(HTTPServer server : servers){

			boolean r = server.UPDATE_VERSION(path, data, version);
			if(r) return true;


		}

		return false;
    }

    public boolean DELETE_VERSION(String path, int version) throws org.apache.thrift.TException{
    	for(HTTPServer server : servers){

			boolean r = server.DELETE_VERSION(path, version);
			if(r) return true;


		}

		return false;

    }


}