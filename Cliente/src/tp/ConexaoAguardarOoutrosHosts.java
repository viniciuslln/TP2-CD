/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinic
 */
public class ConexaoAguardarOoutrosHosts implements Runnable {

    ServerSocket serv;

    public ConexaoAguardarOoutrosHosts(int port) {
        try {
            serv = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ConexaoAguardarOoutrosHosts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        System.out.println("Aguardando parceiros...");

        while (true) {
            try {
                final Socket parceiro = serv.accept();
                EstadoDaRede.getINSTANCE().setConectados(EstadoDaRede.getINSTANCE().getConectados());
                Host h = new Host(parceiro);
                //EstadoDaRede.getINSTANCE().getHosts().add( h );
                System.out.println("Conectado a: " + parceiro.getInetAddress().getHostAddress() + ":" + parceiro.getPort());
                new Thread(new Cliente(h)).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
