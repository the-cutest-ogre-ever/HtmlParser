public class Record {

    private String word;
    private int counter;

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
        return false;
    }

    public boolean equals(Record newRecord) {
        if (this.word.equals(newRecord.word) && this.counter == newRecord.counter)
            return true;
        return false;
    }

    public String getWord() {
        return word;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return this.word + " - " + this.counter;
    }
}

