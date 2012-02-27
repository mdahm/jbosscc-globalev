package de.akquinet.jbosscc.globalev.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.logging.Logger;

import de.akquinet.jbosscc.globalev.dao.MessageService;
import de.akquinet.jbosscc.globalev.events.MessageAddedEvent;
import de.akquinet.jbosscc.globalev.listener.JSFUtil;
import de.akquinet.jbosscc.globalev.model.Message;

/**
 * @author dahm
 */
@Named("messageController")
@SessionScoped
public class MessageController implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = Logger.getLogger(MessageController.class);

  @Inject
  private MessageService _messageService;

  @Inject
  private EventDispatcher _eventDispatcher;

  private String _text = "";

  private List<Message> _messages;

  public List<Message> getMessages() {
    if (_messages == null) {
      LOG.info("Reloading messages");

      _messages = _messageService.loadMessages();
    }

    return _messages;
  }

  public void send() {
    final HttpServletRequest httpServletRequest = JSFUtil.getHttpServletRequest(FacesContext.getCurrentInstance());
    final String userAgent = httpServletRequest.getHeader("user-agent");

    _messageService.addMessage(getText(), userAgent);
    _eventDispatcher.raiseMessageAddedEvent(getText());

    _text = "";
  }

  @NotNull
  @Size(min = 1, max = Message.MAX_TEXT)
  public String getText() {
    return _text;
  }

  public void observeMessageAddedEvent(@Observes final MessageAddedEvent event) {
    LOG.info("Message observed: " + event.getText());
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New message arrived: " + event.getText()));
    _messages = null; // Force reload
  }

  public void setText(final String text) {
    _text = text;
  }
}