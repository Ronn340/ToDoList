param (
    [string]$command,
    [string]$arg1,
    [string]$arg2
)

# Define path to your compiled Java classes
$javaClassPath = "C:\Users\ronra\Documents\JavaCode\helloWorld"

switch ($command) {
    "ls" {
        java -cp $javaClassPath Main list
    }
    "arc" {
        java -cp $javaClassPath Main archive
    }
    "add" {
        java -cp $javaClassPath Main add "$arg1" "$arg2"
    }
    "rm" {
        java -cp $javaClassPath Main rm "$arg1"
    }
    "clear" {
        java -cp $javaClassPath Main clear
    }
    "update" {
        java -cp $javaClassPath Main update
    }
    default {
        Write-Output "Usage:"
        Write-Output "  task ls                      - List tasks"
        Write-Output "  task arc                     - List overdue tasks"
        Write-Output "  task add [desc] [date]       - Add new task"
        Write-Output "  task rm [id]                 - Remove task by ID"
	Write-Output "  task clear                   - Clear all tasks"
        Write-Output "  task update                  - Update overdue tasks"

    }
}
