package org.bds.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bds.lang.BdsNode;
import org.bds.util.Gpr;
import org.bds.vm.BdsVm;

/**
 * Keep coverage statistics when running bds tests (i.e. 'bds -t -coverage ...')
 *
 * @author pcingola
 *
 */
public class Coverage {

	Set<BdsNode> bdsNodes;
	Map<Integer, Integer> coverageCounter;
	Map<String, Boolean[]> coverageByFile;

	public Coverage() {
		bdsNodes = new HashSet<>();
		coverageCounter = new HashMap<>();
	}

	/**
	 * Add coverage statistics from a VM (that already finished running)
	 */
	public void add(BdsVm vm) {
		Gpr.debug(vm.toAsm());
		// Make sure all nodes are added
		bdsNodes.addAll(vm.findNodes());

		// Update node coverage counters
		Map<Integer, Integer> vmcov = vm.getCoverageCounter();
		for (Integer nodeId : vmcov.keySet()) {
			int count = coverageCounter.getOrDefault(nodeId, 0);
			coverageCounter.put(nodeId, count + vmcov.get(nodeId));
		}
	}

	/**
	 * Calculate coverage by file and line
	 */
	private Map<String, Boolean[]> coverageByFile() {
		if (coverageByFile != null) return coverageByFile;
		coverageByFile = createCoverageByFile();

		for (BdsNode bdsNode : bdsNodes) {
			int covcount = coverageCounter.getOrDefault(bdsNode.getId(), 0);
			String fileName = bdsNode.getFileNameCanonical();
			int lineNum = bdsNode.getLineNum();

			// No coverage? Add to array
			Boolean[] lineCoverage = coverageByFile.get(fileName);
			lineCoverage[lineNum] = (covcount > 0);
		}

		return coverageByFile;
	}

	/**
	 * Show coverage line for one file
	 */
	String coverageLine(String file) {
		Boolean[] lines = coverageByFile.get(file);

		// Count line covered
		int countLines = 0, countCovered = 0;
		for (int i = 0; i < lines.length; i++) {
			Boolean l = lines[i];
			if (l != null) {
				countLines++;
				if (l) countCovered++;
			}
		}

		// Lines not covered (intevals)
		int start = -1, end = -1;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			Boolean l = lines[i];
			if (l != null) {
				if (l) {
					if (end >= 0) sb.append(showInterval(start, end));
					start = end = -1;
				} else {
					if (start < 0) start = i;
					end = i;
				}
			}
		}
		if (end >= 0) sb.append(showInterval(start, end));

		// Coverage percentage
		double perc = (100.0 * countCovered) / countLines;

		// Limit file name length
		if (file.length() > 50) file = "..." + file.substring(file.length() - 50 + 3);

		// One line statistics
		return String.format("| %50.50s | %7d / %7d | %5.2f%% | %s", file, countCovered, countLines, perc, sb);
	}

	/**
	 * Create a coverage by file map
	 */
	private Map<String, Boolean[]> createCoverageByFile() {
		Map<String, Boolean[]> coverageByFile = new HashMap<>();
		Map<String, Integer> maxLineNums = new HashMap<>();

		// Get max line number for each file
		for (BdsNode bdsNode : bdsNodes) {
			String fileName = bdsNode.getFileNameCanonical();
			int lineNum = bdsNode.getLineNum();
			int maxLineNum = Math.max(lineNum, maxLineNums.getOrDefault(fileName, 0));
			maxLineNums.put(fileName, maxLineNum);
		}

		// Create boolean arrays
		for (String file : maxLineNums.keySet()) {
			int maxLineNum = maxLineNums.get(file);
			Boolean[] linesCoverage = new Boolean[maxLineNum + 1];
			coverageByFile.put(file, linesCoverage);
		}

		return coverageByFile;
	}

	/**
	 * Total percent coverage
	 */
	public double percent() {
		int countLines = 0, countCovered = 0;

		for (Boolean[] lines : coverageByFile.values()) {
			for (int i = 0; i < lines.length; i++) {
				Boolean l = lines[i];
				if (l != null) {
					countLines++;
					if (l) countCovered++;
				}
			}
		}

		double perc = (100.0 * countCovered) / countLines;
		return perc;
	}

	String showInterval(int start, int end) {
		if (start < 0 || end < 0) return "";
		if (start == end) return start + " ";
		return start + "-" + end + " ";
	}

	String title() {
		return String.format("| %50.50s | %7s / %7s |  %5s | %s", "File name", "Covered", "Total", "%", "Not covered intervals");
	}

	@Override
	public String toString() {
		coverageByFile();

		// Sort by file name
		List<String> files = new ArrayList<>();
		files.addAll(coverageByFile.keySet());
		Collections.sort(files);

		// Show coverage
		StringBuilder sb = new StringBuilder();
		sb.append(title() + "\n");
		for (String file : files) {
			sb.append(coverageLine(file) + "\n");
		}

		return sb.toString();
	}
}
