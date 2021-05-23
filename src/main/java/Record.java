import java.util.ArrayList;

public class Record {

    public String word;
    public int counter;

    public Record(String word) {
        this.word = word;
        this.counter = 1;
    }

    public void incrementCounter() {
        this.counter++;
    }

    public boolean equals(String newWord) {
        if (this.word.equals(newWord))
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "Record{" +
                "word='" + word + '\'' +
                ", counter=" + counter +
                '}';
    }
}

