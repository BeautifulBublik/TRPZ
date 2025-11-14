package com.example.emailclient.UI;


import com.example.emailclient.decorator.EmailSender;
import com.example.emailclient.model.Attachment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WriteMessagePanel extends JPanel {

    private final JTextField toField = new JTextField();
    private final JTextField subjectField = new JTextField();
    private final JTextArea bodyArea = new JTextArea();
    private final JButton attachButton = new JButton("Прикріпити");
    private final JButton sendButton = new JButton("Надіслати");

    private final DefaultListModel<Attachment> attachmentsModel = new DefaultListModel<>();
    private final JList<Attachment> attachmentsList = new JList<>(attachmentsModel);

    private final EmailSender mailSender;
    private final String fromEmail;
    private final String fromPassword;

    private final List<Attachment> attachments = new ArrayList<>();


    public WriteMessagePanel(EmailSender mailSender, String fromEmail, String fromPassword) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildBodyPanel(), BorderLayout.CENTER);
        add(buildButtonsPanel(), BorderLayout.SOUTH);

        initListeners();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        panel.add(new JLabel("Кому:"));
        panel.add(toField);

        panel.add(new JLabel("Тема:"));
        panel.add(subjectField);

        return panel;
    }

    private JPanel buildBodyPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(bodyArea);
        scroll.setPreferredSize(new Dimension(400, 250));

        panel.add(scroll, BorderLayout.CENTER);

        JPanel attachPanel = new JPanel(new BorderLayout());
        attachPanel.add(attachButton, BorderLayout.WEST);
        attachPanel.add(new JLabel("Вкладення:"), BorderLayout.NORTH);
        attachPanel.add(new JScrollPane(attachmentsList), BorderLayout.CENTER);

        panel.add(attachPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(sendButton);
        return panel;
    }

    private void initListeners() {
        attachButton.addActionListener(e -> onAddAttachment());
        sendButton.addActionListener(e -> onSendMessage());
    }

    private void onAddAttachment() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File file : chooser.getSelectedFiles()) {

                Attachment att = new Attachment();
                att.setFileName(file.getName());
                att.setFilePath(file.getAbsolutePath());

                attachments.add(att);
                attachmentsModel.addElement(att);
            }
        }
    }

    private void onSendMessage() {
        String to = toField.getText().trim();
        String subject = subjectField.getText().trim();
        String body = bodyArea.getText().trim();

        if (to.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Поле 'Кому' порожнє", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (attachments.isEmpty()) {
                mailSender.sendEmail(fromEmail, fromPassword, to, subject, body);
            } else {
                // Якщо у тебе є Sender з вкладеннями — тут легко додати
                mailSender.sendEmail(fromEmail, fromPassword, to, subject, body);
            }

            JOptionPane.showMessageDialog(this, "Лист успішно відправлено!");
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Помилка відправки: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        toField.setText("");
        subjectField.setText("");
        bodyArea.setText("");
        attachments.clear();
        attachmentsModel.clear();
    }
}

