package com.example.emailclient.UI;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.springframework.stereotype.Component;

import com.example.emailclient.model.Folder;
import com.example.emailclient.model.Message;
import com.example.emailclient.repository.FolderRepository;
import com.example.emailclient.repository.MessageRepository;
import com.example.emailclient.service.AccountService;
import com.example.emailclient.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class EmailClientUI extends JFrame {

   
	private static final long serialVersionUID = 1L;
	private FolderRepository folderRepository;
    private MessageRepository messageRepository;
    private UserService userService;
    private AccountService accountService;

    private DefaultListModel<Folder> foldersModel = new DefaultListModel<>();
    private DefaultListModel<Message> messagesModel = new DefaultListModel<>();
    private JList<Folder> folderList = new JList<>(foldersModel);
    private JList<Message> messageList = new JList<>(messagesModel);
    private JTextArea messageContent = new JTextArea();

    
    

	public EmailClientUI(FolderRepository folderRepository, MessageRepository messageRepository,
			UserService userService,AccountService accountService ) throws HeadlessException {
		super();
		this.folderRepository = folderRepository;
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.accountService=accountService;
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

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(messageList), new JScrollPane(messageContent));
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(folderList), rightSplit);
        add(mainSplit, BorderLayout.CENTER);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Пошта", mainSplit);
        tabs.addTab("Користувачі", new UserManagementPanel(userService, accountService));
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
        if (folder == null) return;
        List<Message> messages = messageRepository.findByFolder_Id(folder.getId());
        messages.forEach(messagesModel::addElement);
    }

    private void showMessageContent(Message msg) {
        if (msg == null) return;
        messageContent.setText("Subject: " + msg.getSubject() + "\n\n" + msg.getBody());
    }
}

