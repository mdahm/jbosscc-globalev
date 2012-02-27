package de.akquinet.jbosscc.globalev.listener;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import de.akquinet.jbosscc.globalev.events.GlobalEvent;
import de.akquinet.jbosscc.globalev.events.GlobalevHttpSessionController;

/**
 * Lookup events in HTTP session, than get BeanManager from JNDI and fire the events to session.
 * 
 * <br/>
 * http://docs.oracle.com/javaee/6/api/javax/enterprise/inject/spi/BeanManager.html <br/>
 * http://stackoverflow.com/questions/1730201/how-to-intercept-request-ends
 * 
 * @author dahm
 */
public class GlobalevEventPhaseListener implements PhaseListener {
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = Logger.getLogger(GlobalevEventPhaseListener.class);

  @Override
  public void afterPhase(final PhaseEvent event) {
  }

  @Override
  public void beforePhase(final PhaseEvent event) {
    final FacesContext facesContext = event.getFacesContext();
    final HttpSession httpSession = JSFUtil.getHttpSession(facesContext);

    if (httpSession != null) {
      final List<GlobalEvent> globalEvents = getGlobalEvents(httpSession);

      if (!globalEvents.isEmpty()) {
        LOG.info("events: " + globalEvents);
        fireEvents(globalEvents);
      }
    }
  }

  private void fireEvents(final List<GlobalEvent> globalEvents) {
    final BeanManager beanManager = lookBeanManager();

    if (beanManager != null) {
      try {
        for (final GlobalEvent devaGlobalEvent : globalEvents) {
          beanManager.fireEvent(devaGlobalEvent);
        }
      } catch (final Exception e) {
        LOG.warn("Event dispatcher", e);
      }
    }
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE; // RESTORE_VIEW;
  }

  private BeanManager lookBeanManager() {
    try {
      final Object obj = new InitialContext().lookup("java:comp/BeanManager");

      return (BeanManager) obj;
    } catch (final Exception e) {
      LOG.warn("Cannot find bean manager", e);
    }

    return null;
  }

  private synchronized List<GlobalEvent> getGlobalEvents(final HttpSession httpSession) {
    @SuppressWarnings("unchecked")
    final List<GlobalEvent> events = (List<GlobalEvent>) httpSession.getAttribute(GlobalevHttpSessionController.EVENT_ATTRIBUTE_NAME);
    final List<GlobalEvent> result = new ArrayList<GlobalEvent>();

    if (events != null) {
      result.addAll(events);
      events.clear();
    }

    return result;
  }
}
