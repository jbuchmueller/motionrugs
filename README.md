# MotionRugs

MotionRugs is a novel dense pixel display technique for the visualization of collective movement. The technique makes use of spatial linearization strategies to create a condensed, static and two-dimensional representation of spatio-temporal datasets. MotionRugs are created by sequentially transforming the positions of movers in a spatio-temporal dataset using a spatial linearization function such as space-filling curves. For every instant of time available (1), we linearize the positions of all movers at this time using a linearization function (2). Finally, we color each linearized result, which we call a slice, according to a selected feature (e.g. speed of a mover) and aggregate these slices into a MotionRug (3).

![alt text](https://i.lensdump.com/i/A1Z6VK.png)

Read our publication with details here: https://bib.dbvis.de/publications/details/764

Quick video overview on MotionRugs available here: https://youtu.be/xSewCmdgN40

With the code provided in this repository, you can create your own MotionRugs! Just be sure to read the following comments first.

## General Information

* MotionRugs is a JAVA Maven project
* Currently, it consists of the algorithms to reorder movement records, create a MotionRug out of it and display and store the images 
* Some restrictions on the input currently exist, see below
* If you clone the repository and execute the MainGUI.java class, a rudimentary GUI will open allowing you to create new MotionRugs from the selected dataset, feature and linearization strategy. Currently, only one colormap is supported and automatically applied to the data. 
* Whenever you press the "add rug" button, a MotionRug will be created and shown to you in the GUI. At the same time, an image of it will be stored in the main folder of the project / where you executed the code 
* You can actually try and implement your own linearization strategies! All you have to do is implement the Strategy interface and add an instantiation of it to the MainGUI code (explained in Javadoc)

## Data Input
Currently, MotionRugs takes only movement data as csv input which satisfies the below criteria. For you to start off, an exemplary data file with 2000 frames has been included in the /data-folder.
* The number of movers throughout the input cannot change
* The temporal sampling should be equal (e.g. 30 fps)
* For each frame in the dataset, a data point has to exist for all movers. Example: If you have 150 movers, you need to have 150 entries per frame with position and features
* Currently, only cartesian coordinates are supported. 

The csv input can be placed in the data folder and will be loaded upon execution. The csv file *must* contain the following fields: 

* frame,id,x,y,f1,...,fn
* frame: Sequential id of the frame (time). Needs to be an integer beginning at 0 sequentially without gaps
* id: mover id. Integer starting at 0, needs to be gapless sequential
* x: The x coordinate of the mover at this time as decimal, in a cartesian coordinate system.
* y: The y coordinate of the mover at this time as decimal, in a cartesian coordinate system.
* f1...fn: Arbitrary number of features encoded as double. All features need to be decimal with a . as decimal separator

## HELP! It's broken | doesn't work | won't load my data

Just tweet to me @motionrugs or write an email to motionrugs@dbvis.inf.uni-konstanz.de and we'll see whether we can work this out. Or try to fix it yourself and send a pull request :) 

