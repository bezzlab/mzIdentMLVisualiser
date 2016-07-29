#!/usr/bin/env python

"""
Convert mzIdentML to JSON from mzIdentML Viewer visualisation plugin

This script invokes mzidParser.jar java library to read mzIdentML file and generate four json files
which inclides protein, peptide,  PSM and metadata.

"""
# python 2.7
import ConfigParser
import os
import subprocess

class MzIdentMLToJSON():

	def extract(self, inputfile, datasetId, root):

		print "MzIdentML Viewer INFO: Root folder of the Galaxy: " + root
		config = ConfigParser.ConfigParser()
		print config.read(root + "/config/galaxy.ini")

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