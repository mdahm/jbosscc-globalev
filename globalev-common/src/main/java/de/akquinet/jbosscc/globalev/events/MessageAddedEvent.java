package de.akquinet.jbosscc.globalev.events;

public class MessageAddedEvent extends GlobalEvent {
  private static final long serialVersionUID = 1L;

  private final String _text;

  public MessageAddedEvent(final String text) {
    _text = text;
  }

  public String getText() {
    return _text;
  }
}
