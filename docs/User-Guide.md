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

To add a normal task on a certain date, use this command.
```
add <TODO> (at | by | on | to) <date> [#category]
```

`<date>` can be strings like `31Mar` or `31-01`. You can assign category(s) to each task by prepending `#` to the category.

Examples:
* `add complete CS2103 tutorial on 31mar`
* `add Caroline birthday party #birthday on 31apr`

To add a task with date and time ranges, use this command.
```
add <TODO> from <start date> to <end date> from <start time> to <end time>
```

`<* time>` can be strings like `11pm` or `2300`.

## Edit tasks
To edit the date/deadline of a task, use this command.
```
edit <task_ID#> to <date>
```

`<task_ID#>` is the unique ID of a task.

To edit the description of a task, use this command.
```
edit <task_ID#> to <new task description>
```

## Add priority
To prioritise a task, use this command.
```
mark <task_ID#>
```

Examples:
* `mark 2`

## Complete tasks
To complete task(s), use this command.
```
do <task_ID#>
```

You can input several task IDs, separated by spaces.

Examples:
* `do 2 3 5 7`

## Delete tasks
To delete task(s), use this command.
```
delete <task_IDs#>
```

Examples: 
* `delete 2`
* `delete 2 3 5 7`

## View tasks
To view task by category, use this command.
```
view <category>
```

`<category>` can either be categories assigned to tasks, or the keywords `done` and `not done`.

Examples:
* `view shopping`
* `view done`

## Search tasks
To search for task(s) containing string, use this command.
```
search "<searchword(s)>"
```

`<searchword>` can be any string that you want to search for. Dotdotdot can detect minor typos and return task(s) that contains similar dictionary strings. You can input multiple search words separated by space for more detailed searches.

Examples:
* `search priority 14 apr` (returns prioritised tasks on 14 Apr)
* `search busiest in Apr` (returns day with most tasks in April)
* `search "balolon"` (returns results containing "balloon" as well)
* `search "balolon" 14 Apr no priority #yolo
* `search "cake blue"` (returns results containing strings "cake" and "blue")

## Show help
```
[help]
```

Shows a help screen containing all commands available.

## Undo previous command
```
undo
```

Undo the previous command.

# Keyboard Shortcuts

* Start application: <kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>D</kbd>
* Choose file location: <kbd>Alt</kbd>+<kbd>E</kbd>
