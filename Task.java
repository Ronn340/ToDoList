import java.time.LocalDate;

public class Task implements Comparable<Task>{

    int id;
    String description;
    LocalDate due;

    Task(String num, String desc, String date){
        this.id = Integer.parseInt(num);
        this.description = desc;
        this.due = LocalDate.parse(date);
    }

    void printTask() {
        System.out.printf("%10d %-5s %10s %-5s %10s\n", id, " ", description, " ", due);
    }

    @Override
    public int compareTo(Task other) {
        return this.due.compareTo(other.due);
    }
}
