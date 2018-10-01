package ar.edu.ucc;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class FacebookClientTest {

    @Test
    public void testCreatePost() {
        FacebookClient client = new FacebookClient();

        IFacebook iFacebook = mock(IFacebook.class);

        when(iFacebook.connect()).thenReturn("Using mockito is great");

        client.createPost(iFacebook);

        verify(iFacebook, atLeastOnce()).connect(); //checkeo que al menos una vez se llamo al metodo connect adentro de mi codigo
    }
}

//Marcos