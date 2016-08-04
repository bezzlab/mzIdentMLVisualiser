#!/usr/bin/env python

from sys import argv
import os
import shutil
from distutils.dir_util import copy_tree
import time

def main():
	os.system('clear')

	print "\n"
	print " ################################################"
	print "     Welcome to mzIdentML Viewer Admin Tool!     "
	print " ################################################\n"

	print "What do you want to do:"
	print "\t1. Install mzIdentML Viewer"
	print "\t2. Remove temporary files"
	print "\t3. Re-run galaxy tool with existing data"

	option = raw_input("\nPlease type option number:")
	if (option == '1'):
		install()
	elif (option == '2'):
		remove_temp()
	elif (option == '3'):
		rerun()

def install():
	'''
	This method will copy installation files
	into  the galaxy server instance
	'''
	
	# get the current working directory. By default it is the galaxy/config folder
	current_dir = os.getcwd()

	os.system('clear')
	print "\n"
	print " ==============================================================================================="
	print "                                  Install mzIdentML Viewer                                      "
	print " ===============================================================================================\n"

	print "Please answer to following questions carefully."
	print "You can refer examples given below as a guide.\n\n"

	print "-------------------------------------------------------------------------------------------------"
	print "STEP 1 : Visualisation Plugin Directory 	:\n/home/galaxy/Downloads/galaxy/config/plugins/visualizations/\n"
	print "STEP 2 : Web API Directory 				:\n/home/galaxy/Downloads/galaxy/lib/galaxy/webapps/galaxy/api/\n"
	print "STEP 3 : Tool directory(wrapper) save location:\n/home/galaxy/Downloads/galaxy/tools/mzIdentMLToJSON/\n"
	print "STEP 4 : Output dir where temporay files get saved:\n/home/galaxy/Downloads/galaxy/config/plugins/visualizations/protviewer/static/data/\n"
	print "STEP 4 : Output dir relative path reference to config folder:\n/plugins/visualizations/protviewer/static/data/\n"
	print "STEP 4 : Galaxy instance root directory:\n/home/galaxy/Downloads/galaxy\n"
	print "-------------------------------------------------------------------------------------------------\n"

	# STEP 1 - Copy Visualisation Plugin
	printTitle("STEP 1 - Copy Visualisation Plugin")
	# print Visualizations plugin directory: where to look for individual visualization plugins.
	# Absolute path should be given
	vis_plugin_dir		= raw_input("Visualisation Plugin Directory :")
	to_dir 		= os.path.join(vis_plugin_dir, 'protviewer')
	from_dir    = os.path.join(current_dir, 'plugin/protviewer')
	copy(from_dir, to_dir)

	# STEP 2 - Copy Web API Files
	printTitle("STEP 2 - Copy Web API Files")
	web_api_dir			= raw_input("Web API Directory :")
	from_dir    = os.path.join(current_dir, 'plugin/webcontroller')
	copy(from_dir, web_api_dir)

	#STEP 3 - Copy Galaxy Tool
	printTitle("STEP 3 - Copy Galaxy Tool")
	tool_dir 	= raw_input("Tool directory(wrapper) save location:")
	from_dir    = os.path.join(current_dir, 'tool/mzIdentMLToJSON')
	copy(from_dir, tool_dir)

	# assume that java library is in the same folder as tool(wrapper)
	javalib_dir = tool_dir

	# STEP 4 - add settings to configuration file
	printTitle("STEP 4 - Add settings to galaxy.ini config file")
	output_dir 			= raw_input("Output dir where temporay files get saved:")
	rel_output_dir 		= raw_input("Output dir relative path reference to config folder:")
	galaxy_root_location= raw_input("Galaxy instance root directory:")

	error_report_sent_to = "admin@somewhere.com"
	multithreading = "true"
	galaxy_ini = os.path.join(galaxy_root_location, 'config/galaxy.ini')
	settings = "\n\n# ---- mzIdentML Viewer Config ------\n\n[MzIdentML]" + "\noutput_dir = " + output_dir + "\nrel_output_dir = " + rel_output_dir + "\njavalib = " + javalib_dir + "mzIdentMLExtractor.jar" + "\ntool_path = " + tool_dir + "\nmultithreading = " + multithreading + "\nerror_report_sent_to = " + error_report_sent_to
	print settings

	confirmation = raw_input("\nSettings are accurate?(Y/N):")

	if (confirmation == 'Y' or confirmation == 'y'):
		setting_location = galaxy_root_location + "/config/mzidentml_setttings.ini"
		print "Saving at : " + setting_location
		target = open(setting_location, 'w+')
		target.write(settings)
		target.close()
		print "Success! Settings were added to mzidentml_setttings.ini settings file!\n\n"

	print "------------- Installation Completed -------------"

def copy(from_dir, to_dir):
	'''
	Copy file or folder to another folder
	'''
	if os.path.isdir(from_dir) or os.path.isfile(from_dir):
		print "File copying started..."
		time.sleep( 2 )
		print "\nCopying "+ from_dir + " \n--> " + to_dir + "\n"
		try:
			copy_tree(from_dir, to_dir)
			print "copied to " + to_dir
		except Exception as err:
			print("File Copying Error\n: {0}".format(err))
			exit()
		# print "Files/folders : "%os.listdir(to_dir)
		print "\n> Success! Plugin files were copied/replaced!\n\n"
	else:
		print("\033[1;33;40m Sorry, folder path is invalid!\n")
		exit()

def printTitle(title):
	print "\n" + title
	print "---------------------------------------------------\n"


def remove_temp():
	print "\033[1;33;40m\n*** Sorry, Under Constructions ***\n"

def rerun():
	print "\033[1;33;40m\n*** Sorry, Under Constructions ***\n"

if __name__ == "__main__":
    main()
