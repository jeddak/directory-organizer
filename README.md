# directory-organizer
Simple CLI utility to organize the contents of a directory into subdirectories according to name patterns

## Overview

This utility allows you to organize a set of files into subdirectories, based on simple name-based rules. 
If you regularly need to organize files that adhere to simple, predictable naming conventions e.g. a large number of podcast subscriptions, this utility can save you a lot of time.

The rules are specified in a plain text file, using regular expressions.

When the utility runs, if a file is found that matches one of the specified patterns, if the corresponding subdirectory does not exist, it will be created. Once the directory is found or created, the file is move to that location.

## Configuration

The configuration file is a plain text file containing two space-delimited columns:
* The first column is the name of the directory where you want some files to be moved
* The second column is the regular expression pattern that specifies the files that should be moved to the directory specified in the first column.

For example:

`amer_icons ^americanicons.*mp3$
us_of_anxiety ^anxiety.*mp3$
anthropocene ^S.*mp3$
boingboing ^.*boing-boing.*.mp3$
innovation_hub ^.*WEBMIX.*mp3$
innovation_hub ^IHUB.*mp3$
innovation_hub ^.*FULLSHOW.*mp3$
innovation_hub ^.*FullShow.*mp3$
innovation_hub ^.*FULL.SHOW.*mp3$`

Note that multiple regular expressions may be specified to route different sets of files to the same subdirectory (e.g. `innovation hub` )

## Invocation

`scala -cp /path/to/DirectoryOrganizer.jar DirectoryOrganizer /path/to/dir-org-config-file /path/to/source_dir /path/to/base/dest_dir`

where 
	* `/path/to/dir-org-config-file`  is the location of the configuration file
	* `/path/to/source_dir` is the directory containing the files that you wish to organize
	* `/path/to/base/dest_dir` is the base directory where the subdirectories you specified in the config file are/will be located.

## Behavior


