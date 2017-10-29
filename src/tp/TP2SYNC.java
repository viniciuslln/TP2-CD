/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

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
        try {
            EstadoDaRede.getINSTANCE().getCaro().setMe(new Host(id));

            new Thread(new ConexaoMeCliente(id, port)).start();
            new Thread(new ConexaoMeServidor(port)).start();
            
            while (true) {

                if (gerarCondicao()) {
                    Socket impressora = new Socket("localhost", 6969);
                    System.out.println(">>>Tentar Entrar na SC");
                    EstadoDaRede.getINSTANCE().getCaro().entrarSC();
                    System.out.println("===Enviar msg para impressao");
                    (new PrintWriter(impressora.getOutputStream(),true)).println("Server >>>"+id+"<<<<");
                    System.out.println("<<<Sair da SC");
                    EstadoDaRede.getINSTANCE().getCaro().sairSC();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TP2SYNC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean gerarCondicao() throws InterruptedException {
        Thread.sleep(3000);
        return true;
    }

}
