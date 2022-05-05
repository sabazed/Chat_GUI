package chat;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Chat {

    protected static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {

        Socket socket = null;

        while (socket == null) {
            try {
                in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter <port> in order to start the chat server "
                                + "or <host>:<port> in order to connect to a running server. "
                                + "Enter exit for exiting the chat.\n");
                String input = in.readLine();
                if (!input.contains(":")) {
                    ServerSocket server = new ServerSocket(Integer.parseInt(input));
                    System.out.println("Server is started on port " + input + ", accepting connections...");
                    socket = server.accept();
                    server.close();
                }
                else {
                    int port = Integer.parseInt(input.substring(input.indexOf(":") + 1));
                    String host = input.substring(0, input.indexOf(":"));
                    socket = new Socket(host, port);
                }
                break;
            }
            catch (UnknownHostException e) {
                System.out.println("Unknown host input, please try again!");
            }
            catch (PortUnreachableException e) {
                System.out.println("Unreachable port input, please try again!");
            }
            catch (IOException e) {
                System.out.println("I/O Error, please try again!");
            }
            catch (IllegalArgumentException e) {
                System.out.println("Wrong input!");
            }

        }

        if (socket == null) return;
        Chat_GUI.main(args);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            Thread reader = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (Chat_GUI.frame == null) {
                                System.out.println("Connection interrupted!");
                                break;
                            }
                            String output = in.readLine();
                            if (!output.isBlank()) {
                                if (output.length() > 64) {
                                    output = Chat_GUI.getNearestWord(output, 63);
                                }
                                JLabel newMessage = new JLabel("<html> &lt; " + output + "<html/>");
                                newMessage.setFont(Chat_GUI.font);
                                Chat_GUI.messages.add(newMessage);
                                Chat_GUI.messages.revalidate();
                                Chat_GUI.messages.repaint();
                                JScrollBar sb = Chat_GUI.scrollPane.getVerticalScrollBar();
                                sb.setValue(sb.getMaximum());
                            }
                        }
                    }
                    catch (IOException e) {
                        System.out.println("\nReader target disconnected!");
                        interrupt();
                    }
                }
            };

            reader.start();

            try {
                reader.join();
            }
            catch (InterruptedException e) {
                System.out.println("Connection interrupted!");
            }

            System.out.println("Exiting...");

        }
        catch (IOException e) {
            System.out.println("I/O exception occurred!");
        }

    }

}
