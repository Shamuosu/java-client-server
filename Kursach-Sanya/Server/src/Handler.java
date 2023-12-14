import java.io.*;
import java.net.Socket;
import java.util.logging.Level;


public class Handler implements Runnable {
    private final Socket clientSocket;
    BufferedReader reader;
    BufferedWriter writer;

    public Handler(Socket socket){
        clientSocket = socket;
    }

    public void run(){
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            clientSocket.getOutputStream()));
            reader = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
        } catch (IOException e) {
            Server.LOG.log(Level.WARNING, "READER/WRITER FAILURE:" + e);
            throw new RuntimeException(e);
        }
            String response;
            String[] request;
            try {
                String serverInput = reader.readLine();
                System.out.println(serverInput);
                Server.LOG.log(Level.INFO,"READER STARTED");
                String option = serverInput.split(";")[0];
                Server.LOG.log(Level.INFO,"CLIENT REQUEST ACCEPTED: " + option);
                int beginIndex = option.length() + 1;
                System.out.println(serverInput.substring(beginIndex));
                request = serverInput.substring(beginIndex).split(";");
                switch (option) {
                    case "FtchStrg" -> response = "StrgInf;" + DBControl.fetchStorage();
                    case "FtchPrc" -> response = "PrcInf;" + DBControl.fetchPrice();
                    case "UpdStrg" -> {
                        response = "StrgUpd;";
                        Server.LOG.log(Level.INFO, DBControl.addStorage(request));
                    }
                    case "UpdPrc" -> {
                        response = "PrcUpd;";
                        Server.LOG.log(Level.INFO, DBControl.updatePrice(request));
                    }
                    case "Reg" -> response = DBControl.addUser(request[0], request[1], request[2]);
                    case "Auth" -> response = DBControl.checkUser(request[0], request[1], request[2]);
                    default ->{
                        response = "Incorrect request from user! ";
                        Server.LOG.log(Level.WARNING, "INCORRECT CLIENT REQUEST");
                    }
                }
                writer.write(response);
                Server.LOG.log(Level.INFO, "SERVER RESPONSE TO CLIENT: " + response);
                writer.flush();
                writer.newLine();
            } catch (IOException e) {
                Server.LOG.log(Level.INFO, "CLIENT REQUEST HANDLING ERROR: " + e);
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                        Server.LOG.log(Level.INFO, "WRITER CLOSED");
                    }
                    if (reader != null) {
                        reader.close();
                        clientSocket.close();
                        Server.LOG.log(Level.INFO, "CLIENT SOCKET CLOSED. READER CLOSED");
                    }
                } catch (IOException e) {
                    Server.LOG.log(Level.WARNING,"READER/WRITER/SOCKET CLOSURE ERROR:" + e);
                }
            }
    }
}
