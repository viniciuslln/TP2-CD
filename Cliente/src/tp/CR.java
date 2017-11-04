/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            enviarReply(h, new Requisicao("Reply", "saida", me.getId(), OSN));
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
                enviarReply(req, new Requisicao("Reply", "liberar", me.getId(), OSN));
                break;
            //se (estado = ocupado) 
            case OCUPADO:
                //então “enfileire requisição [k, j]” 
                requisicoes.add(req);
                break;
            //se (estado = aguardando) 
            case AGUARDANDO:
                // se timestamp for igual assumir prioriade ao host de menor id
                if (OSN == req.k) {
                    if (me.getId() < req.getId()) {
                        //então  “Enfileire requisição [k, j] 
                        requisicoes.add(req);
                    } else {
                        //senão “Envie reply para Pj”
                        enviarReply(req, new Requisicao("Reply", "liberar", me.getId(), OSN));
                    }
                } //então se [OSN, i]  <  [k, j]
                else if (OSN < req.k) {
                    //então  “Enfileire requisição [k, j] 
                    requisicoes.add(req);
                } else {
                    //senão “Envie reply para Pj”
                    enviarReply(req, new Requisicao("Reply", "liberar", me.getId(), OSN));
                }
                break;
        }
    }

    public void enviarRequest() {

        String enviando = "Enviando req: ";
        for (Host host : EstadoDaRede.getINSTANCE().getHosts().stream().filter(a -> !a.isAutorizado()).collect(Collectors.toList())) {
            enviando += host.getId() + ", ";
        }
        // Enviar requisição aos cliente que ja estraram na SC
        EstadoDaRede.getINSTANCE().getHosts().stream().filter(a -> !a.isAutorizado()).forEach(b -> {
            try {
                b.getPw().println("Req" + ":" + OSN + ":" + me.getId());
            } catch (Exception ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        System.out.println(enviando);
    }

    public void tratarReply(Requisicao req) {
        EstadoDaRede.getINSTANCE().getHosts().stream().filter((a) -> (a.getId() == req.id)).forEachOrdered((a) -> {
            a.setRespondeu(true);
        });
    }

    private void esperarConfirmacaoEntradaSC() {

        boolean espera = false;
        while (!espera) {
            boolean tmp = true;
            for (Host host : EstadoDaRede.getINSTANCE().getHosts()) {
                if (host.isAutorizado()) {
                    continue;
                }
                tmp = tmp && host.isRespondeu();
                if (host.isRespondeu()) {
                    host.setAutorizado(true);
                }
            }
            espera = tmp;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        EstadoDaRede.getINSTANCE().getHosts().forEach((host) -> host.setRespondeu(false));

    }

    private void enviarReply(Requisicao para, Requisicao nova) {
        EstadoDaRede.getINSTANCE().getHosts().stream().filter((host) -> (host.getId() == para.id)).forEachOrdered((host) -> {
            try {
                host.getPw().println(nova.req + ":" + nova.k + ":" + nova.id + ":" + nova.motivo);
                host.setAutorizado(false);
            } catch (Exception ex) {
                Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
