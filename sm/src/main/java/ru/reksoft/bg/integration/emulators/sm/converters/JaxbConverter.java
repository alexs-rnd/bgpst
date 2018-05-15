package ru.reksoft.bg.integration.emulators.sm.converters;

import org.springframework.stereotype.Component;
import ru.reksoft.bg.integration.emulators.sm.messages.Message;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by chaplygin on 06.10.2016.
 */
@Component
public class JaxbConverter
{
    public Message xmlToJaxbConverter(String fileName)
    {
        try
        {
            File file = new File(fileName);

            JAXBContext context = JAXBContext.newInstance(Message.class);
            Unmarshaller un = context.createUnmarshaller();
            Message message = (Message) un.unmarshal(file);
            return message;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
