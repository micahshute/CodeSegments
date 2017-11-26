
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

    public class test{

        public static void main(String[] args){
            System.out.println(IntPart2.part(50));
        }
    }

    class IntPart2 {

        private static List<List<Long>> partAux(long input1, long input2) {
            long minOfInputs = Math.min(input1, input2);
            List<List<Long>> listOfRefactorLists = new ArrayList<List<Long>>();
            if (minOfInputs < 1)
                return listOfRefactorLists;
            for (long decreasingFromMinOfInputs = minOfInputs; decreasingFromMinOfInputs > 0; decreasingFromMinOfInputs--) {
                long r = input1 - decreasingFromMinOfInputs;
                if (r > 0) {
                    List<List<Long>> recursiveSend = partAux(r, decreasingFromMinOfInputs);
                    for (int i = 0; i < recursiveSend.size(); i++) {
                        List<Long> getOneListFromRecursiveSend = recursiveSend.get(i);
                        getOneListFromRecursiveSend.add(decreasingFromMinOfInputs);
                        listOfRefactorLists.add(getOneListFromRecursiveSend);
                    }
                } else {
                    List<Long> q = new ArrayList<Long>();
                    q.add(decreasingFromMinOfInputs);
                    listOfRefactorLists.add(q);
                }
            }
            return listOfRefactorLists;
        }

        public static String part(long n) {
            List<List<Long>> res = IntPart2.partAux(n, n);
            List<Long> r = new ArrayList<Long>();
            for (int i = 0; i < res.size(); i++) {
                List<Long> t = res.get(i);
                long p = 1;
                for (int j = 0; j < t.size(); j++) {
                    p *= t.get(j);
                }
                if (!r.contains(p))
                    r.add(p);
            }
            Collections.sort(r);
            int lg = r.size();
            long sm = 0;
            for (int i = 0; i < r.size(); i++) {
                sm += r.get(i);
            }
            double avg = sm / (double) lg;
            long rge = r.get(lg - 1) - r.get(0);
            double md = (r.get((lg - 1) / 2) + r.get(lg / 2)) / 2.0;
            return String.format(Locale.US, "Range: %d Average: %.2f Median: %.2f",
                    rge, avg, md);
        }
    }

