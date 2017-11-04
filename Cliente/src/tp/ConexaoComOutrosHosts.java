/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.net.Socket;
/**
 *
 * @author vinic
 */
public class ConexaoComOutrosHosts implements Runnable {

    int id;
    int port;

    public ConexaoComOutrosHosts(int id,int port) {
        this.id = id;
        this.port = port;
    }

    @Override
    public void run() {
        for (Tuple<String, Integer> server : EstadoDaRede.getINSTANCE().getServers()) {
            String key = server.x;
            Integer value = server.y;
            if(value == port) continue;
            boolean ok = false;
            while (!ok) {
                //System.out.println("Tendando connectar a: " + key + ":"+value);
                try {
                    Socket s = new Socket(key, value);
                    System.out.println("Conectado a: " + key + ":"+value);
                    ok = true;
                    EstadoDaRede.getINSTANCE().setConectei(EstadoDaRede.getINSTANCE().getConectei() +1);
                    new Thread(new Servidor(s, id)).start();
                } catch (Exception e) {
                    //System.out.println("Tentar novamente a: "+ key + ":"+value);
                    ///Logger.getLogger(ConexaoComOutrosHosts.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

}
