package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * A data file.
 * Local data files do not require download / uploaded
 *
 * @author pcingola
 */
public class DataFile extends Data {

	public static final String PROTOCOL_FILE = "file://";

	File file;

	public static File resolveLocalPath(String fileName, String currentDir) {
		if (fileName.toLowerCase().startsWith(PROTOCOL_FILE)) fileName = fileName.substring(PROTOCOL_FILE.length());
		File f = new File(fileName);

		// If fileName is an absolute path, we just return the appropriate file
		if (f.toPath().isAbsolute() || currentDir == null) return f.getAbsoluteFile();

		// Resolve against 'currentDir'
		return new File(currentDir, fileName).getAbsoluteFile();
	}

	public DataFile(String fileName, String currentDir) {
		super();
		file = resolveLocalPath(fileName, currentDir);
		localPath = file.getAbsolutePath();
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
	public boolean delete() {
		return file.delete();
	}

	@Override
	public void deleteOnExit() {
		file.deleteOnExit();
	}

	@Override
	public boolean download(String localName) {
		return true;
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
	public String getParent() {
		return file.getParent();
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
	public boolean isDownloaded(String localPath) {
		return this.localPath.equals(localPath);
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
	public boolean isUploaded(String localPath) {
		return this.localPath.equals(localPath);
	}

	@Override
	public ArrayList<String> list() {
		String files[] = file.list();
		ArrayList<String> list = new ArrayList<String>();
		if (files == null) return list;

		for (String f : files)
			list.add(f);

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
	public boolean upload(String filename) {
		throw new RuntimeException("Error: Cannot upload local file '" + this + "'");
	}

}
