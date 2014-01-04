/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.test;

import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author hasani
 */
public class Test1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String,Integer> hashMap=new HashMap<>();
        hashMap.put("A", 1);
        hashMap.put("A", 2);
        
        for(int i=0;i<hashMap.size();i++){
            Integer get = hashMap.get("A");
            System.err.println(get);
        }
        
    }

    public InputStream coba() {
        InputStream io = null;

        io = this.getClass().getResourceAsStream("/jmat/deteksisenyawa/helper/MysqlHelper.properties");

        return io;

    }
}
