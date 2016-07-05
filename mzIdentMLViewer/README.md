# mzIdentMLViewer

This is an interactive web visualisation plug-in for the mzIdentML file within the Galaxy bioinformatics platform. mzIdentML is the standard format for reporting identifications in MS-based proteomics experiments.

## Installation

Installation instructions are provided below. These instructions assume that you already have Galaxy installed and have admin access to that installation. If you do not already have Galaxy, please refer link: https://wiki.galaxyproject.org/Admin/GetGalaxy

#### Step 1 - Install Galaxy Visualisation Plugin

* First, you need to make sure you have enabled visualisation plugins on your Galaxy installation. Go to your galaxy.ini file (located in <your galaxy directory>/config/) and search for visualization_plugins_directory setting. There, assign your visualisation directory as below, if it is not already asigned:

```bash
# Visualizations config directory: where to look for individual visualization plugins.
# The path is relative to the Galaxy root dir. To use an absolute path begin the path
# with '/'.
visualization_plugins_directory = config/plugins/visualizations
```
For more details: https://wiki.galaxyproject.org/VisualizationsRegistry

* Secondly, copy entire protviewer folder to <your galaxy directory>/config/plugins/visualizations folder
* Thirdly, go to <your galaxy directory>/lib/galaxy/webapps/galaxy/api/ location. There, copy following files which are in the webcontroller folder:
  * MzIdentMLHandler.py
  * MzIdentMLHandler.pyc
  * SequenceExtractor.py
  * SequenceExtractor.pyc
* Finally, in your galaxy, you sould be able to find a file called datasets.py. There copy and paste following codes:

1. Import these modules first:

```python
from SequenceExtractor import SequenceExtractor
import os.path
import subprocess
```

2. There, paste following code inside Class DatasetsController -> method show -> inside elif data_type == 'mzidentml' block:

```python
datasetId = kwd.get('datasetId')
filename = kwd.get('filename')
rval = filename

fname = "/Users/myname/Documents/mzIdentMLViewer/galaxy/config/plugins/visualizations/protviewer/static/data/"+datasetId+"_protein.json"
if kwd.get('mode') == 'init':
  if os.path.isfile(fname) == False:
    return subprocess.call(['java', '-jar', '/Users/myname/Documents/mzIdentMLVisualiser/galaxy/tools/mzIdentMLToJSON/mzIdentMLExtractor.jar', filename, datasetId])
  else:
    print "Info: Data loaded from the cache!"
elif kwd.get('mode') == 'sequence':
  dbSequenceId =  kwd.get('dbSequenceId')
  # extract the sequence
  seqEx = SequenceExtractor()
  sequence = seqEx.extract(filename, dbSequenceId)
  rval = sequence
  return sequence
```

Warning: mind your indentation!

3. set your file paths. fname is the filename of your output json file. And also mzIdentMLExtractor.jar is the java library runnning in the background.Set these paths here accoringly.

## Install Galaxy Tool

#### Step 1 - Configure tool

Locate the tool_conf.xml configuration file in <your galaxy directory>/config/ location.
Add these parameters to anyware of  the file under <toolbox> tag:

```XML
<section name="mzIdentMLToJSON" id="mzIdentMLToJSON">
    <tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
</section>
```
#### Step 2 - copy tool

Copy mzIdentMLToJSON folder to your instance(<your galaxy directory>/tools/)
This folder contains 
 1. wrapper(mzIdentMLToJSON.xml) 
 2. Python script(mzIdentMLToJSON.py)
 3. and java library(mzIdentMLExtractor.jar) for this tool

Again, you need to change the output file path in mzIdentMLToJSON.py file similar to galaxy plugin

## Other details

If you received any error called "port is already in used", this is nothing to do with plugin. However, workaround for this would be terminating the process and restarting the server:

```bash
# find the process. This will list down all the processes containing name python
ps -fA | grep python

# find which one is related with galaxy.ini and find process id (ps_id)
# terminate process
sudo kill ps_id

# restart galaxy
sudo sh run.sh
```

## Useful links

* http://www.psidev.info/mzidentml
* https://galaxyproject.org


