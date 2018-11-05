package org.bds.compile;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.bds.compile.CompilerMessage.MessageType;

/**
 * This code is mostly from BailErrorStrategy
 *
 * @author pcingola
 */
public class CompileErrorStrategy extends DefaultErrorStrategy {

	/** Instead of recovering from pendingException {@code e}, re-throw it wrapped
	 *  in a {@link ParseCancellationException} so it is not caught by the
	 *  rule function catches.  Use {@link Exception#getCause()} to get the
	 *  original {@link RecognitionException}.
	 */
	@Override
	public void recover(Parser recognizer, RecognitionException e) {
		// Add a compiler error message
		String message = "Cannot parse input, near '" + e.getOffendingToken().getText() + "'";
		CompilerMessage cm = new CompilerMessage(e.getOffendingToken().getInputStream().getSourceName(), e.getOffendingToken().getLine(), -1, message, MessageType.ERROR);
		CompilerMessages.get().add(cm);

		// Add pendingException to all contexts
		for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent())
			context.exception = e;

		throw new ParseCancellationException(e);
	}

	/** Make sure we don't attempt to recover inline; if the parser
	 *  successfully recovers, it won't throw an pendingException.
	 */
	@Override
	public Token recoverInline(Parser recognizer) throws RecognitionException {
		InputMismatchException e = new InputMismatchException(recognizer);

		String message = "Cannot parse input, near '" + e.getOffendingToken().getText() + "'";
		CompilerMessage cm = new CompilerMessage(e.getOffendingToken().getInputStream().getSourceName(), e.getOffendingToken().getLine(), -1, message, MessageType.ERROR);
		CompilerMessages.get().add(cm);

		// Add pendingException to all contexts
		for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent())
			context.exception = e;

		throw new ParseCancellationException(e);
	}

	//	/** Make sure we don't attempt to recover from problems in subrules. */
	//	@Override
	//	public void sync(Parser recognizer) {
	//	}
}
