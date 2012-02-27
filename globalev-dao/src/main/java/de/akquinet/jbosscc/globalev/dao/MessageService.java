package de.akquinet.jbosscc.globalev.dao;

import java.util.List;

import de.akquinet.jbosscc.globalev.model.Message;

public interface MessageService {
  Long addMessage(String text, String id);

  List<Message> loadMessages();
}
