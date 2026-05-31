package com.japan.blog.seguridad;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    @Value("${japan.ruta.imagenes}")
    private String rutaImagenes;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File file = new File(rutaImagenes);
        String absolutePath = file.getAbsolutePath();

        if (!absolutePath.endsWith(File.separator)) {
            absolutePath += File.separator;
        }

        String location = "file:" + absolutePath;
        // /Users/seve/prog/japan/imagenes-subidas/

        System.out.println("cargando  imagenes desde: " + location);

        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations(location);
    }
}
