package practica_3_ppt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class HttpConnection implements Runnable{

    Socket socket = null;
    
    public HttpConnection (Socket s){
        socket=s;
    }
    
    @Override
    public void run() {
        DataOutputStream dos = null;
        try {
            System.out.println("Starting new HTTP connection with "+socket.getInetAddress().toString());
            dos = new DataOutputStream(socket.getOutputStream());
            dos.write("200 OK".getBytes());
            dos.flush();
            BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = bis.readLine();
            dos.write(("ECO "+line).getBytes());
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
