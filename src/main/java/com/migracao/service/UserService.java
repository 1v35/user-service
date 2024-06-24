package com.migracao.service;

import com.migracao.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private List<User> users = new ArrayList<>();
	private List<String> transactions = new ArrayList<>();

	public UserService() {
		// Inicializando alguns usuários para teste
		users.add(new User(1, "user1", "password1", 1000.0));
		users.add(new User(2, "user2", "password2", 500.0));
	}

	public Optional<User> findUserById(int id) {
		return users.stream().filter(user -> user.getId() == id).findFirst();
	}

	public Optional<User> login(String username, String password) {
		return users.stream()
				.filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
				.findFirst();
	}

	public boolean transfer(int fromUserId, int toUserId, double amount) {
		Optional<User> fromUserOpt = findUserById(fromUserId);
		Optional<User> toUserOpt = findUserById(toUserId);

		if (fromUserOpt.isPresent() && toUserOpt.isPresent() && fromUserId != toUserId) {
			User fromUser = fromUserOpt.get();
			User toUser = toUserOpt.get();

			if (fromUser.getBalance() >= amount) {
				fromUser.setBalance(fromUser.getBalance() - amount);
				toUser.setBalance(toUser.getBalance() + amount);
				transactions.add("Transfer from User " + fromUserId + " to User " + toUserId + ": " + amount);
				return true;
			}
		}
		return false;
	}

	public boolean deposit(int userId, double amount) {
		Optional<User> userOpt = findUserById(userId);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setBalance(user.getBalance() + amount);
			transactions.add("Deposit to User " + userId + ": " + amount);
			return true;
		}
		return false;
	}

	public User registerUser(String username, String password, double initialBalance) {
		int newId = users.size() + 1;
		User newUser = new User(newId, username, password, initialBalance);
		users.add(newUser);
		return newUser;
	}

	public List<String> getTransactions() {
		return transactions;
	}
}