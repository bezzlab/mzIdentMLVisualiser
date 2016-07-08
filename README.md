# mzIdentMLVisualiser

This is an interactive web visualisation plug-in for the [mzIdentML](http://www.psidev.info/mzidentml) file within the [Galaxy bioinformatics platform](https://galaxyproject.org). This repository provides you source code of the java library and other installation files to integrate visualisation plugin into your existing galaxy instanse. There are five main folders:
* mzIdentMLExtractor - Java library which converts proteomics data of mzIdentML to JSON files[Not required for the installation]
* mzIdentMLToJSON - Galaxy Tool
* protviewer - Galaxy plugin(client side)
* sampleFiles - sample configurations and other sample files
* webcontroller - Galaxy plugin (Server side)

Galaxy visualisation plugin files are splitted into two folders which are called protviewer and webcontroller.
Additionally, we have a galaxy tool called "mzIdentMLToJSON" which generates temporary JSON files to speed up data loading for visualisation plugin. You need to integrate both plugin and tool in order to work with the visualisation, because galaxy tool contains dependancy files for the visualisation plugin. Although it is mandatory to integrate both plugin and tool, after the integration, you can use plugin alone without using galaxy tool. However, we strongly recommend to use this galaxy tool prior to visualize mzIdentML files for a much faster visualising speed.

## Installation

Installation instructions are provided below. These instructions assume that you already have Galaxy installed and have admin access to that installation. If you do not already have Galaxy, please refer link [here](https://wiki.galaxyproject.org/Admin/GetGalaxy).

In order to proceed, please download this repository to your machine by cloning the repository. If you downloaded as a zip file, then extract the zip file.

### Install Galaxy Visualisation Plugin

#### Step 1 - Enable visualisation from the configuration file 
* You need to make sure, you have enabled visualisation plugins on your Galaxy installation. Go to your *galaxy.ini* configuration file (located in ```<your galaxy directory>/config/```) and search for *visualization_plugins_directory* setting. There, assign your visualisation directory as below, if it is not already asigned:

```bash
# Visualizations config directory: where to look for individual visualization plugins.
# The path is relative to the Galaxy root dir. To use an absolute path begin the path
# with '/'.
visualization_plugins_directory = config/plugins/visualizations
```
As a guidance to above step, we have provided a sample configuration file(galaxy.ini) in *sampleFiles* folder. For more details: https://wiki.galaxyproject.org/VisualizationsRegistry

#### Step 2 - Copy visualisation plugin into visualizations folder
* Copy entire *protviewer* folder to ```<your galaxy directory>/config/plugins/visualizations/``` folder

#### Step 3 - Copy Web Controller section
* Go to ```<your galaxy directory>/lib/galaxy/webapps/galaxy/api/``` location. There, copy following files which are in the webcontroller folder:
  * MzIdentMLHandler.py
  * MzIdentMLHandler.pyc
  * SequenceExtractor.py
  * SequenceExtractor.pyc
* Then, in your galaxy, you sould be able to find a file called **datasets.py**. There, copy and paste following codes:

  * Import these modules first:
   ```python
      from SequenceExtractor import SequenceExtractor
      import os.path
      import subprocess
   ```
  * There, paste following code inside Class **DatasetsController** -> method **show**:
   ```python
      elif data_type == 'mzidentml':
        filename = kwd.get('filename')
        datasetId = kwd.get('datasetId')
        rval = filename
        # CHANGE ROOT HERE
        root = "www.yoursite.com/your_galaxy/"
        tempFile = root + "config/plugins/visualizations/protviewer/static/data/" + datasetId + "_protein.json"
        libraryLocation = root + "tools/mzIdentMLToJSON/mzIdentMLExtractor.jar"
        
        if kwd.get('mode') == 'init':
          if os.path.isfile(tempFile) == False:
            return subprocess.call(['java', '-jar',javalib, filename, datasetId])
          else:
            print "Info: Data loaded from the cache!"
        elif kwd.get('mode') == 'sequence':
          dbSequenceId = kwd.get('dbSequenceId')
          # extract the sequence
          seqEx = SequenceExtractor()
          sequence = seqEx.extract(filename, dbSequenceId)
          rval = sequence
          return rval
    ```
    Warning: Mind your indentation!
  * Set your root path to **root** variable. Root variable are used in two places: 
    * tempfile - file path of your output json file. 
    * javalib - file path of the java library(mzIdentMLExtractor.jar) located in mzIdentMLToJSON folder(see Install Galaxy Tool section).
    
Make sure you have set these paths accurately. You can find a sample *datasets.py* file in *webcontroller* folder

### Install Galaxy Tool

#### Step 1 - Configure tool

Locate the tool_conf.xml configuration file in ```<your galaxy directory>/config/``` location.
Add these parameters to anyware of  the file under <toolbox> tag:

```XML
<section name="mzIdentMLToJSON" id="mzIdentMLToJSON">
    <tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
</section>
```

Sample configuration file is given in *sampleConfig* folder.

#### Step 2 - copy tool

Copy mzIdentMLToJSON folder to your instance(```<your galaxy directory>/tools/```)
This folder contains 
 1. wrapper - mzIdentMLToJSON.xml 
 2. python script - mzIdentMLToJSON.py
 3. java library - mzIdentMLExtractor.jar

Again, you need to set your root path to **root** variable in mzIdentMLToJSON.py file.

**Note:** You need to restart server to reflect the changes. Users have to login to the server in order to use visualisation functionality.
