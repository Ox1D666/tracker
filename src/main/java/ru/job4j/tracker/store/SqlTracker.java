package ru.job4j.tracker.store;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.Item;

public class SqlTracker implements Store, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(SqlTracker.class.getName());
    private Connection cn;

    public SqlTracker() {
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
        init();
    }

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement st = cn.prepareStatement("insert into items(name) values(?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, item.getName());
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.debug(item.getId() + " - item id");
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean result = false;
        try (PreparedStatement st = cn.prepareStatement("update items set name=? where id=?")) {
            st.setString(1, item.getName());
            st.setInt(2, id);
            if (st.executeUpdate() > 0) {
                result = true;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement st = cn.prepareStatement("delete from items where id=?")) {
            st.setInt(1, id);
            if (st.executeUpdate() > 0) {
                result = true;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement st = cn.prepareStatement("select * from items")) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    result.add(new Item(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement st = cn.prepareStatement("select * from items where name like ?")) {
            st.setString(1, key);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    result.add(new Item(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Item findById(int id) {
        Item result = null;
        try (PreparedStatement st = cn.prepareStatement("select * from items where id=?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    result = new Item(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
