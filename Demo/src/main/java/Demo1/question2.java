package Demo1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class question2 {
    private static Path INPATH =new Path("hdfs://localhost:9000/demo1/in");
    private static Path OUTPATH =new Path("hdfs://localhost:9000/demo1/out2");
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(question2.class);

        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.setInputPaths(job,INPATH);

        job.setMapperClass(mapper2.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(reducer2.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job,OUTPATH);

        System.exit(job.waitForCompletion(true)?0:1);
    }
}

//2、统计山西省2013年每个月的汽车销售数量的比例
class mapper2 extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] lines = value.toString().trim().split(",");
        String month = lines[1];
        context.write(new Text(month), new IntWritable(1));
    }
}

class reducer2 extends Reducer<Text, IntWritable, Text, DoubleWritable> {
    HashMap<String, Integer> map = new HashMap<>();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable i : values) {
            count += i.get();
        }
        map.put(key.toString(), count);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (String i : map.keySet()) {
            sum += map.get(i);
        }
        for (String j : map.keySet()) {
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            Double str = Double.parseDouble(decimalFormat.format(map.get(j) / sum));
            context.write(new Text(j), new DoubleWritable(str));
        }
    }
}