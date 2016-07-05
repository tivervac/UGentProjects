package obst;

import tests.Test;

/**
 *
 * @author Titouan Vervack
 */
public class DAIIProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Test test = new Test(".\\trees\\");
        test.warmup();
        test.feedbackTest();
        //test.testOptimize();
        //test.testOptVsSbst();
        test.finished();
    }
}
