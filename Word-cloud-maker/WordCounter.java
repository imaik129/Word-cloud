import java.util.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*; 

public class WordCounter{
    //instance variables
    private Map<String, String> stopWordsMap = new HashMap<>();
    public List<WordCount> wordCountList = new ArrayList<>();
    private WordCountTree Tree;
    //getter method for tree
    public WordCountTree getTree(){
        return Tree; 
    }
    //constructor
    private WordCounter(){
        Tree = new WordCountTree(); 
    }
    //load all the stop words into a Map. 
    public void loadStopWords(String Filepath){
        File file = new File(Filepath);
        Scanner scanner1 = null;
        try{
            scanner1 = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } 
        while (scanner1.hasNextLine()){
            String word = scanner1.nextLine();
            stopWordsMap.put(word,word);
        }
    }
    //add words in file to tree if not in stopwords file
    public void countTextFile(String Filepath){
        File file = new File(Filepath);
        Scanner scanner1 = null;
        try{
            scanner1 = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } 
        while (scanner1.hasNextLine()){
            while (scanner1.hasNext()){
                String word = scanner1.next();
                
                //word = word.replaceAll("\\s*\\p{Punct}+\\s*$", "").toLowerCase().replaceAll("^\\p{Punct}+", "");

                if(stopWordsMap.containsKey(word)){
                }
                else{
                    Tree.incrementCount(word);
                }
            }
    
        //Tree.getWordsCountbyCount();
        }
    }




  //main
    public static void main(String[] args){
        //make instance of wordCounter
        WordCounter WordCounter1 = new WordCounter();
        //loadStopWords from text file
        WordCounter1.loadStopWords("StopWords.txt");
        //if argument is empty, ask to add argument.
        if(args.length==0){
            System.out.println("Please Enter Command line");
        }
        else if (args.length==2){
             WordCounter1.countTextFile(args[0]);
            //set list equal to list of words in tree sorted by number of occurances. 
            List<WordCount> listofWords = WordCounter1.getTree().getWordCountsByCount();
            //take in argument and convert to integer
            int numWords = Integer.parseInt(args[1]);
            //only take in words between 0 and input number
            listofWords = listofWords.subList(0,numWords);
           
            //call getWordCloudHTML method from WordCLoud maker with the name of cloud and the list of wordcount objects as parameter.
            System.out.println(WordCloudMaker.getWordCloudHTML("Word Cloud", listofWords));

            //.makeHTMLWord(WordCount wordCount, int maximumFrequency, int minimumFrequency)

        }
        else{
            System.out.println("Please Enter valid Command Line");
        }
    
    }
}