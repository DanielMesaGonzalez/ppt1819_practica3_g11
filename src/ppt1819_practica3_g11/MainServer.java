package ppt1819_practica3_g11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
public class MainServer {

    static String host = "simu18";
    public static final String Bienvenida="Bienvenido, el Servidor se ha iniciado correctamente."; // Mensaje inicial
    static ServerSocket server=null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            InetAddress serveraddr = InetAddress.getLocalHost();
            server = new ServerSocket(1024,5,serveraddr);   // En mac hay que cambiar el puerto (superior de 1024).
            System.out.println(Bienvenida);  //Mostramos el mensaje de bienvenida cuando se arranca el servidor 
            while(true){
                Socket s = server.accept();
                //System.out.println("Se ha conectado al servidor: "+s.getInetAddress().toString());  //Mostramos mensaje de conexión de cliente
                HttpConnection conn = new HttpConnection(s);
                new Thread(conn).start();
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
}
