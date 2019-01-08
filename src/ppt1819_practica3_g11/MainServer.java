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
 * @author Daniel y Javier
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
            //InetAddress serveraddr = InetAddress.getLocalHost();
            //server = new ServerSocket(1024,5,serveraddr);   // En mac hay que cambiar el puerto (superior de 1024).
            server=new ServerSocket(80);
            System.out.println(Bienvenida);  //Mostramos el mensaje de bienvenida cuando se arranca el servidor 
            while(true){
                Socket s = server.accept();
                System.out.println("Se ha conectado al servidor: "+s.getInetAddress().toString());  //Mostramos mensaje de conexión de cliente
                //HttpConnection conn = new HttpConnection(s);
                //new Thread(conn).start();
                Thread connection=new Thread(new HttpConnection(s));
                connection.start();
            }
        } catch (java.net.BindException ex) {
            //Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        } catch (IOException ex2) {
            //Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex2.getMessage());
        }
        
// prueba commit
    }
    
}
