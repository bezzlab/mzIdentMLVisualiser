#!/usr/bin/env python

"""
Convert mzIdentML to JSON

This script invokes mzidParser.jar java library to read mzIdentML file and generate four json files
which inclides protein, peptide,  PSM and metadata.

usage: ProViewer.py  --filename="$input" --datasetid=$__app__.security.encode_id($input.id)
"""
import optparse, os, subprocess

def __main__():

	# parse command-line arguments
	parser = optparse.OptionParser()
	parser.add_option( '-F', '--filename', dest='filename', help='mzIdentML filename' )
	parser.add_option( '-D', '--datasetid', dest='datasetid', help='datasetid' )
   	(options, args) = parser.parse_args()
	filename = options.filename
	datasetId = options.datasetid

	root = "/Users/sureshhewapathirana/Documents/Projects/ResearchProject/mzIdentMLViewer/galaxy/"

	fname = root+"config/plugins/visualizations/protviewer/static/data/"+datasetId+"_protein.json"
	libraryLocation = root+"tools/mzIdentMLToJSON/mzIdentMLExtractor.jar"

	try:
		# if file not already exists
		if os.path.isfile(fname) == False:
			return subprocess.call(['java', '-jar', libraryLocation, filename, datasetId])
		else:
			print "Info: Data loaded from the cache!";
	except Exception as err:
		print("ERROR: Java library execution error\n: {0}".format(err))

if __name__ == "__main__":
    __main__()