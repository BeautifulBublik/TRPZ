package com.example.emailclient.UI;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.springframework.stereotype.Component;

import com.example.emailclient.model.Folder;
import com.example.emailclient.model.User;
import com.example.emailclient.model.Account;
import com.example.emailclient.model.Attachment;
import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.repository.FolderRepository;
import com.example.emailclient.repository.MessageRepository;
import com.example.emailclient.service.AccountService;
import com.example.emailclient.service.EmailMessageService;
import com.example.emailclient.service.UserService;
import com.example.emailclient.service.receiver.MailReceiver;
import com.example.emailclient.service.sender.EmailSender;
import com.example.emailclient.service.sender.LoggingMailSenderDecorator;
import com.example.emailclient.service.sender.RetryMailSenderDecorator;
import com.example.emailclient.singelton.MailReceiverManager;

import jakarta.annotation.PostConstruct;

@Component
public class EmailClientUI extends JFrame {

   
	private static final long serialVersionUID = 1L;
	private FolderRepository folderRepository;
    private MessageRepository messageRepository;
    private UserService userService;
    private AccountService accountService;
    private UserManagementPanel userPanel;
    private EmailMessageService messageService;
    private MailReceiverManager receiverManager;

    private DefaultListModel<Folder> foldersModel = new DefaultListModel<>();
    private DefaultListModel<EmailMessage> messagesModel = new DefaultListModel<>();
    private DefaultListModel<Attachment> attachmentsModel = new DefaultListModel<>();
    private JList<Folder> folderList = new JList<>(foldersModel);
    private JList<EmailMessage> messageList = new JList<>(messagesModel);
    private JList<Attachment> attachmentsList = new JList<>(attachmentsModel);
    private JEditorPane messageContent = new JEditorPane("text/html", "");
    
    private JButton loadMailButton = new JButton("Завантажити пошту");

    
    

	public EmailClientUI(FolderRepository folderRepository, MessageRepository messageRepository,
			UserService userService,AccountService accountService, EmailMessageService messageService,
			MailReceiverManager receiverManager) throws HeadlessException {
		super();
		this.folderRepository = folderRepository;
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.accountService=accountService;
		this.messageService=messageService;
		this.receiverManager=receiverManager;
	}

	@PostConstruct
    public void init() {
        setTitle("Email Client");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        folderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadMessages(folderList.getSelectedValue());
        });
        messageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showMessageContent(messageList.getSelectedValue());
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(topPanel, BorderLayout.NORTH);
        topPanel.add(loadMailButton);

        loadMailButton.addActionListener(this::onLoadMailClicked);

        JPanel attachmentsPanel = new JPanel(new BorderLayout());
        attachmentsPanel.setBorder(BorderFactory.createTitledBorder("Вкладення"));
        attachmentsPanel.add(new JScrollPane(attachmentsList), BorderLayout.CENTER);

        attachmentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attachmentsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    Attachment selected = attachmentsList.getSelectedValue();
                    if (selected != null) {
                        try {
                            Desktop.getDesktop().open(new File(selected.getFilePath()));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Не вдалося відкрити файл: " + ex.getMessage());
                        }
                    }
                }
            }
        });
        JSplitPane messageSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(messageContent), attachmentsPanel);
        messageSplit.setDividerLocation(400);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(messageList), messageSplit);
        messageContent.setEditable(false);
        messageContent.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        messageContent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(folderList), rightSplit);
        JTabbedPane tabs = new JTabbedPane();
        userPanel=new UserManagementPanel(userService, accountService);
        tabs.addTab("Пошта", mainSplit);
        tabs.addTab("Користувачі", userPanel);
        add(tabs, BorderLayout.CENTER);
        
       

  
        

        

        loadFolders();
        setVisible(true);
    }

    private void loadFolders() {
        foldersModel.clear();
        List<Folder> folders = folderRepository.findAll();
        folders.forEach(foldersModel::addElement);
    }

    private void loadMessages(Folder folder) {
        messagesModel.clear();
        List<EmailMessage> messages;
        if(folder!=null) {
        	 messages = messageRepository.findByFolder_Id(folder.getId());
        }else {
        	 messages = messageRepository.findAll();
        }
		messages.forEach(messagesModel::addElement);
    }

    private void showMessageContent(EmailMessage msg) {
    	attachmentsModel.clear();
    	if (msg == null) {
    	        System.out.println("Повідомлення не вибране або не завантажене.");
    	        return;
    	    }
    	    List<Attachment> attachments = msg.getAttachments();
    	    if (attachments != null && !attachments.isEmpty()) {
    	        for (Attachment a : attachments) {
    	            attachmentsModel.addElement(a);
    	    }
    	}
        messageContent.setText("<b>Тема:</b> " + msg.getSubject() + "<hr>" + msg.getBody());
        messageContent.setCaretPosition(0);
    }
    private void onLoadMailClicked(ActionEvent e) {
    	messagesModel.clear();
        User selectedUser = userPanel.getSelectedUser();
        Account selectedAccount = userPanel.getSelectedAccount();

        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Оберіть користувача!", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, "Оберіть поштовий акаунт користувача!", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {"IMAP (залишає листи на сервері)", "POP3 (завантажує і видаляє листи)"};
        int choice = JOptionPane.showOptionDialog(this,
                "Виберіть спосіб отримання пошти для акаунта:\n" + selectedAccount.getEmail() +
                        "\n\nIMAP — синхронізує пошту з сервером.\nPOP3 — завантажує листи локально.",
                "Вибір протоколу",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        String[] counts = {"10", "20", "50", "100", "Всі"};
        String countStr = (String) JOptionPane.showInputDialog(this,
                "Скільки останніх листів завантажити?",
                "Кількість листів",
                JOptionPane.QUESTION_MESSAGE,
                null,
                counts,
                "20");

        if (countStr == null) return; 

        int count = countStr.equals("Всі") ? Integer.MAX_VALUE : Integer.parseInt(countStr);
        	
        if (choice == JOptionPane.CLOSED_OPTION) return;
        MailReceiver receiver = receiverManager
        		.getReceiver(selectedAccount.getProvider(), choice == 1);


        try {
        	List<EmailMessage> list;
        	if(choice==1) {
        		List<EmailMessage> list1=receiver.receiveEMails(selectedAccount.getEmail(), selectedAccount.getPassword(), count);
        		list=messageService.findByAccountEmail(selectedAccount);
        	}else {
        		list=receiver.receiveEMails(selectedAccount.getEmail(), selectedAccount.getPassword(), count);
        	}
            for(EmailMessage message: list) {
            	boolean alreadyExists = false;
                for (int i = 0; i < messagesModel.size(); i++) {
                    EmailMessage existing = messagesModel.getElementAt(i);
                    if (Objects.equals(existing.getSubject(), message.getSubject()) &&
                        Objects.equals(existing.getDate(), message.getDate())) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    messagesModel.addElement(message);
                }
            }
            JOptionPane.showMessageDialog(this,
                    "Пошта з акаунта " + selectedAccount.getEmail() + " завантажена успішно!",
                    "Успіх", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Помилка при завантаженні пошти: " + ex.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

