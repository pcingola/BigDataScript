import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.DebugEventSocketProxy;

import ca.mcgill.mcb.pcingola.bigDataScript.antlr.*;


public class __Test__ {

    public static void main(String args[]) throws Exception {
        BigDataScriptLexer lex = new BigDataScriptLexer(new ANTLRFileStream("/home/pcingola/workspace/BigDataScript/grammars/output/__Test___input.txt", "UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        BigDataScriptParser g = new BigDataScriptParser(tokens, 49100, null);
        try {
            g.main();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}