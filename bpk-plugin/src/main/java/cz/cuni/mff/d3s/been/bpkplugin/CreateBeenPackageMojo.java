package cz.cuni.mff.d3s.been.bpkplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/*
 * Mojo plugin development is comment-annotation driven. 
 * For simple development documentation see http://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 */

/**
 * This plugin should be used for creating BPK Ebeen packages. BPK files are
 * classical ZIP files with simple structure. Files which should be included in
 * BPK package must specified as plugin parameters. Files "config.xml" and
 * "metadata.xml" will be generated automatically. <br/>
 * <br/>
 * Example of plugin definition in pom.xml:
 * 
 * <pre>
 * &lt;plugin&gt;
 *   &lt;groupId&gt;cz.cuni.mff.d3s.been&lt;/groupId&gt;
 *   &lt;artifactId&gt;bpk-plugin&lt;/artifactId&gt;
 *   &lt;version&gt;1.0.1&lt;/version&gt;
 *   &lt;executions&gt;
 *     &lt;execution&gt;
 *       &lt;goals&gt;
 *         &lt;goal&gt;buildpackage&lt;/goal&gt;
 *       &lt;/goals&gt;
 *     &lt;/execution&gt;
 *   &lt;/executions&gt;
 *   &lt;configuration&gt;
 *     &lt;humanName&gt;Host Manager&lt;/humanName&gt;
 *     &lt;packageJarFile&gt;${project.build.directory}/${project.build.finalName}.one-jar.jar&lt;/packageJarFile&gt;
 *     &lt;mainClassName&gt;cz.cuni.mff.been.hostmanager.HostManagerService&lt;/mainClassName&gt;
 *     &lt;filesToArchive&gt;
 *       &lt;fileItem&gt;
 *         &lt;wildcardWorkingDirectory&gt;${basedir}/src/main/resources/files/&lt;/wildcardWorkingDirectory&gt;
 *         &lt;wildcard&gt;*&lt;/wildcard&gt;
 *       &lt;/fileItem&gt;
 *     &lt;/filesToArchive&gt;
 *   &lt;/configuration&gt;
 * &lt;/plugin&gt;
 * </pre>
 * 
 * @author donarus
 * 
 * @goal buildpackage
 * @phase package
 */
public class CreateBeenPackageMojo extends AbstractMojo {

	// log used in Maven output
	private final Log log = getLog();

	/**
	 * Files specified here will be added to bpk archive. All files will be
	 * placed in "files" folder in root folder of generated bpk package.
	 * 
	 * @parameter
	 * 
	 * @required
	 */
	private List<FileItem> filesToArchive;

	/**
	 * Bpk file will be generated into this directory.
	 * <b>${project.build.directory}</b> by default.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * 
	 * @required
	 */
	private File buildDirectory;

	/**
	 * Final name of bpk file (without bpk extension).
	 * <b>${project.artifactId}-${project.version}</b> by default.
	 * 
	 * @parameter expression="${project.artifactId}-${project.version}"
	 * 
	 * @required
	 */
	private String finalName;

	/**
	 * Jar file with classes for this package. (Will be used in generated
	 * config.xml)
	 * 
	 * @parameter
	 * 
	 * @required
	 */
	private File packageJarFile;

	/**
	 * Fully qualified class name in jar file for this bpk package. (Will be
	 * used in generated config.xml)
	 * 
	 * @parameter
	 * 
	 * @required
	 */
	private String mainClassName;

	/**
	 * Name of the bpk package. <b>${project.build.finalName}</b> by default.
	 * (Will be used in generated metadata.xml)
	 * 
	 * @parameter expression="${project.build.finalName}"
	 * 
	 * @required
	 */
	private String name;

	/**
	 * Version of bpk package. <b>${project.version}</b> by default. (Will be
	 * used in generated metadata.xml)
	 * 
	 * @parameter expression="${project.version}"
	 * 
	 * @required
	 */
	private String version;

	/**
	 * Type of bpk package. <b>task</b> by default. (Will be used in generated
	 * metadata.xml)
	 * 
	 * @parameter default-value="task"
	 * 
	 * @required
	 */
	private String type;

	/**
	 * Human readable name of this bpk package. (Will be used in generated
	 * metadata.xml)
	 * 
	 * @parameter
	 * 
	 * @required
	 */
	private String humanName;

	/**
	 * This is the plugin main method. All generation logic starts here.
	 */
	public void execute() throws MojoExecutionException {
		logStart();

		File bpkFile = createEmptyBpkFile(); // output file

		try {
			List<FileToArchive> files = new ArrayList<FileToArchive>();
			// generace
			files.add(createFileToArchiveFromPackageJar());
			files.add(generateConfigXmlFile());
			files.add(generateMetadataXmlFile());

			for (FileItem fitem : filesToArchive) {
				files.addAll(fitem.getFilesToArchive(log));
			}
			new ZipUtil().createZip(files, bpkFile);
			log.info("BPK exported to '" + bpkFile.getAbsolutePath() + "'");
		} catch (IOException e) {
			log.error("Cannot create BPK archive", e);
		}

		logEnd();
	}

	private FileToArchive generateConfigXmlFile() {
		String nameInBpk = "config.xml";
		log.info("    WILL BE GENERATED: with:mainClass='" + mainClassName
				+ "' -> '" + nameInBpk + "'");
		try {
			File config = File.createTempFile("tmp_generated_config", ".xml");
			String content = String.format("<?xml version=\"1.0\"?>\n"
					+ "<packageConfiguration>\n"
					+ "	<java classPath=\".:%s\" mainClass=\"%s\" />\n"
					+ "</packageConfiguration>\n", packageJarFile.getName(),
					mainClassName);
			FileUtils.write(config, content);
			return new FileToArchive(nameInBpk, config);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	private FileToArchive generateMetadataXmlFile() {
		String nameInBpk = "metadata.xml";
		log.info("    WILL BE GENERATED: with:name='" + name + "', version='"
				+ version + "', type='" + type + "', humanName='" + humanName
				+ "' -> '" + nameInBpk + "'");
		try {
			File config = File.createTempFile("tmp_generated_config", ".xml");
			String content = String.format("<?xml version=\"1.0\"?>\n"
					+ "<package>\n" + "  <name>%s</name>\n"
					+ "  <version>%s</version>\n" + "  <type>%s</type>\n"
					+ "  <humanName>%s</humanName>\n" + "</package>\n", name,
					version, type, humanName);
			FileUtils.write(config, content);
			return new FileToArchive(nameInBpk, config);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	private FileToArchive createFileToArchiveFromPackageJar() {
		String nameInBpk = "files/" + packageJarFile.getName();
		log.info("    WILL BE ADDED: '" + packageJarFile.getAbsolutePath()
				+ "' -> '" + nameInBpk + "'");
		return new FileToArchive(nameInBpk, packageJarFile);
	}

	private File createEmptyBpkFile() {
		return new File(buildDirectory, finalName + ".bpk");
	}

	private void logStart() {
		log.info("=====================================");
		log.info("==  CREATING BEEN PACKAGE STARTED  ==");
		log.info("=====================================");
	}

	private void logEnd() {
		log.info("===================================");
		log.info("==  CREATING BEEN PACKAGE ENDED  ==");
		log.info("===================================");
	}

}