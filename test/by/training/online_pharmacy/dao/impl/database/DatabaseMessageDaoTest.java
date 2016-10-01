package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.MessageDao;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.MessageStatus;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 29.09.16.
 */
public class DatabaseMessageDaoTest {
    @BeforeClass
    public static void initConnectionPool() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.initConnectionPool();
    }

    @Before
    public void reserveConnection() throws ConnectionPoolException, SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        Connection connection = connectionPool.reserveConnection();

        resetAutoIncrement(connection);

        connection.setAutoCommit(false);
    }

    @After
    public void freeConnection() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.freeConnection();
    }

    @AfterClass
    public static void destroyConnectionPool() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.dispose();
    }

    @Test
    public void insertMessageTest() throws DaoException {
        Message expected = new Message();

        expected.setId(4);
        expected.setSenderMessage(SENDER_MESSAGE);

        User sender = new User();
        sender.setLogin(USER_LOGIN);
        sender.setRegistrationType(RegistrationType.NATIVE);

        sender.setFirstName(SENDER_NAME);
        sender.setSecondName(SENDER_NAME);

        expected.setSender(sender);

        User receiver = new User();
        receiver.setRegistrationType(RegistrationType.NATIVE);
        receiver.setLogin(RECEIVER_LOGIN);
        receiver.setFirstName(RECEIVER_LOGIN);
        receiver.setSecondName(RECEIVER_LOGIN);

        expected.setReceiver(receiver);
        MessageDao messageDao = new DatabaseMessageDao();

        messageDao.insertMessage(expected);

        List<Message> messages = messageDao.getMessages(sender, new SearchMessageCriteria(), 0, 6);

        Message actual = messages.get(0);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, IGNORED_REQUEST_DATE, IGNORED_MESSAGE_STATUS);
    }

    @Test
    public void getMessagesTest() throws DaoException {
        List<Message> expected = initMessages();

        User sender = new User();

        sender.setLogin(USER_LOGIN);
        sender.setRegistrationType(RegistrationType.NATIVE);
        sender.setFirstName(SENDER_NAME);
        sender.setSecondName(SENDER_NAME);

        SearchMessageCriteria searchMessageCriteria = new SearchMessageCriteria();

        MessageDao messageDao = new DatabaseMessageDao();

        List<Message> actual = messageDao.getMessages(sender, searchMessageCriteria, 0, 6);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++){
            assertThat(actual.get(i)).isEqualToIgnoringNullFields(expected.get(i));
        }
    }

    @Test
    public void setMessageStatusTest() throws DaoException {
        User sender = new User();

        sender.setLogin(USER_LOGIN);
        sender.setRegistrationType(RegistrationType.NATIVE);
        sender.setFirstName(SENDER_NAME);
        sender.setSecondName(SENDER_NAME);

        MessageDao messageDao = new DatabaseMessageDao();

        messageDao.setMessageStatus(sender, MessageStatus.COMPLETED, 2);

        SearchMessageCriteria searchMessageCriteria = new SearchMessageCriteria();

        Message message = messageDao.getMessages(sender, searchMessageCriteria, 0, 6).get(0);

        assertEquals(MessageStatus.COMPLETED, message.getMessageStatus());
        assertEquals(sender, message.getSender());
        assertEquals(2, message.getId());
    }

    @Test
    public void updateMessageTest() throws DaoException {
        User receiver = new User();

        receiver.setLogin(RECEIVER_LOGIN);
        receiver.setRegistrationType(RegistrationType.NATIVE);
        receiver.setFirstName(RECEIVER_LOGIN);
        receiver.setSecondName(RECEIVER_LOGIN);

        Message expected = new Message();

        expected.setId(1);
        expected.setReceiver(receiver);
        expected.setReceiverMessage(RECEIVER_MESSAGE);

        MessageDao messageDao = new DatabaseMessageDao();

        messageDao.updateMessage(expected);

        Message actual = messageDao.getMessages(receiver, new SearchMessageCriteria(), 0, 6).get(0);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }

    @Test
    public void getReceiverMessageCountTest() throws DaoException {
        User receiver = new User();

        receiver.setLogin(RECEIVER_LOGIN);
        receiver.setRegistrationType(RegistrationType.NATIVE);
        receiver.setFirstName(RECEIVER_LOGIN);
        receiver.setSecondName(RECEIVER_LOGIN);

        MessageDao messageDao = new DatabaseMessageDao();

        int messageCount = messageDao.getReceiverMessageCount(receiver, MessageStatus.COMPLETED);

        assertEquals(2, messageCount);
    }

    @Test
    public void getSenderMessageCountTest() throws DaoException {
        User sender = new User();

        sender.setLogin(USER_LOGIN);
        sender.setRegistrationType(RegistrationType.NATIVE);
        sender.setFirstName(SENDER_NAME);
        sender.setSecondName(SENDER_NAME);

        MessageDao messageDao = new DatabaseMessageDao();

        int messageCount = messageDao.getSenderMessageCount(sender, MessageStatus.NEW);

        assertEquals(2, messageCount);
    }

    private List<Message> initMessages(){
        List<Message> expected = new ArrayList<>();

        User sender = new User();

        sender.setLogin(USER_LOGIN);
        sender.setRegistrationType(RegistrationType.NATIVE);
        sender.setFirstName(SENDER_NAME);
        sender.setSecondName(SENDER_NAME);

        User receiver = new User();

        receiver.setLogin(RECEIVER_LOGIN);
        receiver.setRegistrationType(RegistrationType.NATIVE);
        receiver.setFirstName(RECEIVER_LOGIN);
        receiver.setSecondName(RECEIVER_LOGIN);

        Message message = initMessage(2, SENDER_MESSAGE1, RECEIVER_MESSAGE, sender, receiver, MessageStatus.NEW);

        expected.add(message);

        message = initMessage(3, SENDER_MESSAGE2, RECEIVER_MESSAGE1, sender, receiver, MessageStatus.NEW);

        expected.add(message);


        message = initMessage(1, SENDER_MESSAGE, null, sender, receiver, MessageStatus.IN_PROGRESS);

        expected.add(message);

        return expected;
    }

    private Message initMessage(int id, String senderMessage, String receiverMessage, User sender, User receiver,
                                MessageStatus messageStatus){
        Message message = new Message();

        message.setId(id);
        message.setSenderMessage(senderMessage);
        message.setReceiverMessage(receiverMessage);
        message.setMessageStatus(messageStatus);
        message.setSender(sender);
        message.setReceiver(receiver);

        return message;
    }

    private void resetAutoIncrement(Connection connection) throws SQLException {

        Statement statement = connection.createStatement();
        statement.execute(Constant.RESET_MESSAGE_QUERY);

    }
}
