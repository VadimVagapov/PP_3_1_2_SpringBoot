package web.ProjectOneBoot.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import web.ProjectOneBoot.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Qualifier("users")
@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User newUser, long id) {
        User userOur = findUser(id);
        userOur.setName(newUser.getName());
        userOur.setLastname(newUser.getLastname());
        userOur.setEmail(newUser.getEmail());
    }

    @Override
    public User findUser(long id) {

        return entityManager.find(User.class, id);
    }

    @Override
    public void remove(long id) {
        entityManager.remove(findUser(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getListUsers(String count) {
        List<User> list1 = (List<User>) entityManager.createQuery("FROM User").getResultList();
        list1 = list1.stream().sorted(Comparator.comparingLong(User::getId)).toList();
        if (count.matches("^-?\\d+$")) {
            int id = Integer.parseInt(count);
            if (id < 0) {
                return list1;
            }
            User user = findUser(id);
            if (user == null) {
                return new ArrayList<User>();
            }
            List<User> list2 = new ArrayList<User>();
            list2.add(user);
            return list2;
        }

        return list1.stream()
                .filter(x -> x.getName().toLowerCase().equals(count)
                        || x.getLastname().toLowerCase().equals(count)
                        || x.getEmail().toLowerCase().equals(count))
                .toList();

    }
}
