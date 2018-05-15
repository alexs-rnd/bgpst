package ru.reksoft.bg.integration.emulators.sm;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.reksoft.bg.integration.emulators.sm.converters.JaxbConverter;
import ru.reksoft.bg.integration.emulators.sm.messages.Message;

/**
 * Created by liyv on 23.06.2016.
 */
@Component
public class TestSMServer implements CommandLineRunner{

    public TestSMServer() {}


    @Autowired
    private TestSMServerThread testSMServerThread;

    @Autowired
    private JaxbConverter jaxbConverter;

    private static final String FILE_NAME = "message1.xml";

    @Override
    public void run(String... strings) throws Exception {

        Thread t = new Thread(new Runnable() {
            public void run()
            {
                testSMServerThread.mainMethod(strings.length > 0 ? Integer.valueOf(strings[0]) : 8880);
            }
        });
        t.start();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter command");

            while(true) {
                String text = scanner.nextLine();

                System.out.println("We type: "+ text);

                if(text.equals("1")) {
                    testSMServerThread.sendMessageFlag = true;

                    testSMServerThread.message = jaxbConverter.xmlToJaxbConverter(FILE_NAME);
                    testSMServerThread.CalculateMessLen();

                    System.out.println();
                }
            }
        }
    }
}