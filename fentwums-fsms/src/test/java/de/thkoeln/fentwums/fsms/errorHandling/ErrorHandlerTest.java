/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thkoeln.fentwums.fsms.errorHandling;

import de.thkoeln.fentwums.fsms.logHandling.LogHandler;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * tests error handler
 * @author Marvin Jolk
 */
public class ErrorHandlerTest {

    LogHandler instance;

    public ErrorHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = LogHandler.getInstance();
    }

    @After
    public void tearDown() {
        instance.reset();
    }

    /**
     * Test of setError0 method, of class ErrorHandler.
     */
    @Test
    public void testSetError0() {
        System.out.println("setError0");
        instance.addError("TestError0");
    }

    /**
     * Test of getErrors0 method, of class ErrorHandler.
     */
    @Test
    public void testGetErrors0() {
        System.out.println("getErrors0");
        List result = instance.getErrors();
        assertEquals(0, result.size());
    }

    /**
     * Test of getErrors1 method, of class ErrorHandler.
     */
    @Test
    public void testGetErrors1() {
        System.out.println("getErrors1");
        instance.addError("TestError1");
        instance.addError("TestError2");
        List result = instance.getErrors();
        assertEquals(2, result.size());
    }

    /**
     * Test of getUnknownErrors0 method, of class ErrorHandler.
     */
    @Test
    public void testGetUnknownErrors0() {
        System.out.println("getUnknownErrors0");
        List<String> result = instance.getUnknownErrors();

        assertEquals(0, result.size());
    }

    /**
     * Test of getUnknownErrors1 method, of class ErrorHandler.
     */
    @Test
    public void testGetUnknownErrors1() {
        System.out.println("getUnknownErrors1");
        instance.addError("TestError3");
        instance.addError("TestError4");
        List<String> result = instance.getUnknownErrors();

        assertEquals(2, result.size());
    }

    /**
     * Test of getUnknownErrors2 method, of class ErrorHandler.
     */
    @Test
    public void testGetUnknownErrors2() {
        System.out.println("getUnknownErrors2");
        instance.addError("TestError5");
        instance.addError("TestError6");
        List<String> result = instance.getUnknownErrors();

        instance.addError("TestError7");
        result = instance.getUnknownErrors();

        assertEquals(1, result.size());
    }

    /**
     * Test of setWarning0 method, of class ErrorHandler.
     */
    @Test
    public void testSetWarning0() {
        System.out.println("setWarning0");
        instance.addWarning("TestWarning0");
    }

    /**
     * Test of getWarning0 method, of class ErrorHandler.
     */
    @Test
    public void testGetWarning0() {
        System.out.println("getWarning0");
        List result = instance.getWarnings();
        assertEquals(0, result.size());
    }

    /**
     * Test of getWarnings1 method, of class ErrorHandler.
     */
    @Test
    public void testGetWarnings1() {
        System.out.println("getWarning1");
        instance.addWarning("TestWarning1");
        instance.addWarning("TestWarning2");
        List result = instance.getWarnings();
        assertEquals(2, result.size());
    }

    /**
     * Test of getUnknownWarnings0 method, of class ErrorHandler.
     */
    @Test
    public void testGetUnknownWarningss0() {
        System.out.println("getUnknownWarnings0");
        List<String> result = instance.getUnknownWarnings();

        assertEquals(0, result.size());
    }

    /**
     * Test of getUnknownWarnings1 method, of class ErrorHandler.
     */
    @Test
    public void testGetUnknownWarnings1() {
        System.out.println("getUnknownWarnings1");
        instance.addWarning("TestWarning3");
        instance.addWarning("TestWarning4");
        List<String> result = instance.getUnknownWarnings();

        assertEquals(2, result.size());
    }

    /**
     * Test of getUnknownWarnings2 method, of class ErrorHandler.
     */
    @Test
    public void testGetUnknownWarnings2() {
        System.out.println("getUnknownWarnings2");
        instance.addWarning("TestWarning5");
        instance.addWarning("TestWarning6");
        List<String> result = instance.getUnknownWarnings();

        instance.addWarning("TestWarning7");
        result = instance.getUnknownWarnings();

        assertEquals(1, result.size());
    }

}
