package cz.cuni.mff.d3s.been.bpkplugin;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.plugin.logging.Log;

/**
 * Represents wildcard or file which will be added into generated BPK bundle.
 * 
 * @author donarus
 * 
 */
public final class FileItem {

	/**
	 * Folder in generated archive, where files will be situated. If not
	 * specified, files will be added into root of generated archive.
	 * 
	 * @parameter
	 */
	private String folder;

	/**
	 * When specified, no wildcard will be used, but this file will be added
	 * directly to archive.
	 * 
	 * @parameter
	 */
	private File file;

	/**
	 * Files will be searched in {@link #wildcardWorkingDirectory} by this
	 * wildcard.
	 * 
	 * @parameter
	 */
	private String wildcard;

	/**
	 * When {@link #wildcard} is specified, files will be searched in this
	 * directory.
	 * 
	 * @parameter
	 */
	private File wildcardWorkingDirectory;

	/**
	 * Create list of files, which will be added to archive. Depending on
	 * parameters, single file or files found by specified {@link #wildcard} in
	 * {@link #wildcardWorkingDirectory} will be added.
	 * 
	 * @param log
	 *          maven log
	 * @return files which will be added to archive
	 */
	public List<FileToArchive> getFilesToArchive(Log log) {
		checkParameters(log);

		String folderName = generateFinalFolderName();
		List<File> files = collectFilesForAdding();

		List<FileToArchive> filesToArchive = new ArrayList<FileToArchive>();
		for (File f : files) {
			String nameInBpk = folderName + f.getName();
			log.info("    WILL BE ADDED: '" + f.getAbsolutePath() + "' -> '" + nameInBpk + "'");
			filesToArchive.add(new FileToArchive(nameInBpk, f));
		}
		return filesToArchive;
	}

	private List<File> collectFilesForAdding() {
		List<File> files = new ArrayList<File>();
		if (file != null) {
			files.add(file);
		} else {
			FileFilter filter = new WildcardFileFilter(wildcard);
			files.addAll(Arrays.asList(wildcardWorkingDirectory.listFiles(filter)));
		}
		return files;
	}

	/**
	 * Generate final folder name, to which will be situated input files in
	 * archive.
	 * 
	 * @return
	 */
	private String generateFinalFolderName() {
		String folderName;
		if (folder == null || folder.trim().isEmpty()) {
			folderName = "";
		} else {
			folderName = (folder.endsWith("/") ? folder : folder + "/");
		}
		return folderName;
	}

	/**
	 * Check parameters of item. Log error into maven log, if some parameter is
	 * incorrect. (When ERROR is logge into maven log, build failed)
	 * 
	 * @param log
	 */
	private void checkParameters(Log log) {
		/*
		 * it is possibble to define file or wildcard, not both
		 */
		if (file == null && wildcard == null) {
			log.error("You must specify file or wildcard.");
		} else if (file != null && wildcard != null) {
			log.error("You should specify file or wildcard, you can't specify both.");
		} else if (wildcard != null && wildcardWorkingDirectory == null) {
			log.error("WildcardWorkingDirectory must be specified when wildcard is specified.");
		}
		/*
		 * Files must exists
		 */
		else if (file != null && !file.exists()) {
			log.error("Specified file '" + file.getAbsolutePath() + "' not found.");
		} else if (wildcardWorkingDirectory != null && !wildcardWorkingDirectory.exists()) {
			log.error("Specified wildcardworking directory '" + wildcardWorkingDirectory.getAbsolutePath() + "' not found.");
		}
	}

}