package org.bds.osCmd;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output to two different output streams
 * 
 * @author pcingola
 */
public class TeeOutputStream extends OutputStream {

	OutputStream ostream1, ostream2;

	public TeeOutputStream(OutputStream o1, OutputStream o2) {
		ostream1 = o1;
		ostream2 = o2;
	}

	@Override
	public void close() throws IOException {
		// Do not close if they are STDOUT or STDERR
		if ((ostream1 != System.out) && (ostream1 != System.err)) ostream1.close();
		if ((ostream1 != System.out) && (ostream1 != System.err)) ostream2.close();
	}

	@Override
	public void flush() throws IOException {
		ostream1.flush();
		ostream2.flush();
	}

	@Override
	public void write(byte[] b) throws IOException {
		ostream1.write(b);
		ostream2.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		ostream1.write(b, off, len);
		ostream2.write(b, off, len);
	}

	@Override
	public void write(int b) throws IOException {
		ostream1.write(b);
		ostream2.write(b);
	}
}
