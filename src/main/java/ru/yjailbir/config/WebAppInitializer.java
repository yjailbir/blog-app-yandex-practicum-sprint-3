package ru.yjailbir.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic appServlet = servletContext.addServlet("dispatcher", dispatcherServlet);

        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                "/tmp", 5 * 1024 * 1024, 10 * 1024 * 1024, 2 * 1024 * 1024);
        appServlet.setMultipartConfig(multipartConfigElement);
    }
}
