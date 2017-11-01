/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author vinic
 */
public class Host {
    Socket soc;
    PrintWriter pw;
    int id;
    boolean respondeu = false;
    
    public Host(int id){
        this.id = id;        
    }
    
    public Host(Socket soc) throws IOException{
        this.soc = soc;
        this.pw = new PrintWriter(soc.getOutputStream(),true);
    }

    public Socket getSoc() {
        return soc;
    }

    public void setSoc(Socket soc) {
        this.soc = soc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRespondeu() {
        return respondeu;
    }

    public void setRespondeu(boolean naSC) {
        this.respondeu = naSC;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }
    
    
    
}
