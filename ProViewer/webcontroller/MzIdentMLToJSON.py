#!/usr/bin/env python

"""
Purpose     : Convert mzIdentML to JSON from ProViewer visualisation plugin
Description : This script invokes mzIdentMLExtractor.jar java library to read mzIdentML file and
			  generate four json files which inclides protein, peptide,  PSM and metadata.
Date   		: Sep 2, 2016
@author     : Suresh Hewapathirana

"""
# python 2.7
import ConfigParser
import os
import subprocess

class MzIdentMLToJSON():

	def extract(self, inputfile, datasetId, root):

		print "ProViewer INFO: Root folder of the Galaxy: " + root

		# load settings from configuration settings file
		config = ConfigParser.ConfigParser()
		print config.read(root + "/config/mzidentml_setttings.ini")

		outputfile 		= config.get('MzIdentML', 'output_dir')
		javalib 		= config.get('MzIdentML', 'javalib')
		multithreading 	= config.get('MzIdentML', 'multithreading')
		maxMemory		= config.get('MzIdentML', 'maxMemory')
		tempFile 		= outputfile + datasetId + "_protein.json"

		# print parameters to console for debugging purpose for admin
		print "ProViewer INFO:java -jar " + javalib + " " + inputfile + " " + outputfile + " " + datasetId + " " + multithreading

		try:
			# if file not already exists
			if os.path.isfile(tempFile) == False:
				# execute ProExtractor java library as a seperate process (Command-line)
				return subprocess.call(['java', '-jar', maxMemory, javalib, inputfile, outputfile, datasetId, multithreading])
			else:
				print "ProViewer INFO: Data loaded from the cache!"
		except Exception as err:
			print("ProViewer ERROR: Java library execution error\n: {0}".format(err))