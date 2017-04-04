package hw4.hash;

import java.util.List;
import java.util.LinkedList;
public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        System.out.println("M:" + M);
        LinkedList<Oomage>[] buckets = new LinkedList[M];
        for (int i = 0; i < M; i++) {
            buckets[i] = new LinkedList<>();
        }
        int N = oomages.size();
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum].add(o);
        }

        for (int i = 0; i < M; i++) {
            if (buckets[i].size() < N / 50 || buckets[i].size() > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
