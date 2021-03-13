
package com.lab3.resource;

/**
 *
 * @author Joachim Antfolk
 */
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ContextMocker extends FacesContext {

    private static final Release RELEASE = new Release();

    private ContextMocker() {
    }

    public static FacesContext mockServletRequest() {
        FacesContext context = mock(FacesContext.class);
        setCurrentInstance(context);
        Mockito.doAnswer(RELEASE)
                .when(context)
                .release();
        Map<String, Object> session = new HashMap<>();
        ExternalContext ext = mock(ExternalContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        Flash flash = Mockito.mock(Flash.class);
        when(ext.getFlash()).thenReturn(flash);
        when(ext.getSessionMap()).thenReturn(session);
        when(context.getExternalContext()).thenReturn(ext);
        when(ext.getRequest()).thenReturn(request);
        when(ext.isUserInRole(anyString())).thenReturn(true);
        return context;
    }

    private static class Release implements Answer<Void> {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            setCurrentInstance(null);
            return null;
        }
    }
}