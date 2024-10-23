import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Interpreter
        System.out.println("Interpreter");
        Expression expression = Interpreter.parseExpression("5 + 2 - 3");
        System.out.println("Result '5 + 2 - 3': " + expression.interpret());
        //  Memento
        System.out.println("\nKeeper");
        TextEditor editor = new TextEditor();
        History history = new History();
        editor.type("hello");
        history.saveState(editor.save());
        editor.type("world!");
        System.out.println("Current text: " + editor.getText());
        editor.restore(history.getState(0));
        System.out.println("After change " + editor.getText());
        // Observer
        System.out.println("\n Observer");
        NewsAgency agency = new NewsAgency();
        NewsChannel channel1 = new NewsChannel("Channel 1");
        NewsChannel channel2 = new NewsChannel("Channel 2");
        agency.addObserver(channel1);
        agency.addObserver(channel2);
        agency.setLatestNews("Latest news: The weather has improved.");
        agency.setLatestNews("Breaking news: A storm is expected.");
    }
}
class NumberExpression implements Expression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int interpret() {
        return this.number;
    }
}
class OperationExpression implements Expression {
    private Expression left;
    private Expression right;
    private char operator;

    public OperationExpression(Expression left, Expression right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public int interpret() {
        switch (operator) {
            case '+':
                return left.interpret() + right.interpret();
            case '-':
                return left.interpret() - right.interpret();
            default:
                throw new IllegalArgumentException("operator");
        }
    }
}
class Interpreter {
    public static Expression parseExpression(String input) {
        String[] tokens = input.split(" ");
        Expression result = new NumberExpression(Integer.parseInt(tokens[0]));

        for (int i = 1; i < tokens.length; i += 2) {
            char operator = tokens[i].charAt(0);
            Expression next = new NumberExpression(Integer.parseInt(tokens[i + 1]));
            result = new OperationExpression(result, next, operator);
        }
        return result;
    }
}
class Memento {
    private String text;

    public Memento(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
class TextEditor {
    private StringBuilder currentText;

    public TextEditor() {
        this.currentText = new StringBuilder();
    }

    public void type(String newText) {
        currentText.append(newText);
    }

    public String getText() {
        return currentText.toString();
    }

    public Memento save() {
        return new Memento(currentText.toString());
    }

    public void restore(Memento memento) {
        this.currentText = new StringBuilder(memento.getText());
    }
}
class History {
    private List<Memento> savedStates = new ArrayList<>();

    public void saveState(Memento memento) {
        savedStates.add(memento);
    }

    public Memento getState(int index) {
        return savedStates.get(index);
    }
}

interface Observer {
    void update(String news);
}
class NewsAgency implements Observable {
    private List<Observer> observers = new ArrayList<>();
    private String latestNews;

    public void setLatestNews(String news) {
        this.latestNews = news;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(latestNews);
        }
    }
}
class NewsChannel implements Observer {
    private String channelName;

    public NewsChannel(String name) {
        this.channelName = name;
    }

    @Override
    public void update(String news) {
        System.out.println("Channel " + channelName + ": " + news);
    }
}

