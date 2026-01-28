package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import de.thkoeln.fentwums.fsms.data.datalogic.impl.Expression;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests expression class
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
        Expression instance = new Expression("a ");
        inputVector.put("a", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate1() {
        Expression instance = new Expression("a ");
        inputVector.put("a", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate2() {
        Expression instance = new Expression(" not a ");
        inputVector.put("a", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate3() {
        Expression instance = new Expression(" not a ");
        inputVector.put("a", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate4() {
        Expression instance = new Expression("a less_than b ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate5() {
        Expression instance = new Expression("a greater_than b ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate6() {
        Expression instance = new Expression("a less_equal b ");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate7() {
        Expression instance = new Expression("a greater_equal b ");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate8() {
        Expression instance = new Expression("a or b ");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate9() {
        Expression instance = new Expression("a or b ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate10() {
        Expression instance = new Expression("a or b ");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate11() {
        Expression instance = new Expression("a or b ");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate12() {
        Expression instance = new Expression("a or not b ");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate13() {
        Expression instance = new Expression("not a or b ");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate14() {
        Expression instance = new Expression("a and b ");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate15() {
        Expression instance = new Expression("a and b ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate16() {
        Expression instance = new Expression("a and b ");
        inputVector.put("a", 1);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate17() {
        Expression instance = new Expression("a and b ");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate18() {
        Expression instance = new Expression("a and not b ");
        inputVector.put("a", 1);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate19() {
        Expression instance = new Expression(" not a and b ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate20() {
        Expression instance = new Expression("a equal b ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate21() {
        Expression instance = new Expression("a equal b ");
        inputVector.put("a", 5);
        inputVector.put("b", 5);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate22() {
        Expression instance = new Expression("a not_equal b ");
        inputVector.put("a", 4);
        inputVector.put("b", 5);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate23() {
        Expression instance = new Expression("a not_equal b ");
        inputVector.put("a", 0);
        inputVector.put("b", 0);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate24() {
        Expression instance = new Expression("( a or b ) ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate25() {
        Expression instance = new Expression("not ( a or b ) ");
        inputVector.put("a", 0);
        inputVector.put("b", 1);
        assertFalse(instance.evaluate(inputVector));
    }

    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate26() {
        Expression instance = new Expression("( a or b ) and c ");
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
        Expression instance = new Expression("a or b and c ");
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
        Expression instance = new Expression("a or b and c or d ");
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
        Expression instance = new Expression("a or b less_than c and d or e less_than f ");
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
        Expression instance = new Expression("( not ( a or b ) and c ) or d ");
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
        Expression instance = new Expression("a less_equal 7 ");
        inputVector.put("a", 7);
        assertTrue(instance.evaluate(inputVector));
    }
    
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate32() {
        Expression instance = new Expression("a greater_than 11 ");
        inputVector.put("a", 2);
        assertFalse(instance.evaluate(inputVector));
    }
        
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate33() {
        Expression instance = new Expression("a greater_than 11 ");
        inputVector.put("a", 12);
        assertTrue(instance.evaluate(inputVector));
    }
            
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate34() {
        Expression instance = new Expression("a less_than 11 ");
        inputVector.put("a", 12);
        assertFalse(instance.evaluate(inputVector));
    }
    
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate35() {
        Expression instance = new Expression("( not ( a or b ) and c ) or d ");
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
    public void testEvaluate36() {
        Expression instance = new Expression("( a greater_than 07 ) and b ");
        inputVector.put("a", 8);
        inputVector.put("b", 1);
        assertTrue(instance.evaluate(inputVector));
    }
        
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate37() {
        Expression instance = new Expression("a greater_equal 5 ");
        inputVector.put("a", 8);
        assertTrue(instance.evaluate(inputVector));
    }
            
    /**
     * Test of evaluate method, of class Expression.
     */
    @Test
    public void testEvaluate38() {
        Expression instance = new Expression("Timer greater_equal 5 ");
        inputVector.put("Timer", 5);
        boolean result = instance.evaluate(inputVector);
        assertTrue(result);
    }
}
