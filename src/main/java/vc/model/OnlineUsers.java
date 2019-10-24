package vc.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineUsers {
	private static final Logger logger = LoggerFactory.getLogger(OnlineUsers.class);

	public static final int SCHEDULER_INTERVAL_MINUTES = 5;
	public static final long ONLINE_INTERVAL_MINUTES = 5;
	public static final int INITIAL_DELAY_MINUTES = 6;

	private static final Map<String, Long> onlineUsersMap = new HashMap<String, Long>();
	private static final Object synchObject = new Object();
	private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	static {
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				logger.info("run scheduler");
				Set<String> emailsSet = getAllOnlineUsers();
				synchronized (synchObject) {
					for (String email : emailsSet) {
						logger.info("run scheduler email=" + email);
						if (System.currentTimeMillis() - onlineUsersMap.get(email) > ONLINE_INTERVAL_MINUTES * 60
								* 1000) {
							onlineUsersMap.remove(email);
							logger.info("remove user " + email);
						}
					}
				}
			}

		}, INITIAL_DELAY_MINUTES, SCHEDULER_INTERVAL_MINUTES, TimeUnit.MINUTES);
	}

	public static void addUser(String email) {
		synchronized (synchObject) {
			onlineUsersMap.put(email, System.currentTimeMillis());
			logger.info("add user " + email);
		}
	}

	public static Set<String> getAllOnlineUsers() {
		Set<String> result = new HashSet<String>();
		synchronized (synchObject) {
			result.addAll(onlineUsersMap.keySet());
		}
		return result;
	}
}
