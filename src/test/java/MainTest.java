import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Pattern;

public class MainTest {

    static volatile SynchronousQueue<Date> queue = new SynchronousQueue<>();
    static volatile CyclicBarrier barrier = new CyclicBarrier(3);

    public static void main(String[] args) throws Exception {
        String regex = "^.*?(?<=cn)$";
        Pattern pattern = Pattern.compile(regex);
        String input = "sina.com.cn";
//        exec();
        write();
        System.out.println(pattern.matcher(input).matches());
    }

    private static void exec() throws Exception{
        RandomAccessFile rw = new RandomAccessFile("tt.txt", "rw");
        int length = (int)rw.length();
        byte[] bs = new byte[length];
        rw.read(bs, 0, length);
        byte[] newbs = new byte[length * 2 - 1];
        for (int i = 0; i < length; i++) {
            newbs[i*2] = bs[i];
            if (i < length - 1) {
                newbs[i*2+1] = (byte)58;
            }
        }
        rw.seek(0L);
        rw.write(newbs, 0, newbs.length);
        rw.close();
    }

    private static void write()throws Exception{
        Path path = Paths.get("tt.txt");
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);

        ByteBuffer buffer = ByteBuffer.allocate((int)channel.size()*2-1);

        channel.read(buffer);
        byte[] array = Arrays.copyOf(buffer.array(), (buffer.capacity()+1)/2);
        buffer.clear();

        for (int i = 0; i < array.length; i++) {
            buffer.put(array[i]);
            if (i < array.length-1) {
                buffer.put((byte) 58);
            }
        }
        buffer.position(0);
        channel.position(0);
        channel.write(buffer);
        channel.close();
    }

}

