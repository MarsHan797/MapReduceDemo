package Code.wendusort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MySort extends WritableComparator {
    public MySort(){
        super(MyKey.class,true);
    }
    public int compare(WritableComparable a,WritableComparable b){
        MyKey k1 = (MyKey)a;
        MyKey k2 = (MyKey)b;
        int r1 = Integer.compare(k1.getYear(),k2.getYear());
        if (r1 == 0){
            int r2 = Integer.compare(k1.getMonth(),k2.getMonth());
            if (r2 == 0){
                return -Double.compare(k1.getAir(),k2.getAir());
            }
            return r2;
        }
        return r1;
    }
}
