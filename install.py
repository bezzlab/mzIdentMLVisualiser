#!/usr/bin/env python
# coding: utf-8
# -----------------------------------------------------------------------------------------
# Filename      : 	install.py
# Authors       : 	Suresh Hewapathirana
# Date          : 	2016-09-02
# Description   : 	This script is used to install and uninstall ProViewer plugin to the server.
#					This will copy installation files to relevant location of the server,
#					takes backups where nessasary and creates a setting file. It is recommended
#					to use quick installation, except for GIO Server installation. Furthermore,
#					Uninstallation will delete files from the server.
#
#					This script can be further improved by organising some repititive steps
#					into functions and wrapping with exeption handling.
# -----------------------------------------------------------------------------------------

# python 2.7
# import ConfigParser
import sys
import os
import shutil
import time
import datetime
from distutils.dir_util import copy_tree
from sys import argv


def main():

	# initialise to enter to the loop
	menuoption = 0

	while (menuoption != 4):
		os.system('clear')

		print "\n"
		print " ################################################"
		print "     Welcome to ProViewer Installation Setup!     "
		print " ################################################\n"

		print "What do you want to do:"
		print "\t1. Quick Install"
		print "\t2. Advance Install"
		print "\t3. Uninstall"
		print "\t4. Exit"

		menuoption = raw_input("\nPlease type option number:")
		if   (menuoption == '1'):
			quickInstall()
		elif (menuoption == '2'):
			advancedInstall()
		elif (menuoption == '3'):
			confirm = raw_input("\nAre you sure, you want to uninstall?(Y/N):")
			if (confirm == 'Y' or confirm == 'y'):
				uninstall()
		elif (menuoption == '4' or menuoption == 'exit'):
			print "\033[1;33;40m\n*** Thank you for using ProViewer! ***\n"
			time.sleep( 3 )
			exit()

