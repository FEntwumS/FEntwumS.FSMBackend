/*
 * STDE - State Transition Diagram Editor
 *
 * 2011, 2012 Jan Montag, Andreas Schwenk
 *
 * Component:   Generation
 * Interface:   I_GENERATION
 * Created:     2011-10-28
 */

package Generation;

import Graph.Graph;
import java.io.File;
import java.io.IOException;

/**
 *
 * see class: Generation
 * 
 * @author Andreas Schwenk
 */
public interface I_GENERATION
{
    // verification
// TODO: CHANGE RETURN TYPE IN CLASS DIAGRAM
    public String verifyGraphAndPartialGenerate(Graph graph);
    
    public String checkIntegrityAndDeterminism(Graph graph);
    
    // generation
    public String exportAsSCXML(File file, Graph graph) throws IOException;
    
//TODO: REMOVE FROM CLASS-DIAGRAM:  public void startGeneration(File file, Graph graph) throws IOException;
    
    
//TODO: ADD TO CLASS-DIAGRAM
    public String generateCode_C(File file_h, File file_c, File file_e,  Graph graph) throws IOException;
//TODO: ADD TO CLASS-DIAGRAM
    public String generateCode_VHDL(File file, Graph graph, boolean useProcess) throws IOException;
}
