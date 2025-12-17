package Generation;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Markus de Koster <markus.de_koster@smail.th-koeln.de>
 */
public class ExpressionTest {

    ConcurrentHashMap<String, Integer> inputVector;

    public ExpressionTest() {
    }

    /**
     * Executed before each test
     */
    @Before
    public void setUp() {
        inputVector = new ConcurrentHashMap<String, Integer>();
    }
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate0() {
        Expression instance = new Expression("a");
        inputVector.put("a", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate1() {
        Expression instance = new Expression("a");
        inputVector.put("a", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate2() {
        Expression instance = new Expression("!a");
        inputVector.put("a", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate3() {
        Expression instance = new Expression("!a");
        inputVector.put("a", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate4() {
        Expression instance = new Expression("a<b");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate5() {
        Expression instance = new Expression("a>b");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate6() {
        Expression instance = new Expression("a<=b");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate7() {
        Expression instance = new Expression("a>=b");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate8() {
        Expression instance = new Expression("a||b");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate9() {
        Expression instance = new Expression("a||b");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate10() {
        Expression instance = new Expression("a||b");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate11() {
        Expression instance = new Expression("a||b");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate12() {
        Expression instance = new Expression("a||!b");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate13() {
        Expression instance = new Expression("!a||b");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate14() {
        Expression instance = new Expression("a&&b");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate15() {
        Expression instance = new Expression("a&&b");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate16() {
        Expression instance = new Expression("a&&b");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate17() {
        Expression instance = new Expression("a&&b");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate18() {
        Expression instance = new Expression("a&&!b");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate19() {
        Expression instance = new Expression("!a&&b");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate20() {
        Expression instance = new Expression("a==b");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate21() {
        Expression instance = new Expression("a==b");
        inputVector.put("a", 5);
        inputVector.put("b", 5);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate22() {
        Expression instance = new Expression("a!=b");
        inputVector.put("a", 4);
        inputVector.put("b", 5);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate23() {
        Expression instance = new Expression("a!=b");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate24() {
        Expression instance = new Expression("(a||b)");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate25() {
        Expression instance = new Expression("!(a||b)");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate26() {
        Expression instance = new Expression("(a||b)&&c");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        inputVector.put("c", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate27() {
        Expression instance = new Expression("a||b&&c");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        inputVector.put("c", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate28() {
        Expression instance = new Expression("a||b&&c||d");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        inputVector.put("c", 1);
        inputVector.put("d", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate29() {
        Expression instance = new Expression("a||b<c&&d||e<f");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        inputVector.put("c", 1);
        inputVector.put("d", 1);
        inputVector.put("e", 1);
        inputVector.put("f", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate30() {
        Expression instance = new Expression("(!(a||b)&&c)||d");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        inputVector.put("c", 1);
        inputVector.put("d", 0);
        assertFalse(instance.evaluate(inputVector));
    }
    

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate31() {
        Expression instance = new Expression("a<=0x07");
        inputVector.put("a", 7);
        inputVector.put("7", 7);
        assertTrue(instance.evaluate(inputVector));
    }
    
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate32() {
        Expression instance = new Expression("a>011");
        inputVector.put("a", 2);
        inputVector.put("3", 3);
        assertFalse(instance.evaluate(inputVector));
    }
        
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate33() {
        Expression instance = new Expression("a>#011");
        inputVector.put("a", 12);
        inputVector.put("11", 11);
        assertTrue(instance.evaluate(inputVector));
    }
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate34() {
        Expression instance = new Expression("a||b&&c");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        inputVector.put("c", 0);
        assertTrue(instance.evaluate(inputVector));
    }
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate35() {
        Expression instance = new Expression("a&&b<c");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        inputVector.put("c", 3);
        assertFalse(instance.evaluate(inputVector));
    }
}
