package ru.job4j.tracker;

import ru.job4j.tracker.action.Action;
import ru.job4j.tracker.action.BaseAction;
import ru.job4j.tracker.input.Input;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final Input input;
    private final Store tracker;
    private final List<Action> actions = new ArrayList<>();
    private static int work = 0;

    public Menu(Input input, Store tracker) {
        this.input = input;
        this.tracker = tracker;
        this.actions.add(new ShowAll(0, "Show task list"));
        this.actions.add(new CreateItem(1, "New task"));
        this.actions.add(new UpdateItem(2, "Change task info"));
        this.actions.add(new DeleteItem(3, "Delete task"));
        this.actions.add(new FindById(4, "Find task by ID"));
        this.actions.add(new FindByName(5, "Find task by name"));
        this.actions.add(new EndProgram(6, "End program"));
    }

    private void showMenu() {
        System.out.println("Menu:");
        for (Action action : this.actions) {
            System.out.println(action.info());
        }
    }

    public void init() {
        while (work == 0) {
            this.showMenu();
            int select = input.askInt("Select:", actions.size());
            this.actions.get(select).execute(input, tracker);
        }
    }

    private class ShowAll extends BaseAction {
        public ShowAll(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            for (Item item : tracker.findAll()) {
                System.out.println(String.format("Name: %s | Id: %s",
                        item.getName(), item.getId()));
            }
        }
    }
    private class CreateItem extends BaseAction {
        public CreateItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            String name = input.askStr("Enter name:");
            tracker.add(new Item(name));
        }
    }
    private class UpdateItem extends BaseAction {
        public UpdateItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            if (tracker.replace(input.askStr("Enter id selected item:"), new Item(input.askStr("Enter new name")))) {
                System.out.println("success");
            } else {
                System.out.println("false");
            }
        }
    }
    private class DeleteItem extends BaseAction {
        public DeleteItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            if (tracker.delete(input.askStr("Enter id selected item:"))) {
                System.out.println("success");
            } else {
                System.out.println("false");
            }
        }
    }
    private class FindById extends BaseAction {
        public FindById(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            String id = input.askStr("Enter id selected item:");
            Item item = tracker.findById(id);
            if (item != null) {
                System.out.println(item.getId() + " " + item.getName());
            } else {
                System.out.println("item with entered id not found");
            }
        }
    }
    private class FindByName extends BaseAction {
        public FindByName(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            List<Item> items = tracker.findByName(input.askStr("Enter name selected item:"));
            for (Item element: items) {
                System.out.println(element.getId() + " " + element.getName());
            }
        }
    }
    private class EndProgram extends BaseAction {
        public EndProgram(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Store tracker) {
            work = 1;
        }
    }
}
