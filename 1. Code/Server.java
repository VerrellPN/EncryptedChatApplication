
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class Server extends JPanel implements Runnable, ActionListener, KeyListener {


	private ServerSocket serversocket;
	private BufferedReader br1, br2;
	private PrintWriter pr1;
	private Socket socket;
	Thread t1;
	private String sIn="", plainText="", cipherText="";
	private JScrollPane jsp;
	private JTextField text;
	private String sText="", sChat="", sStatus="";
	private JLabel status;
	private JTextArea chat, cipherField;
	private JButton send;
	private boolean running;
	boolean connect;
	TranspositionCipher cipher = new TranspositionCipher();
	
public Server() {
  
	try {
		t1 = new Thread(this);
		t1.start();
		
		serversocket = new ServerSocket(5000);
	
		} catch (Exception e) {
    }
	
 }


public void run() {
	init();
	
	while(running) {
	
		update();
		
		try {
				int i = 0;
				if( socket == null && i == 0) {
					socket = serversocket.accept();
					sStatus = "Connected with " +  socket.getInetAddress().getHostAddress();
					i++;
				}
				connect = socket.isConnected() && !socket.isClosed();
				//update();
				pr1 = new PrintWriter(socket.getOutputStream(), true);
				br2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				sIn = br2.readLine();
				cipherText = sIn;
				plainText = cipher.doDecryption(sIn);	
				cipherField.setText(cipherText);
				sChat = sChat + "\n" + "Client : " + plainText;
			}
		catch(Exception e) {	
		}
		}
}	

public void update() {
	
	chat.setText(sChat);
	//cipherField.setText(cipherText);
	status.setText(sStatus);
}

public void init() {
	running = true;
	setLayout(null);

	Font f = new Font("Arial", Font.BOLD,16);
	
	chat = new JTextArea(sChat);
	chat.setBackground(Color.WHITE);
	chat.setFont(f);
    chat.setEditable(false);
	chat.setLineWrap(true);
	chat.setBounds(20, 65, 260, 290);
	if(!connect) {
		sStatus = "Waiting for connection . . .";
	}
	
	cipherField = new JTextArea(cipherText);
	cipherField.setBackground(Color.WHITE);
	cipherField.setFont(f);
    cipherField.setEditable(false);
	cipherField.setLineWrap(true);
	cipherField.setBounds(300, 65, 260, 290);
	add(cipherField);
	
	status = new JLabel(sStatus);
	status.setBackground(Color.WHITE);
	status.setFont(f);
	status.setBounds(20,10,600,50);
	add(status);
	
	jsp = new JScrollPane(chat);
	jsp.setBounds(20,65,260,290);
	add(jsp);
	
	text = new JTextField();
	text.setFont(f);
	text.setBounds(20,370,500,40);
	text.addKeyListener(this);
	add(text);

	text.requestFocus();
	
	send = new JButton("SEND");
	send.setBounds(535,370,85,40);
	send.addActionListener(this);
	add(send);
}

public void actionPerformed(ActionEvent e)
{
	Object source = e.getSource();
	if(connect) {
		if(source == send)
		{
			sChat = sChat + "\n" + "Server : " + text.getText();
			plainText = text.getText();
			cipherText = cipher.doEncryption(text.getText());
			sText = plainText;
			pr1.println(cipherText);
			chat.setText(sChat);
			cipherField.setText(cipherText);
			text.setText(null);
			text.requestFocus();
		}
	}
}

public void keyTyped(KeyEvent key) {
	
}

public void keyPressed(KeyEvent key) {
	int code = key.getKeyCode();
	if(code == KeyEvent.VK_ENTER) {
		send.doClick();
	}
}

public void keyReleased(KeyEvent key) {}

}
