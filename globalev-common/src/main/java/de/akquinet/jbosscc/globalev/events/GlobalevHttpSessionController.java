package de.akquinet.jbosscc.globalev.events;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;

/**
 * Management of HTTP sessions and global event dispatching.
 * 
 * @author dahm
 */
@ApplicationScoped
public class GlobalevHttpSessionController {
  public static final String EVENT_ATTRIBUTE_NAME = "HttpSessionControllerEvent";

  private final List<HttpSession> _httpSessions = new ArrayList<HttpSession>();

  public List<HttpSession> getHttpSessions() {
    return new ArrayList<HttpSession>(_httpSessions);
  }

  public void addSession(final HttpSession httpSession) {
    assert httpSession != null : "httpSession != null";
    _httpSessions.add(httpSession);
  }

  public void removeSession(final HttpSession httpSession) {
    assert httpSession != null : "httpSession != null";
    _httpSessions.remove(httpSession);
  }

  public void fireEvent(final GlobalEvent eventObject) {
    assert eventObject != null : "eventObject != null";

    for (final HttpSession session : _httpSessions) {
      fireEvent(session, eventObject);
    }
  }

  private void fireEvent(final HttpSession session, final GlobalEvent eventObject) {
    try {
      final List<GlobalEvent> globalEvents = getGlobalEvents(session);

      globalEvents.add(eventObject);
    } catch (final Exception e) {
      throw new IllegalStateException("fireEvent", e);
    }
  }

  private synchronized List<GlobalEvent> getGlobalEvents(final HttpSession session) {
    @SuppressWarnings("unchecked")
    List<GlobalEvent> globalEvents = (List<GlobalEvent>) session.getAttribute(EVENT_ATTRIBUTE_NAME);

    if (globalEvents == null) {
      globalEvents = new ArrayList<GlobalEvent>();
      session.setAttribute(EVENT_ATTRIBUTE_NAME, globalEvents);
    }

    return globalEvents;
  }
}
