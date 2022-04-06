package tobyspring.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/* @RunWith : Junit 프레임워크의 테스트 실행 방법을 확장할 때 사용하는 애노테이션 */
@RunWith(SpringJUnit4ClassRunner.class)
/* @ContextConfiguration : 자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일 위치를 지정한 것 */
@ContextConfiguration(locations = "/daoContext.xml")
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;
    private UserDaoJdbc dao;
    User user1;
    User user2;
    User user3;

    public static void main(String[] args) {
        JUnitCore.main("tobyspring.user.dao.UserDaoTest");
    }

    @Before
    public void setUp() {
        this.dao = context.getBean("userDao", UserDaoJdbc.class);
        user1 = new User("gyumee", "박성철", "0000", Level.BASIC, 1, 0);
        user2 = new User("leegw700", "이길원", "1111", Level.SILVER, 55, 10);
        user3 = new User("bumjin", "박범진", "2222", Level.GOLD, 100, 40);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test
    public void addAndget() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user1.getId());

        checkSameUser(user1, user2);
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() throws SQLException, ClassNotFoundException {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);
        dao.add(user3);

        List<User> list = dao.getAll();
        assertThat(list.size(), is(3));
    }

    @Test
    public void update() throws SQLException, ClassNotFoundException {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }
}
