package com.nevs.web;

import com.nevs.web.interceptor.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
//扫描配置的过滤器和拦截器，因为这两个是基于Servlet的
@ServletComponentScan
//配置bean时需要加入这个注解
@Configuration
//开启定时任务
@EnableScheduling
public class NevsWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(NevsWebApplication.class, args);
	}

	@Value("${nevs.upload}")
	private String upload;

	@Value("${nevs.download}")
	private String download;

	/**
	 * 配置拦截器
	 * 由于添加拦截器需要继承WebMvcConfigurerAdapter类
	 * 需要用@Bean将MyInterceptor注入容器，才能在拦截器中注入，否为注入为空
	 * 或者在自定义拦截器类上加上@Component注解，通过注入方式配置
	 */
	@Configuration
	class WebMvcConfigurer extends WebMvcConfigurerAdapter {

		/*@Bean
		public MyInterceptor myInterceptor() {
			return new MyInterceptor();
		}*/

		@Autowired
		private CommonInterceptor commonInterceptor;

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			//registry.addInterceptor(commonInterceptor).addPathPatterns("/**");
			//super.addInterceptors(registry);
		}

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/upload/**").addResourceLocations("file:" + upload);
			registry.addResourceHandler("/download/**").addResourceLocations("file:" + download);
			super.addResourceHandlers(registry);
		}
	}

	/**
	 * 配置上传文件大小限制
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//单个文件最大（KB/MB）
		factory.setMaxFileSize("5MB");
		//设置总上传数据总大小
		factory.setMaxRequestSize("15MB");
		return factory.createMultipartConfig();
	}
}
