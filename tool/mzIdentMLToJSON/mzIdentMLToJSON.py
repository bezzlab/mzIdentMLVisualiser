#!/usr/bin/env python

"""
Convert mzIdentML to JSON

This script invokes mzidParser.jar java library to read mzIdentML file and generate four json files
which inclides protein, peptide,  PSM and metadata.

usage: ProViewer.py  --filename="$input" --datasetid=$__app__.security.encode_id($input.id) --root=$__root_dir__
"""
import optparse
import os
import subprocess
# python 2.7
import ConfigParser

def __main__():
	#parse command-line arguments
	parser = optparse.OptionParser()
	parser.add_option( '-F', '--filename', dest='filename', help='mzIdentML filename' )
	parser.add_option( '-D', '--datasetid', dest='datasetid', help='datasetid' )
	parser.add_option( '-R', '--root', dest='root', help='root directory of the galaxy instance' )
   	(options, args) = parser.parse_args()
	inputfile 		= options.filename
	datasetId 		= options.datasetid
	root 			= options.root
	print "MzIdentML Viewer INFO:Root folder of the Galaxy: " + root

	config 			= ConfigParser.ConfigParser()
	print config.read(root + '/config/mzidentml_setttings.ini')
	outputfile 		= config.get('MzIdentML', 'output_dir')
	javalib 		= config.get('MzIdentML', 'javalib')
	multithreading 	= config.get('MzIdentML', 'multithreading')
	tempFile 		= outputfile + datasetId + "_protein.json"
	print "MzIdentML Viewer INFO:java -jar " + javalib + " " + inputfile + " " + outputfile + " " + datasetId + " " + multithreading

	try:
		# if file not already exists
		if os.path.isfile(tempFile) == False:
			# execute mzIdentMLExtractor java library
			return subprocess.call(['java', '-jar',javalib, inputfile, outputfile, datasetId, multithreading])
		else:
			print "MzIdentML Viewer INFO: Data loaded from the cache!"
	except Exception as err:
		print("MzIdentML Viewer ERROR: Java library execution error\n: {0}".format(err))

if __name__ == "__main__":
    __main__()