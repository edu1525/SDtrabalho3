import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.TException;



public class HTTPServer implements Server.Iface{
    private int serverName;
    private HTTPServer nextServer, previousServer;

    private int port;
    private ServerSocket socket;
    private FileTree ft = new FileTree();
    private boolean isOnline = true;
    private Server.Processor processor;


    public HTTPServer(int port, int serverName) throws IOException {

        processor = new Server.Processor(this);

        this.port = port;
        this.serverName = serverName;

        /* Cria a arvore de arquivos
        ft.addFile("/a", "aaaaaaaaa");
        ft.addFile("/b", "bbbbbbbbbb");
        ft.addFile("/a/c", "cccccc");
        ft.addFile("/b/d", "ddddddd");
        //*/

        /*
        try {
            // Cria um socket para o servidor na porta recebida
            this.socket = new ServerSocket(port);
            System.err.println("Started server on port " + port);
        }
        catch (IOException e) {
            System.err.println("Could not create server on port " + port);
            System.exit(-1);
        }
        */
       // while (this.isOnline) {
            // Cria um socket para o cliente e espera uma requisicao
            //System.err.println("Waiting for connection...");
            //Socket client = this.socket.accept();
            //System.err.println("Accepted connection from client " +
            //client.getInetAddress().toString());
            // Dispara uma thread para atender a requisicao e aguarda uma proxima
            //new Thread(new Request(this, client)).start();

            Runnable run = new Runnable(){
                public void run(){
                    simple(processor, port);

                } 

            };

            
        //}
        new Thread (run).start();

        //System.err.println("Server halting...");
        //this.socket.close();
    }

     public void simple(Server.Processor processor, int port) {
        try {
          TServerTransport serverTransport = new TServerSocket(port);
          TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

          System.out.println("Starting server "+serverName+"...");
          server.serve();
        } catch (Exception e) {
          e.printStackTrace();
        }
  }

    public File getFile(String filepath) {
        return this.ft.getFile(filepath);
    }

    public File addFile(String filepath, String data) {
        return this.ft.addFile(filepath, data);
    }

    public boolean removeFile(String filepath) {
        return this.ft.removeFile(filepath);
    }

    public void shutdown() {
        this.isOnline = false;
    }

    /*
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Wrong parameter, please type 'java HTTPServer port'");
            System.exit(-1);
        }
        new HTTPServer(Integer.parseInt(args[0]), 0);
    }
    */

    public String GET(String path) throws org.apache.thrift.TException{
        File f = getFile(path);
        if (f != null) {
            String header = "HTTP/1.1 200 OK\n" + "Verion: " + f.getVersion() + "\nCreation: " 
            + f.getCreationTime() + "\nModification: " +
                    f.getModificationTime() + "\nContent-length: " +
                        f.getData().length() + '\n';

            String retorno = header + f.getData()+"\n";        
            return retorno;
        }
        return null;
        
    }



    public String LIST(String path) throws org.apache.thrift.TException{
        File file = getFile(path);

        ArrayList<File> child = file.getChildren();

        if(child != null){

            StringBuilder builder = new StringBuilder();

            for(File f : child){
                builder.append(f.getName()+"\n");
            }        
           
            return builder.toString();
        } else{

            return null;
        }

    }

    public boolean ADD(String path, String data) throws org.apache.thrift.TException{
        if (addFile(path, data) == null) return false;
        else return true;


    }

    public boolean UPDATE(String path, String data) throws org.apache.thrift.TException{
        File f = getFile(path);

        if (f != null) {
            f.addData(data);
            return true;

        } else return false;
    }

    public boolean DELETE(String path) throws org.apache.thrift.TException{
        return removeFile(path);


    }

    public boolean UPDATE_VERSION(String path, String data, int version) throws org.apache.thrift.TException{
        File f = getFile(path);

        if(f != null && f.getVersion() == version){
            f.addData(data);
            return true;

        } else return false;



    }

    public boolean DELETE_VERSION(String path, int version) throws org.apache.thrift.TException{

        File f = getFile(path);

        if(f != null && f.getVersion() == version){
            return removeFile(path);

        } else return false;

    }

  

    public int getServerName(){
        return this.serverName;
    }

    public void setNextServer(HTTPServer s){
        this.nextServer = s;
    }

    public void setPreviousServer(HTTPServer s){
        this.previousServer = s;
    }

    public HTTPServer getNextServer(){
        return this.nextServer;
    }

    public HTTPServer getPreviousServer(){
        return this.previousServer;

    }

    public boolean equals(Object o ){
        HTTPServer server = (HTTPServer) o;

        if((server != null) && (server.getServerName() == this.serverName)) return true;
        else return false;

    }
}
