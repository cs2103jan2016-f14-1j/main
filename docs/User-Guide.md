# About
Now that you know what dotdotdot is [about](../README.md), you can follow this guide to learn how to use dotdotdot effectively.

# Quick Start
1. **Install JDK 1.8 or later**. You can get it [here] (http://www.oracle.com/technetwork/java/javase/downloads/index.html).

2. **Download dotdotdot**: You can then download `dotdotdot-GUI.jar` from the latest release: https://github.com/cs2103jan2016-f14-1j/main/releases

3. **Launch dotdotdot** Double-click on the `dotdotdot-GUI.jar` file to start the application. You will be greeted with a simple interface that has a command bar.

Refer to the 'Feature Details' section below to learn more details of its features.

# Feature Details
## Adding tasks
To add a task without deadline, use this command.

`add <TODO>`

To add a normal task, use this command.

`add <TODO> (at | by | on | to) <datetime>`

`<datetime>` refers to either a date or a time. 

Examples:
* `add receive quest`
* `add do CS2103 tutorial`

## Edit tasks
To edit a task, use this command.

`edit <task_ID>`

`<task_ID>` is the unique ID of a task.

## Complete tasks
To complete a task, use this command.

`do <TODO>`

`<TODO>` can either be the full name of the task, or the task ID.

Examples:
* `do receive quest`
* `do 1`

## Show help

`[? | help]`

Shows a help screen containing all commands available.


