# ProViewer

![Alt text](Documentation/protein.png)

ProViewer is an interactive web visualisation plug-in for the [mzIdentML](http://www.psidev.info/mzidentml) file within the [Galaxy bioinformatics platform](https://galaxyproject.org). In the repository, we provide the necessary files to install ProViewer plugin into your existing Galaxy instance accomplished with the source codes of the underneath functionality which converts mzIdentML into JSON files to be viewed in the main panel. Main folders of the repository are as follows:

* ProViewer - Galaxy visualisation plugin
  * Proviewer - client-side of the plugin
  * webcontroller - server-side of the plugin
  * tool/mzIdentMLToJSON - Galaxy tool
  * samples - sample configurations and other sample files
* ProExtractor - source code of thr Java library developed for mzIdentML data extraction
* GIOServer - GIO server specific installation files
* install.py - installation setup
* Test
* Documentation

Galaxy visualisation plugin cient-side and server-side files are organised into two folders which are called *proviewer* and *webcontroller* respectively. Additionally, we have a Galaxy tool called "mzIdentMLToJSON" which generates temporary JSON files to speed up data loading for visualisation plugin. You must integrate both plugin and tool in order to work with the visualisation, as Galaxy tool contains dependency files for the visualisation plugin too. Although the integration of both plugin and tool is mandatory for installation, you can use plugin independently without using Galaxy tool. However, we strongly recommend to use our Galaxy tool prior to visualise mzIdentML files for a much faster visualising speed.

This plugin is already installed into [GIO server](http://gio.sbcs.qmul.ac.uk). Installation instructions for GIO also provided here. 

## Installation

Installation instructions are provided below. These instructions assume that you already have Galaxy installed and have admin access to that installation. If you do not already installed Galaxy or not having admin access, please refer link [here](https://wiki.galaxyproject.org/Admin/GetGalaxy).

In order to proceed, please download our repository to your machine by cloning the repository. If you download as a zip file, then extract the zip folder.

### Install Galaxy Visualisation Plugin 

#### Step 1 - Edit the configuration file

You need to make sure, you have enabled visualisation plugins on your Galaxy installation. First, go to your *galaxy.ini*  configuration file (located in ```<your galaxy directory>/config/```). If you do not have a *galaxy.ini* file, but have a  *galaxy.ini.sample* file, then make a copy of *galaxy.ini.sample* file and rename it to *galaxy.ini*. Secondly, search for *visualization_plugins_directory* setting in that  *galaxy.ini* file. If this setting has not already set, assign your visualisation directory/uncomment the (last) line as follows:

```bash
# Visualizations config directory: where to look for individual visualization plugins.
# The path is relative to the Galaxy root dir. To use an absolute path begin the path
# with '/'.
visualization_plugins_directory = config/plugins/visualizations
```

#### Step 2 - Run the installation setup file

Go to the downloaded folder of the repository from the command-line, and execute install.py file as ```python install.py``` and follow the given instructions. "Quick install" option will use all the default settings where "advanced install" option alllows to customise settings at the time of the installation. Examples are provided for both quick and advanced installation as a guide. Please follow same format when you provide file paths, epecially ```/``` at the end where required. This setup will perform following actions:
 * copy installation files to your server 
 * add all the plugin settings into a settings file called proviewer_settings.ini into ```<your galaxy directory>/config/``` location
 * A backup file will be created for ``` datasets.py``` file  with the file name including current timestamp in ```<your galaxy directory>/lib/webapps/galaxy/api``` location.

##### GIO specific instructions - Recommented to used advance installation option. 
 * Step 1 - /home/galaxy/galaxy
 * Step 2 - Y
 * Step 3 - Y
 * Step 4 - N 
  - ```/home/galaxy/gio_applications/misc/``` 
  - Y (recommended)
 * Step 5 - Y
 * Step 6 - Y (this will copy wrapper into ```<your galaxy directory>/tools/gio/Misc/```)
 * Conform Settings - Y

### Install Galaxy Tool

#### Step 1 - Configure Tool

Locate the *tool_conf.xml* configuration file in ```<your galaxy directory>/config/``` location. If you do not find a *tool_conf.xml* file, but have a *tool_conf.xml.sample* file, make a copy of it and rename new file as *tool_conf.xml*.
There, add these parameters anywhere of  the file under ```<toolbox>``` tag:

```XML
<section id="PSI" name="PSI Standards" >
    <tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
</section>
```

##### GIO specific instructions: 

please include following line in section "GIO:Miscellaneous" in both ```<your galaxy directory>/config/tool_conf.xml``` and ```<your galaxy directory>/tools/gio/Misc/tool_conf_Misc.xml``` files:

```XML
<tool file="gio/Misc/mzIdentMLToJSON.xml"/>
```

As a guidance for above step, sample configuration file is given in *samples* folder.

That's it! You are ready to use the visualisation plug-in and the tool.

**Note:** You must **restart server** to reflect the changes.

## How to use visualisation plugin

<img src="/Documentation/HowToUse.png" alt="menu"  width="1137" height="373"/>

User *MUST* **login** to the server in order to use visualisation functionality. This visualisation is enabled for mzIdentML files only. Once you upload mzIdentML file(.mzid file extension), it will be added to the history panel. You can visualise the input mzIdentML file by clicking on the visualisation button and selecting the *ProViewer* from the menu. Time taken to load data into the viewer depends on the size of the input file.

## How to use visualisation tool

<img src="Documentation/galaxytool.png" alt="How to used Galaxy Tool"/>

You can integrate this tool into your protein identification workflows or can execute individually. If you are using search tool in the workflow, the output mzIdentML file of the search tool is the input for this galaxy tool.

## Dependencies

* Python 2.7
* JRE 8
