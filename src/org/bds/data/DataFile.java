package org.bds.data;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

/**
 * A data file.
 * Local data files do not require download / uploaded
 *
 * @author pcingola
 */
public class DataFile extends Data {

	private static final long serialVersionUID = -1399927916073574111L;

	public static final String PROTOCOL_FILE = "file://";

	File file;

	/**
	 * Resolve a path and always return an absolute path (e.g. relative to 'currentDir')
	 */
	public static File resolveLocalPath(String fileName, String currentDir) {
		if (fileName.toLowerCase().startsWith(PROTOCOL_FILE)) fileName = fileName.substring(PROTOCOL_FILE.length());
		File f = new File(fileName);

		// If fileName is an absolute path, we just return the appropriate file
		if (currentDir == null) return f;
		if (f.toPath().isAbsolute()) return f.getAbsoluteFile();

		// Resolve against 'currentDir'
		return new File(currentDir, fileName).getAbsoluteFile();
	}

	public DataFile(String fileName) {
		super(fileName, DataType.LOCAL);
		file = new File(fileName);
		localPath = file.getAbsolutePath();
		relative = !file.isAbsolute();
	}

	/**
	 * Constructor creates an absolute path, unless 'currentDir' is null
	 */
	public DataFile(String fileName, String currentDir) {
		super(fileName, DataType.LOCAL);
		file = resolveLocalPath(fileName, currentDir);
		localPath = file.getAbsolutePath();
		relative = !file.isAbsolute();
	}

	public DataFile(URI uri) {
		super(uri.toString(), DataType.LOCAL);
		file = new File(uri.getPath());
		localPath = file.getPath();
		relative = !file.isAbsolute();
	}

	@Override
	public boolean canExecute() {
		return file.canExecute();
	}

	@Override
	public boolean canRead() {
		return file.canRead();
	}

	@Override
	public boolean canWrite() {
		return file.canWrite();
	}

	@Override
	protected String createUrl() {
		return "file://" + getCanonicalPath();
	}

	@Override
	public boolean delete() {
		return file.delete();
	}

	@Override
	public void deleteOnExit() {
		file.deleteOnExit();
	}

	@Override
	public boolean download() {
		return true;
	}

	@Override
	public boolean download(Data localFile) {
		throw new RuntimeException("Cannot download local file '" + this + "'");
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	@Override
	public String getCanonicalPath() {
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getLastModified() {
		return new Date(file.lastModified());
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public Data getParent() {
		return new DataFile(file.getParent());
	}

	@Override
	public String getPath() {
		return file.getPath();
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

	@Override
	public boolean isDownloaded() {
		return true;
	}

	@Override
	public boolean isDownloaded(Data localFile) {
		return getAbsolutePath().equals(localFile.getAbsolutePath());
	}

	@Override
	public boolean isFile() {
		return file.isFile();
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public boolean isUploaded(Data localFile) {
		return getAbsolutePath().equals(localFile.getAbsolutePath());
	}

	@Override
	public Data join(Data segment) {
		// Cannot join an absolute segment
		if (!segment.isRelative()) return this;
		File f = new File(file, segment.getPath());
		return new DataFile(f.getAbsolutePath());
	}

	@Override
	public ArrayList<Data> list() {
		String files[] = file.list();
		ArrayList<Data> list = new ArrayList<>();
		if (files == null) return list;

		for (String f : files)
			list.add(new DataFile(f, getAbsolutePath()));

		return list;
	}

	@Override
	public boolean mkdirs() {
		return file.mkdirs();
	}

	@Override
	public long size() {
		return file.length();
	}

	@Override
	public boolean upload(Data localFile) {
		throw new RuntimeException("Cannot upload local file '" + this + "'");
	}

}
