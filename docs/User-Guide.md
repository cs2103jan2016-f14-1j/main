# About
Now that you know what dotdotdot is [about](../README.md), you can follow this guide to learn how to use dotdotdot effectively.

# Quick Start
1. **Install JDK 1.8 or later**. You can get it [here] (http://www.oracle.com/technetwork/java/javase/downloads/index.html).

2. **Download dotdotdot**: You can download `dotdotdot-GUI.jar` from the latest release: https://github.com/cs2103jan2016-f14-1j/main/releases

3. **Launch dotdotdot**: Double-click on the `dotdotdot-GUI.jar` file to start the application. You will be greeted with a simple interface that has a command bar.

Refer to the 'Feature Details' section below to learn more details of its features.

# Feature Details
## Adding tasks
To add a task without any deadline, use this command.
```
add <TODO>
```

Examples:
* `add receive quest`
* `add do CS2103 tutorial`

To add a normal task, use this command.
```
add <TODO> (at | by | on | to) <date> [@category]
```

`<date>` refers to the date. Dates can be strings like `31Feb` or `2 Mar`. You can assign single-word category(s) to each task by appending `@` to the word.

Examples:
* `add receive quest on 31feb`
* `add do CS2103 tutorial by 2 mar`
* `add buy milk by 15Feb @shopping`
* `add Caroline @21st birthday party on 31feb`

## Edit tasks
To edit the date/deadline of a task, use this command.
```
edit <task_ID#> to <date>
```

`<task_ID#>` is the unique ID of a task.

## Add priority
To add priority levels to task, use this command.
```
set <task_ID#> to <priority#>
```

`<priority#>` is a positive number used to indicate the priority level of a task.

Examples:
* `set 2 to 10`

## Complete tasks
To complete a task, use this command.
```
do <task_ID#>
```

Examples:
* `do receive quest`
* `do 1`

## Delete tasks
To delete task(s), use this command.
```
delete <task_IDs#>
```

You can input several task IDs, separated by spaces.

Examples: 
* `delete 2`
* `delete 44 72 3 49`

## View tasks
To view task by category, use this command.
```
view <category>
```

`<category>` can either be categories assigned to tasks, or the keywords `done` and `not done`.

Examples:
* `view shopping`
* `view done`

## Show help
```
[? | h | help]
```

Shows a help screen containing all commands available.

## Undo previous command
```
[u | undo]
```

Undo the previous command.

# Keyboard Shortcuts

* Undo previous command: <kbd>Ctrl</kbd>+<kbd>Z</kbd>
* Scroll through command history: <kbd>&uarr;</kbd> or <kbd>&darr;</kbd>
