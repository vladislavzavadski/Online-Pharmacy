package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.MessageDao;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.MessageStatus;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 14.08.16.
 */
public class DatabaseMessageDao implements MessageDao {
    private static final String INSERT_MESSAGE_QUERY = "insert into messages (me_sender_login, me_sender_login_via, me_sender_message, me_receiver_login, me_receiver_login_via, me_status, me_request_date, me_last_update) values (?, ?, ?, ?, ?, 'in_progress', now(), now());";
    private static final String GET_ALL_MESSAGES_QUERY_PREFIX = "select me_id, me_sender_login, me_sender_login_via, me_sender_message, me_receiver_message, me_receiver_login, me_receiver_login_via, \n" +
            "me_status, me_request_date, me_response_date, sender.us_first_name as se_first_name, sender.us_second_name as se_second_name, \n" +
            "receiver.us_first_name as re_first_name, receiver.us_second_name as re_second_name from messages inner join users as sender\n" +
            "on me_sender_login=sender.us_login and me_sender_login_via=sender.login_via \n" +
            "inner join users as receiver on receiver.us_login=me_receiver_login and receiver.login_via=me_receiver_login_via where ((me_sender_login=? and \n" +
            "me_sender_login_via=?) or (me_receiver_login=? and me_receiver_login_via=?)) ";
    private static final String MESSAGE_STATUS = " and me_status=? ";
    private static final String LAST_UPDATE_FROM = " and me_last_update>=? ";
    private static final String LAST_UPDATE_TO = " and me_last_update<=? ";
    private static final String GET_ALL_MESSAGES_QUERY_POSTFIX = " order by me_last_update desc limit ?, ?;";
    private static final String FK_SENDER = "fk_me_sender";
    private static final String FK_RECEIVER = "fk_me_receiver";
    private static final String MARK_MESSAGE_AS_READED_QUERY = "update messages set me_status='completed' where me_sender_login=? and me_sender_login_via=? and me_status='new' and me_id=? ";
    private static final String GET_NEW_MESSAGE_COUNT = "select count(me_id) as me_count from messages where me_sender_login=? and me_sender_login_via=? and me_status='new';";
    @Override
    public void sendMessage(Message message) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_MESSAGE_QUERY)){
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_LOGIN, message.getSender().getLogin());
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_LOGIN_VIA, message.getSender().getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_MESSAGE, message.getSenderMessage());
            databaseOperation.setParameter(TableColumn.MESSAGE_RECEIVER_LOGIN, message.getReceiver().getLogin());
            databaseOperation.setParameter(TableColumn.MESSAGE_RECEIVER_LOGIN_VIA, message.getReceiver().getRegistrationType().toString().toLowerCase());
            databaseOperation.invokeWriteOperation();
        } catch (SQLException e) {
            if(e.getErrorCode()==ErrorCode.ERROR_CODE_1452&&e.getMessage().contains(FK_SENDER)){
                throw new EntityDeletedException("Can not insert new message, sender was deleted from database "+message, true, e);
            }
            else if(e.getErrorCode()==ErrorCode.ERROR_CODE_1452&&e.getMessage().contains(FK_RECEIVER)){
                throw new EntityDeletedException("Can not insert new message, receiver deleted from database "+message, false, e);
            }
            else {
                throw new DaoException("Can not insert new message "+message, e);
            }
        } catch (ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not insert new message "+message, e);
        }
    }

    @Override
    public List<Message> getMessages(User user, String messageStatus, String dateTo, String dateFrom, int limit, int startFrom) throws DaoException {
        StringBuilder query = new StringBuilder(GET_ALL_MESSAGES_QUERY_PREFIX);
        if(messageStatus!=null&&!messageStatus.isEmpty()){
            query.append(MESSAGE_STATUS);
        }
        if(dateTo!=null&&!dateTo.isEmpty()){
            query.append(LAST_UPDATE_TO);
        }
        if(dateFrom!=null&&!dateFrom.isEmpty()){
            query.append(LAST_UPDATE_FROM);
        }
        query.append(GET_ALL_MESSAGES_QUERY_POSTFIX);
        try(DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 1;
            databaseOperation.setParameter(paramNumber++, user.getLogin());
            databaseOperation.setParameter(paramNumber++, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(paramNumber++, user.getLogin());
            databaseOperation.setParameter(paramNumber++, user.getRegistrationType().toString().toLowerCase());
            if(messageStatus!=null&&!messageStatus.isEmpty()){
                databaseOperation.setParameter(paramNumber++, messageStatus);
            }
            if(dateTo!=null&&!dateTo.isEmpty()){
                Date date = new Date(dateTo);
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);
                databaseOperation.setParameter(paramNumber++, date);
            }
            if(dateFrom!=null&&!dateFrom.isEmpty()){
                databaseOperation.setParameter(paramNumber++, new Date(dateFrom));
            }
            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber++, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            return resultSetToMessages(resultSet);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not load messages from database", e);
        }
    }

    @Override
    public void markMessageAsReaded(User user, int messageId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(MARK_MESSAGE_AS_READED_QUERY)){
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_LOGIN_VIA, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.MESSAGE_ID, messageId);
            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityNotFoundException("Message with id="+messageId+" was not found or you are not a message sender.");
            }
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not mark message as readed with id="+messageId, e);
        }
    }

    @Override
    public int getNewMessagesCount(User user) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_NEW_MESSAGE_COUNT)){
            databaseOperation.setParameter(1, user.getLogin());
            databaseOperation.setParameter(2, user.getRegistrationType().toString().toLowerCase());
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            if(resultSet.next()) {
                return resultSet.getInt(TableColumn.MESSAGE_COUNT);
            }
            else {
                return 0;
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not load messages number from database", e);
        }
    }

    private List<Message> resultSetToMessages(ResultSet resultSet) throws SQLException {
        List<Message> result = new ArrayList<>();
        while (resultSet.next()){
            Message message = new Message();
            User sender = new User();
            User receiver = new User();
            message.setReceiver(receiver);
            message.setSender(sender);
            sender.setLogin(resultSet.getString(TableColumn.MESSAGE_SENDER_LOGIN));
            sender.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.MESSAGE_SENDER_LOGIN_VIA).toUpperCase()));
            receiver.setLogin(resultSet.getString(TableColumn.MESSAGE_RECEIVER_LOGIN));
            receiver.setRegistrationType(RegistrationType.valueOf(resultSet.getString(TableColumn.MESSAGE_RECEIVER_LOGIN_VIA).toUpperCase()));
            message.setSenderMessage(resultSet.getString(TableColumn.MESSAGE_SENDER_MESSAGE));
            message.setReceiverMessage(resultSet.getString(TableColumn.MESSAGE_RECEIVER_MESSAGE));
            message.setMessageStatus(MessageStatus.valueOf(resultSet.getString(TableColumn.MESSAGE_STATUS).toUpperCase()));
            message.setRequestDate(resultSet.getTimestamp(TableColumn.MESSAGE_REQUEST_DATE));
            message.setResponseDate(resultSet.getTimestamp(TableColumn.MESSAGE_RESPONSE_DATE));
            message.setId(resultSet.getInt(TableColumn.MESSAGE_ID));
            sender.setFirstName(resultSet.getString(TableColumn.SENDER_FIRST_NAME));
            sender.setSecondName(resultSet.getString(TableColumn.SENDER_SECOND_NAME));
            receiver.setFirstName(resultSet.getString(TableColumn.RECEIVER_FIRST_NAME));
            receiver.setSecondName(resultSet.getString(TableColumn.RECEIVER_SECOND_NAME));
            result.add(message);
        }
        return result;
    }
}
