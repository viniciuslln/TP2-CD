/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorimpressao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author vinic
 */
public class ServidorImpressao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        // TODO code application logic here
        ServerSocket server = new ServerSocket(6969);

        while (true) {
            Socket cliente = server.accept();
            Thread d = new Thread(new ServidorClienteRunnable(cliente));
            d.start();
        }
    }

}
