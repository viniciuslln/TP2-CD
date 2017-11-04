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
 * Classe Runnable responsável por realizar a conexão quando esta instância é servidor
 * Realiza a troca de ids e armazena o nodo na fila de hosts
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
            //Envia id para outro nodo
            pw.println(id);

            String mensagem;
            while ((mensagem = reader.readLine()) != null) {

                try {
                    //Recebe id do nodo conectado e o armazena na lista de hosts
                    int parseInt = Integer.parseInt(mensagem);
                    Host h = new Host(server);
                    h.setId(parseInt);
                    EstadoDaRede.getINSTANCE().getHosts().add(h);
                    System.out.println("Conectado ao parceiro: " + h.getId());
                } catch (Exception e) {

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
