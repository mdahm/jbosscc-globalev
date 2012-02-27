package de.akquinet.jbosscc.globalev.dao;

import javax.ejb.Local;
import javax.ejb.Stateless;

import de.akquinet.jbosscc.globalev.model.Message;

@Stateless
@Local(MessageDao.class)
public class MessageDaoBean extends JpaGenericDao<Message> implements MessageDao {

}