/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorimpressao;

import java.io.IOException;

/**
 *
 * @author vinic
 */
public class ServidorImpressao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        // TODO code application logic here
        Thread d = new Thread(new ServidorClienteRunnable());
        d.start();
        d.join();        
    }

}
