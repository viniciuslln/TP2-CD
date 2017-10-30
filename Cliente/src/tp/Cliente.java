/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe Runnable responsável pela comunidação dos outros nodo a instancia local
 * As mensagens de requisição são repassadas a classe CR resposável pela sincronização
 * @author vinic
 */
public class Cliente implements Runnable {

    Host me;

    Socket soc;

    PrintWriter pw;

    OutputStream out;
    InputStream in;

    BufferedReader reader;

    public Cliente(Host host) throws IOException {
        me = host;
        this.soc = me.getSoc();
        out = soc.getOutputStream();
        in = soc.getInputStream();
        pw = new PrintWriter(out,true);
        InputStreamReader iReader = new InputStreamReader(in);
        reader = new BufferedReader(iReader);
    }

    @Override
    public void run() {
        //Primeira mensagem será o id
        try {
            //me.id = in.read();
            //System.out.println("Conectado ao parceiro: " + me.id);
            while (true) {

                String mensagem;
                while ((mensagem = reader.readLine()) != null) {
                    // A primeira mensagem recebida é o envio do id do nodo
                    // se o parse falhar é porque a mensagem não é id
                    // passar para proxima fase
                    try{
                        // Realisa o parse da string do bufferedreader para int
                        int parseInt = Integer.parseInt(mensagem);
                        me.id = parseInt;
                        System.out.println("Conectado ao parceiro: " + me.id);
                        // Envia o id deste nodo para o server
                        pw.println(EstadoDaRede.getINSTANCE().getCaro().getMe().id);
                        continue;
                    }
                    catch( Exception e){
                        
                    }
                    
                    // Divide a mensagem recebida
                    // Formato: "mensagem:id:relogio"
                    System.out.println("Parceiro " + me.id + ": " + mensagem);
                    String text[] = mensagem.split(":");

                    switch (text[0]) {
                        case "Req":
                            EstadoDaRede.getINSTANCE().getCaro().receberRequisicao(new Requisicao(text));
                            break;
                        case "Reply":
                            EstadoDaRede.getINSTANCE().getCaro().tratarReply(Integer.parseInt(text[2]));
                            break;
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
