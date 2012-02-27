package de.akquinet.jbosscc.globalev.listener;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class JSFUtil {
  public static HttpServletRequest getHttpServletRequest(final FacesContext facesContext) {
    final Object request = facesContext.getExternalContext().getRequest();

    if (request instanceof javax.servlet.http.HttpServletRequest) {
      return (HttpServletRequest) request;
    } else {
      return null;
    }
  }

  public static HttpSession getHttpSession(final FacesContext facesContext) {
    final HttpServletRequest httpServletRequest = getHttpServletRequest(facesContext);

    if (httpServletRequest != null) {
      return httpServletRequest.getSession();
    } else {
      return null;
    }
  }
}
