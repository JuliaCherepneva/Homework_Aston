package org.aston.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Утилитарный класс для настройки Hibernate.
 */
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            logger.info("Начинаю инициализацию SessionFactory.");
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            logger.debug("SessionFactory успешно создан.");
            return sessionFactory;
        } catch (Exception ex) {
            logger.error("Инициализация SessionFactory не удалась.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        logger.debug("Закрытие SessionFactory.");
        getSessionFactory().close();
    }
}