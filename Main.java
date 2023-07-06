import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

interface FileHandler {
    void saveToFile(Tree tree, String fileName) throws IOException;

    Tree loadFromFile(String fileName) throws IOException, ClassNotFoundException;
}

class Person {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;

    public Person(String firstName, String lastName, String birthDate, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getInfo() {
        return firstName + " " + lastName + ", " + birthDate + ", " + gender;
    }
}

class Tree {
    private Person rootPerson;
    private List<Person> people;

    public Tree(Person rootPerson) {
        this.rootPerson = rootPerson;
        this.people = new ArrayList<>();
        this.people.add(rootPerson);
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public List<Person> getChildren(Person person) {
        List<Person> children = new ArrayList<>();
        for (Person p : people) {
            if (!p.equals(person) && p.getBirthDate().compareTo(person.getBirthDate()) > 0) {
                children.add(p);
            }
        }
        return children;
    }
}

class Application {
    private Tree tree;
    private UserInterface ui;
    private FileHandler fileHandler;

    public Application(FileHandler fileHandler) {
        this.tree = null;
        this.ui = null;
        this.fileHandler = fileHandler;
    }

    public void initialize() {
        // Инициализация приложения
        Person rootPerson = new Person("John", "Doe", "01-01-1950", "M");
        this.tree = new Tree(rootPerson);
        this.ui = new UserInterface();
    }

    public void run() {
        initialize();
        ui.display();
    }

    public void processUserInput(String input) {
        // Обработка пользовательского ввода
        Person selectedPerson = ui.getSelectedPerson();
        List<Person> children = tree.getChildren(selectedPerson);
        ui.displayChildren(children);
    }

    public void saveToFile(String fileName) throws IOException {
        fileHandler.saveToFile(tree, fileName);
    }

    public void loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        tree = fileHandler.loadFromFile(fileName);
    }
}

class UserInterface {
    private Person selectedPerson;

    public UserInterface() {
        this.selectedPerson = null;
    }

    public void display() {
        // Отображение графического интерфейса пользователя
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void displayChildren(List<Person> children) {
        // Отображение детей выбранного человека
    }
}

class FileHandlerImpl implements FileHandler {
    @Override
    public void saveToFile(Tree tree, String fileName) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(tree);
        }
    }

    @Override
    public Tree loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Tree) inputStream.readObject();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandlerImpl();
        Application app = new Application(fileHandler);
        try {
            app.saveToFile("tree.dat"); // Сохранение в файл
            app.loadFromFile("tree.dat"); // Загрузка из файла
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}