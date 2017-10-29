/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorimpressao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinic
 */
public class ServidorClienteRunnable implements Runnable {

    private final ServerSocket server;

    public ServidorClienteRunnable() throws IOException {
        this.server = new ServerSocket(6969);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket cliente = server.accept();
                InputStream in = cliente.getInputStream();

                InputStreamReader iReader = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(iReader);

                String mensagem;
                mensagem = reader.readLine();
                System.out.println(" RECEBIDO: " + mensagem);
                
                reader.close();
                iReader.close();
                in.close();
                cliente.close();
            } catch (Exception ex) {
                Logger.getLogger(ServidorClienteRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
