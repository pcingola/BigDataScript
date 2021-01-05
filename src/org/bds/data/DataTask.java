package org.bds.data;

import java.util.ArrayList;
import java.util.Date;

import org.bds.task.TaskDependecies;

/**
 * This 'data' type represents a task
 *
 * I.e. it's a string that only represents a taskID, not a real "data" file
 *
 * @author pcingola
 */
public class DataTask extends Data {

	private static final long serialVersionUID = -5361247001063629052L;

	public static final String PROTOCOL_TASK = "task://";

	public DataTask(String taskId) {
		super(taskId, DataType.TASK);
		localPath = taskId;
		relative = false;
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public boolean canRead() {
		return false;
	}

	@Override
	public boolean canWrite() {
		return false;
	}

	@Override
	protected String createUrl() {
		return "task://" + urlOri;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public void deleteOnExit() {
	}

	@Override
	public boolean download() {
		return true;
	}

	@Override
	public boolean download(Data localFile) {
		throw new RuntimeException("Cannot download taskID '" + this + "'");
	}

	@Override
	public boolean exists() {
		return TaskDependecies.get().hasTask(urlOri);
	}

	@Override
	public String getAbsolutePath() {
		return urlOri;
	}

	@Override
	public String getCanonicalPath() {
		return urlOri;
	}

	@Override
	public Date getLastModified() {
		return null;
	}

	@Override
	public String getName() {
		return urlOri;
	}

	@Override
	public Data getParent() {
		return this;
	}

	@Override
	public String getPath() {
		return urlOri;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isDownloaded() {
		return true;
	}

	@Override
	public boolean isDownloaded(Data localFile) {
		return false;
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public boolean isUploaded(Data localFile) {
		return false;
	}

	@Override
	public Data join(Data segment) {
		throw new RuntimeException("Cannot join data segments for type '" + this.getClass().getName() + "'");
	}

	@Override
	public ArrayList<Data> list() {
		ArrayList<Data> list = new ArrayList<>();
		return list;
	}

	@Override
	public boolean mkdirs() {
		return false;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public boolean upload(Data localFile) {
		throw new RuntimeException("Cannot upload local data type '" + this.getClass().getName() + "'");
	}

}
