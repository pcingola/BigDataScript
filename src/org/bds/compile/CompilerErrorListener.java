package org.bds.compile;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.bds.compile.CompilerMessage.MessageType;

/**
 * Catch some other error messages that 'CompileErrorStrategy' fails to catch
 * @author pcingola
 */
public class CompilerErrorListener implements ANTLRErrorListener {
	@Override
	public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3, boolean arg4, BitSet arg5, ATNConfigSet arg6) {
		// Nothing to do
	}

	@Override
	public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4, ATNConfigSet arg5) {
		// Nothing to do
	}

	@Override
	public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
		// Nothing to do
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		Token token = (Token) offendingSymbol;
		CompilerMessage cm = new CompilerMessage(token.getInputStream().getSourceName(), line, -1, msg, MessageType.ERROR);
		CompilerMessages.get().add(cm);
	}

}
