package redis.clients.jedis.tests.commands;

import org.hamcrest.CoreMatchers;
import org.junit.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.UserACL;
import redis.clients.jedis.exceptions.JedisAuthenticationException;
import redis.clients.jedis.exceptions.JedisPermissionException;
import redis.clients.jedis.tests.utils.RedisVersionUtil;

import java.util.List;

import static org.junit.Assert.*;

// TODO :properly define and test exceptions

public class ACLCommandsTest extends JedisCommandTestBase {


  public static String USER_YYY = "yyy";
  public static String USER_ZZZ = "zzz";
  public static String USER_ZZZ_PASSWORD = "secret";

  /**
   * Use to check if the ACL test should be ran. ACL are available only in 6.0 and later
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
    boolean shouldNotRun = ((new RedisVersionUtil(jedis)).getRedisMajorVersionNumber() < 6);
    if ( shouldNotRun ) {
      org.junit.Assume.assumeFalse("Not running ACL test on this version of Redis", shouldNotRun);
    }
  }

  @Test
  public void aclWhoAmi() {
    String returnValue = jedis.aclWhoAmI();
    assertEquals("default", returnValue);
  }

  @Test
  public void aclListDefault() {
    List<String> listOfACL = jedis.aclList();
    assertEquals(1, listOfACL.size());
  }

  @Test
  public void addAndRemoveUser() {
    String status = jedis.aclSetUser(USER_ZZZ);
    assertEquals("OK", status);
    List<String> listOfACL = jedis.aclList();
    assertEquals(2, listOfACL.size());
    jedis.aclDelUser(USER_ZZZ);
    assertEquals(1, jedis.aclList().size());
  }

  @Test
  public void aclGetUser() {

    // get default user information
    UserACL userInfo = jedis.aclGetUser("default");

    assertEquals(3, userInfo.getFlags().size());
    assertEquals(1, userInfo.getPassword().size());
    assertEquals("+@all", userInfo.getCommands());
    assertEquals("*", userInfo.getKeys().get(0));

    // create new user
    jedis.aclDelUser(USER_ZZZ);
    jedis.aclSetUser(USER_ZZZ);
    userInfo = jedis.aclGetUser(USER_ZZZ);
    assertEquals(1, userInfo.getFlags().size());
    assertEquals("off", userInfo.getFlags().get(0));
    assertTrue(userInfo.getPassword().isEmpty());
    assertTrue(userInfo.getKeys().isEmpty());

    // reset user
    jedis.aclSetUser(USER_ZZZ, "reset", "+@all", "~*", "-@string", "+incr", "-debug",
      "+debug|digest");
    userInfo = jedis.aclGetUser(USER_ZZZ);
    Assert.assertThat(userInfo.getCommands(), CoreMatchers.containsString("+@all"));
    Assert.assertThat(userInfo.getCommands(), CoreMatchers.containsString("-@string"));
    Assert.assertThat(userInfo.getCommands(), CoreMatchers.containsString("+debug|digest"));

    jedis.aclDelUser(USER_ZZZ);

  }

  @Test
  public void createUserAndPasswords() {
    String status = jedis.aclSetUser(USER_ZZZ, ">" + USER_ZZZ_PASSWORD);
    assertEquals("OK", status);

    // create a new client to try to authenticate
    Jedis jedis2 = new Jedis("localhost");
    String authResult = null;

    // the user is just created without any permission the authentication should fail
    try {
      authResult = jedis2.auth(USER_ZZZ, USER_ZZZ_PASSWORD);
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAuthenticationException e) {
      assertNull(authResult);
      assertEquals("WRONGPASS invalid username-password pair", e.getMessage());
    }

    // now activate the user
    authResult = jedis.aclSetUser(USER_ZZZ, "on", "+acl");
    jedis2.auth(USER_ZZZ, USER_ZZZ_PASSWORD);
    String connectedUser = jedis2.aclWhoAmI();
    assertEquals(USER_ZZZ, connectedUser);

    // test invalid password
    jedis2.close();

    try {
      authResult = jedis2.auth(USER_ZZZ, "wrong-password");
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAuthenticationException e) {
      assertEquals("OK", authResult);
      assertEquals("WRONGPASS invalid username-password pair", e.getMessage());
    }

    // remove password, and try to authenticate
    status = jedis.aclSetUser(USER_ZZZ, "<" + USER_ZZZ_PASSWORD);
    try {
      authResult = jedis2.auth(USER_ZZZ, USER_ZZZ_PASSWORD);
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAuthenticationException e) {
      assertEquals("OK", authResult);
      assertEquals("WRONGPASS invalid username-password pair", e.getMessage());
    }

    jedis.aclDelUser(USER_ZZZ); // delete the user
    try {
      authResult = jedis2.auth(USER_ZZZ, "wrong-password");
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAuthenticationException e) {
      assertEquals("OK", authResult);
      assertEquals("WRONGPASS invalid username-password pair", e.getMessage());
    }

    jedis2.close();

  }

  @Test
  public void aclSetUserWithAnyPassword() {
    jedis.aclDelUser(USER_ZZZ);
    String status = jedis.aclSetUser(USER_ZZZ, "nopass");
    assertEquals("OK", status);
    status = jedis.aclSetUser(USER_ZZZ, "on", "+acl");
    assertEquals("OK", status);

    // connect with this new user and try to get/set keys
    Jedis jedis2 = new Jedis("localhost");
    String authResult = jedis2.auth(USER_ZZZ, "any password");
    assertEquals("OK", authResult);

    jedis2.close();
    jedis.aclDelUser(USER_ZZZ);

  }

  @Test
  public void aclExcudeSingleCommand() {
    jedis.aclDelUser(USER_ZZZ);
    String status = jedis.aclSetUser(USER_ZZZ, "nopass");
    assertEquals("OK", status);

    status = jedis.aclSetUser(USER_ZZZ, "on", "+acl");
    assertEquals("OK", status);

    status = jedis.aclSetUser(USER_ZZZ, "allcommands", "allkeys");
    assertEquals("OK", status);

    status = jedis.aclSetUser(USER_ZZZ, "-ping");
    assertEquals("OK", status);

    // connect with this new user and try to get/set keys
    Jedis jedis2 = new Jedis("localhost");
    String authResult = jedis2.auth(USER_ZZZ, "any password");
    assertEquals("OK", authResult);

    jedis2.incr("mycounter");

    String result = null;
    try {
      result = jedis2.ping();
      fail("Should throw a NOPERM exception");
    } catch (JedisPermissionException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to run the 'ping' command or its subcommand",
        e.getMessage());
    }

    jedis2.close();
    jedis.aclDelUser(USER_ZZZ);

  }

  @Test
  public void aclDelUser() {
    String statusSetUser = jedis.aclSetUser(USER_YYY);
    assertEquals("OK", statusSetUser);
    int before = jedis.aclList().size();
    Long statusDelUser = jedis.aclDelUser(USER_YYY);
    assertEquals(1, statusDelUser.longValue());
    int after = jedis.aclList().size();
    assertEquals(before - 1, after);
  }

  @Test
  public void basicPermissionsTest() {

    // create a user with login permissions

    jedis.aclDelUser(USER_ZZZ); // delete the user

    // users are not able to access any command
    String status = jedis.aclSetUser(USER_ZZZ, ">" + USER_ZZZ_PASSWORD);
    String authResult = jedis.aclSetUser(USER_ZZZ, "on", "+acl");

    // connect with this new user and try to get/set keys
    Jedis jedis2 = new Jedis("localhost");
    jedis2.auth(USER_ZZZ, USER_ZZZ_PASSWORD);

    String result = null;
    try {
      result = jedis2.set("foo", "bar");
      fail("Should throw a NOPERM exception");
    } catch (JedisPermissionException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to run the 'set' command or its subcommand",
        e.getMessage());
    }

    // change permissions of the user
    // by default users are not able to access any key
    status = jedis.aclSetUser(USER_ZZZ, "+set");

    jedis2.close();
    jedis2.auth(USER_ZZZ, USER_ZZZ_PASSWORD);

    result = null;
    try {
      result = jedis2.set("foo", "bar");
      fail("Should throw a NOPERM exception");
    } catch (JedisPermissionException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to access one of the keys used as arguments",
        e.getMessage());
    }

    // allow user to access a subset of the key
    result = jedis.aclSetUser(USER_ZZZ, "allcommands", "~foo:*", "~bar:*"); // TODO : Define a DSL
                                                                            // Like for permisson
                                                                            // and more to avoid
                                                                            // confusion

    // create key foo, bar and zap
    result = jedis2.set("foo:1", "a");
    assertEquals("OK", status);

    result = jedis2.set("bar:2", "b");
    assertEquals("OK", status);

    result = null;
    try {
      result = jedis2.set("zap:3", "c");
      fail("Should throw a NOPERM exception");
    } catch (JedisPermissionException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to access one of the keys used as arguments",
        e.getMessage());
    }

    // remove user
    jedis.aclDelUser(USER_ZZZ); // delete the user

  }

}
