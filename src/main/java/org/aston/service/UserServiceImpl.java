package org.aston.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.exception.EntityNotFoundException;
import org.aston.model.UserDTO;
import org.aston.model.UserDTO_withID;
import org.aston.model.UserModel;
import org.aston.util.Mapper;
import org.aston.util.Update;
import org.aston.util.Validate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс для управления сущностями UserModel.
 * Отвечает за CRUD-операции (создание, чтение, обновление, удаление) пользователей в базе данных.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserServiceJPA {
    private final UserRepository userRepository;
    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    //Добавить инициализацию через конструктор

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

    @Override
    public UserDTO_withID create(UserDTO userDTO) {
        UserModel userModel = Mapper.toUserModel(userDTO);
        userModel = userRepository.save(userModel);
        log.info("Создан пользователь с ID = {}, имя = {}", userModel.getId(), userModel.getName());
        return Mapper.toUserModelWithId(userModel);
    }

    @Override
    public UserDTO_withID get(Long id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД"));
        return Mapper.toUserModelWithId(userModel);
    }

    @Override
    public List<UserDTO_withID> getCount(Long count) {
        List<UserModel> users = userRepository.findUsersCount(count.intValue());
        return users.stream()
                .map(Mapper::toUserModelWithId)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO_withID update(UserDTO userDTO, Long id) {
        Validate.validateUser(userDTO);
        UserModel userModel = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД"));
        Update.toUpdate(userModel, userDTO);
        userModel = userRepository.save(userModel);
        log.info("Пользователь с ID = {} обновлён", userModel.getId());
        return Mapper.toUserModelWithId(userModel);
    }
}
