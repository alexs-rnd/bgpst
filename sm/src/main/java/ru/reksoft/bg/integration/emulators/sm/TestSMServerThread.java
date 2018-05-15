package ru.reksoft.bg.integration.emulators.sm;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.reksoft.bg.integration.emulators.sm.messages.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

/**
 * Created by chaplygin on 05.10.2016.
 */
@Component
public class TestSMServerThread {

    private int messLen = 330;
    public final static byte POLYNOM_16_12_5[] = {(byte) 0x01, (byte) 0x00, (byte) 0x11, (byte) 0x21, (byte) 0x21, (byte) 0x42, (byte) 0x31, (byte) 0x63, (byte) 0x41, (byte) 0x84, (byte) 0x51, (byte) 0xa5, (byte) 0x61, (byte) 0xc6, (byte) 0x71, (byte) 0xe7,
            (byte) 0x80, (byte) 0x08, (byte) 0x90, (byte) 0x29, (byte) 0xa0, (byte) 0x4a, (byte) 0xb0, (byte) 0x6b, (byte) 0xc0, (byte) 0x8c, (byte) 0xd0, (byte) 0xad, (byte) 0xe0, (byte) 0xce, (byte) 0xf0, (byte) 0xef,
            (byte) 0x13, (byte) 0x31, (byte) 0x03, (byte) 0x10, (byte) 0x33, (byte) 0x73, (byte) 0x23, (byte) 0x52, (byte) 0x53, (byte) 0xb5, (byte) 0x43, (byte) 0x94, (byte) 0x73, (byte) 0xf7, (byte) 0x63, (byte) 0xd6,
            (byte) 0x92, (byte) 0x39, (byte) 0x82, (byte) 0x18, (byte) 0xb2, (byte) 0x7b, (byte) 0xa2, (byte) 0x5a, (byte) 0xd2, (byte) 0xbd, (byte) 0xc2, (byte) 0x9c, (byte) 0xf2, (byte) 0xff, (byte) 0xe2, (byte) 0xde,
            (byte) 0x25, (byte) 0x62, (byte) 0x35, (byte) 0x43, (byte) 0x05, (byte) 0x20, (byte) 0x15, (byte) 0x01, (byte) 0x65, (byte) 0xe6, (byte) 0x75, (byte) 0xc7, (byte) 0x45, (byte) 0xa4, (byte) 0x55, (byte) 0x85,
            (byte) 0xa4, (byte) 0x6a, (byte) 0xb4, (byte) 0x4b, (byte) 0x84, (byte) 0x28, (byte) 0x94, (byte) 0x09, (byte) 0xe4, (byte) 0xee, (byte) 0xf4, (byte) 0xcf, (byte) 0xc4, (byte) 0xac, (byte) 0xd4, (byte) 0x8d,
            (byte) 0x37, (byte) 0x53, (byte) 0x27, (byte) 0x72, (byte) 0x17, (byte) 0x11, (byte) 0x07, (byte) 0x30, (byte) 0x77, (byte) 0xd7, (byte) 0x67, (byte) 0xf6, (byte) 0x57, (byte) 0x95, (byte) 0x47, (byte) 0xb4,
            (byte) 0xb6, (byte) 0x5b, (byte) 0xa6, (byte) 0x7a, (byte) 0x96, (byte) 0x19, (byte) 0x86, (byte) 0x38, (byte) 0xf6, (byte) 0xdf, (byte) 0xe6, (byte) 0xfe, (byte) 0xd6, (byte) 0x9d, (byte) 0xc6, (byte) 0xbc,
            (byte) 0x49, (byte) 0xc4, (byte) 0x59, (byte) 0xe5, (byte) 0x69, (byte) 0x86, (byte) 0x79, (byte) 0xa7, (byte) 0x09, (byte) 0x40, (byte) 0x19, (byte) 0x61, (byte) 0x29, (byte) 0x02, (byte) 0x39, (byte) 0x23,
            (byte) 0xc8, (byte) 0xcc, (byte) 0xd8, (byte) 0xed, (byte) 0xe8, (byte) 0x8e, (byte) 0xf8, (byte) 0xaf, (byte) 0x88, (byte) 0x48, (byte) 0x98, (byte) 0x69, (byte) 0xa8, (byte) 0x0a, (byte) 0xb8, (byte) 0x2b,
            (byte) 0x5b, (byte) 0xf5, (byte) 0x4b, (byte) 0xd4, (byte) 0x7b, (byte) 0xb7, (byte) 0x6b, (byte) 0x96, (byte) 0x1b, (byte) 0x71, (byte) 0x0b, (byte) 0x50, (byte) 0x3b, (byte) 0x33, (byte) 0x2b, (byte) 0x12,
            (byte) 0xda, (byte) 0xfd, (byte) 0xca, (byte) 0xdc, (byte) 0xfa, (byte) 0xbf, (byte) 0xea, (byte) 0x9e, (byte) 0x9a, (byte) 0x79, (byte) 0x8a, (byte) 0x58, (byte) 0xba, (byte) 0x3b, (byte) 0xaa, (byte) 0x1a,
            (byte) 0x6d, (byte) 0xa6, (byte) 0x7d, (byte) 0x87, (byte) 0x4d, (byte) 0xe4, (byte) 0x5d, (byte) 0xc5, (byte) 0x2d, (byte) 0x22, (byte) 0x3d, (byte) 0x03, (byte) 0x0d, (byte) 0x60, (byte) 0x1d, (byte) 0x41,
            (byte) 0xec, (byte) 0xae, (byte) 0xfc, (byte) 0x8f, (byte) 0xcc, (byte) 0xec, (byte) 0xdc, (byte) 0xcd, (byte) 0xac, (byte) 0x2a, (byte) 0xbc, (byte) 0x0b, (byte) 0x8c, (byte) 0x68, (byte) 0x9c, (byte) 0x49,
            (byte) 0x7f, (byte) 0x97, (byte) 0x6f, (byte) 0xb6, (byte) 0x5f, (byte) 0xd5, (byte) 0x4f, (byte) 0xf4, (byte) 0x3f, (byte) 0x13, (byte) 0x2f, (byte) 0x32, (byte) 0x1f, (byte) 0x51, (byte) 0x0f, (byte) 0x70,
            (byte) 0xfe, (byte) 0x9f, (byte) 0xee, (byte) 0xbe, (byte) 0xde, (byte) 0xdd, (byte) 0xce, (byte) 0xfc, (byte) 0xbe, (byte) 0x1b, (byte) 0xae, (byte) 0x3a, (byte) 0x9e, (byte) 0x59, (byte) 0x8e, (byte) 0x78,
            (byte) 0x90, (byte) 0x88, (byte) 0x80, (byte) 0xa9, (byte) 0xb0, (byte) 0xca, (byte) 0xa0, (byte) 0xeb, (byte) 0xd0, (byte) 0x0c, (byte) 0xc0, (byte) 0x2d, (byte) 0xf0, (byte) 0x4e, (byte) 0xe0, (byte) 0x6f,
            (byte) 0x11, (byte) 0x80, (byte) 0x01, (byte) 0xa1, (byte) 0x31, (byte) 0xc2, (byte) 0x21, (byte) 0xe3, (byte) 0x51, (byte) 0x04, (byte) 0x41, (byte) 0x25, (byte) 0x71, (byte) 0x46, (byte) 0x61, (byte) 0x67,
            (byte) 0x82, (byte) 0xb9, (byte) 0x92, (byte) 0x98, (byte) 0xa2, (byte) 0xfb, (byte) 0xb2, (byte) 0xda, (byte) 0xc2, (byte) 0x3d, (byte) 0xd2, (byte) 0x1c, (byte) 0xe2, (byte) 0x7f, (byte) 0xf2, (byte) 0x5e,
            (byte) 0x03, (byte) 0xb1, (byte) 0x13, (byte) 0x90, (byte) 0x23, (byte) 0xf3, (byte) 0x33, (byte) 0xd2, (byte) 0x43, (byte) 0x35, (byte) 0x53, (byte) 0x14, (byte) 0x63, (byte) 0x77, (byte) 0x73, (byte) 0x56,
            (byte) 0xb4, (byte) 0xea, (byte) 0xa4, (byte) 0xcb, (byte) 0x94, (byte) 0xa8, (byte) 0x84, (byte) 0x89, (byte) 0xf4, (byte) 0x6e, (byte) 0xe4, (byte) 0x4f, (byte) 0xd4, (byte) 0x2c, (byte) 0xc4, (byte) 0x0d,
            (byte) 0x35, (byte) 0xe2, (byte) 0x25, (byte) 0xc3, (byte) 0x15, (byte) 0xa0, (byte) 0x05, (byte) 0x81, (byte) 0x75, (byte) 0x66, (byte) 0x65, (byte) 0x47, (byte) 0x55, (byte) 0x24, (byte) 0x45, (byte) 0x05,
            (byte) 0xa6, (byte) 0xdb, (byte) 0xb6, (byte) 0xfa, (byte) 0x86, (byte) 0x99, (byte) 0x96, (byte) 0xb8, (byte) 0xe6, (byte) 0x5f, (byte) 0xf6, (byte) 0x7e, (byte) 0xc6, (byte) 0x1d, (byte) 0xd6, (byte) 0x3c,
            (byte) 0x27, (byte) 0xd3, (byte) 0x37, (byte) 0xf2, (byte) 0x07, (byte) 0x91, (byte) 0x17, (byte) 0xb0, (byte) 0x67, (byte) 0x57, (byte) 0x77, (byte) 0x76, (byte) 0x47, (byte) 0x15, (byte) 0x57, (byte) 0x34,
            (byte) 0xd8, (byte) 0x4c, (byte) 0xc8, (byte) 0x6d, (byte) 0xf8, (byte) 0x0e, (byte) 0xe8, (byte) 0x2f, (byte) 0x98, (byte) 0xc8, (byte) 0x88, (byte) 0xe9, (byte) 0xb8, (byte) 0x8a, (byte) 0xa8, (byte) 0xab,
            (byte) 0x59, (byte) 0x44, (byte) 0x49, (byte) 0x65, (byte) 0x79, (byte) 0x06, (byte) 0x69, (byte) 0x27, (byte) 0x19, (byte) 0xc0, (byte) 0x09, (byte) 0xe1, (byte) 0x39, (byte) 0x82, (byte) 0x29, (byte) 0xa3,
            (byte) 0xca, (byte) 0x7d, (byte) 0xda, (byte) 0x5c, (byte) 0xea, (byte) 0x3f, (byte) 0xfa, (byte) 0x1e, (byte) 0x8a, (byte) 0xf9, (byte) 0x9a, (byte) 0xd8, (byte) 0xaa, (byte) 0xbb, (byte) 0xba, (byte) 0x9a,
            (byte) 0x4b, (byte) 0x75, (byte) 0x5b, (byte) 0x54, (byte) 0x6b, (byte) 0x37, (byte) 0x7b, (byte) 0x16, (byte) 0x0b, (byte) 0xf1, (byte) 0x1b, (byte) 0xd0, (byte) 0x2b, (byte) 0xb3, (byte) 0x3b, (byte) 0x92,
            (byte) 0xfc, (byte) 0x2e, (byte) 0xec, (byte) 0x0f, (byte) 0xdc, (byte) 0x6c, (byte) 0xcc, (byte) 0x4d, (byte) 0xbc, (byte) 0xaa, (byte) 0xac, (byte) 0x8b, (byte) 0x9c, (byte) 0xe8, (byte) 0x8c, (byte) 0xc9,
            (byte) 0x7d, (byte) 0x26, (byte) 0x6d, (byte) 0x07, (byte) 0x5d, (byte) 0x64, (byte) 0x4d, (byte) 0x45, (byte) 0x3d, (byte) 0xa2, (byte) 0x2d, (byte) 0x83, (byte) 0x1d, (byte) 0xe0, (byte) 0x0d, (byte) 0xc1,
            (byte) 0xee, (byte) 0x1f, (byte) 0xfe, (byte) 0x3e, (byte) 0xce, (byte) 0x5d, (byte) 0xde, (byte) 0x7c, (byte) 0xae, (byte) 0x9b, (byte) 0xbe, (byte) 0xba, (byte) 0x8e, (byte) 0xd9, (byte) 0x9e, (byte) 0xf8,
            (byte) 0x6f, (byte) 0x17, (byte) 0x7f, (byte) 0x36, (byte) 0x4f, (byte) 0x55, (byte) 0x5f, (byte) 0x74, (byte) 0x2f, (byte) 0x93, (byte) 0x3f, (byte) 0xb2, (byte) 0x0f, (byte) 0xd1, (byte) 0x1f, (byte) 0xf0};

