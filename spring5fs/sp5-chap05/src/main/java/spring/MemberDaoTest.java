package spring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "AppCtx.java")
public class MemberDaoTest {

	private MemberDao memberDao;
	
//	public static void main(String[] args) {
//		JUnitCore.main("spring.MemberDaoTest");
//	}
	
	@Test
	public void selectByEmail() {
		
	}
}
