/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    List<Requisicao> visitantes;
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
        visitantes = new ArrayList();
    }

    public void entrarSC() throws InterruptedException {

        //estado:= aguardando; 
        estado = Estado.AGUARDANDO;
        //OSN:= HSN + 1; 
        OSN = HSN + 1;
        //“Enviar requisição <OSN, i> para todos    processos, exceto Pi”
        enviarRequest();
        //Se alguem estiver na SC bloqueia
        esperarConfirmacaoEntradaSC();
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
            enviarReply(h, new Requisicao("Reply", "saida", me.id, OSN));
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
                enviarReply(req, new Requisicao("Reply", "liberar", me.id, OSN));
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
                    enviarReply(req, new Requisicao("Reply", "liberar", me.id, OSN));
                }
                break;
        }
    }

    public void enviarRequest() {
        //if (visitantes.isEmpty()) {
            EstadoDaRede.getINSTANCE().getHosts().forEach((h) -> {
                try {
                    h.getPw().println("Req" + ":" + OSN + ":" + me.id);
                } catch (Exception ex) {
                    Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
//        } else {
//            // Enviar requisição aos cliente que ja estraram na SC
//            List<Requisicao> paraEnviar = new ArrayList<>();
//            visitantes.stream().filter((visitante) -> (visitante.k <= OSN)).forEachOrdered((visitante) -> {
//                paraEnviar.add(visitante);
//            });
//            paraEnviar.forEach((requisicao) -> {
//                EstadoDaRede.getINSTANCE().getHosts().stream().filter((h) -> (h.id == requisicao.id)).forEachOrdered((h) -> {
//                    try {
//                        h.getPw().println("Req" + ":" + OSN + ":" + me.id);
//                        visitantes.remove(requisicao);
//                    } catch (Exception ex) {
//                        Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//            }); 
//        }
    }

    public void tratarReply(Requisicao req) {
        EstadoDaRede.getINSTANCE().getHosts().stream().filter((a) -> (a.getId() == req.id)).forEachOrdered((a) -> {
            a.setRespondeu(true);
        });
    }

    private void esperarConfirmacaoEntradaSC() {

        boolean espera = false;
        while (!espera) {
            //if(EstadoDaRede.getINSTANCE().getHosts().size() != 2 )return;
            boolean tmp = true;
            for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
                tmp = tmp && host.isRespondeu();
            }
            espera = tmp;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
            host.setRespondeu(false);
        }
    }

    private void enviarReply(Requisicao para, Requisicao nova) {
        if(!nova.motivo.equals("liberar"))
            visitantes.add(para);
        EstadoDaRede.getINSTANCE().getHosts().stream().filter((host) -> (host.id == para.id)).forEachOrdered((host) -> {
            try {
                host.getPw().println(nova.req + ":" + nova.k + ":" + nova.id + ":" + nova.motivo );
            } catch (Exception ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
