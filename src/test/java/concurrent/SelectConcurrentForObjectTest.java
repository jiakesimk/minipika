package concurrent;

/*
 * Creates on 2019/11/13.
 */

import entity.User;
import mapper.UserMapper;
import org.jiakesiws.minipika.framework.thread.ThreadFactory;
import org.jiakesiws.minipika.framework.thread.Threads;

/**
 * @author lts
 * @email jiakesiws@gmail.com
 */
public class SelectConcurrentForObjectTest {

  static final UserMapper mapper = UserMapper.mapper;

  public static void main(String[] args) {

    ThreadFactory.createThread("thread1", () -> {
      while (true) {
        User user = mapper.findUser("name1");
        System.out.println(Threads.getCurrentThreadName() + ": =========>>> " + user.getId());
      }
    }).start();

    ThreadFactory.createThread("thread2", () -> {
      while (true) {
        User user = mapper.findUser("name2");
        System.out.println(Threads.getCurrentThreadName() + ": =========>>> " + user.getId());
      }
    }).start();

    ThreadFactory.createThread("thread3", () -> {
      while (true) {
        User user = mapper.findUser("name3");
        System.out.println(Threads.getCurrentThreadName() + ": =========>>> " + user.getId());
      }
    }).start();

  }

}
