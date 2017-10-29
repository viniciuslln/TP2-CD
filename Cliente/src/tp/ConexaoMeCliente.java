/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinic
 */
public class ConexaoMeCliente implements Runnable {

    List<Tuple<String,Integer> > servers;
    int id;
    int port;

    public ConexaoMeCliente(int id,int port) {
        servers = new ArrayList<>();
        servers.add( new Tuple<>("localhost", 7770));
        servers.add( new Tuple<>("localhost", 7771));
        servers.add( new Tuple<>("localhost", 7772));
        //servers.add( new Tuple<>("localhost", 7773));
        this.id = id;
        this.port = port;
    }

    @Override
    public void run() {
        for (Tuple<String, Integer> server : servers) {
            
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
                    new Thread(new Servidor(s, id)).start();
                } catch (Exception e) {
                    //System.out.println("Tentar novamente a: "+ key + ":"+value);
                    ///Logger.getLogger(ConexaoMeCliente.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

}
