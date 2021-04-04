package seedu.duke;

import seedu.duke.capsimulator.ModuleGradeEnum;
import seedu.duke.link.Links;
import seedu.duke.task.Assignment;
import seedu.duke.task.FinalExam;
import seedu.duke.task.Midterm;
import seedu.duke.task.Task;
import seedu.duke.task.TaskManager;
import seedu.duke.task.command.DeleteTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModuleInfo {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static ArrayList<Module> modules = new ArrayList<>();
    private static final String EMPTY_REVIEW_MESSAGE = "You have not reviewed this module yet.";

    private static final String TASK_TYPE = "[Task]";
    private static final String ASSIGNMENT_TYPE = "[Assignment]";
    private static final String MIDTERM_TYPE = "[Midterm]";
    private static final String FINAL_EXAM_TYPE = "[Final Exam]";

    public ModuleInfo() {
    }

    public static void moduleInfoMenu() {
        while (true) {
            Ui.printModuleInfoMessage();
            String command = Ui.readCommand();
            try {
                int taskNumber = Integer.parseInt(command);
                if (taskNumber == 15) {
                    Ui.printReturnToMainMenuMessage();
                    break; // exit to Main Menu
                }
                switch (taskNumber) {
                case 1:
                    addNewModule();
                    break;
                case 2:
                    viewAModule();
                    break;
                case 3:
                    viewAllModules();
                    break;
                case 4:
                    deleteModule();
                    break;
                case 5:
                    getComponents();
                    break;
                case 6:
                    addModuleMC();
                    break;
                case 7:
                    addModuleGrade();
                    break;
                case 8:
                    addReview();
                    break;
                case 9:
                    viewAllReviews();
                    break;
                case 10:
                    deleteReview();
                    break;
                case 11:
                    TaskManager.addNewTask();
                    break;
                case 12:
                    TaskManager.deleteTask();
                    break;
                case 13:
                    Links.add();
                    break;
                case 14:
                    Links.delete();
                    break;
                default:
                    Ui.printInvalidIntegerMessage();
                }
            } catch (NumberFormatException n) {
                Ui.printInvalidIntegerMessage();
            }
            try {
                Storage.saveAllFiles();
            } catch (IOException e) {
                System.out.println("modules.txt file could not be auto-saved:(");
            }
            Ui.printReturnToModuleInfoMenuMessage();
        }
    }

    private static void addModuleGrade() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return;
        }
        viewAllModules();
        System.out.println("Please choose which module you would like to assign a grade"
                + " and enter the number:");
        int moduleNumberInt = Ui.readCommandToInt();
        if (moduleNumberInt >= 1 && moduleNumberInt <= modules.size()) {
            moduleNumberInt--;
            System.out.println("Enter the grade for this module: ");
            String moduleGrade = Ui.readCommand();
            if (ModuleGradeEnum.checkGradeExist(moduleGrade)) {
                modules.get(moduleNumberInt).setGrade(moduleGrade.toUpperCase());
            } else {
                System.out.println("Module grade does not exist. ");
            }
        } else {
            Ui.printInvalidIntegerMessage();
        }
    }

    private static void addModuleMC() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return;
        }
        viewAllModules();
        System.out.println(
                "Please choose which module you would like to allocate modular credits (MCs)"
                        + " and enter the number:");
        int moduleNumberInt = Ui.readCommandToInt();
        if (moduleNumberInt >= 1 && moduleNumberInt <= modules.size()) {
            moduleNumberInt--;
            System.out.println("Enter the number of MCs for this module: ");
            int moduleCredits = Ui.readCommandToInt();
            modules.get(moduleNumberInt).setMc(moduleCredits);
        } else {
            Ui.printInvalidIntegerMessage();
        }
    }

    public static void addNewModule() {
        System.out.println("Enter name of the new module:");
        String moduleName = Ui.readCommand();
        for (Module module : modules) {
            if (module.getName().trim().equalsIgnoreCase(moduleName)) {
                System.out.println("Hey, you have already added this module!");
                return;
            }
        }
        moduleName = moduleName.trim().toUpperCase();
        System.out.println("Enter module description:");
        String moduleDescription = Ui.readCommand();
        modules.add(new Module(moduleName, moduleDescription));
        System.out.println("New module added:\n" + moduleName + ":\n" + moduleDescription);
        Ui.printHorizontalLine();
    }

    public static void viewAModule() {
        viewAllModules();
        System.out.println("Which module would you like to view?");
        int moduleNumberInt = Ui.readCommandToInt();
        boolean isValidModuleNumber = (moduleNumberInt >= 1 && moduleNumberInt <= modules.size());

        if (isValidModuleNumber) {
            moduleNumberInt--;
            Module module = modules.get(moduleNumberInt);
            System.out.println(module.toString()); //name, description, review, MCs, grade are printed
            printModuleTaskList(module.getName());
            // add other methods to print other features of a module

            Ui.printHorizontalLine();
        } else {
            Ui.printInvalidIntegerMessage();
        }
    }

    public static void printModuleTaskList(String module) {
        int taskNumber = 1;
        System.out.println("\nThese are your tasks: ");
        for (Task task : TaskManager.tasks) {
            if (!task.getModule().equals(module)) {
                continue;
            }
            System.out.println(taskNumber + ". " + task.getTaskType() + task.toString());
            taskNumber++;
        }
        for (Assignment assignment : TaskManager.assignments) {
            if (!assignment.getModule().equals(module)) {
                continue;
            }
            System.out
                    .println(taskNumber + ". " + assignment.getTaskType() + assignment.toString());
            taskNumber++;
        }
        for (Midterm midterm : TaskManager.midterms) {
            if (!midterm.getModule().equals(module)) {
                continue;
            }
            System.out.println(taskNumber + ". " + midterm.getTaskType() + midterm.toString());
            taskNumber++;
        }
        for (FinalExam finalExam : TaskManager.finalExams) {
            if (!finalExam.getModule().equals(module)) {
                continue;
            }
            System.out.println(taskNumber + ". " + finalExam.getTaskType() + finalExam.toString());
            taskNumber++;
        }
    }


    public static int readYN(String command) {
        if (command.trim().equalsIgnoreCase("N")) {
            return 0;
        } else if (!command.trim().equalsIgnoreCase("Y")) {
            System.out.println("You did not enter a valid letter:(");
            return 2;
        }
        return 1;
    }

    public static boolean viewAllModules() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return false;
        }
        System.out.println("Here are the modules in your Modules List:");
        Ui.printHorizontalLine();
        for (int i = 1; i <= modules.size(); ++i) {
            System.out.println("[" + i + "] --- " + modules.get(i - 1).getName());
        }
        Ui.printHorizontalLine();
        return true;
    }

    public static void addReview() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return;
        }
        viewAllModules();
        System.out.println("Please choose which module you would like to review"
                + " and enter the number:\n");
        int moduleNumberInt = Ui.readCommandToInt();
        moduleNumberInt--;
        boolean isValidInt = checkIfIndexIsWithinBounds(moduleNumberInt);
        if (isValidInt) {
            String review = printAlreadyAddedReviewMessage(modules.get(moduleNumberInt));
            modules.get(moduleNumberInt).setReview(review);
        } else {
            Ui.printInvalidIntegerMessage();
        }
    }

    public static String printAlreadyAddedReviewMessage(Module module) {
        if (!module.getReview().equals(EMPTY_REVIEW_MESSAGE)) {
            System.out.println("You already have added a review:");
            System.out.println(module.getReview());
            System.out.println("Would you like to replace this with another review? [Y/N]");
            logger.log(Level.WARNING, "You will delete your old review. This cannot be undone.");
            String command = Ui.readCommand();
            if (readYN(command) == 0) {
                System.out.println("Okay:) You still have the same review!");
                return module.getReview();
            } else if (readYN(command) == 2) {
                return module.getReview();
            }
            assert readYN(command) == 1 : "readYN(command) should be 1 here";
        }
        System.out.println("After you finish your review, "
                + "type '/end' to finish reviewing.");
        System.out.println("Enter your review for " + module.getName() + " below: ");
        return readReview();
    }

    public static String readReview() {
        StringBuilder review = new StringBuilder();
        while (true) {
            String input = Ui.readCommand();
            review.append(input);
            review.append("\n");
            if (input.contains("/end")) {
                break;
            }
        }
        //drop everything after "/end"
        String reviewString = review.toString().split("/end")[0];
        if (!reviewString.trim().isEmpty()) {
            printReviewAddedMessage(reviewString.trim());
            return reviewString.trim();
        } else {
            System.out.println("You entered an empty review.");
            return EMPTY_REVIEW_MESSAGE;
        }
    }

    public static void printReviewAddedMessage(String review) {
        System.out.println("Woohoo~ Review added:");
        System.out.println(review);
    }

    public static void viewAllReviews() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return;
        }
        for (Module module : modules) {
            System.out.println("For " + module.getName() + ":");
            System.out.println(module.getReview());
            Ui.printHorizontalLine();
        }
    }

    public static void deleteModule() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return;
        }
        viewAllModules();
        int moduleNumberInt = readModuleNumberToBeDeleted("module");
        boolean isValidInt = checkIfIndexIsWithinBounds(moduleNumberInt);
        if (isValidInt) {
            logger.log(Level.WARNING, "You are making a change that cannot be undone.");
            System.out.println("Are you sure you want to delete "
                    + modules.get(moduleNumberInt).getName()
                    + "? [Y/N]");
            String command = Ui.readCommand();
            if (readYN(command) == 1) {
                printDeletedModuleMessage(modules.get(moduleNumberInt));
                deleteTasksforModule(modules.get(moduleNumberInt).getName());
                testDeleteModule(moduleNumberInt);
            } else if (readYN(command) == 0) {
                System.out.println("Ok. I did not delete "
                        + modules.get(moduleNumberInt).getName());
            }
        } else {
            logger.log(Level.INFO, "You did not enter a valid integer.");
            Ui.printInvalidIntegerMessage();
        }
    }

    public static boolean checkIfIndexIsWithinBounds(int index) {
        return index >= 0 && index < modules.size();
    }

    public static boolean testDeleteModule(int index) {
        modules.remove(index);
        return true;
    }

    public static int readModuleNumberToBeDeleted(String moduleOrReview) {
        if (moduleOrReview.equals("module")) {
            Ui.printSelectModuleToDeleteMessage();
        } else if (moduleOrReview.equals("review")) {
            Ui.printSelectReviewToDeleteMessage();
        }
        int moduleNumberInt = Ui.readCommandToInt();
        moduleNumberInt--;
        return moduleNumberInt;
    }

    public static void printDeletedModuleMessage(Module module) {
        System.out.println("You've deleted this: " + module.getName());
        System.out.println("NOTE: You are deleting your module description\n"
                + module.getDescription());
        if (!module.getReview().trim().equals(EMPTY_REVIEW_MESSAGE)) {
            System.out.println("NOTE: You are deleting your review\n"
                    + module.getReview());
        }
        Ui.printHorizontalLine();
    }

    private static void deleteTasksforModule(String module) {
        for (int i = 0; i < TaskManager.tasks.size(); i++) {
            Task task = TaskManager.tasks.get(i);
            if (task.getModule().equals(module)) {
                Task pinnedTask = task;
                DeleteTask.deleteTask(TASK_TYPE, task);
                DeleteTask.findAndDeletePinnedTask(TASK_TYPE, pinnedTask);
                i--;
            }
        }
        for (int i = 0; i < TaskManager.assignments.size(); i++) {
            Assignment assignment = TaskManager.assignments.get(i);
            if (assignment.getModule().equals(module)) {
                Assignment pinnedTask = assignment;
                DeleteTask.deleteTask(ASSIGNMENT_TYPE, assignment);
                DeleteTask.findAndDeletePinnedTask(ASSIGNMENT_TYPE, pinnedTask);
                i--;
            }
        }
        for (int i = 0; i < TaskManager.midterms.size(); i++) {
            Midterm midterm = TaskManager.midterms.get(i);
            if (midterm.getModule().equals(module)) {
                Midterm pinnedTask = midterm;
                DeleteTask.deleteTask(MIDTERM_TYPE, midterm);
                DeleteTask.findAndDeletePinnedTask(MIDTERM_TYPE, pinnedTask);
                i--;
            }
        }
        for (int i = 0; i < TaskManager.finalExams.size(); i++) {
            FinalExam finalExam = TaskManager.finalExams.get(i);
            if (finalExam.getModule().equals(module)) {
                FinalExam pinnedTask = finalExam;
                DeleteTask.deleteTask(FINAL_EXAM_TYPE, finalExam);
                DeleteTask.findAndDeletePinnedTask(FINAL_EXAM_TYPE, pinnedTask);
                i--;
            }
        }
    }

    private static void getComponents() {
        // prompts user for view or add instruction
        Ui.printModulePrompt();
        int addView = Ui.readCommandToInt();
        if (addView == 1) {
            Component.addComponent(modules);
        } else if (addView == 2) {
            Component.viewComponent(modules);
        } else {
            Ui.printInvalidIntegerMessage();
        }


    }

    /**
     * This method read in module name and decipher if module exists. If module exists, module
     * description previously added is printed. Else, method prompts user to enter module
     * description and creates a new Module object. This method returns to module information menu.
     */
    private static void getModuleDescriptions() {
        Ui.printModuleNameToModifyPrompt();
        String moduleName = Ui.readCommand(); // read in module name, i.e. CS2113T
        boolean isModuleExist = false;
        for (Module module : modules) {
            if (module.getName().equals(moduleName)) {
                Ui.printModuleExistMessage();
                isModuleExist = true;
                System.out.println(module.getDescription() + "\n");
                //Safety break in cases of more than 1 same module name present.
                //In fact, two same module should not be present.
                break;
            }
        }
        if (!isModuleExist) {
            Ui.printModuleDoesNotExistMessage();
            String userInput;
            userInput = Ui.readCommand(); //read in [Y/N]
            if (userInput.equals("Y")) {
                Ui.printModuleDescriptionPrompt(moduleName);
                String moduleDescription = Ui.readCommand(); //read in description
                Module module = new Module(moduleName, moduleDescription);
                modules.add(module);
                Ui.printModuleDescriptionAddedMessage(moduleName,
                        module.getDescription());
            }
        }

    }

    public static Module getModule(String description) {
        for (Module module : modules) {
            if (module.getName().equals(description)) {
                return module;
            }
        }
        return null;
    }

    public static void deleteReview() {
        if (modules.isEmpty()) {
            logger.log(Level.INFO, "You have not added any modules.");
            return;
        }
        viewAllModules();
        int moduleNumberInt = readModuleNumberToBeDeleted("review");
        if (moduleNumberInt >= 0 && moduleNumberInt < modules.size()) {
            Module module = modules.get(moduleNumberInt);
            if (module.getReview().equals(EMPTY_REVIEW_MESSAGE)) {
                System.out.println(EMPTY_REVIEW_MESSAGE);
                return;
            }
            logger.log(Level.WARNING, "You are making a change that cannot be undone.");
            System.out.println("Are you sure you want to delete this review? [Y/N]\n"
                    + "For " + module.getName() + ":\n"
                    + "Review:\n" + module.getReview());
            String command = Ui.readCommand();
            if (readYN(command) == 1) {
                printDeletedReviewMessage(module);
                modules.get(moduleNumberInt).removeReview();
            } else if (readYN(command) == 0) {
                System.out.println("Ok. I did not delete this review:\n"
                        + "For " + module.getName() + ":\n"
                        + "Review:\n" + module.getReview());
            }
        } else {
            logger.log(Level.INFO, "You did not enter a valid integer.");
            Ui.printInvalidIntegerMessage();
        }
    }

    public static void printDeletedReviewMessage(Module module) {
        System.out.println("You've deleted this review: " + module.getReview());
        Ui.printHorizontalLine();
    }

}
