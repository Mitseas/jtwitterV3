/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jtwitter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author r0n
 */
class OrgModel {
    
    private SocketChannel channel=null;
    public boolean vflag=false;
           String username="";
           String host="";
     
    void connect() throws IOException{
        URL u = new URL("http://search.twitter.com/");
        host = u.getHost();
        int port = 80;//port
        SocketAddress remote = new InetSocketAddress(host, port);
        channel = SocketChannel.open(remote);
    }
    
    void sendRequest() throws IOException{
        String file = "/search.json?q=from%3A"+username+"&src=typd";
        String request = "GET " + file + " HTTP/1.1\r\n" + "User-Agent: HTTPGrab\r\n" //send request ston server gia na paro to apotelesma tou search
                    + "Accept: text/*\r\n" + "Connection: close\r\n" + "Host: " + host + "\r\n" + "\r\n"; 
        ByteBuffer header = ByteBuffer.wrap(request.getBytes());
        channel.write(header);
    }
    
    String recieveRequest() throws IOException {     
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int i=0;
        String s="";
        while (channel.read(buffer) != -1) {//diavasma istera apo to request
            if(i==1){//kathara dedomena json
                buffer.flip();
                Charset charset = Charset.defaultCharset(); //metatropi to bytebuffer se charbuffer kai metgatropi sde string 
                CharsetDecoder decoder = charset.newDecoder();  
                CharBuffer charBuffer = decoder.decode(buffer);  
                s += charBuffer.toString();  
                buffer.clear();
            }
            if(i==0){ //stixia pou den xriazomaste
                buffer.flip();
                i=1;
            }
        }
        channel.close();
        return s;
    }
    
   String getInfo(String req){
        JSONObject jsonObject = (JSONObject) JSONValue.parse(req);
        JSONArray array=(JSONArray) jsonObject.get("results");//to apotelesma mpori nane ena i polla
        Iterator iterator = array.iterator();
        int n=1;
        String apotelesma="";
        while (iterator.hasNext()) {
            apotelesma+=("\n======Tweet \""+ n +"\"==========\n");
            apotelesma+=((JSONObject)iterator.next()).get("text").toString();
            n++;
        }
        if (n==1){ 
            apotelesma=("======No public Tweets======");
        }
        return apotelesma;
    }
}
