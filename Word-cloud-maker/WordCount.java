
public class WordCount{
    public int count; 
    public String word;
    
    public WordCount(String word, int count){
        this.word = word;
        this.count = count; 
    }

    public String getWord(){
            return word; 
    }
    
    public int getCount(){
            return count;
    }
}