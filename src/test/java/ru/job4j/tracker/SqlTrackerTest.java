package ru.job4j.tracker;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;

public class SqlTrackerTest {
    public Connection init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void createItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            tracker.add(new Item("name", "desc"));
            assertThat(tracker.findByName("desc").size(), is(1));
        }
    }

    @Test
    public void replaceItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item item = new Item("1", "asd");
            tracker.add(item);
            tracker.replace(item.getId(), new Item("1", "qwe"));
            assertThat(tracker.findById(item.getId()).getName(), is( "qwe"));
        }
    }

    @Test
    public void deleteItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            tracker.add(new Item("1", "asd"));
            tracker.delete("1");
            assertNull(tracker.findById("1"));
        }
    }

    @Test
    public void findAllItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            tracker.add(new Item("1", "asd"));
            tracker.add(new Item("2", "qwe"));
            assertThat(tracker.findAll().size(), is(2));
        }
    }
    @Test
    public void findByName() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            tracker.add(new Item("1", "asd"));
            tracker.add(new Item("2", "asd"));
            assertThat(tracker.findByName("asd").size(), is(2));
        }
    }
    @Test
    public void findById() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            Item item = new Item("1", "asd");
            tracker.add(item);
            assertNotNull(tracker.findById(item.getId()));
        }
    }
}