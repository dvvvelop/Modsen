import java.util.ArrayList;
import java.util.List;

public class StackOfString {
    private final List<String> stack;

    public StackOfString() {
        stack = new ArrayList<>();
    }

    public void push(String value) {
        stack.add(value);
    }

    public String pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return stack.remove(stack.size() - 1);
    }


    public boolean isEmpty() {
        return stack.isEmpty();
    }

}