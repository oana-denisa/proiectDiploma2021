package ro.pub.elth.itee.oana.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    //ADDED ROLE
    public static final String MEDIC = "ROLE_MEDIC";

    public static final String CLIENT = "ROLE_CLIENT";

    private AuthoritiesConstants() {}
}
