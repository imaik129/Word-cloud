import java.util.*;
import java.util.Collections;
import java.util.Comparator;

class SortWordCount implements Comparator<WordCount> {
    public int compare(WordCount a, WordCount b){
        return b.count - a.count;
    }

}