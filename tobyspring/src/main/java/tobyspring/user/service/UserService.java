package tobyspring.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    //JDBC의 로컬 트랜잭션을 이용한다면 DataSourceTransactionManager(dataSource)를 사용하면 된다
    PlatformTransactionManager transactionManager;
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    DataSource dataSource;
    UserDao userDao;


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void add(User user) throws SQLException, ClassNotFoundException {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    /* JDBC의 트랜잭션은 하나의 connection을 가져와 사용하다가 닫는 사이에서 일어난다.
    * upgradeLevels() 메소드를 보면 여러번의 dao 클래스의 update() 함수를 실행한다.
    * update() 함수가 실행된 만큼 트랜잭션이 생성된다. 이 트랜잭션을 하나로 묶을 순 없을까?
    * upgradeLevels() 메소드가 트랜잭션 경계설정을 해야 한다는 사실은 피할 수 없다.
    *
    * <트랜잭션 경계설정 : setAutoCommit(false)로 트랜잭션의 시작을 선언하고 commit() 또는 rollback()으로 트랜잭션을 종료하는 작업>
    *
    * 스프링이 제안하는 방법은 독립적인 트랜잭션 동기화방식이다. Connection 오브젝트를 트랜잭션 동기화 저장소에 저장하고
    * 이후에 호출되는 DAO의 메소드에서 저장된 Connection을 사용하는 것이다. 지금까지 JdbcTemplate은 직접 Connection을 생성하고 종료했지만
    * 이제는 저장소에서 가져다쓴다.
    *
    * <하나 이상의 DB가 참여하는 트랜잭션을 만들려면 JTA를 사용해야 한다.>
    *
    * 문제는 로컬 DB를 트랜잭션하는 코드와 JTA를 트랜잭션하는 코드가 조금씩 다르다...
    * 각 방법마다 메소드를 만들어야할까? 그렇지 않다.
    *  JDBC, JTA, 하이버네이트, JPA, JDO 심지어 JMS도 트랜잭션 개념을 갖고 있으니 모두 그 트랜잭션 경계설정 방법에서 공통점이 있을 것이다.
    *  이 공통적인 특징을 모아서 추상화된 트랜잭션 관리 계층을 만들 수 있다.
    *  */
    public void upgradeLevels() throws SQLException, ClassNotFoundException {

        //트랜잭션 시작
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> userList = userDao.getAll();
            for (User user : userList) {
                Boolean changed = null;
                /* if/else 구조를 메소드로 추출해서 정리했다. */
                if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    userLevelUpgradePolicy.upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (Exception e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    /* 로컬 트랜잭션 방식 */
//    public void upgradeLevels() throws SQLException, ClassNotFoundException {
//        TransactionSynchronizationManager.initSynchronization();
//        Connection c = DataSourceUtils.getConnection(dataSource);
//        c.setAutoCommit(false);
//
//        try {
//            List<User> userList = userDao.getAll();
//            for (User user : userList) {
//                Boolean changed = null;
//                /* if/else 구조를 메소드로 추출해서 정리했다. */
//                if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
//                    userLevelUpgradePolicy.upgradeLevel(user);
//                }
//            }
//            c.commit();
//        } catch (Exception e) {
//            c.rollback();
//            throw e;
//        } finally {
//            DataSourceUtils.releaseConnection(c, dataSource);
//            TransactionSynchronizationManager.unbindResource(this.dataSource);
//            TransactionSynchronizationManager.clearSynchronization();
//        }
//    }
}

