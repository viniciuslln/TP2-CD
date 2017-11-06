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
        int porta = 6969;
        if(args.length > 0){
            porta = Integer.parseInt(args[0]);
        }
        // TODO code application logic here
        ServerSocket server = new ServerSocket(porta);

        while (true) {
            Socket cliente = server.accept();
            Thread d = new Thread(new ServidorClienteRunnable(cliente));
            d.start();
        }
    }

}
