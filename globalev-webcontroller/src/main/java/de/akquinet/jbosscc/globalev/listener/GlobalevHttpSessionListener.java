package de.akquinet.jbosscc.globalev.listener;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.logging.Logger;

import de.akquinet.jbosscc.globalev.events.GlobalevHttpSessionController;

/**
 * Register HTTP session events and tell HttpSessionController.
 * 
 * @author dahm
 */
@WebListener
public class GlobalevHttpSessionListener implements HttpSessionListener {
  private static final Logger LOG = Logger.getLogger(GlobalevHttpSessionListener.class);

  @Inject
  private GlobalevHttpSessionController _httpSessionController;

  @Override
  public void sessionCreated(final HttpSessionEvent se) {
    LOG.info("Session created " + se.getSession().getId());
    _httpSessionController.addSession(se.getSession());
  }

  @Override
  public void sessionDestroyed(final HttpSessionEvent se) {
    LOG.info("Session destroyed " + se.getSession().getId());
    _httpSessionController.removeSession(se.getSession());
  }
}
