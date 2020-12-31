# KeyMapper
Maps key-to-key &amp; mouse interactions for statistical analysis and reproduction

## Usage ##
Simply run and press **End** key to finish the task.

## Purpose ##
Everyone types and clicks in different ways. This application records and stores different metrics of how we use input devices.
This data can be used to create profiles and imitate humanlike behavior in virtual input/automation.

#### Click interval ####
Records the duration of mouse events.

#### Key map ####
Records the time interval between pressing two keys.
The time interval tends to vary based on physical distance as well as how frequently we use said keys.

#### Key overlap ####
Maps negative time difference when holding down two or more keys simultaneously.

## Output ##
The data is output to a json file defined by **Main.jsonFilename**

#### clickMap #### 
Stored in intervals of 16 milliseconds starting from 0 milliseconds. **Main.clickCutoff** defines the number of indices to store.
<br />To calculate the time in milliseconds for each click entry, use ***index * 16***.

#### characterMap ####
Stored in intervals of 16 milliseconds. <br />The first indices are negative values dedicated to key overlap. **Main.negativeEntries** defines the number of indices for negative values. **Main.positiveEntries** defines the remaining number of indices. <br />To calculate the time in milliseconds for each key entry, use ***(index - Main.negativeEntries) * 16***.

## How ##
Uses JNativeHook to create low-level hooks for the system event queue.
<br />Google's GSON is used for output.
