package ru.job4j.tracker.action;

import ru.job4j.tracker.store.Store;
import ru.job4j.tracker.input.Input;

public interface Action {
    int key();

    void execute(Input input, Store tracker);

    String info();
}
