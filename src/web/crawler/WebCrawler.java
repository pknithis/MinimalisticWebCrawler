/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.helper.HttpConnection;
import java.lang.System;
import static java.lang.System.out;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
/**
 *
 * @author nithish
 */

class LinkNode implements Comparable
{
    String url;
    String tag;
    int occurence;
    
    LinkNode(String url)
    {
        this.url = url;       
        tag = url.substring(url.indexOf("www.")+4,url.lastIndexOf("."));
        occurence = 0;
    }
    
    public boolean equals(Object ob)
    {
        LinkNode node1 = (LinkNode)ob;
        out.println();
        out.println("Equals");
        out.println();
        
        if(this.url.equals(node1.url))
        {
            this.occurence++;
            return true;
        }
        else
        {
            return false;
        }
       
    }

    @Override
    public int compareTo(Object o) {
        out.println();
        out.println("CompareTo");
        out.println();
        LinkNode node1 = (LinkNode)o;
        return this.occurence-node1.occurence;
    }
}

class LinkStore
{
    List <LinkNode>links;
    static int linkIndex =  0;
    
    LinkStore()
    {
        links =  new LinkedList<LinkNode>();              
    }
    
    
    public boolean storeLink(String link)
    {
        if(!links.contains(link))
        {
            links.add(new LinkNode(link));
            
        }
        else
        {
            out.println("Reached here");
            System.exit(1);
        }
     return true;   
    }
   
    public String getNextLink()
    {  
       
       LinkNode node =  links.get(linkIndex);
       linkIndex = (linkIndex+1)%links.size();
       return node.url;
    }
    
    public void printTopList()
    {
        Collections.sort(links);
        
        for(int i=0; i<5; i++)
       {
           LinkNode ln=(links.get(i));
           out.println(ln.occurence);
           out.println(ln.tag);
           out.println();
       }
    }
}

class PatternStore
{
    LinkedList <String>patternList;
    static int patternIndex = 0;
    
    PatternStore()
    {
        patternList = new LinkedList<String>();
    }
    
    public boolean addPattern(String pattern)
    {
        patternList.add(pattern);
        return true;
    }
    
    public String getPattern()
    {     
           String s = patternList.get(patternIndex);
           patternIndex = (patternIndex+1)%patternList.size();
           return s;           
    }
}
class Connector
{
    void connect()
    {
        int count = 0;
        URL url = null;
        String urlLink = "http://server2client.com/java6collections/collectionovw.html";
        StringBuilder pageData =  new StringBuilder();
        PatternStore ps = new PatternStore();
        LinkStore ls = new LinkStore();
        
        ps.addPattern("http:.*com");
        
        Pattern pattern = Pattern.compile(ps.getPattern());
        Matcher match = null;
        
        while(count++ < 50)
        {
            out.println("Fetching the next link:"+urlLink);
            
        try {
            url = new URL(urlLink);
        } catch (MalformedURLException ex) {
            urlLink = ls.getNextLink();
//            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream is = connection.getInputStream();
            
            
            while(is.available() > 0)
            {               
                pageData.append((char)is.read());
            }
            
            match = pattern.matcher(pageData);
            
            while(match.find())
            {
                ls.storeLink(match.group());
            }
            
            urlLink = ls.getNextLink();
            
            
                                   
            
        } catch (Exception ex) {
            urlLink = ls.getNextLink();
           // Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        }               
       ls.printTopList();
       
    }
}
public class WebCrawler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connector cn = new Connector();
        cn.connect();
    }
    
}
