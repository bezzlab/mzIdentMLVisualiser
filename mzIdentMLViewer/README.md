# mzIdentMLViewer

This is an interactive web visualisation plug-in for the mzIdentML file within the Galaxy bioinformatics platform. mzIdentML is the standard format for reporting protein identification results in the context of mass spectrometry.

## Installation

This repository contains web plugin files along with galaxy local installation files. However, it is highly recommended to follow the instructions if you are integrating this plugin to your existing galaxy instance.

#### Step 1 - Install Galaxy Instance

```bash
# Clone the repository
% git clone https://github.com/galaxyproject/galaxy/

# Shift to master branch
% cd galaxy
% git checkout -b master origin/master

# Run the server
% sudo sh run.sh
```
You can find more details about galaxy installation at https://wiki.galaxyproject.org/Admin/GetGalaxy

#### Step 2 - Install Galaxy Visualisation Plugin

* First, you need to make sure you have enabled visualisation plugins on your Galaxy installation. Go to your galaxy.ini file (located in <your galaxy directory>/config/) and search for visualization_plugins_directory setting. The defalut setting is as bellow:

```bash
# Visualizations config directory: where to look for individual visualization plugins.
# The path is relative to the Galaxy root dir. To use an absolute path begin the path
# with '/'.
visualization_plugins_directory = config/plugins/visualizations
```
For more details: https://wiki.galaxyproject.org/VisualizationsRegistry

* Secondly, copy entire protviewer folder located in galaxy/config/plugins/visualizations in the repository to <your galaxy directory>/config/plugins/visualizations folder
* Thirdly, go to galaxy/lib/galaxy/webapps/galaxy/api/ repository location. copy following files to exact location of your instance:
  * MzIdentMLHandler.py
  * MzIdentMLHandler.pyc
  * SequenceExtractor.py
  * SequenceExtractor.pyc
* Finally, There is a file called datasets.py in the same location(galaxy/lib/galaxy/webapps/galaxy/api/). Import these modules first:

```python
from SequenceExtractor import SequenceExtractor
import os.path
import subprocess
```

* There, paste following code inside Class DatasetsController -> method show -> inside elif data_type == 'mzidentml' block:

```python
datasetId = kwd.get('datasetId')
filename = kwd.get('filename')
rval = filename

fname = "/Users/sureshhewapathirana/Documents/Projects/ResearchProject/mzIdentMLViewer/galaxy/config/plugins/visualizations/protviewer/static/data/"+datasetId+"_protein.json"
if kwd.get('mode') == 'init':
  if os.path.isfile(fname) == False:
    return subprocess.call(['java', '-jar', '/Users/sureshhewapathirana/Documents/Projects/ResearchProject/mzIdentMLExtractor/dist/mzidParser.jar', filename, datasetId])
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

fname is the filename of your output json file. And also mzidParser.jar is the java library runnning in the background.
Set these paths accoringly.

## Install Galaxy Tool

#### Step 1 - Configure tool

Locate the tool_conf.xml configuration file in <your galaxy directory>/config/ location.
Add these parameters to anyware of  the file under <toolbox> tag
```XML
<section name="mzIdentMLToJSON" id="mzIdentMLToJSON">
    <tool file="mzIdentMLToJSON/mzIdentMLToJSON.xml" />
</section>
```
#### Step 2 - copy tool

Copy mzIdentMLToJSON tool located in galaxy/tools/mzIdentMLToJSON in the repository to the same location of your galaxy instance(<your galaxy directory>/tools/mzIdentMLToJSON)

Again, you need to change the output file path in mzIdentMLToJSON.py file similar to galaxy plugin

## Other details

If you received any error called "port is already in used", this is nothing to do with plugin. However you can terminate the process and restart the server.

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


