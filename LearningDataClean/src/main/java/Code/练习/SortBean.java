package 练习;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SortBean implements WritableComparable<SortBean> {
    private String word;
    private int num;

    public String getName() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return word + '\t' + num;
    }

    @Override
    public int compareTo(SortBean sortBean) {
        int result = this.word.compareTo(sortBean.word);
        if (result == 0) {
            return this.num - sortBean.num;
        }
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(word);
        dataOutput.writeInt(num);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.word = dataInput.readUTF();
        this.num = dataInput.readInt();
    }
}
