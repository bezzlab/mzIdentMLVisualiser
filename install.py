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

	# STEP 1 - Get galaxy root folder
	printTitle("STEP 1 - Set your galaxy root directory")
	print "\nThis is the absolute file path for your galaxy main folder\n"
	print "\nExample : /home/galaxy/Downloads/galaxy\n"
	galaxy_root_location= raw_input("Galaxy instance root directory:")

	# STEP 2 - Copy Visualisation Plugin
	printTitle("STEP 2 - Copy Visualisation Plugin")
    print "\nVisualizations plugin directory: absolute folder path where to look for individual visualization plugins.\n"
	suggested_path = galaxy_root_location + "/config/plugins/visualizations/"
	print "Defalut location :" + suggested_path
	confirm = raw_input("Save to default location(Y/N):")
	if (confirmation == 'Y' or confirmation == 'y'):
		vis_plugin_dir = suggested_path
	else:
		vis_plugin_dir = raw_input("Customized Visualisation Plugin Directory :")
	to_dir 		= os.path.join(vis_plugin_dir, 'protviewer')
	from_dir    = os.path.join(current_dir, 'plugin/protviewer')
	copy(from_dir, to_dir)

	# STEP 3 - Copy Web API Files
	printTitle("STEP 3 - Copy Web API Files")
	print "\nWeb API directory: absolute folder path where you have galaxy webapp api files\n"
	suggested_path = galaxy_root_location + "/lib/galaxy/webapps/galaxy/api/"
	print "Defalut location :" + suggested_path
	confirm = raw_input("Save to default location(Y/N):")
	if (confirmation == 'Y' or confirmation == 'y'):
		web_api_dir = suggested_path
	else:
		web_api_dir = raw_input("Customized Web API Directory :")
	from_dir    = os.path.join(current_dir, 'plugin/webcontroller')
	copy(from_dir, web_api_dir)

	#STEP 4 - Copy Galaxy Tool
	printTitle("STEP 4 - Copy Galaxy Tool")
	print "\nGalaxy tool folder: absolute folder path where individual tool saved in galaxy\n"
	suggested_path = galaxy_root_location + "/tools/mzIdentMLToJSON/"
	print "Defalut location :" + suggested_path
	confirm = raw_input("Save to default location(Y/N):")
	if (confirmation == 'Y' or confirmation == 'y'):
		tool_dir = suggested_path
	else:
		tool_dir 	= raw_input("Customized Tool directory(wrapper):")
	from_dir    = os.path.join(current_dir, 'tool/mzIdentMLToJSON')
	copy(from_dir, tool_dir)

	print "\n mzIdentMLExtractor.jar java library saves in the same folder as other tool files\n"
	confirm = raw_input("Do you want to use default location(Y/N):")
	if (confirmation == 'Y' or confirmation == 'y'):
		javalib_dir = tool_dir
	else:
		javalib_dir = raw_input("what is the directory you have your mzIdentMLExtractor.jar java library (absolute path):")

	# STEP 5 - add settings to configuration file
	printTitle("STEP 5 - Set output directory")
	print "\nOutput directory: folder where temporary JSON files generated from java library get saved\n"
	suggested_path = galaxy_root_location + "/config/plugins/visualizations/protviewer/static/data/"
	confirm = raw_input("is your Output Directory \n "+ suggested_path +"(Y/N):")
	if (confirmation == 'Y' or confirmation == 'y'):
		output_dir = suggested_path
		rel_output_dir = "/plugins/visualizations/protviewer/static/data/"
	else:
		output_dir 			= raw_input("Output dir where temporay files get saved:")
		rel_output_dir 		= raw_input("Output dir relative path reference to config folder:")

	error_report_sent_to = "admin@somewhere.com"
	multithreading = "true"

	# STEP 6 - add settings to configuration file
	printTitle("STEP 6 - create a setting file")
	print "\nA setting file called mzidentml_setttings.ini will be created in \n"+ galaxy_root_location + "/config/ location\n"
	print "These settings are only specific to mzIdentML visualization plugin\n"

	galaxy_ini = os.path.join(galaxy_root_location, 'config/galaxy.ini')
	settings = "\n\n# ---- mzIdentML Viewer Config ------\n\n[MzIdentML]" + "\noutput_dir = " + output_dir + "\nrel_output_dir = " + rel_output_dir + "\njavalib = " + javalib_dir + "mzIdentMLExtractor.jar" + "\n + "\nmultithreading = " + multithreading + "\nerror_report_sent_to = " + error_report_sent_to
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
