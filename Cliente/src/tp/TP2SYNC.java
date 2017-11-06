/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinic
 */
public class TP2SYNC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(args[0]);
        int id = Integer.parseInt(args[1]);
        String servidorAddr = args.length > 2 ? args[2] : "localhost";
        int servidorPorta = args.length > 3 ? Integer.parseInt(args[3]) : 6969;
        
        try {
            pegarIps();
            EstadoDaRede.getINSTANCE().getCaro().setMe(new Host(id));

            new Thread(new ConexaoComOutrosHosts(id, port)).start();
            new Thread(new ConexaoAguardarOoutrosHosts(port)).start();

            while (EstadoDaRede.getINSTANCE().getConectados() != EstadoDaRede.getINSTANCE().getServers().size()-1 && EstadoDaRede.getINSTANCE().getConectei() != EstadoDaRede.getINSTANCE().getServers().size()-1) {
                Thread.sleep(1000);
            }

            Socket impressora = new Socket(servidorAddr, servidorPorta);
            PrintWriter pw = (new PrintWriter(impressora.getOutputStream(), true));
            while (true) {

                if (gerarCondicao()) {
                    System.out.println(">>>Tentar Entrar na SC");
                    EstadoDaRede.getINSTANCE().getCaro().entrarSC();
                    System.out.println("===Enviar msg para impressao");
                    for (int i = 1; i <= 10; i++) {
                        pw.println("Server >>>" + id + "<<<< " + i);
                        Thread.sleep(500);
                    }
                    System.out.println("<<<Sair da SC");
                    EstadoDaRede.getINSTANCE().getCaro().sairSC();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TP2SYNC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean gerarCondicao() throws InterruptedException {
        double r = Math.random();
        return r > 0.5;
        //, cada nodo deve sortear um valor pseudoaleatório no intervalo [0,1]. 
        //Caso o valor seja menor ou igual a 0.5 o nodo decidirá por não acessar o recurso compartilhado,
        //caso o valor seja superior a 0.5 o nodo irá solicitar acesso ao recurso compartilhado. 

        //return true;
    }

    public static void pegarIps() throws FileNotFoundException {
        File f = new File("hosts.txt");
        BufferedReader b = new BufferedReader(new FileReader(f));
        String m;
        try {
            while ((m = b.readLine()) != null) {
                System.out.println("pegado" + m);
                String[] t = m.trim().split(":");
                EstadoDaRede.getINSTANCE().getServers().add(new Tuple<>(t[0], Integer.parseInt(t[1])));
            }
        } catch (IOException ex) {
            Logger.getLogger(TP2SYNC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
