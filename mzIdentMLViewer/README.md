# mzIdentMLViewer

This is an interactive web visualisation plug-in for the [mzIdentML](http://www.psidev.info/mzidentml) file within the [Galaxy bioinformatics platform](https://galaxyproject.org).Here, we have a galaxy tool called "mzIdentMLToJSON" which generate temporary files to speed up data loading for visualisation plugin. Although it is optional to use, We strongly recommend to use the galaxy tool with your workflows prior using visualisation plugin for instant visualisation of the file.

## Installation

Installation instructions are provided below. These instructions assume that you already have Galaxy installed and have admin access to that installation. If you do not already have Galaxy, please refer link [here](https://wiki.galaxyproject.org/Admin/GetGalaxy).

### Install Galaxy Visualisation Plugin

#### Step 1 - Enable visualisation from the configuration file 
* You need to make sure you have enabled visualisation plugins on your Galaxy installation. Go to your galaxy.ini file (located in ```<your galaxy directory>/config/```) and search for visualization_plugins_directory setting. There, assign your visualisation directory as below, if it is not already asigned:

```bash
# Visualizations config directory: where to look for individual visualization plugins.
# The path is relative to the Galaxy root dir. To use an absolute path begin the path
# with '/'.
visualization_plugins_directory = config/plugins/visualizations
```
For more details: https://wiki.galaxyproject.org/VisualizationsRegistry

#### Step 2 - Copy visualisation plugin into visualizations folder
* Copy entire protviewer folder to <your galaxy directory>/config/plugins/visualizations folder

#### Step 3 - Copy Web Controller section
* Go to ```<your galaxy directory>/lib/galaxy/webapps/galaxy/api/``` location. There, copy following files which are in the webcontroller folder:
  * MzIdentMLHandler.py
  * MzIdentMLHandler.pyc
  * SequenceExtractor.py
  * SequenceExtractor.pyc
* Then, in your galaxy, you sould be able to find a file called datasets.py. There, copy and paste following codes:

  * Import these modules first:
   ```python
      from SequenceExtractor import SequenceExtractor
      import os.path
      import subprocess
   ```
  * There, paste following code inside Class DatasetsController -> method show:
   ```python
      elif data_type == 'mzidentml':
        filename = kwd.get('filename')
        datasetId = kwd.get('datasetId')
        javalib = "/Users/myname/Documents/mzIdentMLViewer/galaxy/tools/mzIdentMLToJSON/mzIdentMLExtractor.jar"
        tempfile = "/Users/myname/Documents/mzIdentMLViewer/galaxy/config/plugins/visualizations/protviewer/static/data/"+datasetId+"_protein.json"
        if kwd.get('mode') == 'init':
          if os.path.isfile(tempfile) == False:
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
    Warning: mind your indentation!
  * Set your file paths. 
    * tempfile - file path of your output json file. 
    * javalib - file path of the java library(mzIdentMLExtractor.jar) located in mzIdentMLToJSON folder(see Install Galaxy Tool section). 
    Set these paths here accoringly.

### Install Galaxy Tool

#### Step 1 - Configure tool

Locate the tool_conf.xml configuration file in ```<your galaxy directory>/config/``` location.
Add these parameters to anyware of  the file under <toolbox> tag:

```XML
<section name="mzIdentMLToJSON" id="mzIdentMLToJSON">
    <tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
</section>
```
#### Step 2 - copy tool

Copy mzIdentMLToJSON folder to your instance(```<your galaxy directory>/tools/```)
This folder contains 
 1. wrapper - mzIdentMLToJSON.xml 
 2. python script - mzIdentMLToJSON.py
 3. java library - mzIdentMLExtractor.jar

Again, you need to change the output file path in mzIdentMLToJSON.py file similar to you did in galaxy plugin installation.
