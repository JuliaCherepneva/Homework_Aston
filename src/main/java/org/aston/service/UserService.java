package org.aston.service;

import org.aston.model.UserModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

/**
 * Сервисный класс для управления сущностями UserModel.
 * Отвечает за CRUD-операции (создание, чтение, обновление, удаление) пользователей в базе данных.
 */
public class UserService implements ServiceImpl {
    private SessionFactory sessionFactory;
    public UserService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Создает нового пользователя в базе данных.
     *
     * @param user объект пользователя для сохранения.
     */
    @Override
    public void create(UserModel user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Считывает пользователя по его ID.
     *
     * @param id идентификатор пользователя.
     * @return объект UserModel или null, если пользователь не найден.
     */
    @Override
    public UserModel read(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(UserModel.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Обновляет данные пользователя в базе данных.
     *
     * @param user объект пользователя с обновлёнными данными.
     */
    @Override
    public void update(UserModel user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет пользователя по его ID.
     *
     * @param id идентификатор пользователя для удаления.
     */
    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            UserModel user = session.get(UserModel.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает список всех пользователей.
     *
     * @return список пользователей, либо пустой список в случае ошибки.
     */
    @Override
    public List<UserModel> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM UserModel", UserModel.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
