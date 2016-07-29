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
	current_dir = os.getcwd()

	os.system('clear')
	print "\n"
	print " ==============================================================================================="
	print "                                  Install mzIdentML Viewer                                      "
	print " ===============================================================================================\n"

	print "Please answer to following questions carefully."
	print "You can refer examples given below as a guidance.\n\n"

	print "-------------------------------------------------------------------------------------------------"
	print "Question 1 : /Users/name/Downloads/galaxy/config/plugins/visualizations/protviewer/static/data/"
	print "Question 2 : /plugins/visualizations/protviewer/static/data/"
	print "Question 3 : /Users/name/Downloads/galaxy/tools/mzIdentMLToJSON/"
	print "Question 4 : /Users/name/Downloads/galaxy/tools/"
	print "Question 5 : /Users/name/Downloads/galaxy"
	print "Question 6 : /Users/name/Downloads/galaxy/plugins/visualizations/"
	print "Question 7 : /Users/name/Downloads/galaxy/lib/galaxy/webapps/galaxy/api/"
	print "-------------------------------------------------------------------------------------------------\n"

	output_dir 			= raw_input("1. Output dir where temporay files get saved \t\t:")
	rel_output_dir 		= raw_input("2. Output dir relative path reference to config folder\t:")
	javalib_dir        	= raw_input("3. mzIdentMLToJSON java library save directory\t\t:")
	tool_dir 			= raw_input("4. Tool directory(wrapper) save location \t\t:")
	galaxy_root_location= raw_input("5. Galaxy instance root directory\t\t\t:")
	vis_plugin_dir		= raw_input("6. Visualisation Plugin Directory\t\t\t:")
	web_api_dir			= raw_input("7. Web API Directory\t\t\t\t\t:")

	error_report_sent_to = "admin@somewhere.com"
	multithreading = "true"

	# STEP 1 - add settings to configuration file
	galaxy_ini = os.path.join(galaxy_root_location, 'config/galaxy.ini')
	settings = "\n\n# ---- mzIdentML Viewer Config ------\n\n[MzIdentML]" + "\noutput_dir = " + output_dir + "\nrel_output_dir = " + rel_output_dir + "\njavalib = " + javalib_dir + "mzIdentMLExtractor.jar" + "\ntool_path = " + tool_dir + "\nmultithreading = " + multithreading + "\nerror_report_sent_to = " + error_report_sent_to
	print settings

	confirmation = raw_input("\nDo you want to add settings to galaxy.ini configuration file?(Y/N):")

	if (confirmation == 'Y' or confirmation == 'y'):
		if os.path.exists(galaxy_ini):
			open(galaxy_ini, 'a').write(settings)
			print "> Success! Settings were added to galaxy.ini config file!\n\n"
		else:
			print("\033[1;33;40m Sorry, Galaxy.ini path is invalid!\n")
			exit()

	print "File copying started..."
	time.sleep( 2 )
	# STEP 2 - Copy Visualisation Plugin
	from_dir    = os.path.join(current_dir, 'plugin/protviewer')
	copy("STEP 2 - Copy Visualisation Plugin", from_dir, vis_plugin_dir)

	# STEP 3 - Copy Web API Files
	from_dir    = os.path.join(current_dir, 'plugin/webcontroller')
	copy("STEP 3 - Copy Web API Files", from_dir, web_api_dir)

	#STEP 4 - Copy Galaxy Tool
	from_dir    = os.path.join(current_dir, 'tool/mzIdentMLToJSON')
	copy("STEP 4 - Copy Galaxy Tool", from_dir, tool_dir)
	os.remove(tool_dir + "tool/mzIdentMLToJSON/mzIdentMLExtractor.jar")

	# copy java library to the javalib_dir user specified in step 1
	from_dir    = os.path.join(current_dir, 'tool/mzIdentMLToJSON/mzIdentMLExtractor.jar')
	copy("STEP 4 - Copy Galaxy Tool", from_dir, javalib_dir)
	print "Successfully installed!!!"


def copy(title, from_dir, to_dir):
	'''
	Copy file or folder to another folder
	'''
	print "\n" + title
	print "---------------------------------------------------\n"

	if os.path.isdir(to_dir):
		print "\nCopying "+ from_dir + " --> " + to_dir + "...\n"
		time.sleep( 2 )
		try:
			copy_tree(from_dir, to_dir)
		except Exception as err:
			print("File Copying Error\n: {0}".format(err))
			exit()
		print "Files/folders : "%os.listdir(to_dir)
		print "\n> Success! Plugin files were copied/replaced!\n\n"
	else:
		print("\033[1;33;40m Sorry, folder path is invalid!\n")
		exit()

def remove_temp():
	print "\033[1;33;40m\n*** Sorry, Under Constructions ***\n"

def rerun():
	print "\033[1;33;40m\n*** Sorry, Under Constructions ***\n"

if __name__ == "__main__":
    main()
