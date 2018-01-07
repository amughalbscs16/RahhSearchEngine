/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsa.project;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import org.tartarus.martin.Stemmer;
public class Main {
    
   public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {
       int todo = 0;
       System.out.println("What do you want to do? {search or process}");
       Scanner input = new Scanner(System.in);
       todo = input.nextInt();
       if (todo == 1){
        try {
         File inputFile = new File("D:\\Search Engine Java\\Xml file\\simplewiki.txt");
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         UserHandler userhandler = new UserHandler();
         saxParser.parse(inputFile, userhandler);
        } catch (Exception e) {
         e.printStackTrace();
        }
        }
        else if (todo == 2){
            System.out.println("Enter a Search Query");
            String searchQuery = new Scanner(System.in).nextLine();
            try
            {
                SearchingPart.Search(searchQuery);
            }
            catch(FileNotFoundException fnfe)
            {
                System.out.println("File Not Found Exception");
            }
       }
   }   

}
class SearchingPart {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
       public static void Search(String sentence) throws FileNotFoundException
         {
        String words[] = sentence.split("[\\s+]");
        String directory = "D:\\Search Engine Java\\processeddata\\";
        Hashtable<Integer,WordPage> htable;
        htable = new Hashtable<>();
        ArrayList<WordPage> Result = new ArrayList<>();
        File file;          String content="";     Scanner input;
        for(String word:words)
        {
            System.out.println("Word is :"+word);
         int id=0;
         try{
         file = new File(directory+"\\"+word+"\\documents.txt");
         input =new Scanner(file);
         while(input.hasNext())
         {
         String line = input.nextLine();
         try{
         id = Integer.parseInt(line.split(",")[0]);
         }
         catch(ArrayIndexOutOfBoundsException aioob)
         {
         }
                /*Making a List from
                HashMap of WordPages and incrementing pagerank of repeated pages.*/
         WordPage check = htable.get(id);
         if(check == null){
         htable.put(id,new WordPage(line));
         }
         else 
         {
             htable.get(id).incrementPageRank(check.getPageRank());
             htable.get(id).incrementCount(check.getCount(0), 0);
             htable.get(id).incrementCount(check.getCount(1), 1);
         }
         }   
        }
         catch (FileNotFoundException fnfe)
        {
                System.out.println("No Results Available for this Query");
        }
        }
        Set<Integer> keys = htable.keySet();
        /*Generating a Result List.*/
        for (int key:keys)
        {
            Result.add(htable.get(key));
        }
        Collections.sort(Result, new RankComparator());
        /*Printing The Search Results.*/
        System.out.println("Total Generated Results are: "+Result.size());
        for (int k=0;k<20;k++){
        try{
        System.out.println(Result.get(k).toStringDisplay());  
        }
        catch(ArrayIndexOutOfBoundsException ioobe){
        }
        
        }
    }
    
}
class RankComparator implements Comparator<WordPage>{
        @Override
        public int compare(WordPage o1, WordPage o2) {
            return (o1.getPageRank() > o2.getPageRank() ) ? -1: (o1.getPageRank() < o2.getPageRank()) ? 1:0 ; //To change body of generated methods, choose Tools | Templates.
        }
 }