def quickInstall():
	'''
	This method will copy installation files
	into  the galaxy server instance  using default settings
	'''

	# get the current working directory. By default it is the galaxy/config folder
	current_dir = os.getcwd()
	printMainTitle()

	# STEP 1 - Get galaxy root folder
	galaxy_root_location = getGalaxyRootDir()

	# STEP 2 - Copy Visualisation Plugin
	printTitle("STEP 2 - Copy Visualisation Plugin")
	print "\nVisualizations plugin directory: absolute folder path where to look for individual visualization plugins.\n"
	suggested_path = galaxy_root_location + "/config/plugins/visualizations/"
	print "Defalut location :" + suggested_path
	print "Save to default location...\n"
	to_dir 		= os.path.join(suggested_path, 'proviewer')
	from_dir    = os.path.join(current_dir, 'ProViewer/proviewer')
	copy(from_dir, to_dir)

	# STEP 3 - Copy Web API Files
	printTitle("STEP 3 - Copy Web API Files")
	print "\nWeb API directory: absolute folder path where you have galaxy webapp api files\n"
	suggested_path = galaxy_root_location + "/lib/galaxy/webapps/galaxy/api/"
	print "Defalut location :" + suggested_path
	print "Creating a backup for datasets.py..."
	original_name = suggested_path + 'datasets.py'
	backup_name = '%sdatasets_%s.py.bak' % (suggested_path, datetime.datetime.now())
	os.rename(original_name, backup_name)
	print "Backup created for %s" % backup_name
	print "Save to default location...\n"
	web_api_dir = suggested_path
	from_dir    = os.path.join(current_dir, 'ProViewer/webcontroller')
	copy(from_dir, web_api_dir)

	#STEP 4 - Copy Galaxy Tool
	printTitle("STEP 4 - Copy Galaxy Tool")
	print "\nGalaxy tool folder: absolute folder path where individual tool saved in galaxy\n"
	suggested_path = galaxy_root_location + "/tools/mzIdentMLToJSON/"
	print "Defalut location :" + suggested_path
	print "Save to default location...\n"
	tool_dir = suggested_path
	from_dir    = os.path.join(current_dir, 'ProViewer/tool/mzIdentMLToJSON')
	copy(from_dir, tool_dir)

	print "\n ProExtractor.jar java library saves in the same folder as other tool files\n"
	print "Save to default location...\n"
	javalib_dir = tool_dir
	progressbar()

	# STEP 5 - add settings to configuration file
	printTitle("STEP 5 - Set output directory")
	print "\nOutput directory: folder where temporary JSON files generated from java library get saved\n"
	suggested_path = galaxy_root_location + "/config/plugins/visualizations/proviewer/static/data/"
	print "Save to default location...\n"
	output_dir = suggested_path
	rel_output_dir = "/plugins/visualizations/proviewer/static/data/"
	error_report_sent_to = "admin@somewhere.com"
	multithreading = "true"
	maxMemory = "-Xmx7000M"

	# STEP 6 - add settings to configuration file
	printTitle("STEP 6 - create a setting file")
	print "\nA setting file called mzidentml_setttings.ini will be created in \n"+ galaxy_root_location + "/config/ location\n"
	print "These settings are only specific to mzIdentML visualization plugin\n"

	galaxy_ini = os.path.join(galaxy_root_location, 'config/galaxy.ini')
	settings = "# ---- ProViewer Config ------\n\n[MzIdentML]" + "\noutput_dir = " + output_dir + "\nrel_output_dir = " + rel_output_dir + "\njavalib = " + javalib_dir + "ProExtractor.jar" + "\nmultithreading = " + multithreading + "\nmaxMemory = " + maxMemory + "\nerror_report_sent_to = " + error_report_sent_to
	print settings

	setting_location = galaxy_root_location + "/config/proviewer_setttings.ini"
	print "Settings will be added to : " + setting_location
	target = open(setting_location, 'w+')
	target.write(settings)
	target.close()
	progressbar()

	print "\nInstallation Completed!!!"

	confirm = raw_input("\nDo you want to continue?(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		pass
	else:
		exit()

def advancedInstall():
	'''
	This method will copy installation files
	into  the galaxy server instance
	'''

	# get the current working directory. By default it is the galaxy/config folder
	current_dir = os.getcwd()

	printMainTitle()

	# STEP 1 - Get galaxy root folder
	galaxy_root_location = getGalaxyRootDir()

	# STEP 2 - Copy Visualisation Plugin
	printTitle("STEP 2 - Copy Visualisation Plugin")
	print "\nVisualizations plugin directory: absolute folder path where to look for individual visualization plugins.\n"
	suggested_path = galaxy_root_location + "/config/plugins/visualizations/"
	print "Defalut location :" + suggested_path
	confirm = raw_input("Save to default location(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		vis_plugin_dir = suggested_path
	else:
		vis_plugin_dir = raw_input("Customized Visualisation Plugin Directory :")
	to_dir 		= os.path.join(vis_plugin_dir, 'proviewer')
	from_dir    = os.path.join(current_dir, 'ProViewer/proviewer')
	copy(from_dir, to_dir)

	# STEP 3 - Copy Web API Files
	printTitle("STEP 3 - Copy Web API Files")
	print "\nWeb API directory: absolute folder path where you have galaxy webapp api files\n"
	suggested_path = galaxy_root_location + "/lib/galaxy/webapps/galaxy/api/"
	print "Defalut location :" + suggested_path
	confirm = raw_input("Save to default location(Y/N):")

	if (confirm == 'Y' or confirm == 'y'):
		web_api_dir = suggested_path
	else:
		web_api_dir = raw_input("Customized Web API Directory :")

	print "Creating a backup for datasets.py..."
	original_name = '%sdatasets.py' % web_api_dir
	backup_name = '%sdatasets_%s.py.bak' % (web_api_dir, datetime.datetime.now())
	os.rename(original_name, backup_name)
	print "Backup created: %s" % backup_name

	from_dir    = os.path.join(current_dir, 'ProViewer/webcontroller')
	copy(from_dir, web_api_dir)

	#STEP 4 - Copy Galaxy Tool
	printTitle("STEP 4 - Copy Galaxy Tool")
	print "\nGalaxy tool folder: absolute folder path where tool (both executables and wrapper) saved in galaxy\n"
	suggested_path = galaxy_root_location + "/tools/mzIdentMLToJSON/"
	print "Defalut location :" + suggested_path
	confirm = raw_input("Save to default location(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		tool_dir = suggested_path
	else:
		tool_dir 	= raw_input("Customized Tool directory(executable):")
	from_dir    = os.path.join(current_dir, 'ProViewer/tool/mzIdentMLToJSON')
	copy(from_dir, tool_dir)

	print "\n ProExtractor.jar java library saves in the same folder as other tool files\n"
	confirm = raw_input("Do you want to use default location(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		javalib_dir = tool_dir
	else:
		javalib_dir = raw_input("Directory of ProExtractor.jar java library (absolute path):")

	# STEP 5 - Set Output directory
	printTitle("STEP 5 - Set output directory")
	print "\nOutput directory: folder where temporary JSON files generated from java library get saved\n"
	suggested_path = galaxy_root_location + "/config/plugins/visualizations/proviewer/static/data/"
	confirm = raw_input("is your Output Directory \n "+ suggested_path +"(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		output_dir = suggested_path
		rel_output_dir = "/plugins/visualizations/proviewer/static/data/"
	else:
		output_dir 			= raw_input("Output dir where temporay files get saved:")
		rel_output_dir 		= raw_input("Output dir relative path reference to config folder:")

	error_report_sent_to = "admin@somewhere.com"
	multithreading = "true"
	maxMemory = "-Xmx7000M"

	# STEP 6 - Copy GIO server Specific files to the server
	printTitle("STEP 6 - GIO server")
	confirm = raw_input("\nCopying tool into GIO Server?(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		wrapper_location = galaxy_root_location + "/tools/gio/Misc/"
		print "Galaxy tools wrapper location:" + suggested_path
		from_dir    = os.path.join(current_dir, 'GIOServer')
		copy(from_dir, wrapper_location)


	# STEP 7 - add settings to configuration file
	printTitle("STEP 6 - create a setting file")
	print "\nA setting file called mzidentml_setttings.ini will be created in \n"+ galaxy_root_location + "/config/ location\n"
	print "These settings are only specific to mzIdentML visualization plugin\n"

	galaxy_ini = os.path.join(galaxy_root_location, 'config/galaxy.ini')
	settings = "# ---- ProViewer Config ------\n\n[MzIdentML]" + "\noutput_dir = " + output_dir + "\nrel_output_dir = " + rel_output_dir + "\njavalib = " + javalib_dir + "ProExtractor.jar" + "\nmultithreading = " + multithreading + "\nmaxMemory = " + maxMemory + "\nerror_report_sent_to = " + error_report_sent_to
	print settings

	confirm = raw_input("\nSettings are accurate?(Y/N):")

	if (confirm == 'Y' or confirm == 'y'):
		setting_location = galaxy_root_location + "/config/proviewer_setttings.ini"
		print "Settings will be added to : " + setting_location
		target = open(setting_location, 'w+')
		target.write(settings)
		target.close()
		progressbar()

	print "\nInstallation Completed!!!"

	confirm = raw_input("\nDo you want to continue?(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		pass
	else:
		exit()

def uninstall():

	# This is a dictionary of filepaths which are failing to delete from this method
	# they should be manually deleted
	filesFailedToRemove = {}

	# get the current working directory. By default it is the galaxy/config folder
	current_dir = os.getcwd()

	# STEP 1 - Get galaxy root folder
	galaxy_root_location = getGalaxyRootDir()

	# reading folder locations from configuration settings file
	# config = ConfigParser.ConfigParser()
	# config.read(galaxy_root_location + "/config/proviewer_setttings.ini")
	# outputfile = config.get('MzIdentML', 'output_dir')

	# STEP 2 - remove proviewer_settings.ini setting file from galaxy/config folder
	filepath = galaxy_root_location + "/config/proviewer_setttings.ini"
	if os.path.isfile(filepath):
		os.remove(filepath)
		print "\nRemoved : " + filepath
	else:
		filesFailedToRemove['setting'] = filepath

	# STEP 3 - Remove files in the webcontroller folder
	# namely: mzIdentMLHandler.py, MzIdentMLToJSON.py and SequenceExtratctor.py
	# DatasetController will not get deleted. It has to be manually removed,
	# because it contains other critical functions that shouldn't be deleted!

	webcontrollerPath = galaxy_root_location + "/lib/galaxy/webapps/galaxy/api/"
	filelist = ['MzIdentMLHandler','MzIdentMLToJSON','SequenceExtractor']

	for filename in filelist:
		filepath  = webcontrollerPath + filename + ".py"
		if os.path.isfile(filepath):
			os.remove(filepath)
			print "\nRemoved : " + filepath
		else:
			filesFailedToRemove[filename] = filepath

	# STEP 3 - Remove ProViewer client side folder
	filepath = galaxy_root_location + "/config/plugins/visualizations/proviewer/"
	if os.path.isdir(filepath):
		# remove everyting in this directory
		shutil.rmtree(filepath)
		print "\nRemoved : " + filepath
	else:
		filesFailedToRemove['proviewer'] = filepath

	# STEP 4 - Remove mzIdentMLToJSON tool
	filepath = galaxy_root_location + "/tools/mzIdentMLToJSON/"
	if os.path.isdir(filepath):
		# remove everyting in this directory
		shutil.rmtree(filepath)
		print "\nRemoved : " + filepath
	else:
		filesFailedToRemove['mzIdentMLToJSON'] = filepath

	print "\nThese files/folders cannot be deleted. Please remove them manually:"
	for key, value in filesFailedToRemove.iteritems():
		print "\n" + key + " : " + value

	print "\n*** Thank you for using ProViewer! ***\n"
	time.sleep( 2 )

	confirm = raw_input("\nDo you want to continue?(Y/N):")
	if (confirm == 'Y' or confirm == 'y'):
		pass
	else:
		exit()

def copy(from_dir, to_dir):
	'''
	Copy file or folder to another folder
	'''
	if os.path.isdir(from_dir) or os.path.isfile(from_dir):
		print "File copying started..."
		print "\nCopying "+ from_dir + " \n--> " + to_dir + "\n"
		try:
			copy_tree(from_dir, to_dir)
			print "copied to " + to_dir
		except Exception as err:
			print("File Copying Error\n: {0}".format(err))
			exit()
		progressbar()
	else:
		print("\033[1;33;40m Sorry, folder path is invalid!\n")
		exit()

def printMainTitle():

	os.system('clear')
	print "\n"
	print " ================================================"
	print "                Install ProViewer                "
	print " ================================================\n"

	print "Please answer to following questions carefully."
	print "You can refer examples given below as a guide.\n\n"

def printTitle(title):

	print "\n" + title
	print "------------------------------------------------\n"

def getGalaxyRootDir():

	printTitle("STEP 1 - Set your galaxy root directory")
	print "\nThis is the absolute file path for your galaxy main folder\n"
	print "\nExample : /home/galaxy/Downloads/galaxy\n"
	galaxy_root_location= raw_input("Galaxy instance root directory:")
	return galaxy_root_location

# Show progress bar with increamenting value
# This function was take from :
# Cordier, B. (2016). Text Progress Bar in the Console. [online] Stackoverflow.com.
# Available at: http://stackoverflow.com/questions/3173320/text-progress-bar-in-the-console
# [Accessed 24 Aug. 2016].
def progressbar():
	"""
	This function  increases the value of the progress bar
	However, this is just to show to the user, that something is happening
	Nothing to do with the value of the progress bar
	"""
	items = list(range(0, 100))
	i     = 0
	l     = len(items)

	# Initial call to print 0% progress
	printProgress(i, l, prefix = 'Progress:', suffix = 'Complete', barLength = 50)
	for item in items:
    		time.sleep( 0.01 )
    		# Update Progress Bar
    		i += 1
    		printProgress(i, l, prefix = 'Progress:', suffix = 'Complete', barLength = 50)


# Print iterations progress
# This function was take from :
# Cordier, B. (2016). Text Progress Bar in the Console. [online] Stackoverflow.com.
# Available at: http://stackoverflow.com/questions/3173320/text-progress-bar-in-the-console
# [Accessed 24 Aug. 2016].
def printProgress (iteration, total, prefix = '', suffix = '', decimals = 1, barLength = 100):
    """
    Call in a loop to create terminal progress bar
    @params:
        iteration   - Required  : current iteration (Int)
        total       - Required  : total iterations (Int)
        prefix      - Optional  : prefix string (Str)
        suffix      - Optional  : suffix string (Str)
        decimals    - Optional  : positive number of decimals in percent complete (Int)
        barLength   - Optional  : character length of bar (Int)
    """
    formatStr       = "{0:." + str(decimals) + "f}"
    percents        = formatStr.format(100 * (iteration / float(total)))
    filledLength    = int(round(barLength * iteration / float(total)))
    bar             = '█' * filledLength + '-' * (barLength - filledLength)
    sys.stdout.write('\r%s |%s| %s%s %s' % (prefix, bar, percents, '%', suffix)),
    sys.stdout.flush()
    if iteration == total:
        sys.stdout.write('\n')
        sys.stdout.flush()

if __name__ == "__main__":
    main()
