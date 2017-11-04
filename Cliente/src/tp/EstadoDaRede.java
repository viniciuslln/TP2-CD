/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 *
 * @author vinic
 */
public class EstadoDaRede {
int conectados =0;
int conectei = 0;
    private List<Host> hosts;
    private List<Socket> servidores;

    public List<Tuple<String, Integer>> getServers() {
        return servers;
    }

    public void setServers(List<Tuple<String, Integer>> servers) {
        this.servers = servers;
    }
    // Queue<Host> filaHosts;
    private Object lock = new Object();

    private static EstadoDaRede INSTANCE;

    private CR caro;
    
    List<Tuple<String,Integer> > servers;

    private EstadoDaRede() {
        servers = new ArrayList<>();
        hosts = new ArrayList<>();
        servidores = new ArrayList<>();
        //    filaHosts = new SynchronousQueue<>();
        caro = new CR();
    }

    public static EstadoDaRede getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new EstadoDaRede();
        }
        return INSTANCE;
    }

    public List<Host> getHosts() {
        synchronized (lock) {
            return hosts;
        }
    }
    
    

//    public Queue<Host> getFilaHosts() {
//        return filaHosts;
//    }

    public CR getCaro() {
        return caro;
    }

    public void setCaro(CR caro) {
        this.caro = caro;
    }

    public List<Socket> getServidores() {
        return servidores;
    }

    public void setServidores(List<Socket> servidores) {
        this.servidores = servidores;
    }

    public int getConectados() {
        return conectados;
    }

    public void setConectados(int conectados) {
        this.conectados = conectados;
    }

    public int getConectei() {
        return conectei;
    }

    public void setConectei(int conectei) {
        this.conectei = conectei;
    }    
    
}
