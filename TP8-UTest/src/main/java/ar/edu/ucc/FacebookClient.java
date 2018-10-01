package ar.edu.ucc;

public class FacebookClient {

    public void createPost(IFacebook fb) {
        String message = fb.connect();

        System.out.println(message);

        // Otro código con la conexión ya hecha
    }

}
