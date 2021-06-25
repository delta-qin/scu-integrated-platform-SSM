//package com.deltaqin.scussm;
//
//import com.nowcoder.community.dao.AlphaDao;
//import com.nowcoder.community.service.AlphaService;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = SSMStartApplication.class)
//class CommunityApplicationTests implements ApplicationContextAware {
//
//
//	private ApplicationContext applicationContextl;
//	// BeanFactory 是父接口，顶层接口
//	// ApplicationContext 是扩展，功能更加强一点。
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContextl = applicationContext;
//	}
//
//	@Test
//	void contextLoads() {
//		System.out.println(applicationContextl);
//		// 有多个实现类的时候使用有@Primary注解的
//		// 还可以自定义bean的名字
//		AlphaDao bean = applicationContextl.getBean(AlphaDao.class);
//		// 使用bean的名字获取对应类的对象，可以实现打破@Primary注解，拿到自己想要的实现类
//		applicationContextl.getBean("beanName", AlphaDao.class);
//		String select = bean.select();
//		System.out.println(select);
//	}
//
//	@Test
//	public void getThirdBean() {
//		// 在配置类里面使用@Bean注入的
//		SimpleDateFormat bean = applicationContextl.getBean(SimpleDateFormat.class);
//		String format = bean.format(new Date());
//	}
//
//	// 使用依赖注入，而不是getBean的方式主动获取，
//	// 也可以 加在类的构造器和set方法上面，但是一般会写在属性上面，简洁方便
//	//	    在方法上该方法如果有参数，会使用autowired的方式在容器中查找是否有该参数，还会自动执行这个方法
//	//	    在构造器上是一样的，作为参数会自动注入
//	@Autowired
//	// 希望使用自己指定的，而不是默认的优先级@Priority
//	@Qualifier("alphaHibernate")
//	private AlphaDao alphaDao;
//
//
//	@Autowired
//	private AlphaService alphaService;
//
//	@Autowired
//	private SimpleDateFormat simpleDateFormat;
//
//	@Test
//	public void testDI() {
//		System.out.println(alphaDao);
//		System.out.println(alphaService);
//		System.out.println(simpleDateFormat);
//	}
//}
