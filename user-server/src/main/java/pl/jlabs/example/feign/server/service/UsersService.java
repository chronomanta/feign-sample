package pl.jlabs.example.feign.server.service;

import org.springframework.stereotype.Service;
import pl.jlabs.example.feign.client.model.User;
import pl.jlabs.example.feign.client.model.UserData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * in-memory repo - just for sample simplicity
 */
@Service
public class UsersService {

    private final AtomicLong idSequence = new AtomicLong();
    private final Map<Long, User> usersRepo = new ConcurrentHashMap<>();

    public List<User> getUsers() {
        return usersRepo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
    }

    public long createUser(UserData userData) {
        final long id = idSequence.incrementAndGet();
        final User user = new User(id, userData);
        usersRepo.put(id, user);
        return id;
    }


    public User getUser(Long userId) {
        return usersRepo.get(userId);
    }

    public User updateUser(Long userId, UserData userData) {
        return usersRepo.computeIfPresent(userId, (key, value) -> new User(userId, userData));
    }

    public void deleteUser(Long userId) {
        usersRepo.remove(userId);
    }

    public void clear() {
        usersRepo.clear();
    }

}
