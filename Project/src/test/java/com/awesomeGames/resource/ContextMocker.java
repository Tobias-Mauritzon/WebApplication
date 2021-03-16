package com.awesomeGames.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This is a class used for mocking FacesContext in Unit tests
 * @author Joachim Antfolk
 */
public abstract class ContextMocker extends FacesContext {

    private static final Release RELEASE = new Release();

    public static FacesContext mockServletRequest() {
        FacesContext context = mock(FacesContext.class);
        setCurrentInstance(context);
        Mockito.doAnswer(RELEASE)
                .when(context)
                .release();
        Map<String, Object> session = new HashMap<>();
        Map<String, String> parameterMap = new HashMap<>();
        ArrayList<FacesMessage> messages = new ArrayList<>();
        ExternalContext ext = mock(ExternalContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        Flash flash = Mockito.mock(Flash.class);

        when(context.getExternalContext()).thenReturn(ext);
        when(ext.getFlash()).thenReturn(flash);
        when(ext.getSessionMap()).thenReturn(session);
        when(ext.getRequestParameterMap()).thenReturn(parameterMap);
        when(context.getMessageList()).thenReturn(messages);
        when(ext.getRequest()).thenReturn(request);
        when(ext.isUserInRole(anyString())).thenReturn(true);

        doAnswer(invocation -> {
            FacesMessage arg = (FacesMessage) invocation.getArguments()[1];
            messages.add(arg);
            return null;
        }).when(context).addMessage(anyString(), any(FacesMessage.class));

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
