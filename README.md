# mzIdentMLVisualiser

![Alt text](samples/snapshots/protein.png)

This is an interactive web visualisation plug-in for the [mzIdentML](http://www.psidev.info/mzidentml) file within the [Galaxy bioinformatics platform](https://galaxyproject.org). In the repository, we provide the necessary files to install mzIdentML Viewer plugin into your existing Galaxy instance accomplished with the source codes of the underneath functionality which converts mzIdentML into JSON files to be viewed in the main panel. There are four main folders:

* plugin - Galaxy visualisation plugin
* tool - Galaxy tool
* samples - sample configurations and other sample files
* source - mzIdentMLExtractor java library [Not required for the installation]

Galaxy visualisation plugin files are organised into two folders which are called *protviewer* and *webcontroller*.
Additionally, we have a galaxy tool called "mzIdentMLToJSON" which generates temporary JSON files to speed up data loading for visualisation plugin. You must integrate both plugin and tool in order to work with the visualisation, as galaxy tool contains dependency files for the visualisation plugin too. Although the integration of both plugin and tool is mandatory for installation, you can use plugin independently without using galaxy tool. However, we strongly recommend to use our galaxy tool prior to visualise mzIdentML files for a much faster visualising speed.

## Installation

Installation instructions are provided below. These instructions assume that you already have Galaxy installed and have admin access to that installation. If you do not already installed Galaxy or not having admin access, please refer link [here](https://wiki.galaxyproject.org/Admin/GetGalaxy).

In order to proceed, please download our repository to your machine by cloning the repository. If you download as a zip file, then extract the zip file.

### Install Galaxy Visualisation Plugin

#### Step 1 - Edit configuration file
* You need to make sure, you have enabled visualisation plugins on your Galaxy installation. First, go to your *galaxy.ini* configuration file (located in ```<your galaxy directory>/config/```). If you do not have a *galaxy.ini* file, but have a *galaxy.ini.sample* file, then make a copy of *galaxy.ini.sample* file and rename it to *galaxy.ini*. Secondly, search for *visualization_plugins_directory* setting in that  *galaxy.ini* file . If this setting has not already set, assign your visualisation directory/uncomment the line as follows :

```bash
# Visualizations config directory: where to look for individual visualization plugins.
# The path is relative to the Galaxy root dir. To use an absolute path begin the path
# with '/'.
visualization_plugins_directory = config/plugins/visualizations
```

Add following settings in the bottom of the *galaxy.ini* file

```bash
[MzIdentML]
# This is where tempory JSON file of the plugin will get stored
output_file_dir = /Users/yourname/Downloads/galaxy/config/plugins/visualizations/protviewer/static/data/
# Absolute path for the java library. We recommend to keep in the mzIdentMLToJSON tool folder
javalib  = /Users/yourname/Downloads/galaxy/tools/mzIdentMLToJSON/mzIdentMLExtractor.jar
# Absolute path for the tool directory in your galaxy instance
tool_path = /Users/yourname/Downloads/galaxy/tools/
# If there is an error, whom do you want to send the error message(admin)
error_report_to = j.fan@qmul.ac.uk
```

As a guidance to above step, we have provided you a sample configuration file(galaxy.ini) in *samples* folder.

#### Step 2 - Copy *protviewer* folder into your visualisations directory
* Copy the entire *protviewer* folder in the *plugin* folder to ```<your galaxy directory>/config/plugins/visualizations/``` folder. After copying, go to protviewer/templates/protviewer.mako, line Number 134, change your ```bash rootLocation ``` your galaxy root folder path. Further, if you want to change output directory, you can change in line Number 134, change your ```bash dataLocation  ```

#### Step 3 - Copy *webcontroller* files into your web API Controller 
*  You are given three files in the *webcontroller* folder in the *plugin* folder with the mzIdentMLVisualiser repository which are namely:
  * MzIdentMLHandler.py
  * SequenceExtractor.py 
  * MzIdentMLToJSON.py They has to be copied into your galaxy instance at ```<your galaxy directory>/lib/galaxy/webapps/galaxy/api/``` location.

* Then, in your galaxy, you should be able to find a file called **datasets.py** at the same location(```<your galaxy directory>/lib/galaxy/webapps/galaxy/api/```). There, copy and paste following codes:

  * Import these modules first:
   ```python
      from MzIdentMLToJSON import MzIdentMLToJSON
      from SequenceExtractor import SequenceExtractor
      import os.path
      import subprocess
   ```
  * There, paste following code inside Class **DatasetsController** -> method **show**:
   ```python
      elif data_type == 'mzidentml':
        rval = self._mzIdentMLProcess(**kwd)
    ```
    
  * Just below method **show** method add this function:
  
 ```python
     def _mzIdentMLProcess( self, **kwd):
        print  "MzIdentML Viewer INFO: called Web API controller!"
        # input mzIdentML file
        inputfile = kwd.get('inputFile')
        # unique sequrity encoded id assigned for the input file
        datasetId = kwd.get('datasetId')
        # galaxy root directory
        root = kwd.get('root')
        rval = inputfile
        # Web plugin loading time
        if kwd.get('mode') == 'initial_load':
            converter = MzIdentMLToJSON()
            converter.extract(inputfile, datasetId, root)
        elif kwd.get('mode') == 'sequence':
            seqEx = SequenceExtractor()
            rval = seqEx.extract(inputfile, kwd.get('dbSequenceId'))
        return rval
 ```
    Warning: **Mind your indentation!** As a guidance for the above step, you can find a sample *datasets.py* file in *sample* folder.

### Install Galaxy Tool

#### Step 1 - Configure Tool

Locate the *tool_conf.xml* configuration file in ```<your galaxy directory>/config/``` location. If you do not find a *tool_conf.xml* file, but have a *tool_conf.xml.sample* file, make a copy of it and rename new file as *tool_conf.xml*
There, add these parameters anywhere of  the file under ```<toolbox>``` tag:

```XML
<section id="PSI" name="PSI Standards" >
    <tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
</section>
```

We have created a separate section called "PSI Standards" in the tool panel. However, you can add this tool to one of your existing sections by only specifying ```<tool>``` tag as below:

```XML
<tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
```

As a guidance for above step, sample configuration file is given in *samples* folder.

#### Step 2 - copy tool

Copy given entire *mzIdentMLToJSON* folder in the *tool* folder to your galaxy instance at ```<your galaxy directory>/tools/```.
This folder contains:
 1. wrapper - mzIdentMLToJSON.xml
 2. python script - mzIdentMLToJSON.py
 3. java library - mzIdentMLExtractor.jar
 4. java library dependencies - lib folder

That's it! You are ready to use the visualisation plug-in and the tool.

**Note:** You must **restart server** to reflect the changes.

## How to use visualisation plugin

<img src="samples/snapshots/how_to_use.png" alt="menu"  width="1137" height="373"/>

User *MUST* **login** to the server in order to use visualisation functionality. This visualisation is enabled for mzIdentML files only. Once you upload mzIdentML file(.mzid file extension), it will be added to the history panel. You can visualise the input mzIdentML file by clicking on the visualisation button and selecting the *mzIdentML viewer* from the menu. Time taken to load data into the viewer depends on the size of the input file.

## How to use visualisation tool

<img src="samples/snapshots/galaxytool.png" alt="How to used Galaxy Tool"/>

You can integrate this tool into your protein identification workflows or can execute individually. If you are using search tool in the workflow, the output mzIdentML file of the search tool is the input for this galaxy tool.
