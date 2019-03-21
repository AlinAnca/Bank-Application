import java.util.*;

public class MainApp {

    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Hello! Please insert a file: ");
        String fileName;

        DataFile dataFile = new DataFile();
        Set<User> users;

        do {
            fileName = keyboard.nextLine();

            dataFile.setFileName(fileName);
            users = dataFile.readFile();
            System.out.println(dataFile.getMessage());

            if(!dataFile.isCompleted()) {
                System.out.println("Please insert a file again:");
            }

        }while(!dataFile.isCompleted());

        int n = 0;
        while( n != 3 ) {
            if (n != 1) {
                System.out.println("\nPlease choose an option: ");
                System.out.println("1 - Login");
                //   System.out.println("2 - Logout");
                System.out.println("3 - Exit");
                n = keyboard.nextInt();
                System.out.print("\n");
            }

            if (n != 2 && n != 3)
            {
                System.out.print("Please insert the username: ");
                String inpUser = keyboard.next();
                System.out.print("Please insert the password: ");
                String inpPass = keyboard.next();

                for (User u : users) {
                    if (inpUser.equals(u.getUsername()) && inpPass.equals(u.getPassword())) {
                        System.out.println("\nWelcome " + u.getUsername() + "!");
                        System.out.println("Your options are: ");
                        System.out.println("2 - Logout");
                        System.out.println("3 - Exit");
                        n = keyboard.nextInt();
                    }
                }

                if (n == 2) {
                    System.out.println("Successfully logout.");
                }

                if (n != 2 && n != 3) {
                    System.out.println("Wrong username or password! \n");
                    n = 1;
                }
            }
        }
    }
}
