package chatlara;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);//leer desde teclado
        int Puerto = 500;
        String Host = "127.0.0.1";
        Thread enviar;
        Thread recibir;
        String nombre="Cliente";
        try {
            /*
             * información del servidor (puerto y dirección IP o nombre del host)
             * 127.0.0.0.0.1 es la dirección local de la máquina
             */
            clientSocket = new Socket(Host, Puerto);

            //flujo de salida 
            out = new PrintWriter(clientSocket.getOutputStream());
            //flujo de entrada
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //crea un nuevo hilo que envia lo que escribe 
            enviar = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        out.println(nombre + ": " + msg);
                        out.flush();
                    }
                }
            });
            enviar.start();

            //crea un hilo que recibe lo que escribe desde el servidor
            recibir = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println(msg);
                            msg = in.readLine();
                        }
                        System.out.println("Servidor desconectado");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("ERROR durante la lectura");
                    }
                }
            });
            recibir.start();

        } catch (IOException e) {
            System.out.println("ERROR! No se pudo conectar");
        }
    }
}
