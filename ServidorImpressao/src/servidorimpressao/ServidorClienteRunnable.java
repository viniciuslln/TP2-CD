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

    private Socket cliente;
    private BufferedReader reader;

    ServidorClienteRunnable(Socket cliente) throws IOException {
        this.cliente = cliente;

        InputStream in = cliente.getInputStream();

        InputStreamReader iReader = new InputStreamReader(in);
        reader = new BufferedReader(iReader);
    }

    @Override
    public void run() {
        while (true) {
            try {

                String mensagem;
                for (int i = 1; i <= 10; i++) {
                    mensagem = reader.readLine();
                    System.out.println(" RECEBIDO: " + mensagem);
                }
            } catch (Exception ex) {
                Logger.getLogger(ServidorClienteRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
