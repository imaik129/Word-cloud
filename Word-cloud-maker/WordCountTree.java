import java.util.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class WordCountTree{
    Node root;
    //public ArrayList<WordCount> WordCounts = new ArrayList<WordCount>();
    //private List<WordCount> children;
    
    int numWordCounts; 
    public int numWords; 

    public class Node{
        //instance variables
        private Map<Character, Node> children;
        private char Letter; 
        private int count; 
        private Node prevNode; 
        //Node constructor
        private Node(char Character){
            Letter = Character; 
            count = 0; 
            children = new HashMap<>();
        }
        //getter+setter methods for variables
        public char getLetter(){
            return Letter; 
        }
        public int getCount(){
            return count;
        }
        public Map<Character, Node> getChildren(){
            return children;
        }
        public void setCount(){
            count = count + 1;
        }
        public Node getParent(){
            return prevNode;
        }
        public void setParent(Node parent){
            prevNode = parent; 
        }
    }

        /**
        * Constructs an empty NodeTree.
        */         
        public WordCountTree(){
            root = new Node('\0');
        }
        //method to make sure character at [0] entered is a valid character. 
        public char ValidCharacter(String word){
            if(word!=null && word.length()!=0) {
			    char ch = word.charAt(0);
                return ch;
		    }
            else{
                char ch = '\0';
                return ch;
            }
        }
        /**
        * Adds 1 to the existing count for word, or adds word to the NodeTree
        * with a count of 1 if it was not already present.
        * Implementation must be recursive, not iterative.
        */
        public void incrementCountHelper(String word, Node curr){
            int wordLength = word.length();
            //set variable equal to first character of word; 
            char item = ValidCharacter(word);
            //char item = word.charAt(0); 

            if(wordLength == 1){
                //if letter if in tree, set curr to last letter, and add count to the last letter. 
                if(curr.getChildren().containsKey(item)){
                    curr = curr.getChildren().get(item); 
                    //add one to count
                    curr.setCount();
                }
                else{
                    //if last letter is not in tree, make new Node for letter;
                    Node newNode = new Node(item);
                    newNode.setParent(curr);
                    curr.getChildren().put(item,newNode);
                    //set curr equal to newNode
                    curr = curr.getChildren().get(item);
                    curr.setCount();
                }
                //System.out.println(curr.getLetter());
                
            }
            else if(curr.getChildren().containsKey(item)){
                curr = curr.getChildren().get(item);
                // System.out.println("word.charAt(0) isssss " + word.charAt(0) + " and word.substring(1) is : " + word.substring(1));
                incrementCountHelper(word.substring(1), curr);
                        
            }
            else{
                Node newNode = new Node(item);
                newNode.setParent(curr);
                curr.getChildren().put(item,newNode);
                // System.out.println("the word.charAt(0) isssss " + word.charAt(0) + " and the word.substring(1) is : " + word.substring(1));
                incrementCountHelper(word.substring(1), newNode);
            }     
        }

        public void incrementCount(String word){
            incrementCountHelper(word,root);
        }
        /**
        * Returns true if word is stored in this NodeTree with
        * a count greater than 0, and false otherwise.
        * Implementation must be recursive, not iterative.
        */
        public boolean containsHelper(String word, Node curr){
            int wordLength = word.length();
            char item = word.charAt(0); 
            //if the length of the word is down to 0, and if the curr node is more than 0, return true
            //else return false
            if (wordLength == 1){
                //set curr to last node; 
                curr =  curr.getChildren().get(item);
                //if curr is last letter of word, return true. Otherwise false. 
                if(curr.getCount()>0){
                    return true;
                }
                else{
                    return false;
                }
            }
            //if the hashmap of curr node already contains the item, call containshelper for next item/node; 
            else if (curr.getChildren().containsKey(item)){ 
                curr = curr.getChildren().get(item);
                return containsHelper(word.substring(1),curr);
            }
            //return false if none of the above conditions are met. == word is not stored in tree with count greater than 0;
            else{
                return false;
            }
        }
        //call helper to see if the word is contained in the tree starting from root; 
        public boolean contains(String word){
            //if the root has no children, it will not contain any words so return false;
            if (root.getChildren().isEmpty()){
                return false;
            }
            return containsHelper(word, root);
        }

        /**
        * Returns the count of word, or -1 if word is not in the NodeTree.
        * Implementation must be recursive, not iterative.
        */
        public int getCountHelper(String word, Node curr){
            int wordLength = word.length();
            char item = word.charAt(0); 
            //return count of curr node
            if (wordLength == 1){
                curr = curr.getChildren().get(item);
                return curr.getCount();
            }
            //if curr node has children that contains item, make recursive call on smaller substirng and next item/node. 
            else if (curr.getChildren().containsKey(item)){
                curr = curr.getChildren().get(item);
                return getCountHelper(word.substring(1), curr);
            }
            //return -1 if none of the above conditions are met == word is not in tree. 
            else{
                return -1; 
            }
        }

        public int getCount(String word){
            return getCountHelper(word, root);
        }

        /** 
        * Returns a list of WordCount objects, one per word stored in this 
        * WordCountTree, sorted in decreasing order by count. 
        */

        public List<WordCount> getWordCountsByCountHelper(List<WordCount> list, Node curr){
            //make collection of all child nodes of curr.
            Collection<Node> CollectionsOfAllNode = curr.getChildren().values(); 
            //use iterator to iterate through the child nodes;
            Iterator<Node> iterator =CollectionsOfAllNode.iterator();
            //if the count for curr node is more than 0, call makeWord function to build the word. 
            //then, create new wordCount object, which will be added to the list. 
            if(curr.getCount()>0){
                String temp = makeWord(curr);
                WordCount word = new WordCount(temp, curr.getCount());
                list.add(word);
            }
            //while there is another child in the collection, change curr value to the next child. 
            //make recurisve call to the new curr node, and continue until child nodes are explored.
            while(iterator.hasNext()){
                curr = iterator.next();
                getWordCountsByCountHelper(list, curr);
            }
            //sort in order of highest count to lowest count;
            Collections.sort(list, new SortWordCount());
            //return list;
            return list; 
        }

        public String makeWord(Node curr){
            //build the world starting from curr node;
            //create stack;  
            Stack<String> wordStack = new Stack<>();
            String returnWord = "";
            //while the curr is not at the root of the tree, add character of curr to stack; 
            while(curr != root){
                char ch = curr.getLetter();
                //change char to string;
                String str = Character.toString(ch);
                wordStack.push(str);
                //change curr value to parent node; 
                curr = curr.getParent();  
            } 
            //while stack is not empty, return stack values as string. 
            //for (int i = 0; i < wordStack.size();i++){
            while (!wordStack.empty()) {
                String temp = wordStack.pop();
                returnWord = returnWord + temp;
            }
           //return word; 
           //System.out.println("returnword : " + returnWord);
            return  returnWord;
        }
        
       
        public List<WordCount> getWordCountsByCount(){
            //make new list to use as parameter
            List<WordCount> list = new ArrayList<WordCount>();
             //call helper method. 
            return getWordCountsByCountHelper(list, root);
        }

        /** 
        * Returns a count of the total number of Nodes in the tree. 
        * A tree with only a root is a tree with one Node; it is an acceptable
        * implementation to have a tree that represents no words have either
        * 1 Node (the root) or 0 Nodes.
        * Implementation must be recursive, not iterative.
        */
        public int getNodeCountHelper(Node curr){
            //intialize count;
            int count = 0; 
            //if curr does not have children, return 0.
            if (curr.getChildren().isEmpty()){
                return 0; 
            }
            else{
                //make a collection of all the child nodes of curr;
                Collection<Node> CollectionsOfChildNodes = curr.getChildren().values();
                //make interator to iterate through the collection
                Iterator<Node> iterator = CollectionsOfChildNodes.iterator();
                //if there are still child nodes left, make recursive call on the child node
                while (iterator.hasNext()){
                    count = count + 1 + getNodeCountHelper(iterator.next()); 
                }
            }
            return count;

            
        }
        public int getNodeCount(){
            //if root has no children, return 1, as the root counts as one node. 
            if (root.getChildren().isEmpty()){
                return 1;
            }
            //otherwise, call helper method on root.
            return 1 + getNodeCountHelper(root);
        }

   

        public static void main(String[] args){
            WordCountTree WordCountTree1 = new WordCountTree();
            //add word to tree//or add count if same word; 
            WordCountTree1.incrementCount("Hello");
            WordCountTree1.incrementCount("Hello");
            WordCountTree1.incrementCount("Hello");
            WordCountTree1.incrementCount("Great");
            WordCountTree1.incrementCount("Great");
            WordCountTree1.incrementCount("Heat");
            WordCountTree1.incrementCount("Pent");
            WordCountTree1.incrementCount("Pen");
            //find number of nodes in tree; 
            System.out.println("Number of Nodes in Tree: "+ WordCountTree1.getNodeCount());

            //find number of times word appears in tree;
            System.out.println("# of time X appears : " + WordCountTree1.getCount("Great"));

            //print all wordcount objects in tree;
            for (int i = 0; i < WordCountTree1.getWordCountsByCount().size(); i++){
                System.out.println("Word: " + WordCountTree1.getWordCountsByCount().get(i).getWord() + "  Apearances: " + WordCountTree1.getWordCountsByCount().get(i).getCount());
            }
            //System.out.println("All words: " + WordCountTree1.getWordCountsByCount());

            //check if tree contains a word;
            System.out.println("Does it Contain X ?: " +   WordCountTree1.contains("Hello"));
             
        }


}