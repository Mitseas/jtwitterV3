/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jtwitter;

import java.io.IOException;
import java.net.MalformedURLException;


/**
 *
 * @author r0n
 */
class OrgController {
    
    private OrgModel o_model;
    private OrgView  o_view;
    
    OrgController(OrgModel model, OrgView view, String[] inputCml) throws MalformedURLException, IOException{
        o_model=model;
        o_view=view;
        if(o_view.showExists(checkArg(inputCml))){
            o_model.connect();
            o_model.sendRequest();
            o_view.showString(o_model.getInfo(o_model.recieveRequest()));
        }
    } 
    
     Boolean checkArg(String[] cmlArg) throws MalformedURLException, IOException {
        int k = 0, j=2;
        String arg;
        if (j == cmlArg.length && cmlArg[k].startsWith("-")) {
            arg = cmlArg[k++];
            if (arg.equals("-u")) {
                arg = cmlArg[k++];
                o_model.username=arg;  
                return true;
              }
         } 
        return false;
     }
    
}
