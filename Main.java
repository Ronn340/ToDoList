public class Main {

	public static void main(String[] args){ 
		if (args.length == 0) {
			System.out.println("No command given");
			return;
		}

		String command = args[0];
		String fileName = "C:\\Users\\ronra\\My Drive\\SharedFolder\\TaskData\\Tasks.txt";
		String archives = "C:\\Users\\ronra\\My Drive\\SharedFolder\\TaskData\\TaskArchive.txt";

		switch (command) {
			case "list":
				Functions.printTasks(Functions.readFile(fileName));
				break;
			case "archive":
				Functions.printArchive(Functions.readFile(archives));
				break;
			case "add":
				if (args.length < 3) {
					System.out.println("Usage: add [description] [due-date]");
					return;
				}
				String description = args[1];
				String due = args[2];
				Functions.insertTask(fileName, description, due);
				break;
			case "rm":
				if (args.length < 2) {
					System.out.println("Usage: rm [id]");
					return;
				}
				int id = Integer.parseInt(args[1]);
				Functions.deleteTask(fileName, id);
				break;
			case "clear":
				Functions.clear(fileName);
				break;
			case "update":
				Functions.updateTasks(fileName, archives);
				break;
			
			default:
				System.out.println("Unknown command.");
		}
	}
}