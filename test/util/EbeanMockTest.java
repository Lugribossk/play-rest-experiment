package util;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import org.junit.Before;
import org.junit.BeforeClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

/**
 * Abstract test that sets up a mock {@link EbeanServer}.
 * Only a little faster startup time than {@link EbeanInMemoryTest} for some reason.
 *
 * @author Bo Gotthardt
 */
public abstract class EbeanMockTest {
    public static EbeanServer mockServer;

    @BeforeClass
    public static void setupServer() {
        // Create a mock server that will override the default one.
        mockServer = mock(EbeanServer.class);
        when(mockServer.getName()).thenReturn("default");

        try {
            // Ebean has a method for "injecting" new EbeanServers, just a shame it's not public...
            // Boolean.TYPE instead of Boolean.class since the parameter type is primitive boolean and not Boolean.
            Method register = Ebean.class.getDeclaredMethod("register", EbeanServer.class, Boolean.TYPE);
            register.setAccessible(true);
            register.invoke(null, mockServer, true);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Unable to inject mock EbeanServer.", e);
        }
    }

    @Before
    public void resetServer() {
        reset(mockServer);
//        when(mockServer.getName()).thenReturn("default");
    }
}
