package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	/**
	 * JACKSON 라이브러리에게 지연로딩(fetch = FetchType.LAZY)인 경우는 건들지말라고 알림
	 * */
	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		/*hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);*/
		return hibernate5Module;
	}
}
