package chatlara;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * www.codeurjava.com
 */

public class Server {

    public static void main(String[] test) {

        final ServerSocket Chat;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);
        int Puerto = 500;

        Thread enviar;
        Thread recibir;
        String nombre = "Servidor";

        try {
            //inicia el Socket del servidor
            Chat = new ServerSocket(Puerto);

            //inicia la conexion con el cliente
            clientSocket = Chat.accept();
            //inicia los buffers de entrada y salida
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            enviar = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                        if (msg.equalsIgnoreCase("q")) {
                            try {
                                //cierra conexion con el cliente
                                clientSocket.close();
                                //cierra servidor
                                Chat.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });

           //inicia el hilo
            enviar.start();

            recibir = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        //siempre y cuando el cliente esté conectado
                        while (msg != null) {
                            System.out.println(nombre + ": " + msg);
                            msg = in.readLine();
                        }
                        //salir del bucle si el cliente se ha desconectado
                        System.out.println("Cliente desconectado");
                        //cierra el buffer
                        out.close();
                        //cierra conexion con el cliente
                        clientSocket.close();
                        //cierra servidor
                        Chat.close();
                    } catch (IOException e) {
                        System.out.println("ERROR durante la lectura");
                    }
                }
            });

            //escucha lo que le envian por el servidor
            recibir.start();
        } catch (IOException e) {
            System.out.println("ERROR! No se pudo conectar");
        }
    }
}