    public static volatile boolean sendMessageFlag = false;
    private static Charset utf8 = Charset.forName("utf-8");
    private static CharsetEncoder utf8Enc = utf8.newEncoder();
    private static CharsetDecoder utf8Dec = utf8.newDecoder();

    public Message message;
    public int id;

    private static byte alarm1[] = {(byte) 0x4a, (byte) 0x01
            , (byte) 0x01
            , (byte) 0x1a, (byte) 0x48, (byte) 0x00, (byte) 0x00
            //, (byte) 0xe8, (byte) 0x03, (byte) 0x00, (byte) 0x00//kard key
            , (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00//kard key
            , (byte) 0x00, (byte) 0x00
            , (byte) 0xcb, (byte) 0xe5, (byte) 0xed, (byte) 0xe8, (byte) 0xed, (byte) 0xe0, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0x31, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0x31, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0xcf, (byte) 0xee, (byte) 0xe6, (byte) 0xe0, (byte) 0xf0, (byte) 0xed, (byte) 0xe0, (byte) 0xff, (byte) 0x20, (byte) 0xf2, (byte) 0xf0, (byte) 0xe5, (byte) 0xe2, (byte) 0xee, (byte) 0xe3, (byte) 0xe0, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0xd2, (byte) 0xe5, (byte) 0xf1, (byte) 0xf2, (byte) 0x20, (byte) 0x31, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0x00
            , (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
            , (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
// ---------add required fields with alarm type information
//            , (byte) 0x1a, (byte) 0x48, (byte) 0x00, (byte) 0x00
//            , (byte) 0xe8, (byte) 0x03, (byte) 0x00, (byte) 0x00
            , (byte) 0x07, (byte) 0x10, (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
//---------------------------------------------------------
            , (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            , (byte) 0xdf, (byte) 0xc0, (byte) 0xd0, (byte) 0xcb, (byte) 0x50, (byte) 0xc5, (byte) 0xe4, (byte) 0x40
            , (byte) 0x44, (byte) 0xb7,

    };


    public void sendImmediately(ServerSocket server) {
        try {
            new Sender(server.accept());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainMethod(int port) {
        try {

            // привинтить сокет на локалхост, порт 3128
            ServerSocket server = new ServerSocket(port);

            System.out.println("server is started");

            // слушаем порт
            while (true) {
                // ждём нового подключения, после чего запускаем обработку клиента
                // в новый вычислительный поток и увеличиваем счётчик на единичку

                sendImmediately(server);

            }
        } catch (Exception e) {
            System.out.println("init error: " + e);
        } // вывод исключений
    }

    public void CalculateMessLen() {
        messLen = 330;
        if(message.getDescription() != null) {
            messLen += message.getDescription().length();
        }
    }


    class Sender extends Thread {
        Socket s;

        public Sender(Socket s) {
            this.s = s;
            setDaemon(true);
            setPriority(NORM_PRIORITY);
            start();
        }

        public void run() {
            try {
                DataInputStream inStream;
                DataOutputStream outStream;

                inStream = new DataInputStream(s.getInputStream());
                outStream = new DataOutputStream(s.getOutputStream());

                byte buf[] = new byte[1024];
//                byte sendbuf[] = new byte[alarm1.length];
                byte empty_buff[] = new byte[0];

                while (true) {
                    try {

                        int count = inStream.read(buf);

                        System.out.println("try to read " + buf);
                        int len = inStream.read(buf, 0, 1024);
                        //TO DO read more than 1024 byte
//                        if (len > 0 && buf[2] == 3 && count % 20 == 0) {
                        if (sendMessageFlag) {
//                            System.arraycopy(line, 0, sendbuf, 0, line.length());
//                            outStream.write(sendbuf);
                            System.out.println("before send len : " + messLen);
                            sendMessageFlag = false;
                            ByteBuffer buffer = ByteBuffer.allocate(messLen);
                            buffer.order(ByteOrder.LITTLE_ENDIAN);
                            WriteMessageIntoBuffer(buffer);
                            outStream.write(buffer.array());
//                            outStream.write(alarm1);
                            System.out.println("send message  ");

                        } else
                            outStream.write(empty_buff);

                    } catch (Exception e) {
                        break;
                    }
//                    count++;
                }

                s.close();
            } catch (Exception e) {
                try {
                    s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.println("init error: " + e);
            } // вывод исключений
        }
    }

    private void WriteMessageIntoBuffer(ByteBuffer buffer) throws UnsupportedEncodingException {
//        int len = (StringUtils.isEmpty(message.getDescription())) ? messLen : messLen + message.getDescription().getBytes("Cp1251").length;
        buffer.putShort((short) (messLen)); //temporary for length
        buffer.put((byte) 1);
        buffer.putInt(id);
        id++;
        buffer.putInt(message.getCardKey() == null ? 0 : message.getCardKey());
        buffer.putShort(message.getFloor() == null ? (short)0 : message.getFloor().shortValue());
        WriteFixedBufferFromString(buffer, 30, message.getStreet() == null ? "" : message.getStreet());
        WriteFixedBufferFromString(buffer, 10, message.getHouseNumber() == null ? "" : message.getHouseNumber());
        WriteFixedBufferFromString(buffer, 5, message.getCorps() == null ? "" : message.getCorps());
        WriteFixedBufferFromString(buffer, 50, message.getAlarmName() == null ? "" : message.getAlarmName());
        WriteFixedBufferFromString(buffer, 30, message.getObjectName() == null ? "" : message.getObjectName());
        WriteFixedBufferFromString(buffer, 5, message.getFraction() == null ? "" : message.getFraction());
        buffer.put(message.getPorch() == null ? (byte)0 : message.getPorch().byteValue());
        WriteFixedBufferFromString(buffer, 10, message.getPorchNumber() == null ? "" : message.getPorchNumber());
        WriteFixedBufferFromString(buffer, 10, message.getFlatNumber() == null ? "" : message.getFlatNumber());
        WriteFixedBufferFromString(buffer, 30, message.getPhone() == null ? "" : message.getPhone());
        buffer.putInt(message.getEvCode() == null ? 0 : message.getEvCode());
        buffer.putInt(message.getEvAddCode() == null ? 0 : message.getEvAddCode());
        if(!StringUtils.isEmpty(message.getDescription())){
            buffer.putShort((short)message.getDescription().getBytes("Cp1251").length);//obj descr
            WriteFixedBufferFromString(buffer, message.getDescription().getBytes("Cp1251").length, message.getDescription());
        }else{
            buffer.putShort((short) 0);//obj descr
        }

        buffer.putShort((short) 0);//tender spot
        buffer.putShort((short) 0);//chart

        for (int i = 0; i < 16 * 2 + 16 + 16 * 2 + 16 * 2; i++)
            buffer.put((byte) 0);

        buffer.putDouble(GetDelphyTime());

        KS_16_12_5_1(buffer.position(), buffer.array(), buffer);
    }

    private double GetDelphyTime() {
        Date delphiStart = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            delphiStart = ft.parse("1899-12-30");
        } catch (java.text.ParseException e) {
        }

        Date current = new Date();
        double res = ((double) (current.getTime() - delphiStart.getTime()))/(24 * 60 * 60 * 1000);
        return res;
    }

    private void WriteFixedBufferFromString(ByteBuffer buffer, int n, String str) {
        try {
            if(str.length() > n) {
                buffer.put(str.substring(0, n).getBytes("Cp1251"));
            } else {
                buffer.put(str.getBytes("Cp1251"));
                String e = new String(" ");

                for(int i = str.length(); i < n; ++i) {
                    buffer.put(e.getBytes("Cp1251"));
                }
            }
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }
    }

    public static void KS_16_12_5_1(int len, byte[] dan, ByteBuffer buffer) {
        byte a = 0, o = 0, o1 = 0;

        for (int j = 0; j < len + 2; j++) {
            if (j >= len)
                o1 = 0;
            else
                o1 = dan[j];

            o = (byte) ((byte) (o ^ POLYNOM_16_12_5[(a & 0xff) * 2]) ^ 0x01);
            o1 = (byte) (o1 ^ POLYNOM_16_12_5[(a & 0xff) * 2 + 1]);
            a = o;
            o = o1;
        }
        buffer.put(a);
        buffer.put(o1);
    }
}
