import java.util.ArrayList;
import java.util.List;

public class UserService {

	private List<User> users = new ArrayList<>();

	{
		users.add(new User(1, "Karlo", "Novaq"));
	}

	public List<User> findAllUsers() {
		return users;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public User findUser(Integer id) {
		return users.stream()
					.filter(user -> user.getId().equals(id))
					.findFirst()
					.get();
	}

}
