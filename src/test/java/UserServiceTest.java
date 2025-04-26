
import org.aston.model.UserModel;
import org.aston.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private UserService userService;

    @BeforeEach
    void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        userService = new UserService(sessionFactory);
    }

    @Test
    void testCreateUser() {
        UserModel user = new UserModel("John Doe", "john@example.com", 30, null);

        userService.create(user);
        verify(session).save(user);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testReadUser() {
        int userId = 1;
        UserModel expectedUser = new UserModel("Вася", "васся.ком", 30, null);
        when(session.get(UserModel.class, userId)).thenReturn(expectedUser);
        UserModel actualUser = userService.read(userId);
        assertNotNull(actualUser);
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        verify(session).get(UserModel.class, userId);
    }

    @Test
    void testUpdateUser() {
        UserModel user = new UserModel("Вася", "васся.ком", 30, null);
        user.setId(1);
        userService.update(user);
        verify(session).update(user);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        UserModel user = new UserModel("Вася", "васся.ком", 30, null);
        user.setId(userId);
        when(session.get(UserModel.class, userId)).thenReturn(user);
        userService.delete(userId);
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        verify(session).delete(captor.capture());
        assertEquals(userId, captor.getValue().getId());
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testGetAllUsers() {
        List<UserModel> expectedUsers = new ArrayList<>();
        expectedUsers.add(new UserModel("Вася", "васся.ком", 30, null));
        expectedUsers.add(new UserModel("Саня", "саня.ком", 25, null));

        org.hibernate.query.Query<UserModel> mockQuery = mock(org.hibernate.query.Query.class);

        when(session.createQuery("FROM UserModel", UserModel.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(expectedUsers);

        List<UserModel> actualUsers = userService.getAll();

        assertNotNull(actualUsers);
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getName(), actualUsers.get(0).getName());
        verify(session).createQuery("FROM UserModel", UserModel.class);
    }
}