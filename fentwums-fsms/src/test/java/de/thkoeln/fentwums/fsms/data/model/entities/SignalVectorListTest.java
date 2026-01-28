package de.thkoeln.fentwums.fsms.data.model.entities;

import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;
import de.thkoeln.fentwums.fsms.data.model.entities.Signal;
import java.util.ArrayList;
import java.util.Hashtable;
import org.junit.Test;
import static org.junit.Assert.*;
import de.thkoeln.fentwums.fsms.data.model.enums.Direction;

/**
 *
 * @author Markus de Koster <markus.de_koster@smail.th-koeln.de>
 */
public class SignalVectorListTest {

    public SignalVectorListTest() {
    }

    @Test
    public void testNextStep_0() {
        ArrayList<Signal> signalList = new ArrayList<>();
        ArrayList<Hashtable<String, Integer>> signalsVectorList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Signal signal = new Signal("signal" + i, Direction.IN);
            signalList.add(signal);
            Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
            for (int j = 0; j < 3; j++) {
                hash.put("signal" + j, i + j);
            }
            signalsVectorList.add(hash);
        }
        SignalVectorList svl = new SignalVectorList();
        svl.setSignals(signalList);
        svl.setSignalsVectorList(signalsVectorList);

        assertTrue(svl.nextStep());
        assertEquals(0, signalList.get(0).getValue());
        assertEquals(1, signalList.get(1).getValue());
        assertEquals(2, signalList.get(2).getValue());
        assertTrue(svl.nextStep());
        assertEquals(1, signalList.get(0).getValue());
        assertEquals(2, signalList.get(1).getValue());
        assertEquals(3, signalList.get(2).getValue());
        assertTrue(svl.nextStep());
        assertEquals(2, signalList.get(0).getValue());
        assertEquals(3, signalList.get(1).getValue());
        assertEquals(4, signalList.get(2).getValue());
    }

}
