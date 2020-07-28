package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlTracker implements Store {
    private static final Logger LOG = LoggerFactory.getLogger(SqlTracker.class.getName());
    private Connection cn;

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
        init();
        try (PreparedStatement st = cn.prepareStatement("insert into items(name) values(?)")) {
            st.setString(1, item.getName());
            st.executeUpdate();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        init();
        boolean result = false;
        try (PreparedStatement st = cn.prepareStatement("update items set name=? where id=?;")) {
            st.setString(1, item.getName());
            st.setInt(2, Integer.parseInt(id));
            st.executeUpdate();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        init();
        boolean result = false;
        try (PreparedStatement st = cn.prepareStatement("delete from items where id=?")) {
            st.setInt(1, Integer.parseInt(id));
            st.executeUpdate();
            result = true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        init();
        List<Item> result = new ArrayList<>();
        try (PreparedStatement st = cn.prepareStatement("select * from items")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(new Item(String.valueOf(rs.getInt("id")), rs.getString("name")));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        init();
        List<Item> result = new ArrayList<>();
        try (PreparedStatement st = cn.prepareStatement("select * from items where name like ?")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(new Item(String.valueOf(rs.getInt("id")), rs.getString("name")));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Item findById(String id) {
        init();
        Item result = null;
        try (PreparedStatement st = cn.prepareStatement("select * from items where id=?")) {
            ResultSet rs = st.executeQuery();
            result = new Item(String.valueOf(rs.getInt("id")), rs.getString("name"));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
