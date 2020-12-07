package ru.job4j.tracker;

import ru.job4j.tracker.input.ConsoleInput;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.input.ValidateInput;
import ru.job4j.tracker.store.SqlTracker;
import ru.job4j.tracker.store.Store;

public class StartUI {
    public static void main(String[] args) {
        Input input = new ValidateInput(new ConsoleInput());
        Store tracker = new SqlTracker();
        Menu menuTracker = new Menu(input, tracker);
        menuTracker.init();
    }
}
