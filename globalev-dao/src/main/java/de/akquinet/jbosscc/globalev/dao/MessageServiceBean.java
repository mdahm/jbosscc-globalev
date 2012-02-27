package de.akquinet.jbosscc.globalev.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import de.akquinet.jbosscc.globalev.model.Message;

@Stateless
@Local(MessageService.class)
public class MessageServiceBean implements MessageService {
  @EJB
  private MessageDao _messageDao;

  @Override
  public Long addMessage(final String text, final String id) {
    final Message message = new Message();

    message.setText(text);
    message.setIdentifier(id != null ? id : "Unknown");
    message.setFrom(new Date());

    _messageDao.persist(message);

    return message.getId();
  }

  @Override
  public List<Message> loadMessages() {
    return _messageDao.loadAll();
  }
}
