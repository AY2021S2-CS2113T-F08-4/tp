package seedu.duke;

public class UniTracker {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        Ui.printWelcomeMessage();
        runMainMenu();
    }

    public static void runMainMenu() {
        while (true) {
            Ui.printMainMenu();
            String command = Ui.readCommand();
            try {
                int commandInt = Integer.parseInt(command);

                if (commandInt == 5) {
                    break;
                }

                switch (commandInt) {
                case 1:
                    //moduleInfo
                    break;
                case 2:
                    //helpGraduation
                    break;
                case 3:
                    //manageTask
                    break;
                case 4:
                    //externalLinks
                    Ui.printLinksMessage();
                    int linkCommand = Integer.parseInt(Ui.readCommand());
                    Links link = new Links(linkCommand);
                    link.execute();
                    break;
                default:
                    Ui.printInvalidIntegerMessage();
                }

            } catch (NumberFormatException n) {
                Ui.printInvalidIntegerMessage();
            }
        }
    }
}