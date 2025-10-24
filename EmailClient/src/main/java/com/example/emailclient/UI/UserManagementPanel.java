package com.example.emailclient.UI;

import com.example.emailclient.model.User;
import com.example.emailclient.model.Account;
import com.example.emailclient.service.AccountService;
import com.example.emailclient.service.UserService;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {

	
	private static final long serialVersionUID = 1L;
	
	private final UserService userService;
	private final AccountService accountService;

	private final DefaultListModel<User> userModel = new DefaultListModel<>();
	private final JList<User> userList = new JList<>(userModel);
	
	private final DefaultListModel<Account> accountModel = new DefaultListModel<>();
	private final JList<Account> accountList = new JList<>(accountModel);
	private final JButton createUserButton = new JButton("Створити користувача");
	private final JButton addAccountButton = new JButton("Додати акаунт");
	private final JComboBox<String> comboBoxAccounts=new JComboBox<String>();

	public UserManagementPanel(UserService userService, AccountService accountService) {
		this.userService = userService;
		this.accountService = accountService;
		setLayout(new BorderLayout(10, 10));

		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(createUserButton);
		buttonPanel.add(addAccountButton);

		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(userList);

		add(buttonPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(new JLabel("Акаунти користувача:"));
		bottomPanel.add(comboBoxAccounts);
		add(bottomPanel, BorderLayout.SOUTH);

		loadUsers();
		initActions();
	}

	private void initActions() {
		createUserButton.addActionListener(e -> {
			String name = JOptionPane.showInputDialog(this, "Ім'я користувача:");
			if (name == null || name.isBlank())
				return;
			String password = JOptionPane.showInputDialog(this, "Пароль:");
			if (password == null || password.isBlank())
				return;

			userService.createUser(name, password);
			loadUsers();
			JOptionPane.showMessageDialog(this, "Користувача створено!");
		});

		addAccountButton.addActionListener(e -> {
			User selectedUser = userList.getSelectedValue();
			if (selectedUser == null) {
				JOptionPane.showMessageDialog(this, "Оберіть користувача зі списку!");
				return;
			}

			String email = JOptionPane.showInputDialog(this, "Email акаунта:");
			if (email == null || email.isBlank())
				return;
			String pass = JOptionPane.showInputDialog(this, "Пароль акаунта:");
			if (pass == null || pass.isBlank())
				return;
			String provider = JOptionPane.showInputDialog(this, "Провайдер (gmail, ukr.net, i.ua):");
			if (provider == null || provider.isBlank())
				return;

			try {
				accountService.ValidateAccountNoRepeat(selectedUser.getId(), email);
				userService.addAccount(selectedUser.getId(), email, pass, provider);
				JOptionPane.showMessageDialog(this, "Акаунт додано до користувача!");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Помилка: " + ex.getMessage(), "Помилка",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		  userList.addListSelectionListener(e -> {
		        if (!e.getValueIsAdjusting()) {
		            User selectedUser = userList.getSelectedValue();
		            loadAccountsForUser(selectedUser);
		        }
		    });
	}
	
	private void loadUsers() {
		userModel.clear();
		List<User> users = userService.getAllUsers();
		for (User u : users)
			userModel.addElement(u);
	}
	private void loadAccountsForUser(User user) {
	    comboBoxAccounts.removeAllItems();

	    if (user == null) return;

	    List<Account> accounts = accountService.getAccountsForUser(user.getId());
	    for (Account account : accounts) {
	        comboBoxAccounts.addItem(account.getEmail());
	        accountModel.addElement(account);
	    }
	}
	public User getSelectedUser() { return userList.getSelectedValue(); }

	public Account getSelectedAccount() {
		String selectedEmail = (String) comboBoxAccounts.getSelectedItem();
		if (selectedEmail == null)
			return null;

		for (int i = 0; i < accountModel.size(); i++) {
			Account acc = accountModel.getElementAt(i);
			if (acc.getEmail().equals(selectedEmail)) {
				return acc;
			}
		}
		return null;
	}
}

