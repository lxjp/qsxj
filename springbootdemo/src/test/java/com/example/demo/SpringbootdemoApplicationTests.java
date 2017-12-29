package com.example.demo;

import com.example.demo.entity.Book;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SpringbootdemoApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setMockMvc() { //设置MockMvc
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	public void postTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/craig")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED) //设置为application/x-www-form-urlencoded，这才是运行应用程序时浏览器会发送的内容类型
						.param("title", "BOOK TITLE") //设置表单域
						.param("author", "BOOK AUTHOR")
						.param("description", "BOOK DESCRIPTION")
						.param("isbn", "1234567890"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()) //检查结果为重定向
				.andExpect(MockMvcResultMatchers.header().string("Location", "/readingList"));

		Book expectedBook = new Book();
		expectedBook.setId(1L);
		expectedBook.setReader("craig");
		expectedBook.setTitle("BOOK TITLE");
		expectedBook.setAuthor("BOOK AUTHOR");
		expectedBook.setIsbn("1234567890");
		expectedBook.setDescription("BOOK DESCRIPTION");

		mockMvc.perform(MockMvcRequestBuilders.get("/readingList"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("readingList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("books"))	//检查返回的内容是否与期望一致
			.andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.contains(Matchers.samePropertyValuesAs(expectedBook))));
	}

	@Test
	public void contextLoads() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/readingList"))   // /readingList发起一个 GET 请求
				.andExpect(MockMvcResultMatchers.status().isOk())	// 期望：isOk() 会判断HTTP 200响应码
				.andExpect(MockMvcResultMatchers.view().name("readingList")) //视图的逻辑名称为 readingList
				.andExpect(MockMvcResultMatchers.model().attributeExists("books")) //断定模型包含一个名为 books 的属性
				.andExpect(MockMvcResultMatchers.model().attribute("books",
						Matchers.is(Matchers.empty())));	// 该属性是一个空集合
	}

}
