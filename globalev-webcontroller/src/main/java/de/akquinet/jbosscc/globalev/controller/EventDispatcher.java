package de.akquinet.jbosscc.globalev.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import de.akquinet.jbosscc.globalev.events.GlobalevHttpSessionController;
import de.akquinet.jbosscc.globalev.events.MessageAddedEvent;

@RequestScoped
public class EventDispatcher {
  @Inject
  private GlobalevHttpSessionController _httpSessionController;

  public void raiseMessageAddedEvent(final String text) {
    _httpSessionController.fireEvent(new MessageAddedEvent(text));
  }
}
