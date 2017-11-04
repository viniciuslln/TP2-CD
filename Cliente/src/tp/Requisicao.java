/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp;

/**
 *
 * @author vinic
 */
public class Requisicao {
    String req;
    String motivo;
    int id;
    int k;
    
    public Requisicao(){
        
    }
    
    public Requisicao(String[] req){
        this.req = req[0];
        this.k = Integer.parseInt(req[1]);
        this.id = Integer.parseInt(req[2]);
        if(req.length > 3){
            this.motivo = req[3];
        }
    }

    public Requisicao(String req, int id, int k) {
        this.req = req;
        this.id = id;
        this.k = k;
    }

    public Requisicao(String req, String motivo, int id, int k) {
        this.req = req;
        this.motivo = motivo;
        this.id = id;
        this.k = k;
    }

    
    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
}
