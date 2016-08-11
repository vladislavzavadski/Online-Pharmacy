package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.MessageDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.message.Message;

import java.sql.SQLException;

/**
 * Created by vladislav on 11.08.16.
 */
public class DatabaseMessageDAO implements MessageDAO {
    private static final String INSERT_MESSAGE_QUERY = "insert into messages (me_sender_login, me_sender_login_via, me_sender_message, me_receiver_login, me_receiver_login_via, me_request_sended, me_response_sended) values(?, ?, ?, ?, ?, true, false);";
    private static final String FK_SENDER = "fk_me_sender";
    private static final String FK_RECEIVER = "fk_me_receiver";
    @Override
    public void insertMessage(Message message) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_MESSAGE_QUERY)){
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_LOGIN, message.getSender().getLogin());
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_LOGIN_VIA, message.getSender().getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.MESSAGE_SENDER_MESSAGE, message.getSenderMessage());
            databaseOperation.setParameter(TableColumn.MESSAGE_RECEIVER_LOGIN, message.getReceiver().getLogin());
            databaseOperation.setParameter(TableColumn.MESSAGE_RECEIVER_LOGIN_VIA, message.getReceiver().getRegistrationType().toString().toLowerCase());
            databaseOperation.invokeWriteOperation();
        } catch (ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not insert message "+message, e);
        } catch (SQLException ex){
            if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1452&&ex.getMessage().contains(FK_RECEIVER)) {
                throw new EntityDeletedException("Can not insert message " + message, false, ex);
            }else if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1452&&ex.getMessage().contains(FK_SENDER)){
                throw new EntityDeletedException("Can not insert message " + message, true, ex);
            }
            else {
                throw new DaoException("Can not insert message "+message, ex);
            }
        } catch (Exception e) {
            throw new DaoException("Can not insert message "+message, e);
        }
    }
}
