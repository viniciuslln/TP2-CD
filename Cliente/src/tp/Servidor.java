/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinic
 */
public class Servidor implements Runnable {

    Socket server;
    int id;
    PrintWriter pw;
    private final BufferedReader reader;

    Servidor(Socket s, int id) throws IOException {
        this.server = s;
        this.id = id;
        pw = new PrintWriter(server.getOutputStream(), true);
        InputStreamReader iReader = new InputStreamReader(server.getInputStream());
        reader = new BufferedReader(iReader);
    }

    @Override
    public void run() {
        try {
            pw.println(id);

            String mensagem;
            while ((mensagem = reader.readLine()) != null) {

                try {
                    int parseInt = Integer.parseInt(mensagem);
                    Host h = new Host(server);
                    h.id = parseInt;
                    EstadoDaRede.getINSTANCE().getHosts().add(h);
                    System.out.println("Conectado ao parceiro: " + h.id);
                } catch (Exception e) {

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
