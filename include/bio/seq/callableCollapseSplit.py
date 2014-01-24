#!/usr/bin/python

#-------------------------------------------------------------------------------
# Read a BED file and merge contiguous callable regions
#
#
#																Pablo Cingolani
#-------------------------------------------------------------------------------

import sys

maxDistance = 1000		# Maximum distance to merge callable regions

sizes = {}				# Size of each line
outLines = []			# Output lines

#-------------------------------------------------------------------------------
# Print a BED line
#-------------------------------------------------------------------------------
def addBed(prevChr, start, end):
	if start > 0:
		# Create output line, store line and size
		size = end - start
		outLine = prevChr + '\t' + str(start) + '\t' + str(end)
		outLines.append( outLine )
		sizes[outLine] = size
		return size
	return 0

#-------------------------------------------------------------------------------
# Red BED file and collapse contuguous regions
#-------------------------------------------------------------------------------
def readBedCollapse(maxDistance):
	# Initialize
	totalBases = 0		# Count total number of callable bases
	prevChr, prevStart, prevEnd = '', -1, -1
	callStart = -1

	# Read BED from stdin
	for line in sys.stdin:
		# Parse bed line
		line = line.rstrip()
		bed = line.split('\t')
		chr, start, end = bed[0], int(bed[1]), int(bed[2])

		# Calculate size & distance from previous entry
		size = end - start
		dist = start - prevEnd

		# Should we split here?
		if chr != prevChr or dist > maxDistance:
			totalBases += addBed(prevChr, callStart, prevEnd)
			callStart = start

		prevChr, prevStart, prevEnd = chr, start, end

	# Add last entry
	if callStart != start: 
		totalBases += addBed(prevChr, callStart, prevEnd)

	return totalBases

#-------------------------------------------------------------------------------
# Write bed files having roughly avgBases in each file
#-------------------------------------------------------------------------------
def writeBedFiles( outFiles, avgBases ):
	# Write output files
	bases = 0
	outFileIdx = 0

	print 'Writing files:\n\t', outFiles[outFileIdx]
	f = open( outFiles[outFileIdx], 'w' )

	for outLine in outLines:
		bases += sizes[ outLine ]
		f.write(outLine + '\n')

		# Should we open the next file
		if bases > avgBases and outFileIdx < numberOfSplits-1 :
			f.close()
			outFileIdx += 1
			print '\t', outFiles[outFileIdx]
			f = open( outFiles[outFileIdx], 'w' )
			bases = 0

	f.close()

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

# Pasrse command line arguments
# Command line arguments: Names of output (split) files
if len(sys.argv) <= 1:
	print "Usage: cat file.bed | {} file_1.bed file_2.bed ... file_N.bed".format(sys.argv[0])
	sys.exit(1)

outFiles = sys.argv[1:]
numberOfSplits = len(outFiles)

# Read BED from stdin, collapse contiguour regions
totalBases = readBedCollapse(maxDistance)

# OK, write output files. Try to have a similar number of bases in each file
avgBases = totalBases / numberOfSplits + 1
avgLines = len(outLines) / numberOfSplits + 1
print "Total bases         :", totalBases
print 'Split into          :', numberOfSplits
print 'Avg bases per split :', avgBases
print 'Avg lines per split :', avgLines

writeBedFiles( outFiles, avgBases )

