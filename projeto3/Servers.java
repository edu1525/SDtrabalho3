import java.util.ArrayList;
import java.io.IOException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.TException;

public class Servers implements Server.Iface {
	private HashMap<int, int> servers = new HashMap<int, int>();
	private int serverCont = 0;

	public static void main(String[] args) throws IOException {
		Servers s = new Servers();
		s.createManager(Integer.parseInt(args[0]));
	}

	private void createManager(int port) {
        Server.Processor processor = new Server.Processor(this);
        Runnable run = new Runnable() {
            public void run() {
                try {
                    TServerTransport serverTransport = new TServerSocket(port);
                    TServer server = new TSimpleServer(
                        new Args(serverTransport).processor(processor)
                    );
                    //System.out.println("Starting server " + this.serverName + "...");
                    server.serve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(run).start();
    }

	@Override
	public void CREATE(int port, int name) {
		servers.put(name, port);
	}

	public int getPort(int serverName) {
		return servers.get(serverName);
	}

	public String GET(String path)
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

    public String LIST(String path) throws org.apache.thrift.TException {
		return null;
    }

    public boolean ADD(String path, String data)
	throws org.apache.thrift.TException {
    	return false;
    }

    public boolean UPDATE(String path, String data)
	throws org.apache.thrift.TException {
		return false;
    }

    public boolean DELETE(String path) throws org.apache.thrift.TException {
		return false;
    }

    public boolean UPDATE_VERSION(String path, String data, int version) throws org.apache.thrift.TException{
		return false;
    }

    public boolean DELETE_VERSION(String path, int version) throws
	org.apache.thrift.TException {
		return false;
    }

}
