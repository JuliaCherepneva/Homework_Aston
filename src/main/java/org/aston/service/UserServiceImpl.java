package org.aston.service;

import org.aston.model.UserModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Сервисный класс для управления сущностями UserModel.
 * Отвечает за CRUD-операции (создание, чтение, обновление, удаление) пользователей в базе данных.
 */
public class UserServiceImpl implements UserService {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(SessionFactory sessionFactory) {
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
            logger.info("Пользователь сохранён: {}", user);
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя: {}", e.getMessage(), e);
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
            UserModel user = session.get(UserModel.class, id);
            if (user != null) {
                logger.info("Пользователь найден: {}", user);
            } else {
                logger.warn("Пользователь с ID {} не найден.", id);
            }
            return user;
        } catch (Exception e) {
            logger.error("Ошибка при считывании пользователя с ID {}: {}", id, e.getMessage(), e);
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
            logger.info("Пользователь успешно обновлён: {}", user);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя: {}", e.getMessage(), e);
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
                logger.info("Пользователь успешно удалён: {}", user);
            } else {
                logger.warn("Пользователь с ID {} не найден для удаления.", id);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage(), e);
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
            List<UserModel> users = session.createQuery("FROM UserModel", UserModel.class).list();
            logger.info("Получено {} пользователей.", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
