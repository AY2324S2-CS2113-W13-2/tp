# CLI-nton Task Management User Guide

## Introduction

This user guide provides comprehensive instructions on how to use CLI-nton, from getting started to using advanced features. Whether you're a new user or an experienced one, this guide is designed to help you get the most out of CLI-nton.

Read on to learn how to manage your tasks efficiently with CLI-nton!

### Table of Contents
* [Table of Contents](#table-of-contents)
* [Quick Start](#quick-start)
* [Features](#features)
  * [Moving to Next Week or Month: `next`](#moving-to-next-week-or-month-next)
  * [Moving to Previous Week or Month: `prev`](#moving-to-previous-week-or-month-prev)
  * [Switching to Month View: `month`](#switching-to-month-view-month)
  * [Switching to Week View: `week`](#switching-to-week-view-week)
  * [Adding a New Task: `add`](#adding-a-new-task-add)
    * [Adding a `Todo`](#adding-a-todo)
    * [Adding a `Deadline`](#adding-a-deadline)
    * [Adding an `Event`](#adding-an-event)
    * [Notes to users](#notes-to-users)
  * [Updating a Task Description: `update`](#updating-a-task-description-update)
    * [Updating a `Todo`:](#updating-a-todo)
    * [Updating a `Deadline`:](#updating-a-deadline)
    * [Updating an `Event`:](#updating-an-event)
  * [Deleting a Task: `delete`](#deleting-a-task-delete)
  * [Marking a Task as Complete or Incomplete: `mark`](#marking-a-task-as-complete-or-incomplete-mark)
  * [Setting Priority Level for a Task: `priority`](#setting-priority-level-for-a-task-priority)
  * [Quitting the Application: `quit`](#quitting-the-application-quit)
  * [ICS Exporting and Importing: `ics`](#ics-exporting-and-importing-ics-coming-in-v21)
* [FAQ](#faq)
* [Command Summary](#command-summary)


## Quick Start

To quickly get started with CLI-nton, follow these simple steps:

1. Ensure that you have Java 11 or above installed on your system.
2. Download the latest version of `CLI-nton` from [here](https://github.com/AY2324S2-CS2113-W13-2/tp/releases).
3. Open a command terminal and navigate to the directory where you downloaded CLI-nton.
4. Run the application using the command `java -jar clinton.jar`.
5. You're all set to start managing your tasks efficiently!

## Features

CLI-nton offers a variety of features to streamline your task management process:

Here are the sections for the User Guide covering the `next`, `prev`, `month`, `week`, `add`, `update`, `delete`, `priority` and `quit` commands:


### Moving to Next Week or Month: `next`

The `next` command allows you to move to the next week or next month, depending on whether you are currently in the week or month view.

**Usage:**

```
next
```

**Examples:**

If you are currently in the **Week view:**

![Example console outputs before using next command for WeekView](images/WeekView-Next-Before-Example.png)

The application will move to the following week view, displaying the following week.

![Example console outputs after using next command for WeekView](images/WeekView-Next-After-Example.png)

If you are currently in the **Month view:**

![Example console outputs before using next command for MonthView](images/MonthView-Next-Before-Example.png)

The application will move to the next month view, displaying the following month.

![Example console outputs after using next command for MonthView](images/MonthView-Next-After-Example.png)

### Moving to Previous Week or Month: `prev`

The `prev` command allows you to move to the previous week or previous month, depending on whether you are currently in the week or month view.

**Usage:**

```
prev
```

**Example:**

If you are currently in the **Week view:**

![Example console outputs before using prev command for WeekView](images/WeekView-Next-After-Example.png)

The application will move to the previous week view, displaying the week prior.

![Example console outputs after using prev command for WeekView](images/WeekView-Next-Before-Example.png)

If you are currently in the **Month view:**

![Example console outputs before using prev command for MonthView](images/MonthView-Next-After-Example.png)

The application will move to the previous month view, displaying the month prior.

![Example console outputs after using prev command for MonthView](images/MonthView-Next-Before-Example.png)

### Switching to Month View: `month`

The `month` command allows you to switch to the month view from any other view.

**Usage:**

```
month
```

**Example:**

If you are currently in the week view:

![Example console outputs before changing WeekView to MonthView](images/Week-To-Month-View-Before-Example.png)

The application will switch to the month view from week view and current month will be displayed.

![Example console outputs after changing WeekView to MonthView](images/Week-To-Month-View-After-Example.png)

### Switching to Week View: `week`

The `week` command allows you to switch to the week view from any other view.

**Usage:**

```
week
```

**Example:**

If you are currently in the **Month view:**

![MonthView Display](images/Week-To-Month-View-After-Example.png)

The application will switch to the **Week view.**

![Example console outputs after changing MonthView to WeekView](images/Month-To-Week-View-After-Example.png)

### Adding a New Task: `add`
> Note that there are 3 different types of tasks: Todos, Events and Deadlines.
> This section details how to add all 3 types of tasks to your calendar.

Adds a new task to the calendar.

Format: `add, <day number>, <taskType>, <taskDescription>`

You will be prompted on further inputs based on your desired task type.

#### Adding a `Todo`
> Todos are regular tasks with no time limit!

Format to add a `Todo`: `add, <day number>, T, <todo description>`

Your Todo Task is now added to your calendar on the selected day (of whichever week/month you are viewing)!

Here's what that would look like in week view:

![Todo Adding Example Console Inputs and Outputs](images/Todo-Example.png)

Here's what that would look like in month view:

![AddTodoInMonthView.png](images/AddTodoInMonthView.png)

The Todos are represented by blue parenthesis, and they are empty indicating  the todo is not marked.

#### Adding a `Deadline`
> Deadlines are tasks with an additional date/time of completion!

Format to add a `Deadline`: `add, <day number>, D, <deadline description>`

You will then be asked to provide the deadline (date and time) for this task in the format `"DD/MM/YYYY HHMM"`.

`Enter the deadline date and time of this task, separated by a space: `

Example format: `06/04/2024 1800`

Your Deadline Task is now added to your calendar on the selected day (of whichever week/month you are viewing)!

Here's what that would look like in week view:

![Deadline Adding Example Console Inputs and Outputs](images/Deadline-Example.png)

Here's what that would look like in month view:

![AddDeadlineInMonthView.png](images/AddDeadlineInMonthView.png)

The Deadlines are represented by yellow parenthesis, and they are empty indicating  the deadline is not marked.

#### Adding an `Event`
> Events are tasks with a start and end date/time!

Format to add an `Event`: `add, <day number>, E, <event description here>`

You will then be asked to provide the start and end dates/times for this task in the format `"DD/MM/YYYY HHMM"`.

`Enter the start date of this task, along with the start time separated by a space: `

Example format: `05/04/2024 1200`

`Enter the end date of this task, along with the end time separated by a space: `

Example format: `07/05/2024 2000`

Your Event Task is now added to your calendar on the selected day (of whichever week/month you are viewing)!

Here's what that would look like in week view:

![Event Adding Example Console Inputs and Outputs](images/Event-Example.png)

Here's what that would look like in month view:

![AddEventInMonthView.png](images/AddEventInMonthView.png)

The Events are represented by red parenthesis, and they are empty indicating the event is not marked.

#### **Notes to users**
> Commas`,` and pipes`|` are used in our application as command delimiters as well as for file storage purposes.
> Please avoid using these in your task descriptions as it could lead to parsing errors or storage issues.
>
> You have been **informed**!

### Updating a Task Description: `update`

>You can update the description of a task using the `update` command. This command allows you to change the description of a task on a specific day for todos, or additionally also change the date and time for deadlines and events.

Format to update a task Description: `update, <day number>, <task index>, <new description>`

You will be prompted on further inputs based on the task type you seek to update.

#### Updating a `Todo`:

```
update, <day number>, <task index>, <new todo description>
```

Your Todo will now be updated to `"<new todo description>"` on the 31st (of whichever week/month you are viewing)!
Here's a sample of what that would look like:

![Todo Update Example](images/Update-Todo-Example.png)

After updating the todo, the task will be displayed as follows:

![Todo Updated Example](images/Update-Todo-Example-After.png)

#### Updating a `Deadline`:

To update a Deadline, follow this format:
```
update, <day number>, <task index>, <new deadline description>
```

Your Deadline Task will be updated to `"<new deadline description>"` on the 31st! You will then be prompted to provide the 
updated deadline date and time, if desired.

Here's a visual representation of the update process:

![Example console inputs for updating a Deadline Task](images/Update-Deadline-Example.png)

After the update, the task will be displayed as follows:

![Example calendar display after updating a Deadline Task](images/Update-Deadline-Example_After.png)

#### Updating an `Event`:

To update an Event, use the following format:
```
update, <day number>, <task index>, <new event description>
```

Your Event Task will be updated to `"<new event description>"` on the 1st! You will then be prompted to provide the updated 
start and end dates/times for the event.

Here's an example illustrating the update process:

![Example console inputs for updating an Event Task](images/Updated-Event-Example.png)

After the update, the task will be displayed as follows:

![Example calendar display after updating an Event Task](images/Updated-Event-Example-After.png)

By following these instructions, you can efficiently update task descriptions in your calendar, ensuring accurate and 
up-to-date scheduling.


### Deleting a Task: `delete`

Deletes a task.

Format: `delete, <day number>, <taskIndex>`
- Deletes the task at the specified index on the given day
- Shows an error message if the task index does not exist

Example of usage:
- Deletes task 2 on Wednesday

```
//deletes task 2 on day 3
delete, 3, 2

//deletes task 1 on day 5
delete, 5, 1
```
- Shows error message if task does not exist

```
//attempts to delete task 1 on day 5, but the task does not exist
delete, 5, 1
```
Output: 
![Invalid Delete Example Console Inputs and Outputs](images/Invalid-Delete-Example.png)

### Marking a Task as Complete or Incomplete: `mark`

Marks a task as complete or not complete.

Format: `mark, <day number>, <taskIndex>`
- Marks task as complete if it is incomplete
- Marks task as incomplete if it is complete

Example of usage:

```
// marks task 3 on day 2 as complete
mark, 2, 3

// marks task 1 on day 4 as incomplete
mark, 4, 1
```

### Setting Priority Level for a Task: `priority`

Sets priority level for a task.

Format: `priority, <day number>, <taskIndex>, <priority>`
- Priority levels: `H` (high), `M` (medium), `L` (low)
- Default priority level: `L`
- Setting priority level will update any existing priority level for the task to the new level

Example of usage:

```
// sets priority level HIGH for task 1 on day 5
priority, 5, 1, H

// sets priority level MEDIUM for task 2 on day 3
priority, 3, 2, M
```

### Quitting the Application: `quit`

The `quit` command allows you to exit the CLI-nton application.

**Usage:**

```
quit
```

**Example:**

```
Week View: 21/04/2024 - 27/04/2024
+---------------+---------------+---------------+---------------+---------------+---------------+---------------+
|Sunday         |Monday         |Tuesday        |Wednesday      |Thursday       |Friday         |Saturday       |
|21/04/2024     |22/04/2024     |23/04/2024     |24/04/2024     |25/04/2024     |26/04/2024     |27/04/2024     |
+---------------+---------------+---------------+---------------+---------------+---------------+---------------+
+---------------+---------------+---------------+---------------+---------------+---------------+---------------+

quit
Exiting Calendar...
```
The application will exit after displaying a goodbye message.

### ICS Exporting and Importing: `ics` `[coming in future versions]`
Exporting and importing tasks to and from an ICS file.

Format: `ics <export> <filename>` or `ics <import> <filename>`
- Exporting tasks to an ICS file will create a new ICS file with the specified filename
- Importing tasks from an ICS file will add the tasks from the ICS file to the current task list

Example of usage:

```
// exports tasks to an ICS file named "tasks.ics" into the current working directory
ics, export, tasks.ics

// imports tasks from an ICS file named "tasks.ics" in the current working directory
ics, import, tasks.ics
```

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: You can transfer your data to another computer by copying the data file saved in the CLI-nton application directory. The data file is typically named `clintonData.txt` and contains all your tasks. Simply copy this file to the same location on the other computer to transfer your tasks.

**Q**: When adding an Event task, I entered `add,1,E,Event 1` to add an event on the first day of the month, but after that, I was prompted to enter the start date again. Why is that?

**A**: Not to worry! Our program registers the start date as the date you keyed into the original command, i.e. the first day of the month. The subsequent prompt for the start date is for formatting purposes and will not affect the actual date of the event.

**Q**: I have an event lasting from 15 Apr to 19 Apr within a week. Why is it that `free, 16` shows that the day is free?

**A**: For events which stretch over multiple days, we only consider the original day of the event when calculating free times. In this case, the event starts on the 15th and ends on the 19th, so the 16th, 17th, 18th and 19th are considered free days. Our advise: add events with their corresponding timings for each day of the event to get a more accurate representation of your schedule.

**Q**: Does the scheduler account for clashes in event timings?

**A**: No. This is to accomodate for users who may have overlapping events or tasks. We recommend users to manually check for clashes and adjust their schedules accordingly.

## Command Summary

For a quick reference, here's a summary of available commands:

- Move to next week or month `next`
- Move to previous week or month `prev`
- Update task description `update, <day number>, <taskIndex>, <newDescription>`
- Add new task `add, <day number>, <taskType>, <taskDescription>`
- Delete task `delete, <day number>, <taskIndex>`
- Mark task as complete or not complete `mark, <day number>, <taskIndex>`
- Set priority level for task `priority, <day number>, <taskIndex>, <priority>`
- Find free times in a day `free, <day number>`
- Switch to month view `month`
- Switch to week view `week`
- Quit the application `quit`

Here's a summary of task types:

- Todo (`T`): A basic task with no start or end date / times.
- Deadline (`D`): A task that has a date / time to complete by.
- Event (`E`): A task that has a start and end date / time.

Tasks can be classified in the following ways:
- marked as HIGH (`H`), MEDIUM (`M`) or LOW (`L`) priority. Default level is LOW.
- marked as COMPLETE (`X`) or INCOMPLETE (`O`). Default is INCOMPLETE.

Start managing your tasks efficiently with CLI-nton today!
