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
		updateCoverage(false);
		updateCoverage(true);
		return coverageByFile;
	}

	/**
	 * Show coverage line for one file
	 */
	String coverageLine(String file) {
		Boolean[] lines = coverageByFile.get(file);
		for (int i = 0; i < lines.length; i++) {
			Boolean l = lines[i];
			if (l != null && !l) {
				System.out.println("\t" + i);
			}
		}
		return "";
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

	public double percent() {
		//	int countCovered=
		return 0.0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		coverageByFile();

		// Sort by file name
		List<String> files = new ArrayList<>();
		files.addAll(coverageByFile.keySet());
		Collections.sort(files);

		// Show coverage
		for (String file : files) {
			Gpr.debug("COV: " + file + "\t" + coverageLine(file));
		}
		return sb.toString();
	}

	/**
	 * Update coverage by by statistics
	 * @param coverageByFile
	 * @param updateNoCoverage : Update whether there was coverage or there was no coverage
	 */
	private void updateCoverage(boolean updateNoCoverage) {
		for (BdsNode bdsNode : bdsNodes) {
			int covcount = coverageCounter.getOrDefault(bdsNode.getId(), 0);
			String fileName = bdsNode.getFileNameCanonical();
			int lineNum = bdsNode.getLineNum();

			Gpr.debug("COVERAGE COUNT:\tnodeId:" + bdsNode.getId() + "\tcount: " + covcount + "\t" + bdsNode.getFileNameCanonical() + ":" + bdsNode.getLineNum());

			// No coverage? Add to array
			Boolean[] lineCoverage = coverageByFile.get(fileName);
			//			if (updateNoCoverage) {
			//				// We want to overwrite the entries with 'false' if some other
			//				// bdsNode (in the same line) did not have any coverage. E.g.:
			//				//
			//				//     if( ok ) { println 'OK' } else { println 'BAD' }
			//				//
			//				// The if/else statements are in one line, so when only one
			//				// of the branches are covered, we might have the false impression
			//				// that both of them are covered, just because we didn't mark
			//				// the lines with no coverage
			//				if (covcount == 0) lineCoverage[lineNum] = false;
			//			} else lineCoverage[lineNum] = (covcount > 0);
			lineCoverage[lineNum] = (covcount > 0);
		}
	}

}
