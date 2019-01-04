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
    private static int comprobacion=0;
    private static String cadena;
    Socket socket = null;
    
    public HttpConnection (Socket s){
        socket=s;
    }
    
    @Override
    public void run() {
        Random r = new Random(System.currentTimeMillis());
        int n=r.nextInt();
        String request_line="";
        BufferedReader input;
        DataOutputStream output;
        FileInputStream input_file;
       // DataOutputStream dos = null;
        try {
            /*dos = new DataOutputStream(socket.getOutputStream());
            dos.write("200 OK".getBytes());
            dos.flush();
            BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = bis.readLine();
            dos.write(("ECO "+line).getBytes());
            dos.flush();*/
            byte[] HTTP_Response=null;
            String HTTP_Status_Line="";
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Buffer de entrada 
            output = new DataOutputStream(socket.getOutputStream());  // y de salida
            
            String resourceFile="index.html";  //Recurso por defecto
           
                
                request_line= input.readLine();
                
                
                String parts[]=request_line.split(" "); //separamos por espacios
                
                if(parts.length==3){ //COMPROBACION DE ERRORES PARA METODO_HTTP,RECURSO,VERSION Y LONGITUD DE PETICION HTTP
                    
                    //El startsWith() te indica si un string inicia con los caracteres de otro string, true o false.
                    if(request_line.startsWith("GET ") || request_line.startsWith("get ")){ //Si recibimos un GET o un get ...
                   // El equalsIgnoreCase() compara dos string para ver si son iguales ignorando las diferencias
                   
                        if(!parts[2].equalsIgnoreCase("HTTP/1.1") && !parts[2].equalsIgnoreCase("HTTP/1.0") 
                             && !parts[2].equalsIgnoreCase("http/1.1") && !parts[2].equalsIgnoreCase("http/1.0")   ){
                            
                            HTTP_Status_Line=HTTPStatusLine_505;  //Version HTTP no soportada 
                            cadena=RecursoHTML_505;
                            HTTP_Response=cadena.getBytes();  //Pasamos la respuesta a una matriz de Bytes
                            comprobacion=1;
                        }else{
                            if(parts[1].equalsIgnoreCase("/")){  // Si la primera posición es / mostramos el recurso por defecto /index.html
                                resourceFile="/index.html";
                            }else{
                                resourceFile=parts[1];             //Si no es una barra (vamos para el directorio) mostramos el archivo que nos solicita
                            }
                            String cadena2=getExtension(resourceFile);
                            HTTP_Response=leerRecurso(resourceFile);
                            
                            if(HTTP_Response==null){ //COMPROBACION SI EXISTE EL RECURSO EN EL SERVIDOR (si no existe el recurso)
                                HTTP_Status_Line=HTTPStatusLine_404;
                                cadena=RecursoHTML_404;
                                HTTP_Response=cadena.getBytes();   //Mostramos codigo 404 Not Found
                                comprobacion=1;
                            }else{ 
                                HTTP_Status_Line=HTTPStatusLine_200; //Si existe mostramos un 200 OK
                                comprobacion=0;
                            }
                        }
                        
                        
                    }else{  //Si no es un GET 
                        HTTP_Status_Line=HTTPStatusLine_405; 
                        cadena=RecursoHTML_405;
                        HTTP_Response=cadena.getBytes();  //Mostramos codigo 405 Método no permitido
                        comprobacion=1;  //Para indicar el tipo de recurso
                    }
                    
                    
                    
                }else{                 //Si la URL está mal especificada
                    HTTP_Status_Line=HTTPStatusLine_400;
                    cadena=RecursoHTML_400;
                    HTTP_Response=cadena.getBytes();  //Mostramos codigo 400 Bad Request 
                    comprobacion=1;  //Para indicar el tipo de recurso
                }

              do{   
            
                  request_line= input.readLine();  //Leemos la entrada 
                       
  
                System.out.println(request_line);
            }while(request_line.compareTo("")!=0);  //Hasta que sea 0
            
            //CABECERAS
            
            int tamaño=HTTP_Response.length;   //Recogemos el tamaño de la respuesta
            
            String connection="Connection:close\r\n"; //cabecera connection
            String contentlength="Content-length:" + String.valueOf(tamaño)+"\r\n"; // cabecera content length
            String contenttype="";
            String date=obtenerFecha(); //cabecera date a partir del metodo obtener fecha (opcional)
            String server="Server: Servidor Protocolos Grupo 11\r\n";//cabecera server
            String allow="Allow: GET\r\n"; //cabecera allow
            if(comprobacion==1){ //cabecera content type en funcion del tipo de recurso
                contenttype="Content-type:text/html\r\n";
                
            }else{  //Si la cabecera no es de tipo texto...
                if(getExtension(resourceFile).equalsIgnoreCase("jpeg")){  //Comprobamos si contiene .jpeg
                     contenttype="Content-type:image/jpeg\r\n";
                }else if(getExtension(resourceFile).equalsIgnoreCase("html")) {  //Comprobamos si contiene .html
                    contenttype="Content-type:text/html; charset=utf-8\r\n"; 
                }else if(getExtension(resourceFile).equalsIgnoreCase("txt")){    //Comprobamos si contiene .txt
                    contenttype="Content-type:text/plain\r\n";
                }
                
            }
            String cadena1="\r\n";                       //Mostramos por pantalla las cabeceras de la petición
            output.write(HTTP_Status_Line.getBytes());
            output.write(connection.getBytes());
            output.write(contenttype.getBytes());
            output.write(date.getBytes());
            output.write(server.getBytes());
            output.write(allow.getBytes());
            output.write(contentlength.getBytes());
            output.write(cadena1.getBytes());
            output.write(HTTP_Response);
            
            //recurso
           
            input.close();   //liberamos el bufer de entrada
            output.close();  // liberamos la salida
            socket.close();  //Cerramos el socket
            
        } catch (IOException ex) {
           // Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
           System.err.println("Exception" + ex.getMessage());  // Mostramos las posibles excepciones 
        }      
    }
    public byte[] leerRecurso(String resourceFile) {

        String cadena="." + resourceFile;   //El punto es para buscar en la carpeta raiz
        try {

        
        File f= new File(cadena); // Almacena en cadena el directorio donde se almacena el archivo
        int tam=(int) f.length();  //Obtenemos la longitud
            
         
        FileInputStream fis = new FileInputStream(f); //Leemos el recurso f
        byte[]data=new byte[tam]; //Lo pasamos a bytes
        fis.read(data); // Leemos
       
         return data;
         
      }
      catch(Exception e){
         e.printStackTrace();
          return null;
      }
        
        
        
    }
    public static String getExtension(String filename) {

            String parts[]=filename.split("\\."); //Separamos el nombre del archivo en nombre y extension
            int longitud=parts.length;  //Cogemos toda la longitud (que será 2 (nombre y extension))
            return parts[longitud-1];  //Solo cogemos la ultima posicion que corresponde con la extensión
            
    }
    
    public static String obtenerFecha(){    //Queda implementar la clase para obtener la fecha
        return null;
       
      }
        
    
}
