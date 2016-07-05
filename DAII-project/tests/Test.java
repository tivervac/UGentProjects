package tests;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import obst.AbstractTree;
import obst.NumberReader;
import obst.Obst1;
import obst.Obst4;
import obst.Sbst1;
import obst.Sbst2;

/**
 *
 * @author Titouan Vervack
 * Contains all performance tests.
 */
public class Test {

    private static final int WU_NODES = 1000000;
    private static final int CLEANER = 10;
    private static final int ITS = 3;
    private final Logger log;
    private final String dir;

    public Test(String loc) {
        dir = loc;
        DateFormat time = new SimpleDateFormat("dd-HH-mm-ss");
        String format = time.format(time.getCalendar().getTime()) + ".log";
        File prettyFile = new File(loc + "Logs\\pretty" + format);
        File numFile = new File(loc + "Logs\\tests" + format);
        log = new Logger(prettyFile, numFile);
    }

    public double getMem() {
        return (((double) ((double) (Runtime.getRuntime().totalMemory() / 1024) / 1024)) - ((double) ((double) (Runtime.getRuntime().freeMemory() / 1024) / 1024)));
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public double avgTime(long time) {
        return ((double) time) / 1000 / ITS;
    }

    public double formatTime(long end, long start) {
        return ((double) (end - start) / 1000);
    }

    public void clean() {
        for (int i = 0; i < CLEANER; i++) {
            System.gc();
        }
    }

    public void warmup() {
        Sbst2 s = new Sbst2();
        Random rng = new Random(8);
        for (int j = 0; j < WU_NODES; j++) {
            s.add(rng.nextInt());
        }
        clean();
        log.log("Warmup finished. Build Sbst2 of " + WU_NODES + " nodes.");
    }

    public void feedbackTest() {
        int[] solution = {0, 0, 0, 1, 1, 1, -1, 2, -1, 3, 1, 2, 8, -3, 3, -3, -2, 1, 2, 1, -2, -2, 2, 2, 2, 7, 30};
        Obst1 o = new Obst1();
        int i = 0;
        int[] calculated = new int[solution.length + 1];
        long start = getTime();
        calculated[i++] = o.size();
        calculated[i++] = o.contains(0);
        calculated[i++] = o.add(3);
        calculated[i++] = o.size();
        calculated[i++] = o.cost();
        calculated[i++] = o.contains(3);
        calculated[i++] = o.contains(1);
        calculated[i++] = o.cost();
        calculated[i++] = o.add(3);
        calculated[i++] = o.cost();
        calculated[i++] = o.add(5);
        calculated[i++] = o.add(6);
        calculated[i++] = o.cost();
        calculated[i++] = o.add(6);
        calculated[i++] = o.contains(6);
        calculated[i++] = o.contains(7);
        calculated[i++] = o.add(5);
        calculated[i++] = o.add(1);
        calculated[i++] = o.add(0);
        calculated[i++] = o.contains(3);
        calculated[i++] = o.contains(4);
        calculated[i++] = o.contains(2);
        calculated[i++] = o.contains(5);
        calculated[i++] = o.add(2);
        calculated[i++] = o.add(4);
        calculated[i++] = o.size();
        calculated[i++] = o.cost();
        long end = getTime();
        double buildTime = formatTime(end, start);
        start = getTime();
        if (solution.length == calculated.length) {
            log.logFail("feedbackTest: arrays are not equal in length");
            return;
        }
        for (int x = 0; x < solution.length; x++) {
            if (solution[x] != calculated[x]) {
                log.logFail("feedbackTest: index = " + x + " expected: " + solution[x] + " found: " + calculated[x]);
                return;
            }
        }
        end = getTime();
        log.logPass("feedbackTest", buildTime, formatTime(end, start), getMem(), 1, solution.length);
        clean();
    }

    public void testOptimize() {
        for (String s : new File(dir + "\\Optimize").list()) {
            if (!new File(dir + "\\Optimize\\" + s).isDirectory()) {
                long buildTime = 0;
                long runTime = 0;
                double mem = 0;
                Obst4 o = null;
                ArrayList<Integer> addList = new ArrayList<>(1000);
                ArrayList<Integer> searchList = new ArrayList<>(10000);
                NumberReader.readFile(dir + "\\Optimize\\" + s, addList, searchList);
                for (int a = 0; a < ITS; a++) {
                    System.out.println("Started build " + a);
                    o = new Obst4();
                    long start = getTime();
                    for (int i = 0; i < addList.size(); i++) {
                        o.add(addList.get(i));
                    }
                    long end = getTime();
                    buildTime += end - start;
                    clean();
                    System.out.println("Started optimize " + a);
                    start = getTime();
                    o.optimize();
                    end = getTime();
                    runTime += end - start;
                    mem += getMem();
                }
                log.logPass("testOptimize " + o, avgTime(buildTime), avgTime(runTime), mem / ITS, ITS, addList.size());
                containsTest(o, searchList);
            }
        }
    }

    public void containsTest(AbstractTree tree, ArrayList<Integer> searchList) {
        long runTime = 0;
        for (int a = 0; a < ITS; a++) {
            long start = getTime();
            for (int i = 0; i < searchList.size(); i++) {
                tree.contains(searchList.get(i));
            }
            long end = getTime();
            runTime += end - start;
        }
        log.logPass("containsTest" + tree, avgTime(runTime), ITS, searchList.size());
    }

    public void bigContainsTest(AbstractTree tree, ArrayList<Integer> searchList, String ongelijk) {
        long navigations = 0;
        long start = getTime();
        for (int i = 0; i < searchList.size(); i++) {
            navigations += tree.contains(searchList.get(i));
        }
        long end = getTime();
        log.logPass("bigContainsTest " + tree, ((double) (end - start)) / 1000, navigations, navigations / searchList.size(), 1, searchList.size(), ongelijk);
    }

    public void buildAndSearchTest() {
        for (String path : new File(dir).list()) {
            if (!new File(dir + path).isDirectory()) {
                String ongelijk = path.split("_")[2];
                long[] buildTime = new long[3];
                double[] mem = new double[3];
                AbstractTree[] trees = new AbstractTree[3];
                ArrayList<Integer> addList = new ArrayList<>(1000000);
                ArrayList<Integer> searchList = new ArrayList<>(10000000);
                NumberReader.readFile(dir + path, addList, searchList);
                trees[0] = new Obst4();
                trees[1] = new Sbst1();
                trees[2] = new Sbst2();
                for (int b = 0; b < trees.length; b++) {
                    System.out.println("Started tree " + b);
                    for (int a = 0; a < ITS; a++) {
                        clean();
                        long start = getTime();
                        for (int i = 0; i < addList.size(); i++) {
                            trees[b].add(addList.get(i));
                        }
                        long end = getTime();
                        buildTime[b] += end - start;
                        mem[b] += getMem();
                        if (!"Obst4".equals(trees[b].toString())) {
                            break;
                        }
                    }
                    log.logPass("testBuildAndContains " + trees[b], avgTime(buildTime[b]), mem[b] / ITS, ITS, addList.size(), ongelijk);
                    clean();
                    bigContainsTest(trees[b], searchList, ongelijk);
                }
            }
        }
    }

    public void testOptVsSbst() {
        for (String s : new File(dir + "\\Optimize").list()) {
            if (!new File(dir + "\\Optimize\\" + s).isDirectory()) {
                AbstractTree[] trees = new AbstractTree[2];
                trees[0] = new Obst4();
                trees[1] = new Sbst2();
                ArrayList<Integer> addList = new ArrayList<>(1000);
                ArrayList<Integer> searchList = new ArrayList<>(10000);
                NumberReader.readFile(dir + "\\Optimize\\" + s, addList, searchList);
                for (int b = 0; b < trees.length; b++) {
                    System.out.println("Started build " + b);
                    for (int a = 0; a < ITS; a++) {
                        for (int i = 0; i < addList.size(); i++) {
                            trees[b].add(addList.get(i));
                        }
                        if (b == 0) {
                            System.out.println("Started optimize " + a);
                            trees[b].optimize();
                        }
                        log.log(trees[b] + " " + trees[b].cost());
                        if (!"Obst4".equals(trees[b].toString())) {
                            break;
                        }
                    }
                    bigContainsTest(trees[b], searchList, "1");
                }
            }
        }
    }

    public void finished() {
        log.close();
    }
}
