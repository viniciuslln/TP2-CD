/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinic
 */
public class CR {

    public enum Estado {
        LIVRE,
        OCUPADO,
        AGUARDANDO
    }

    Queue<Requisicao> requisicoes;
    Estado estado;
    int OSN;
    int HSN;
    Host me;

    public Host getMe() {
        return me;
    }

    public void setMe(Host me) {
        this.me = me;
    }

    public CR() {
        estado = Estado.LIVRE;
        HSN = 0;
        requisicoes = new LinkedList<>();
    }

    public void entrarSC() throws InterruptedException {

        //estado:= aguardando; 
        estado = Estado.AGUARDANDO;
        //OSN:= HSN + 1; 
        OSN = HSN + 1;
        //“Enviar requisição <OSN, i> para todos    processos, exceto Pi”
        //Enviar req para quem ja entrou na SC
        enviarRequest();
        esperarConfirmacaoEntradaSC();
        //Se alguem estiver na SC bloqueia
//        for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
//            while (host.isNaSC()) {
//                Thread.sleep(500);
//            }
//        }
        //estado:= ocupado 
        estado = Estado.OCUPADO;
    }

    public void sairSC() {
        //estado:= livre; 
        estado = Estado.LIVRE;
        //“Envie reply para todas requisições enfileiradas (e a retire da fila)”
        for (Requisicao h = this.requisicoes.poll();
                h != null;
                h = this.requisicoes.poll()) {
            enviarReply(h.id, new Requisicao("Reply", me.id, OSN));
            receberRequisicao(h);
        }
    }

    public void receberRequisicao(Requisicao req) {
        //HSN:= max (HSN, k) 
        HSN = Math.max(HSN, req.k);
        switch (estado) {
            //se (estado = livre)
            case LIVRE:
                //então “envie reply para Pj” 
                enviarReply(req.id, new Requisicao("Reply", me.id, OSN));
                break;
            //se (estado = ocupado) 
            case OCUPADO:
                //então “enfileire requisição [k, j]” 
                requisicoes.add(req);
                break;
            //se (estado = aguardando) 
            case AGUARDANDO:
                //então se [OSN, i]  <  [k, j]
                if (OSN < req.k) {
                    //então  “Enfileire requisição [k, j] 
                    requisicoes.add(req);
                } else {
                    //senão “Envie reply para Pj”
                    enviarReply(req.id, new Requisicao("Reply", me.id, OSN));
                }
                break;
        }
    }

    public void enviarRequest() {
        // TODO Enviar requisição aos cliente que ja estraram na SC
        /* for (Host h = EstadoDaRede.getINSTANCE().getFilaHosts().poll();
                h != null;
                h = EstadoDaRede.getINSTANCE().getFilaHosts().poll()) {*/
        for (Host h : EstadoDaRede.getINSTANCE().getHosts()) {
//            if (me.id == h.id) {
//                continue;
//            }
            try {
                //TODO
                h.getPw().println("Req" + ":" + OSN + ":" + me.id);
            } catch (Exception ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void tratarReply(int id) {
        for (Host a : EstadoDaRede.getINSTANCE().getHosts()) {
            if (a.getId() == id) {
                a.setNaSC(true);
            }
        }
    }

    private void esperarConfirmacaoEntradaSC() {
        
        boolean espera = false;
        while(!espera){
            //if(EstadoDaRede.getINSTANCE().getHosts().size() != 2 )return;
            boolean tmp = true;
            for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
                tmp = tmp && host.isNaSC();
            }
            espera = tmp;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
                host.setNaSC(false);
            }
    }

    private void enviarReply(int paraId, Requisicao h) {
        for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
//            if (host.id != paraId) {
//                continue;
//            }
            try {
                host.getPw().println(h.req + ":" + OSN + ":" + me.id);
            } catch (Exception ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
