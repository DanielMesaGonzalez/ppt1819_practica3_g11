package ppt1819_practica3_g11;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;
import java.util.Random;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */

/*ERRORES QUE DEBEMOS IMPLEMENTAR:

- 505: Versión de HTTP no soportada.
- 400: Solicitud Incorrecta.
- 404: Recurso no encontrado.
- 405: Método no permitido.

*/

public class HttpConnection implements Runnable{
    
    public static final String HTTP_Ok="200";
    public static final String HTTPStatusLine_505="HTTP/1.1 505 HTTP VERSION NOT SUPPORTED\r\n";
    public static final String RecursoHTML_505="<html><body><h1>Versi&oacute;n HTTP no soportada</h1></body></html>";
    public static final String HTTPStatusLine_404="HTTP/1.1 404 NOT FOUND\r\n";
    public static final String RecursoHTML_404="<html><body><h1>Recurso No encontrado</h1></body></html>";
    public static final String HTTPStatusLine_200="HTTP/1.1 200 OK\r\n";
    public static final String HTTPStatusLine_400="HTTP/1.1 400 Bad Request\r\n";
    public static final String RecursoHTML_400="<html><body><h1>Petici(&oacute)n incorrecta</h1></body></html>";
    public static final String HTTPStatusLine_405="HTTP/1.1 405 METHOD NOT ALLOWED\r\n";
    public static final String RecursoHTML_405="<html><body><h1>M&eacute;todo no permitido</h1></body></html>";
    
    Socket socket = null;
    
    public HttpConnection (Socket s){
        socket=s;
    }
    
    @Override
    public void run() {
        DataOutputStream dos = null;
        try {
            System.out.println("Se ha conectado al servidor: "+socket.getInetAddress().toString());
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
