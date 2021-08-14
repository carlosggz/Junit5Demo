package com.example.junit5demo.infrastructure.helpers;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.Map;

@UtilityClass
public class TemplatesHelper {

    public String getRenderedTemplate(String templateName, Map<String, Object> scopes) throws IOException {

        final StringReader stringReader = new StringReader(getContentFromTemplate(templateName));

        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(stringReader, "customTemplate");
        mustache.execute(writer, scopes);
        writer.flush();

        return writer.toString();
    }

    public String getContentFromTemplate(String templateName) throws IOException {
        final File file = new File(TemplatesHelper.class.getClassLoader().getResource(templateName).getFile());
        return new String(Files.readAllBytes(file.toPath()));
    }
}
