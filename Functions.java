import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class Functions {
    

    public static final Scanner userInput = new Scanner(System.in);

    /* Reading txt file */
    public static PriorityQueue<Task> readFile(String fileName){

        PriorityQueue<Task> tasks = new PriorityQueue<>();
        try {
            File textFile = new File(fileName);
            Scanner fileInput = new Scanner(textFile);

            while(fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] data = line.split(", ");
                Task taskRead = new Task(data[0], data[1], data[2]);
                tasks.add(taskRead);
            }
            fileInput.close();

        } catch (FileNotFoundException e) {
            System.out.print("Cannot open\n");
            return null;
        }
        return tasks;
    }

    public static void writeFile(String fileName, PriorityQueue<Task> tasks) {
        try {
            FileWriter file = new FileWriter(fileName);
            int taskCount = 0;
            while (!tasks.isEmpty()) {
                Task t = tasks.poll();
                file.write(++taskCount + ", " + t.description + ", " + t.due + "\n");
            }
            file.close();
        } catch (IOException e) {
            System.out.print("Cannot write\n");
        }
    }

    /* Printing all tasks (after read) */
    public static void printTasks(PriorityQueue<Task> pq) {
        System.out.printf("%5s %20s %-5s %10s %-5s %11s\n", "Id", "Task", " ", "Due date", " ", "Days left");
        System.out.print("-----------------------------------------------------------------------------------\n");

        Iterator<Task> pqIterator = pq.iterator();
        LocalDate currentDate = LocalDate.now();

        while (pqIterator.hasNext()) {
            Task current = pqIterator.next();
            long daysLeft = ChronoUnit.DAYS.between(currentDate, current.due);
            if (daysLeft == 0)
                System.out.printf("%5d %20s %-5s %10s %-5s %10s\n", current.id, current.description, " ", current.due, " ",  "Today");
            else
                System.out.printf("%5d %20s %-5s %10s %-5s %10s\n", current.id, current.description, " ", current.due, " ", daysLeft + "d");
        }
    }


    public static void printArchive(PriorityQueue<Task> pq) {
        System.out.printf("%5s %20s %-5s %10s %-5s %11s\n", "Id", "Task", " ", "Due date", " ", "Days Ago");
        System.out.print("-----------------------------------------------------------------------------------\n");

        Iterator<Task> pqIterator = pq.iterator();
        LocalDate currentDate = LocalDate.now();

        while (pqIterator.hasNext()) {
            Task current = pqIterator.next();
            long daysAgo = ChronoUnit.DAYS.between(currentDate, current.due) * -1;
            System.out.printf("%5d %20s %-5s %10s %-5s %10s\n", current.id, current.description, " ", current.due, " ", daysAgo + "d ago");
        }
    }


    public static void insertTask(String fileName, String inputDesc, String inputDate) {
            String dateString = translateToDate(inputDate);
            LocalDate dueDateObj = tryParseDate(dateString);
            if (dueDateObj == null) {
                    return;
            }

            PriorityQueue<Task> dataFile = readFile(fileName);
            String totalTasks = Integer.toString(dataFile.size());
            Task newTask = new Task(totalTasks, inputDesc, dateString);
            dataFile.add(newTask);
            writeFile(fileName, dataFile);
        }

    public static void deleteTask(String fileName, int id) {
        PriorityQueue<Task> dataFile= readFile(fileName);
        Task deleting = null;
        for (Task t : dataFile) {
            if (t.id == id){
                deleting = t;
                break;
            }
        }
        if (deleting != null)
            dataFile.remove(deleting);
        writeFile(fileName, dataFile);
    }

    public static void clear(String fileName) {
        try {
            FileWriter eraser = new FileWriter(fileName);
            eraser.write("");
            eraser.close();
        } catch (IOException e) {
            System.out.println("Could not open");
        }
    }

    public static void updateTasks(String origin, String archive) {
        PriorityQueue<Task> finishedTasks = new PriorityQueue<>();
        PriorityQueue<Task> unFinishedTasks = new PriorityQueue<>();
        LocalDate today = LocalDate.now();
        try {
            File textFile = new File(origin);
            Scanner fileInput = new Scanner(textFile);

            while(fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] data = line.split(", ");
                Task taskRead = new Task(data[0], data[1], data[2]);
                if (taskRead.due.isBefore(today)) {
                    finishedTasks.add(taskRead);
                } else {
                    unFinishedTasks.add(taskRead);
                }
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            System.out.print("Cannot open\n");
        }

        try {
            File textFile = new File(archive);
            Scanner fileInput = new Scanner(textFile);

            while(fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] data = line.split(", ");
                Task taskRead = new Task(data[0], data[1], data[2]);
                finishedTasks.add(taskRead);
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            System.out.print("Cannot open\n");
        }
        writeFile(archive, finishedTasks);
        writeFile(origin, unFinishedTasks);
    }

    /* Date operations */

    public static String translateToDate(String userStr) {

        String[] monthsArr = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };
        Map<String, Integer> monthNum = new HashMap<>();
            monthNum.put("January", 1);
            monthNum.put("February", 2);
            monthNum.put("March", 3);
            monthNum.put("April", 4);
            monthNum.put("May", 5);
            monthNum.put("June", 6);
            monthNum.put("July", 7);
            monthNum.put("August", 8);
            monthNum.put("September", 9);
            monthNum.put("October", 10);
            monthNum.put("November", 11);
            monthNum.put("December", 12);

        for (int i = 0; i < monthsArr.length; i++) {
            monthNum.put(monthsArr[i].substring(0, 3), i + 1); // Jan, Feb...
            monthNum.put(monthsArr[i].toLowerCase(), i + 1);
            monthNum.put(monthsArr[i].substring(0, 3).toLowerCase(), i + 1); // jan, feb...
        }
        Map<Integer, String> months = new HashMap<>();
            months.put(1, "January");
            months.put(2, "February");
            months.put(3, "March");
            months.put(4, "April");
            months.put(5, "May");
            months.put(6, "June");
            months.put(7, "July");
            months.put(8, "August");
            months.put(9, "September");
            months.put(10, "October");
            months.put(11, "November");
            months.put(12, "December");

        int date = 0, month = 0, year = 0;

        
        String[] strArray = userStr.split("[ /-]+");
        
        for (int i = 0; i < strArray.length; i++) 
            strArray[i] = strArray[i].replaceFirst("^0+(?!$)", "");

        ArrayList<Integer> unknown = new ArrayList<>();
        for(int i = 0; i < strArray.length; i++)
            unknown.add(i);

        /* String "month" to number generate */
        Iterator<Integer> it = unknown.iterator();
        while (it.hasNext()){
            int index = it.next();
            String value = strArray[index];
            Integer maybeMonth = monthNum.get(value);

            if (maybeMonth != null) {
                month = maybeMonth;
                it.remove();
                break;
            }

            try {
                int x = Integer.parseInt(strArray[index]);
                if (x > 12 && x < 32){
                    date = x;
                    it.remove();
                    break;
                }
                else if (x > 1000){
                    year = x;
                    it.remove();
                    break;
                }
            } catch (NumberFormatException e) {
                it.remove();
            }
        }

        if (month == 0) {
            month = Integer.parseInt(strArray[0]);
        }

        if (month != 0 && date == 0) {
            date = Integer.parseInt(strArray[1]);
        }
        /* Generate current year if not typed ()*/
        if (year == 0) {
            year = LocalDate.now().getYear();
        }

        /* Generate dd/mm or mm/dd possibility*/
        if (unknown.size() > 1) {
            int first = Integer.parseInt(strArray[unknown.get(0)]);
            int second = Integer.parseInt(strArray[unknown.get(1)]);

            if (first != second) {
                System.out.print("Specify date: \n");
                System.out.println("1: " + months.get(first) + " " +  second);
                System.out.println("or");
                System.out.println("2: " + months.get(second) + " " +  first);

                int answer = requestInt("Option");
                if (answer == 1) {
                    month = first;
                    date = second;
                } else {
                    month = second;
                    date = first;
                }
            } else {
                month = first;
                date = first;
            }
        }
        
        /* RETURN invalid date for re-entry */
        if (date == 0 || month == 0) {
            return "9999-99-99";
        }

        /* Prepare leading zero for date formate yyyy-mm-dd */
        String monthStr, dateStr;
        if (month < 10)
            monthStr = "0" + month;
        else 
            monthStr = Integer.toString(month);

        if (date < 10)
            dateStr = "0" + date;
        else 
            dateStr = Integer.toString(date);

        String generatedString = year + "-" + monthStr + "-" + dateStr;
        LocalDate current = LocalDate.now();
        LocalDate generated = LocalDate.parse(generatedString);

        /* If Date/month has passed, assume its next year */
        if (generated.isBefore(current)) {
            year = LocalDate.now().getYear() + 1;
        } 

        generatedString = year + "-" + monthStr + "-" + dateStr;
        return generatedString;
    }

    public static LocalDate tryParseDate(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(input, formatter);
        } 
        catch (DateTimeParseException e) {
            System.out.println("Invalid date: " + input + "\n");
            return null; 
        }
    }

    /* Requesting inputs + validators */
    public static boolean isEmpty(String str){
            return str == null || str.trim().isEmpty();
    }

    public static String requestString(String message) {
        String value = "";
        while (isEmpty(value)){
            System.out.print(message + ": ");
            value = userInput.nextLine();
        }
        return value;
    }

    public static int requestInt(String message) { 
        System.out.print(message + ": ");
        int value = userInput.nextInt();
        userInput.nextLine();
        return value;
    }
}
