package tobyspring.user.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/daoContext.xml")
public class UserServiceTest {
    public static void main(String[] args) {
        JUnitCore.main("tobyspring.user.service.UserServiceTest");
    }

    private void checkSameUser(User user, Level expectedLevel) throws SQLException, ClassNotFoundException {
        User userUpdate = userService.userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }

    @Autowired
    UserService userService;
    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
            new User("bimjin", "박범진", "p1", Level.BASIC, 49, 0),
            new User("joytouch", "강명성", "p1", Level.BASIC, 50, 0),
            new User("erwins", "신승한", "p1", Level.SILVER, 60, 29),
            new User("madnite1", "이상호", "p1", Level.SILVER, 60, 30),
            new User("green", "오민규", "p1", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void bean() {
         assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void upgradeLevels() throws SQLException, ClassNotFoundException {
        userService.userDao.deleteAll();
        for (User user : users) {
            userService.userDao.add(user);
        }

        userService.upgradeLevels();

        checkSameUser(users.get(0), Level.BASIC);
        checkSameUser(users.get(1), Level.SILVER);
        checkSameUser(users.get(2), Level.SILVER);
        checkSameUser(users.get(3), Level.GOLD);
        checkSameUser(users.get(4), Level.GOLD);
    }
}
